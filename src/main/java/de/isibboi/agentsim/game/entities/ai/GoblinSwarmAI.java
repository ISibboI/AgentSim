package de.isibboi.agentsim.game.entities.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.isibboi.agentsim.Settings;
import de.isibboi.agentsim.algorithm.BlockadeMap;
import de.isibboi.agentsim.algorithm.KnowledgeBasedBlockadeMap;
import de.isibboi.agentsim.game.EntityLocationManager;
import de.isibboi.agentsim.game.entities.Entity;
import de.isibboi.agentsim.game.entities.Goblin;
import de.isibboi.agentsim.game.entities.ai.tasks.GoblinTaskFactory;
import de.isibboi.agentsim.game.entities.ai.tasks.Task;
import de.isibboi.agentsim.game.entities.buildings.SwarmMainBuilding;
import de.isibboi.agentsim.game.map.Material;
import de.isibboi.agentsim.game.map.Point;

/**
 * An AI system that simulates a swarm of autonomous agents.
 * Agents can share knowledge if they meet another agent.
 * 
 * @author Sebastian Schmidt
 * @since 0.2.0
 */
public class GoblinSwarmAI extends TaskExecutingAI {
	private static final Logger _log = LogManager.getLogger(GoblinSwarmAI.class);

	private static AtomicInteger _idCounter = new AtomicInteger(0);
	private final int _id = _idCounter.incrementAndGet();

	private final EntityLocationManager _entityLocationManager;
	private final GoblinTaskFactory _goblinTaskFactory;
	private final Goblin _goblin;
	private final ProviderBackedKnowledgeMap<Material> _mapKnowledge;

	private final float _saturationBufferDistanceFactor;
	private final int _saturationBufferMinimum;

	private Task _moveToSpawnTask = null;

	/**
	 * Creates a new {@link GoblinSwarmAI}.
	 * @param entityLocationManager The entity location manager.
	 * @param goblinTaskFactory The goblin task factory.
	 * @param goblin The entity that is controlled by this AI.
	 */
	public GoblinSwarmAI(final EntityLocationManager entityLocationManager, final GoblinTaskFactory goblinTaskFactory, final Goblin goblin) {
		_entityLocationManager = entityLocationManager;
		_goblinTaskFactory = goblinTaskFactory;
		_goblin = goblin;

		Settings settings = entityLocationManager.getSettings();

		KnowledgeMap<Material> knowledgeRepresentation;

		switch (entityLocationManager.getSettings().get(Settings.CORE_AI_KNOWLEDGE_REPRESENTATION)) {
		case Settings.CORE_AI_KNOWLEDGE_REPRESENTATION_ARRAY:
			knowledgeRepresentation = new ArrayKnowledgeMap<>(entityLocationManager.getMap().getWidth(),
					entityLocationManager.getMap().getHeight());
			break;

		case Settings.CORE_AI_KNOWLEDGE_REPRESENTATION_HASHMAP:
			knowledgeRepresentation = new HashKnowledgeMap<>();
			break;

		default:
			knowledgeRepresentation = null;
			entityLocationManager.getSettings().throwIllegalSettingValue(Settings.CORE_AI_KNOWLEDGE_REPRESENTATION);
		}

		_mapKnowledge = new ProviderBackedKnowledgeMap<>(knowledgeRepresentation, entityLocationManager.getMap());

		// ---- Learn path to spawn
		final int spawnRadius = entityLocationManager.getSettings().getInt(Settings.GAME_SPAWN_RADIUS);
		List<Point> spawnPattern = new ArrayList<>((spawnRadius + 1) * (spawnRadius + 1));

		for (int i = -spawnRadius; i <= spawnRadius; i++) {
			for (int j = -spawnRadius; j <= spawnRadius; j++) {
				spawnPattern.add(new Point(i, j));
			}
		}

		_mapKnowledge.updateLocation(entityLocationManager.getMap().getSpawnPoint(), spawnPattern);
		// ---- Learned path to spawn

		_saturationBufferDistanceFactor = settings.getFloat(Settings.GAME_AI_SATURATION_BUFFER_DISTANCE_FACTOR);
		_saturationBufferMinimum = settings.getInt(Settings.GAME_AI_SATURATION_BUFFER_MINIMUM);

		setIdleTask(goblinTaskFactory.createIdleTask());
	}

