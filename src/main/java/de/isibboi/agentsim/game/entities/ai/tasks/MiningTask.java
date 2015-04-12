package de.isibboi.agentsim.game.entities.ai.tasks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.isibboi.agentsim.Environment;
import de.isibboi.agentsim.game.EntityLocationManager;
import de.isibboi.agentsim.game.entities.Entity;
import de.isibboi.agentsim.game.map.Material;
import de.isibboi.agentsim.game.map.Point;

/**
 * A task that mines a block.
 * 
 * @author Sebastian Schmidt
 * @since 0.2.0 
 */
public class MiningTask extends MotionlessTimedTask {
	private final Logger _log = LogManager.getLogger(getClass());

	private final Point _miningLocation;
	private final Entity _entity;
	private final EntityLocationManager _entityLocationManager;

	/**
	 * Creates a new mining task.
	 * 
	 * @param miningLocation The location that should be mined.
	 * @param entity The entity that performs the task.
	 * @param entityLocationManager The entity location manager.
	 */
	public MiningTask(final Point miningLocation, final Entity entity, final EntityLocationManager entityLocationManager) {
		_miningLocation = miningLocation;
		_entity = entity;
		_entityLocationManager = entityLocationManager;
	}

	@Override
	public int guessDuration() {
		return getDuration();
	}

	@Override
	protected void eventFinished() {
		_entityLocationManager.getMap().setMaterial(_miningLocation, Environment.MATERIAL_AIR);
		_entityLocationManager.getGoblinSpawner().spawnGoblin(_miningLocation);
	}

	@Override
	public void start() {
		Material minedMaterial = _entityLocationManager.getMap().getMaterialAt(_miningLocation);
		Point location = _entityLocationManager.getLocation(_entity);
		int duration = _entityLocationManager.getMap().getMaterialAt(_miningLocation).getDurability();

		setDuration(duration);
		super.start();

		if (!location.isNeighborOf(_miningLocation) || minedMaterial == Environment.MATERIAL_AIR) {
			fail();
		}
	}
}