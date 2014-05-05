package de.isibboi.agentsim.game.entities;

import de.isibboi.agentsim.game.entities.ai.AI;
import de.isibboi.agentsim.game.entities.ai.MiningTask;
import de.isibboi.agentsim.game.entities.ai.Task;
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
	private Task _newTask;
	private final LocationLock _locationLock;
	private Point _locationToLock;

	/**
	 * Creates a new {@link GoblinSwarmAI}.
	 * @param map The game map.
	 */
	public GoblinSwarmAI(final GameMap map) {
		_map = map;
		_locationLock = new LocationLock(map);
	}

	@Override
	public void eventCollideWithWall(final Point location) {
		if (!_map.isLocationLocked(location)) {
			_newTask = new MiningTask(location, _map);
			_locationToLock = location;
		}
	}

	@Override
	public void eventCollideWithEntity(final Goblin goblin) {
		// TODO Auto-generated method stub

	}

	@Override
	public void eventMoveTo(final Point location) {
		// TODO Auto-generated method stub

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
}