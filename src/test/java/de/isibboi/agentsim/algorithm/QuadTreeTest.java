package de.isibboi.agentsim.algorithm;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import de.isibboi.agentsim.game.map.Point;

/**
 * @author Sebastian Schmidt
 * @since 0.3.0
 *
 */
public class QuadTreeTest {
	private QuadTree<Integer> _bigTree;
	private QuadTree<Integer> _smallTree;
	private Map<Point, Integer> _referenceMap;

	/**
	 * Sets up the test cases.
	 */
	@Before
	public void setUp() {
		_bigTree = new QuadTree<>(1 << 20, 1 << 8, new Point(0, 0));
		_smallTree = new QuadTree<>(1 << 5, 1 << 2, new Point(0, 0));
		_referenceMap = new HashMap<>();
	}

	/**
	 * Tests if inserted elements can be retrieved correctly.
	 */
	@Test
	public void testRandomInsertGet() {
		Random r = new Random(0x7367de42);

		// Random points
		List<Point> points = generateRandomPoints(1000, r, _smallTree.getSideLength(), _smallTree.getSideLength());

		// Edge cases
		points.addAll(generateCornerPoints(_smallTree.getSideLength(), _smallTree.getSideLength()));

		// Overwriting
		for (int i = 0; i < 10; i++) {
			points.add(points.get(r.nextInt(points.size())));
		}

		for (Point point : points) {
			int element = r.nextInt();
			_smallTree.insert(point, element);
			_referenceMap.put(point, element);
		}

		for (Point point : points) {
			assertEquals(_referenceMap.get(point), _smallTree.get(point));
		}
	}

	/**
	 * Generates random points.
	 * @param amount The amount of points to generate.
	 * @param r Source of randomness used for generation.
	 * @param maxX The maximum x value, exclusive.
	 * @param maxY The maximum y value, exclusive.
	 * @return A list of random points.
	 */
	private List<Point> generateRandomPoints(final int amount, final Random r, final int maxX, final int maxY) {
		List<Point> result = new ArrayList<>(amount);

		for (int i = 0; i < amount; i++) {
			result.add(new Point(r.nextInt(maxX), r.nextInt(maxY)));
		}

		return result;
	}

	/**
	 * Generates the points at the four corners of the given rectangle.
	 * @param maxX The maximum x value, exclusive.
	 * @param maxY The maximum y value, exclusive.
	 * @return A list of corner points.
	 */
	private List<Point> generateCornerPoints(final int maxX, final int maxY) {
		List<Point> result = new ArrayList<>(4);

		result.add(new Point(0, 0));
		result.add(new Point(maxX - 1, 0));
		result.add(new Point(0, maxY - 1));
		result.add(new Point(maxX - 1, maxY - 1));

		return result;
	}
}