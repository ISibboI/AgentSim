package de.isibboi.agentsim.game.map;

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
		return _x ^ (_y << 16);
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
}