package de.isibboi.agentsim.game.entities.ai;

import de.isibboi.agentsim.game.EntityLocationManager;
import de.isibboi.agentsim.game.entities.Entity;
import de.isibboi.agentsim.game.entities.ai.tasks.GoblinTaskFactory;
import de.isibboi.agentsim.game.map.Point;

/**
 * An AI system that simulates a swarm of autonomous agents. Agents can only share knowledge if they meet another agent.
 * 
 * @author Sebastian Schmidt
 * @since 0.2.0
 */
public class GoblinSwarmAI extends TaskExecutingAI {
	private final EntityLocationManager _entityLocationManager;
	private final GoblinTaskFactory _goblinTaskFactory;
	private final Entity _entity;

	/**
	 * Creates a new {@link GoblinSwarmAI}.
	 * @param entityLocationManager The entity location manager.
	 * @param goblinTaskFactory The goblin task factory.
	 * @param entity The entity that is controlled by this AI.
	 */
	public GoblinSwarmAI(final EntityLocationManager entityLocationManager, final GoblinTaskFactory goblinTaskFactory, final Entity entity) {
		_entityLocationManager = entityLocationManager;
		_goblinTaskFactory = goblinTaskFactory;
		_entity = entity;

		setIdleTask(goblinTaskFactory.createIdleTask());
	}

	@Override
	public void eventCollideWithWall(final Point location) {
		enqueueTask(_goblinTaskFactory.createMiningTask(location, _entity, _entityLocationManager));
	}

	@Override
	public void eventCollideWithEntity(final Entity entity) {
		// Ignored
	}

	@Override
	public void eventCollideWithMapBorder(final Point location) {
		// Ignored
	}

	@Override
	public void eventMoveTo(final Point location) {
		// Ignored
	}

	@Override
	public void eventTaskFinished() {
		// Ignored
	}

	@Override
	protected void eventExecutionFinished() {
		// Ignored
	}
}