	@Override
	public void eventCollideWithWall(final Point location) {
		explorePoint(location);

		if (!_entityLocationManager.getMap().isLocationLocked(location)) {
			tryEnqueueTask(_goblinTaskFactory.createMiningTask(location, _goblin, _entityLocationManager));
		}
	}

	@Override
	public void eventCollideWithEntity(final Entity entity) {
		// Those downcasts should be removed and replaced with proper object oriented code.
		if (entity instanceof Goblin) {
			Goblin g = (Goblin) entity;
			GoblinSwarmAI ai = (GoblinSwarmAI) g.getAI();
			exchangeInformation(ai);
		} else if (entity instanceof SwarmMainBuilding) {
			int feedAmount = _goblin.getAttributes().feedCompletely();
			_log.trace("A goblin was fed at main building. It ate " + feedAmount + " units.");
		}
	}

	/**
	 * Exchanges information with the other AI.
	 * @param ai The other AI.
	 */
	public void exchangeInformation(final GoblinSwarmAI ai) {
		_mapKnowledge.exchangeInformation(ai._mapKnowledge);
	}

	@Override
	public void eventCollideWithMapBorder(final Point location) {
		// Ignored
	}

	@Override
	public void eventMoveTo(final Point location) {
		explorePoint(location);
	}

	/**
	 * Explores the given location. That means that the entity gains some knowledge about the location.
	 * @param location The location to explore.
	 */
	private void explorePoint(final Point location) {
		_mapKnowledge.updateLocation(location);
	}

	@Override
	public void eventTaskFinished(final Task task, final int nextTaskDuration) {
		Objects.requireNonNull(task);

		// Make entities move to spawn point if saturation is low.
		moveToSpawnIfNecessary(nextTaskDuration);

		// Goblin has reached spawn.
		if (task == _moveToSpawnTask) {
			_moveToSpawnTask = null;
		}
	}

	@Override
	protected void eventExecutionFinished() {
		// Ignored
	}

	@Override
	protected void eventExecutingIdleTask() {
		moveToSpawnIfNecessary(1);
	}

	/**
	 * Moves the goblin back to the spawn point, if it would be starving otherwise.
	 * @param nextTaskDuration The guesstimated duration of the next task.
	 */
	protected void moveToSpawnIfNecessary(final int nextTaskDuration) {
		// Prevent moving to spawn more than once.
		if (_moveToSpawnTask != null) {
			return;
		}

		int distanceToHome = _goblin.getLocation().manhattanDistance(_entityLocationManager.getMap().getSpawnPoint());
		int saturation = _goblin.getAttributes().getSaturation();
		saturation /= _saturationBufferDistanceFactor;
		saturation -= _saturationBufferMinimum;

		if (saturation <= distanceToHome + nextTaskDuration) {
			_moveToSpawnTask = _goblinTaskFactory.createMoveToTask(_entityLocationManager.getMap().getSpawnPoint(), _goblin);
			enqueueTaskFront(_moveToSpawnTask);

			_log.trace("Goblin moving back to spawn to prevent starvation. Distance to home is " + distanceToHome + ", the next task takes " + nextTaskDuration
					+ " ticks, and the current adjusted saturation is " + saturation + ".");
		}
	}

	/**
	 * Tries to enqueue the given task.
	 * The task is only enqueued, if its execution does not make the goblin starve.
	 * @param task The task to enqueue.
	 * @return True if the task was enqueued, false otherwise.
	 */
	protected boolean tryEnqueueTask(final Task task) {
		int currentDuration = guessDurationToFinishQueue();
		int additionalDuration = task.guessDuration();
		int moveHomeDuration = _entityLocationManager.getMap().getSpawnPoint().manhattanDistance(_goblin.getLocation());

		if (currentDuration + additionalDuration + moveHomeDuration < _goblin.getAttributes().getSaturation()) {
			enqueueTask(task);
			return true;
		} else {
			moveToSpawnIfNecessary(1);
			return false;
		}
	}

	/**
	 * Returns the blockade map that represents the locations that are known as blocking to this AI.
	 * Unknown locations are blocked.
	 * 
	 * @return The blockade map for this AI.
	 */
	public BlockadeMap getBlockadeMap() {
		return new KnowledgeBasedBlockadeMap(_mapKnowledge);
	}
}