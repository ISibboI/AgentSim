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
import de.isibboi.agentsim.algorithm.PrioritizedRandomSelector;
import de.isibboi.agentsim.game.EntityLocationManager;
import de.isibboi.agentsim.game.entities.Entity;
import de.isibboi.agentsim.game.entities.Goblin;
import de.isibboi.agentsim.game.entities.ai.intends.CompositeTask;
import de.isibboi.agentsim.game.entities.ai.intends.Intend;
import de.isibboi.agentsim.game.entities.ai.tasks.GoblinTaskFactory;
import de.isibboi.agentsim.game.entities.ai.tasks.Task;
import de.isibboi.agentsim.game.entities.buildings.SwarmMainBuilding;
import de.isibboi.agentsim.game.map.Material;
import de.isibboi.agentsim.game.map.Point;

/**
 * An AI system that simulates a swarm of autonomous agents.
 * Agents can share knowledge if they meet another agent.
 * 
 * The goblin automaton.
 * If it has no task, select next task.
 * If the selected task can be completed without starving, do it.
 * Otherwise, get food.
 * 
 * @author Sebastian Schmidt
 * @since 0.2.0
 */
public class GoblinSwarmAI extends TaskExecutingAI {
	private static final Logger LOG = LogManager.getLogger(GoblinSwarmAI.class);

	private static AtomicInteger idCounter = new AtomicInteger(0);
	private final int _id = idCounter.incrementAndGet();

	private final EntityLocationManager _entityLocationManager;
	private final GoblinTaskFactory _goblinTaskFactory;
	private final Goblin _goblin;
	private final ProviderBackedKnowledgeMap<Material> _mapKnowledge;

	private final float _saturationBufferDistanceFactor;
	private final int _saturationBufferMinimum;
	private final List<Point> _viewingPattern;

	private final PrioritizedRandomSelector<Intend> _intendSelector = new PrioritizedRandomSelector<>();
	private Intend _currentIntend;

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

		// ---- Create viewing pattern
		_viewingPattern = new ArrayList<>();
		_viewingPattern.add(new Point(0, 0));
		_viewingPattern.add(new Point(1, 0));
		_viewingPattern.add(new Point(-1, 0));
		_viewingPattern.add(new Point(0, 1));
		_viewingPattern.add(new Point(0, -1));
		// ---- Created viewing pattern

		_saturationBufferDistanceFactor = settings.getFloat(Settings.GAME_AI_SATURATION_BUFFER_DISTANCE_FACTOR);
		_saturationBufferMinimum = settings.getInt(Settings.GAME_AI_SATURATION_BUFFER_MINIMUM);

		setIdleTask(goblinTaskFactory.createIdleTask());
	}

	@Override
	public void eventCollideWithWall(final Point location) {
		LOG.trace("Collided with wall");

		explorePoint(location);

		// TODO Try restarting the current intend?
		abort();
	}

	@Override
	public void eventCollideWithEntity(final Entity entity, final int tick) {
		// Those downcasts should be removed and replaced with proper object oriented code.
		if (entity instanceof Goblin) {
			Goblin g = (Goblin) entity;
			GoblinSwarmAI ai = g.getAI();
			exchangeInformation(ai);
		} else if (entity instanceof SwarmMainBuilding) {
			int feedAmount = _goblin.getAttributes().feedCompletely();
			_intendSelector.update(((SwarmMainBuilding) entity).getIntends(tick));
			LOG.trace("A goblin was fed at main building. It ate " + feedAmount + " units.");
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
		LOG.trace("Collided with map border");

		// TODO Try restarting the current intend?
		abort();
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
		_mapKnowledge.updateLocation(location, _viewingPattern);
	}

	@Override
	public void eventTaskFinished(final Task task, final int nextTaskDuration) {
		Objects.requireNonNull(task);
	}

	@Override
	protected void eventExecutionAborted() {
		LOG.trace("Execution aborted");

		_intendSelector.add(_currentIntend);
		_currentIntend = null;

		selectNextTask();
	}

	@Override
	protected void eventExecutionFinished() {
		LOG.trace("Execution finished");
	}

	@Override
	protected void eventExecutingIdleTask() {
		selectNextTask();
	}

	/**
	 * Selects the next task that should be executed, keeping the goblin from starving.
	 */
	private void selectNextTask() {
		_currentIntend = _intendSelector.select();

		if (_currentIntend == null) {
			moveToSpawnIfNecessary(2);
		} else {
			CompositeTask task = _currentIntend.execute(_goblin);

			if (task != null) {
				if (!moveToSpawnIfNecessary(task.getDuration())) {
					enqueueCompositeTask(task);
					LOG.trace("Executing: " + _currentIntend);
				}
			} else {
				_intendSelector.add(_currentIntend);
				moveToSpawnIfNecessary(2);
			}
		}
	}

	/**
	 * Moves the goblin back to the spawn point, if it would be starving otherwise.
	 * @param nextTaskDuration The duration of the next task.
	 * 
	 * @return True if the goblin is moving to spawn as a result of calling this method, false otherwise.
	 */
	protected boolean moveToSpawnIfNecessary(final int nextTaskDuration) {
		return moveToSpawnIfNecessary(nextTaskDuration, _goblin.getLocation());
	}

	/**
	 * Moves the goblin back to the spawn point, if it would be starving otherwise.
	 * @param nextTaskDuration The duration of the next task.
	 * @param nextTaskFinishingPoint The finishing point of the next task.
	 * 
	 * @return True if the goblin is moving to spawn as a result of calling this method, false otherwise.
	 */
	protected boolean moveToSpawnIfNecessary(final int nextTaskDuration, final Point nextTaskFinishingPoint) {
		int distanceToHome = nextTaskFinishingPoint.manhattanDistance(_entityLocationManager.getMap().getSpawnPoint());
		int saturation = _goblin.getAttributes().getSaturation();
		saturation /= _saturationBufferDistanceFactor;
		saturation -= _saturationBufferMinimum;

		if (saturation <= distanceToHome + nextTaskDuration) {
			Iterable<? extends Task> _moveToSpawnTask = _goblinTaskFactory.createMoveToTask(_entityLocationManager.getMap().getSpawnPoint(), _goblin);
			enqueueTasks(_moveToSpawnTask);

			LOG.trace("Goblin moving back to spawn to prevent starvation. Distance to home is " + distanceToHome + ", the next task takes " + nextTaskDuration
					+ " ticks, and the current adjusted saturation is " + saturation + ".");
			return true;
		}

		return false;
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

	/**
	 * Returns the {@link EntityLocationManager}.
	 * @return The EntityLocationManager.
	 */
	public EntityLocationManager getEntityLocationManager() {
		return _entityLocationManager;
	}

	/**
	 * Returns the map knowledge of this AI.
	 * @return The map knowledge.
	 */
	public KnowledgeMap<Material> getMapKnowledge() {
		return _mapKnowledge;
	}
}