package de.isibboi.agentsim.game.entities.ai.knowledge;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.isibboi.agentsim.algorithm.DeterministicRandom;
import de.isibboi.agentsim.algorithm.TemporalVariant;
import de.isibboi.agentsim.game.map.Point;

/**
 * @author Sebastian Schmidt
 * @since 0.3.0
 *
 */
public class MapKnowledgeTreeTest {
	private static CategoryGroup EMPTY_CATEGORY_GROUP;
	private static CategoryGroup CATEGORY_GROUP;

	private static final class IntTreeValue implements Prioritized, Categorized, TemporalVariant {
		int _value;
		int _priority;
		CategorySet _categorySet = new BitMapCategorySet(EMPTY_CATEGORY_GROUP);

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
			return _categorySet;
		}

		@Override
		public int getPriority() {
			return _priority;
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

	/**
	 * Sets up the static variables.
	 */
	@BeforeClass
	public static void setUpBeforeClass() {
		CategoryGroup.Builder builder = new CategoryGroup.Builder();

		builder.add("a");
		builder.add("b");
		builder.add("c");
		builder.add("d");
		builder.add("e");
		builder.add("f");

		CATEGORY_GROUP = builder.build();
		EMPTY_CATEGORY_GROUP = new CategoryGroup.Builder().build();
	}

	private MapKnowledgeTree<IntTreeValue> _tree;
	private Map<Point, IntTreeValue> _referenceMap;
	private Random _r;
	private List<Point> _points;

	/**
	 * Sets up the test cases.
	 */
	@Before
	public void setUp() {
		_tree = new MapKnowledgeTree<>(1 << 5, 1 << 2, new Point(0, 0), EMPTY_CATEGORY_GROUP);
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
	 * Tests if the {@link MapKnowledgeTree#selectRandomElement(Random)} method works.
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
	 * Tests if the {@link MapKnowledgeTree#selectDistinctRandomElements(int, Random)} method works.
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

		Collection<MapKnowledgeTree.Entry<IntTreeValue>> selected = _tree.selectDistinctRandomElements(100, deterministicRandom);

		assertNotNull(selected);
		assertEquals(100, selected.size());

		for (MapKnowledgeTree.Entry<IntTreeValue> entry : selected) {
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
		final Class<?> leafClass = Class.forName(MapKnowledgeTree.class.getCanonicalName() + "$Leaf");

		// side length of 16, half side length of 8
		final Object leafObject = leafClass.getDeclaredConstructor(Integer.TYPE, CategoryGroup.class).newInstance(8, EMPTY_CATEGORY_GROUP);

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
	 * Tests if the priority is correctly handled when inserting and deleting elements.
	 */
	@Test
	public void testPrioritySummation() {
		int priority = 0;

		for (Point p : _points) {
			IntTreeValue value = new IntTreeValue(_r.nextInt(100));
			value._priority = _r.nextInt(100);
			priority += value._priority;
			value = _tree.insert(p, value);

			if (value != null) {
				priority -= value._priority;
			}

			assertEquals(priority, _tree.getPriority());
		}

		Collections.shuffle(_points, _r);

		for (Point p : _points) {
			IntTreeValue value = _tree.delete(p);

			if (value != null) {
				priority -= value._priority;
			}

			assertEquals(priority, _tree.getPriority());
		}
	}

	/**
	 * Tests if the categories are summarized correctly.
	 */
	@Test
	public void testCategorySummation() {
		Point pa = new Point(0, 2);
		Point pb = new Point(17, 4);
		Point pc = new Point(17, 5);
		IntTreeValue va = new IntTreeValue(4);
		IntTreeValue vb = new IntTreeValue(5);
		IntTreeValue vc = new IntTreeValue(6);

		CategoryGroup.Builder builder = new CategoryGroup.Builder();
		builder.add("x");
		builder.add("y");
		builder.add("z");
		CategoryGroup categoryGroup = builder.build();
		_tree = new MapKnowledgeTree<>(1 << 5, 1 << 2, new Point(0, 0), categoryGroup);

		va._categorySet = new BitMapCategorySet(categoryGroup);
		vb._categorySet = new BitMapCategorySet(categoryGroup);
		vc._categorySet = new BitMapCategorySet(categoryGroup);

		va._categorySet.add(categoryGroup.getCategory(0));
		va._categorySet.add(categoryGroup.getCategory(1));
		va._categorySet.add(categoryGroup.getCategory(2));
		vb._categorySet.add(categoryGroup.getCategory(1));

		_tree.insert(pa, va);
		_tree.insert(pb, vb);
		_tree.insert(pc, vc);

		CategoryMultiset referenceSet = new ArrayCategoryMultiset(categoryGroup);
		referenceSet.setCount(categoryGroup.getCategory(0), 1);
		referenceSet.setCount(categoryGroup.getCategory(1), 2);
		referenceSet.setCount(categoryGroup.getCategory(2), 1);

		assertEquals(referenceSet, _tree.getCategorySet());

		_tree.delete(pa);
		_tree.delete(pb);
		_tree.delete(pc);

		assertEquals(new ArrayCategoryMultiset(categoryGroup), _tree.getCategorySet());
	}

	/**
	 * Tests if selecting objects by category works.
	 */
	@Test
	public void testCategorySelection() {
		// TODO implement
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