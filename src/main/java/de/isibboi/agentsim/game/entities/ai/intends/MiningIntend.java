package de.isibboi.agentsim.game.entities.ai.intends;

import de.isibboi.agentsim.game.entities.Goblin;
import de.isibboi.agentsim.game.entities.ai.tasks.Task;
import de.isibboi.agentsim.game.map.Material;

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
	public Iterable<Task> execute(final Goblin goblin) {
		// TODO Auto-generated method stub
		return null;
	}
}