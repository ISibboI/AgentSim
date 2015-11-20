package de.isibboi.agentsim.game.entities.ai;

import java.util.ArrayList;
import java.util.List;

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

	private final EntityLocationManager _entityLocationManager;
	private final GoblinTaskFactory _goblinTaskFactory;
	private final Goblin _goblin;
	private final ProviderBackedKnowledgeMap<Material> _mapKnowledge;

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
	public void eventTaskFinished() {
		// Make entities move to spawn point if saturation is low.
		moveToSpawnIfNecessary();
	}

	@Override
	protected void eventExecutionFinished() {
		// Ignored
	}

	@Override
	protected void eventExecutingIdleTask() {
		moveToSpawnIfNecessary();
	}

	/**
	 * Moves the goblin back to the spawn point, if it would be starving otherwise.
	 */
	protected void moveToSpawnIfNecessary() {
		if (!isExecuting() && _goblin.getAttributes().getSaturation() <= _goblin.getLocation().manhattanDistance(_entityLocationManager.getMap().getSpawnPoint())) {
			enqueueTask(_goblinTaskFactory.createMoveToTask(_entityLocationManager.getMap().getSpawnPoint(), _goblin));
			_log.trace("Goblin moving back to spawn to prevent starvation.");
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
			moveToSpawnIfNecessary();
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