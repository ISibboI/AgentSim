package de.isibboi.agentsim.game.map;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A 2 dimensional final point with integer coordinates.
 * 
 * @author Sebastian Schmidt
 * @since 0.2.0
 */
public class Point {
	/**
	 * A builder for the point.
	 * @author Sebastian Schmidt
	 * @since 0.2.0
	 */
	public static class Builder {
		private int _x;
		private int _y;

		/**
		 * Creates a new point builder with the given coordinates.
		 * 
		 * @param x The x initial coordinate.
		 * @param y The y initial coordinate.
		 */
		public Builder(final int x, final int y) {
			_x = x;
			_y = y;
		}

		/**
		 * Creates a new point builder with the coordinates (0, 0).
		 */
		public Builder() {
		}

		/**
		 * Copies the given builder.
		 * 
		 * @param builder A point builder.
		 */
		public Builder(final Builder builder) {

		}

		/**
		 * Creates a builder with the coordinates from the given point.
		 * @param point The initial coordinates.
		 */
		public Builder(final Point point) {
			_x = point.getX();
			_y = point.getY();
		}

		/**
		 * Builds a point from this builder.
		 * @return A point.
		 */
		public Point build() {
			return new Point(_x, _y);
		}

		/**
		 * Returns the current x coordinate.
		 * @return The current x coordinate.
		 */
		public int getX() {
			return _x;
		}

		/**
		 * Sets the x coordinate to the given value.
		 * @param x The new x coordinate.
		 */
		public void setX(final int x) {
			this._x = x;
		}

		/**
		 * Returns the current y coordinate.
		 * @return The current y coordinate.
		 */
		public int getY() {
			return _y;
		}

		/**
		 * Sets the y coordinate to the given value.
		 * @param y The new y coordinate.
		 */
		public void setY(final int y) {
			this._y = y;
		}

		/**
		 * Sets the x and the y coordinate to the given value.
		 * @param x The new x coordinate.
		 * @param y The new y coordinate.
		 */
		public void setXY(final int x, final int y) {
			_x = x;
			_y = y;
		}

		/**
		 * Adds the given values to this point.
		 * @param p The values.
		 */
		public void add(final Point p) {
			_x += p.getX();
			_y += p.getY();
		}

		/**
		 * Subtracts the given values from this point.
		 * @param p The values.
		 */
		public void sub(final Point p) {
			_x -= p.getX();
			_y -= p.getY();
		}
	}

	private final int _x;
	private final int _y;

	/**
	 * Creates a new point with the given coordinates.
	 * 
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 */
	public Point(final int x, final int y) {
		_x = x;
		_y = y;
	}

	/**
	 * Returns the x coordinate of the point.
	 * @return The x coordinate.
	 */
	public int getX() {
		return _x;
	}

	/**
	 * Returns the y coordinate of the point.
	 * @return The y coordinate.
	 */
	public int getY() {
		return _y;
	}

	@Override
	public String toString() {
		return "[" + _x + ", " + _y + "]";
	}

	@Override
	public int hashCode() {
		return _x ^ (_y << 16) ^ ((_y >> 16) & 0xFFFF);
	}

	@Override
	public boolean equals(final Object o) {
		if (o instanceof Point) {
			Point p = (Point) o;

			return p._x == _x && p._y == _y;
		} else {
			return false;
		}
	}

	/**
	 * Creates a new point that is the sum of this and the given point.
	 * @param other The other point.
	 * @return A sum of {@code this} and {@code other}.
	 */
	public Point add(final Point other) {
		return new Point(_x + other._x, _y + other._y);
	}

	/**
	 * Returns true if the given point is a neighbor of this point.
	 * That is if the two points have a shortest path of length one connecting them.
	 * @param other The other point.
	 * @return True if the other point is a neighbor of this point.
	 */
	public boolean isNeighborOf(final Point other) {
		return Math.abs(_y - other._y) + Math.abs(_x - other._x) == 1;
	}

	/**
	 * Returns the Manhattan distance from this point to the other point.
	 * @param other The other point.
	 * @return The Manhattan distance from this to other.
	 */
	public int manhattanDistance(final Point other) {
		return Math.abs(_x - other._x) + Math.abs(_y - other._y);
	}

	/**
	 * Returns a collection of all neighbours of this point.
	 * @return The neighbours of this point.
	 */
	public Collection<Point> getNeighbours() {
		Collection<Point> neighbours = new ArrayList<>(4);

		neighbours.add(new Point(_x, _y + 1));
		neighbours.add(new Point(_x, _y - 1));
		neighbours.add(new Point(_x + 1, _y));
		neighbours.add(new Point(_x - 1, _y));

		return neighbours;
	}

	/**
	 * Creates a new point that is the result of the subtraction this - other.
	 * @param other The point to subtract from this point.
	 * @return The result of the subtraction.
	 */
	public Point subtract(final Point other) {
		return new Point(_x - other._x, _y - other._y);
	}
}