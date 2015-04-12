package de.isibboi.agentsim.game.entities;

import de.isibboi.agentsim.game.map.Point;

/**
 * @author Sebastian Schmidt
 * @since 0.3.0
 *
 */
public abstract class Movement {
	/**
	 * A movement one step to the top of the screen.
	 * @author Sebastian Schmidt
	 * @since 0.3.0
	 *
	 */
	public static final class UpMovement extends Movement {
		@Override
		public Point getPoint() {
			return new Point(0, -1);
		}
	}

	/**
	 * A movement one step to the bottom of the screen.
	 * @author Sebastian Schmidt
	 * @since 0.3.0
	 *
	 */
	public static final class DownMovement extends Movement {
		@Override
		public Point getPoint() {
			return new Point(0, 1);
		}
	}

	/**
	 * A movement one step to the left of the screen.
	 * @author Sebastian Schmidt
	 * @since 0.3.0
	 *
	 */
	public static final class LeftMovement extends Movement {
		@Override
		public Point getPoint() {
			return new Point(-1, 0);
		}
	}

	/**
	 * A movement one step to the right of the screen.
	 * @author Sebastian Schmidt
	 * @since 0.3.0
	 *
	 */
	public static final class RightMovement extends Movement {
		@Override
		public Point getPoint() {
			return new Point(1, 0);
		}
	}

	public static final Movement UP = new UpMovement();
	public static final Movement DOWN = new DownMovement();
	public static final Movement LEFT = new LeftMovement();
	public static final Movement RIGHT = new RightMovement();

	/**
	 * No external instantiation allowed.
	 */
	private Movement() {
	}

	/**
	 * Returns the point that represents this movement.
	 * @return The point that represents this movement.
	 */
	public abstract Point getPoint();

	/**
	 * Moves the given location.
	 * @param location The location.
	 * @return The moved location.
	 */
	public Point move(final Point location) {
		return location.add(getPoint());
	}
}
