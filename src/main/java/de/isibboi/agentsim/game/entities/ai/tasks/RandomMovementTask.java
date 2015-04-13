package de.isibboi.agentsim.game.entities.ai.tasks;

import java.util.Random;

import de.isibboi.agentsim.game.GameUpdateException;
import de.isibboi.agentsim.game.entities.Movement;

/**
 * Makes the entity move around randomly.
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public class RandomMovementTask extends InfiniteTask {
	private Movement _movement;

	@Override
	public void update(final Random random, final int tick) throws GameUpdateException {
		_movement = getRandomMovement(random);
	}

	/**
	 * @param random The pseudo random number generator used to generate a random movement.
	 * @return A random movement.
	 */
	private Movement getRandomMovement(final Random random) {
		switch (random.nextInt(4)) {
		case 0:
			return Movement.UP;
		case 1:
			return Movement.DOWN;
		case 2:
			return Movement.LEFT;
		case 3:
			return Movement.RIGHT;
		default:
			throw new RuntimeException("Wrong random number!");
		}
	}

	@Override
	public Movement getMovement() {
		return _movement;
	}

	@Override
	public void start() {
		// Ignore
	}
}