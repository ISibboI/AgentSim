package de.isibboi.agentsim.game.entities.ai.tasks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.isibboi.agentsim.Environment;
import de.isibboi.agentsim.game.EntityLocationManager;
import de.isibboi.agentsim.game.entities.Goblin;
import de.isibboi.agentsim.game.map.Material;
import de.isibboi.agentsim.game.map.Point;

/**
 * A task that mines a block.
 * 
 * @author Sebastian Schmidt
 * @since 0.2.0 
 */
public class MiningTask extends MotionlessTimedTask {
	private static final Logger LOG = LogManager.getLogger(MiningTask.class);

	private final Point _miningLocation;
	private final Goblin _goblin;
	private final EntityLocationManager _entityLocationManager;

	private Material _minedMaterial;

	/**
	 * Creates a new mining task.
	 * The mined material is set on creation, and the task is aborted if the material at the mining location changes.
	 * 
	 * @param miningLocation The location that should be mined.
	 * @param goblin The goblin that performs the task.
	 * @param entityLocationManager The entity location manager.
	 */
	public MiningTask(final Point miningLocation, final Goblin goblin, final EntityLocationManager entityLocationManager) {
		super(entityLocationManager.getMap().getMaterialAt(miningLocation).getDurability());

		_miningLocation = miningLocation;
		_goblin = goblin;
		_entityLocationManager = entityLocationManager;
		_minedMaterial = _entityLocationManager.getMap().getMaterialAt(miningLocation);
	}

	@Override
	public int guessDuration() {
		return getDuration();
	}

	@Override
	protected void eventFinished(final int tick) {
		_entityLocationManager.getMap().setMaterial(_miningLocation, Environment.MATERIAL_AIR);
		_goblin.getAI().getMapKnowledge().updateLocation(_miningLocation, Environment.MATERIAL_AIR, tick);

		LOG.trace("MiningTask finished: " + _goblin + " mined a " + _minedMaterial + " at " + _miningLocation);
	}

	@Override
	public void eventStarted() {
		if (_minedMaterial != _entityLocationManager.getMap().getMaterialAt(_miningLocation)) {
			fail();
		}

		Point location = _entityLocationManager.getLocation(_goblin);

		if (!location.isNeighborOf(_miningLocation)) {
			fail();
			LOG.trace("MiningTask failed: " + _goblin + " tried to mine a " + _minedMaterial + " at " + _miningLocation + " from " + location);
		}
	}
}