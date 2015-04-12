package de.isibboi.agentsim.game.entities.ai;

import java.util.Random;

import de.isibboi.agentsim.Settings;
import de.isibboi.agentsim.game.EntityLocationManager;
import de.isibboi.agentsim.game.entities.Entity;
import de.isibboi.agentsim.game.entities.Goblin;
import de.isibboi.agentsim.game.map.GameMap;
import de.isibboi.agentsim.game.map.LocationLock;
import de.isibboi.agentsim.game.map.Point;

/**
 * An AI system that tries to simulate a swarm of autonomous agents. Agents can only share knowledge if they meet another agent.
 * 
 * @author Sebastian Schmidt
 * @since 0.2.0
 */
public class GoblinSwarmAI implements AI {
	private final GameMap _map;
	private final EntityLocationManager _entityLocationManager;
	private final Goblin _goblin;

	private Task _newTask;
	private final LocationLock _locationLock;
	private Point _locationToLock;

	private int _age;
	private int _lifeTimeLeft;

	/**
	 * Creates a new {@link GoblinSwarmAI}.
	 * @param map The game map.
	 * @param entityLocationManager The entity location manager.
	 * @param goblin The controlled entity.
	 */
	public GoblinSwarmAI(final GameMap map, final EntityLocationManager entityLocationManager, final Goblin goblin) {
		_map = map;
		_entityLocationManager = entityLocationManager;
		_locationLock = new LocationLock(map);
		_goblin = goblin;

		_age = 0;
		_lifeTimeLeft = _map.getSettings().getInt(Settings.AI_LIFE_TIME);
	}

	@Override
	public void eventCollideWithWall(final Point location) {
		if (!_map.isLocationLocked(location)) {
			_newTask = new MiningTask(location, _map, _entityLocationManager);
			_locationToLock = location;
		}
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
		_locationLock.unlockLocation();
	}

	@Override
	public void eventTaskAccepted() {
		if (_locationToLock != null) {
			_locationLock.lockLocation(_locationToLock);
			_locationToLock = null;
		}
	}

	@Override
	public Task getNewTask() {
		Task result = _newTask;
		_newTask = null;

		return result;
	}

	@Override
	public boolean update(final Random random) {
		_age++;
		_lifeTimeLeft--;

		if (_lifeTimeLeft < 0) {
			die();
			return false;
		}

		return true;
	}

	/**
	 * Lets the goblin die.
	 */
	private void die() {
		_locationLock.unlockLocationIfLocked();
		_entityLocationManager.removeEntity(_goblin);
	}
}