package de.isibboi.agentsim.game.entities.ai.intends;

import de.isibboi.agentsim.game.entities.Goblin;
import de.isibboi.agentsim.game.entities.ai.tasks.LockLocationTask;
import de.isibboi.agentsim.game.entities.ai.tasks.MiningTask;
import de.isibboi.agentsim.game.entities.ai.tasks.MoveToTask;
import de.isibboi.agentsim.game.entities.ai.tasks.Task;
import de.isibboi.agentsim.game.entities.ai.tasks.UnlockLocationTask;
import de.isibboi.agentsim.game.map.Material;
import de.isibboi.agentsim.game.map.Point;

/**
 * The intend to mine a specific block.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 *
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
	public Task execute(final Goblin goblin) {
		final Point currentPoint = goblin.getLocation();
		final Point miningPoint = goblin.getAI().getMapKnowledge().searchNearestEqualKnowledge(currentPoint, _material);
		final CompositeTask tasks = new CompositeTask();

		final MoveToTask movement = searchAccessPoint(currentPoint, miningPoint, goblin);
		final LockLocationTask lock = new LockLocationTask(goblin.getMap(), miningPoint, goblin);

		tasks.add(movement);
		tasks.add(lock);
		tasks.add(new MiningTask(miningPoint, goblin, goblin.getEntityLocationManager()));
		tasks.add(new UnlockLocationTask(lock));

		return tasks;
	}
}