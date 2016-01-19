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

	private Material _minedMaterial;

	/**
	 * Creates a new mining task.
	 * The mined material is set on creation, and the task is aborted if the material at the mining location changes.
	 * 
	 * @param miningLocation The location that should be mined.
	 * @param entity The entity that performs the task.
	 * @param entityLocationManager The entity location manager.
	 */
	public MiningTask(final Point miningLocation, final Entity entity, final EntityLocationManager entityLocationManager) {
		super(entityLocationManager.getMap().getMaterialAt(miningLocation).getDurability());

		_miningLocation = miningLocation;
		_entity = entity;
		_entityLocationManager = entityLocationManager;
		_minedMaterial = _entityLocationManager.getMap().getMaterialAt(miningLocation);
	}

	@Override
	public int guessDuration() {
		return getDuration();
	}

	@Override
	protected void eventFinished() {
		_entityLocationManager.getMap().setMaterial(_miningLocation, Environment.MATERIAL_AIR);

		// Goblin newGoblin = _entityLocationManager.getGoblinSpawner().spawnGoblin(_miningLocation);
		// newGoblin.collideWith(_entity);

		_log.trace("MiningTask finished: " + _entity + " mined a " + _minedMaterial + " at " + _miningLocation);
	}

	@Override
	public void eventStarted() {
		if (_minedMaterial != _entityLocationManager.getMap().getMaterialAt(_miningLocation)) {
			fail();
		}

		Point location = _entityLocationManager.getLocation(_entity);

		if (!location.isNeighborOf(_miningLocation)) {
			fail();
			_log.trace("MiningTask failed: " + _entity + " tried to mine a " + _minedMaterial + " at " + _miningLocation + " from " + location);
		}
	}
}