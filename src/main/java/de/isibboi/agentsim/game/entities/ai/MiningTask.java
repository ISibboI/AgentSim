package de.isibboi.agentsim.game.entities.ai;

import de.isibboi.agentsim.Environment;
import de.isibboi.agentsim.game.EntityLocationManager;
import de.isibboi.agentsim.game.map.GameMap;
import de.isibboi.agentsim.game.map.Point;

/**
 * A task that mines a block.
 * 
 * @author Sebastian Schmidt
 * @since 0.2.0 
 */
public class MiningTask extends AbstractLocalTask {
	/**
	 * Creates a new mining task.
	 * 
	 * @param location The location of the task.
	 * @param map The game map.
	 * @param entityLocationManager The entity location manager.
	 */
	public MiningTask(final Point location, final GameMap map, final EntityLocationManager entityLocationManager) {
		super(map.getMaterialAt(location).getDurability(), location, map);
	}

	@Override
	public void complete(final GameMap map, final EntityLocationManager entityLocationManager) {
		map.setMaterial(getLocation(), Environment.MATERIAL_AIR);
		entityLocationManager.getGoblinSpawner().spawnGoblin(getLocation());
	}
}