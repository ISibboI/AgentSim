package de.isibboi.agentsim.game.entities.ai.intends;

import de.isibboi.agentsim.game.entities.Goblin;
import de.isibboi.agentsim.game.entities.ai.tasks.LockLocationTask;
import de.isibboi.agentsim.game.entities.ai.tasks.MiningTask;
import de.isibboi.agentsim.game.entities.ai.tasks.MoveToTask;
import de.isibboi.agentsim.game.entities.ai.tasks.UnlockLocationTask;
import de.isibboi.agentsim.game.map.Material;
import de.isibboi.agentsim.game.map.Point;

/**
 * The intend to mine a specific block type.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public class MiningIntend extends AbstractIntend {

	private final Material _material;

	/**
	 * Creates a new mining intend.
	 * @param tick The current tick.
	 * @param priority The priority of this intend.
	 * @param material The material that should be mined.
	 */
	public MiningIntend(final int tick, final int priority, final Material material) {
		super(tick, priority);

		_material = material;
	}

	@Override
	public CompositeTask execute(final Goblin goblin) {
		final Point currentPoint = goblin.getLocation();
		final Point miningPoint = goblin.getAI().getMapKnowledge().searchNearestEqualKnowledge(currentPoint, _material);

		if (miningPoint == null) {
			return null;
		}

		final CompositeTask.Builder taskBuilder = new CompositeTask.Builder();

		final MoveToTask movement = searchAccessPoint(currentPoint, miningPoint, goblin);

		if (movement == null) {
			return null;
		}

		final LockLocationTask lock = new LockLocationTask(goblin.getMap(), miningPoint, goblin);

		taskBuilder.add(movement);
		taskBuilder.add(lock);
		taskBuilder.add(new MiningTask(miningPoint, goblin, goblin.getEntityLocationManager()));
		taskBuilder.add(new UnlockLocationTask(lock));

		taskBuilder.setFinishingPoint(movement.getTarget());

		return taskBuilder.build();
	}
}