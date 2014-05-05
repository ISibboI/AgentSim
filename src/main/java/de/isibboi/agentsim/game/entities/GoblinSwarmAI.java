package de.isibboi.agentsim.game.entities;

import java.awt.Point;

import de.isibboi.agentsim.game.entities.ai.AI;
import de.isibboi.agentsim.game.entities.ai.Task;

/**
 * An AI system that tries to simulate a swarm of autonomous agents. Agents can only share knowledge if they meet another agent.
 * 
 * @author Sebastian Schmidt
 * @since 0.2.0
 */
public class GoblinSwarmAI implements AI {
	@Override
	public void eventCollideWithWall(final Point location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void eventCollideWithEntity(Goblin goblin) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void eventMoveTo(final Point location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void eventTaskFinished() {
		// TODO Auto-generated method stub

	}

	@Override
	public void eventTaskAccepted() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Task getNewTask() {
		// TODO Auto-generated method stub
		return null;
	}
}