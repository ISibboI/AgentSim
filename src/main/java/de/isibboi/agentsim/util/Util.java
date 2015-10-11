package de.isibboi.agentsim.util;

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import de.isibboi.agentsim.game.map.Point;

/**
 * A bunch of functions needed everywhere around the code.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 *
 */
public final class Util {
	/**
	 * Utility class.
	 */
	private Util() {
	}

	/**
	 * Creates a rectangle from the given points.
	 * 
	 * @param a Point a.
	 * @return The unambiguous rectangle having a and b as corners.
	 * @param b Point b.
	 */
	public static Rectangle createRectangle(final Point a, final Point b) {
		return new Rectangle(Math.min(a.getX(), b.getX()), Math.min(a.getY(), b.getY()), Math.abs(a.getX() - b.getX()), Math.abs(a.getY() - b.getY()));
	}

	/**
	 * Creates a rectangle from the given points.
	 * 
	 * @param a Point a.
	 * @return The unambiguous rectangle having a and b as corners.
	 * @param b Point b.
	 */
	public static Rectangle2D.Double createRectangle(final Point2D.Double a, final Point2D.Double b) {
		return new Rectangle2D.Double(Math.min(a.getX(), b.getX()), Math.min(a.getY(), b.getY()), Math.abs(a.getX() - b.getX()), Math.abs(a.getY() - b.getY()));
	}
}
