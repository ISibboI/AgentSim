package de.isibboi.agentsim.algorithm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
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
	private static final class IntTreeValue implements Prioritized, Categorized, TemporalVariant {
		int _value;

		/**
		 * Creates a new object.
		 * @param value The value.
		 */
		IntTreeValue(final int value) {
			_value = value;
		}

		@Override
		public int getInformationRecordTime() {
			return 0;
		}

		@Override
		public CategorySet getCategorySet() {
			return null;
		}

		@Override
		public int getPriority() {
			return 0;
		}

		@Override
		public boolean equals(final Object o) {
			if (o instanceof IntTreeValue) {
				IntTreeValue i = (IntTreeValue) o;

				return i._value == _value;
			} else {
				return false;
			}
		}

		@Override
		public int hashCode() {
			return _value;
		}
	}

	private QuadTree<IntTreeValue> _tree;
	private Map<Point, IntTreeValue> _referenceMap;
	private Random _r;
	private List<Point> _points;

	/**
	 * Sets up the test cases.
	 */
	@Before
	public void setUp() {
		_tree = new QuadTree<>(1 << 5, 1 << 2, new Point(0, 0), new CategoryGroup());
		_referenceMap = new HashMap<>();

		_r = new Random(0x7367de42);

		// Random points
		_points = generateRandomPoints(1000, _r, _tree.getSideLength(), _tree.getSideLength());

		// Edge cases
		_points.addAll(generateCornerPoints(_tree.getSideLength(), _tree.getSideLength()));

		// Overwriting
		for (int i = 0; i < 10; i++) {
			_points.add(_points.get(_r.nextInt(_points.size())));
		}

		for (Point point : _points) {
			int element = _r.nextInt();
			_tree.insert(point, new IntTreeValue(element));
			_referenceMap.put(point, new IntTreeValue(element));
		}
	}

	/**
	 * Tests if inserted elements can be retrieved correctly.
	 */
	@Test
	public void testRandomInsertGet() {
		for (Point point : _points) {
			int element = _r.nextInt();
			_tree.insert(point, new IntTreeValue(element));
			_referenceMap.put(point, new IntTreeValue(element));
		}

		for (Point point : _points) {
			assertEquals(_referenceMap.get(point), _tree.get(point));
		}
	}

	/**
	 * Tests the size method of the quad tree.
	 */
	@Test
	public void testSize() {
		for (Point point : _points) {
			int element = _r.nextInt();
			_tree.insert(point, new IntTreeValue(element));
			_referenceMap.put(point, new IntTreeValue(element));
			assertEquals(_referenceMap.size(), _tree.size());
		}

		for (Point point : _points) {
			_tree.delete(point);
			_referenceMap.remove(point);
			assertEquals(_referenceMap.size(), _tree.size());
		}
	}

	/**
	 * Tests if the {@link QuadTree#selectRandomElement(Random)} method works.
	 */
	@Test
	public void testSelectRandomElement() {
		for (Point point : _points) {
			int element = _r.nextInt();
			_tree.insert(point, new IntTreeValue(element));
			_referenceMap.put(point, new IntTreeValue(element));
		}

		DeterministicRandom deterministicRandom = new DeterministicRandom();

		for (int i = 0; i < _tree.size(); i++) {
			deterministicRandom.setNextInt(i);
			Point selected = _tree.selectRandomElement(deterministicRandom).getLocation();
			assertNotNull(selected);
			assertNotEquals("i = " + i, null, _referenceMap.remove(selected));
		}
	}

	/**
	 * Tests if the {@link QuadTree#selectDistinctRandomElements(int, Random)} method works.
	 */
	@Test
	public void testSelectDistinctRandomElements() {
		Random deterministicRandom = new Random() {
			private static final long serialVersionUID = 1L;
			private int _nextInt = 0;

			@Override
			public int nextInt() {
				return _nextInt++;
			}

			@Override
			public int nextInt(final int bound) {
				return _nextInt++;
			}
		};

		Map<Point, IntTreeValue> map = new HashMap<>();
		map.putAll(_referenceMap);

		Collection<QuadTree.Entry<IntTreeValue>> selected = _tree.selectDistinctRandomElements(100, deterministicRandom);

		assertNotNull(selected);
		assertEquals(100, selected.size());

		for (QuadTree.Entry<IntTreeValue> entry : selected) {
			assertNotNull(map.remove(entry.getLocation()));
		}

		assertEquals(100, _referenceMap.size() - map.size());
	}

	/**
	 * Tests if the location to index and reverse conversion works.
	 * @throws ClassNotFoundException If the leaf class name has changed.
	 * @throws SecurityException Should not be thrown.
	 * @throws NoSuchMethodException If the constructor of leaf has changed.
	 * @throws InvocationTargetException Don't
	 * @throws IllegalArgumentException want
	 * @throws IllegalAccessException to
	 * @throws InstantiationException document
	 */
	@Test
	public void testLocationToIndexAndReverse() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		final Class<?> leafClass = Class.forName(QuadTree.class.getCanonicalName() + "$Leaf");

		// side length of 16, half side length of 8
		final Object leafObject = leafClass.getDeclaredConstructor(Integer.TYPE).newInstance(8);

		final Method locationToIndex = leafClass.getDeclaredMethod("locationToIndex", Point.Builder.class);
		final Method indexToLocation = leafClass.getDeclaredMethod("indexToLocation", Integer.TYPE);

		locationToIndex.setAccessible(true);
		indexToLocation.setAccessible(true);

		// -> [0, 16*16) to check.
		for (int i = 0; i < 256; i++) {
			Object result = indexToLocation.invoke(leafObject, i);
			assertEquals(i, locationToIndex.invoke(leafObject, result));
		}
	}

	/**
	 * Tests if the tree throws the correct exception if a insert location is out of bounds.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testInsertOutOfBounds() {
		_tree.insert(new Point(-1, -1), new IntTreeValue(3));
	}

	/**
	 * Tests if the tree throws the correct exception if a get location is out of bounds.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetOutOfBounds() {
		_tree.get(new Point(-1, -1));
	}

	/**
	 * Tests if the tree forbids null values by exception.
	 */
	@Test(expected = NullPointerException.class)
	public void testForbidNullValues() {
		_tree.insert(new Point(0, 0), null);
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