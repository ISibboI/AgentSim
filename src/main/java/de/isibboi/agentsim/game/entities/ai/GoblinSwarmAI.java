package de.isibboi.agentsim.game.entities.ai;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.isibboi.agentsim.Settings;
import de.isibboi.agentsim.algorithm.BlockadeMap;
import de.isibboi.agentsim.algorithm.KnowledgeBasedBlockadeMap;
import de.isibboi.agentsim.game.EntityLocationManager;
import de.isibboi.agentsim.game.entities.Entity;
import de.isibboi.agentsim.game.entities.Goblin;
import de.isibboi.agentsim.game.entities.ai.tasks.GoblinTaskFactory;
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

		setIdleTask(goblinTaskFactory.createIdleTask());
	}

	@Override
	public void eventCollideWithWall(final Point location) {
		explorePoint(location);

		if (!_entityLocationManager.getMap().isLocationLocked(location)) {
			enqueueTask(_goblinTaskFactory.createMiningTask(location, _goblin, _entityLocationManager));
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
		if (!isExecuting() && _goblin.getAttributes().getSaturation() <= _goblin.getLocation().manhattanDistance(_entityLocationManager.getMap().getSpawnPoint())) {
			enqueueTask(_goblinTaskFactory.createMoveToTask(_entityLocationManager.getMap().getSpawnPoint(), _goblin));
		}
	}

	@Override
	protected void eventExecutionFinished() {
		// Ignored
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