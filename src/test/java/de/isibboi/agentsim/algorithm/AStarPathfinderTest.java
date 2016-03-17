package de.isibboi.agentsim.algorithm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.junit.Before;
import org.junit.Test;

import de.isibboi.agentsim.game.entities.Movement;
import de.isibboi.agentsim.game.map.Point;

/**
 * Test case for the A* pathfinder.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public class AStarPathfinderTest {
	private AStarPathfinder _pathfinder;
	private BlockadeMap _map;
	private BlockadeMap _bigMap;

	/**
	 * Creates the path finder.
	 */
	@Before
	public void setUp() {
		_pathfinder = new AStarPathfinder();
		_map = new BooleanBlockadeMap(new boolean[][] {
				{ false, true, false, false, false },
				{ false, false, false, true, false },
				{ true, true, false, true, false },
				{ false, true, false, true, false },
				{ false, true, false, false, false } });

		boolean[][] bigMapArray = new boolean[100][100];

		for (int i = 20; i < 80; i++) {
			bigMapArray[i][50] = true;
			bigMapArray[i][20] = true;
			bigMapArray[30][i] = true;
		}

		_bigMap = new BooleanBlockadeMap(bigMapArray);
	}

	/**
	 * Tests if null is returned if there is not path from start to target.
	 */
	@Test
	public void testUnreachable() {
		Queue<Movement> path = _pathfinder.findPath(new Point(3, 0), new Point(4, 4), _map);
		assertNull("Path is not null.", path);
	}

	/**
	 * Checks if the algorithm finds the shortest path.
	 */
	@Test
	public void testShortestPath() {
		Queue<Movement> path = _pathfinder.findPath(new Point(0, 0), new Point(4, 4), _map);
		assertNotNull("Path is null.", path);
		assertEquals("Path is not the shortest one.", 8, path.size());
	}

	/** 
	 * Checks if the deterministic multi target algorithm finds the correct path.
	 * Correctness of this test depends on the correctness of the single target algorithm.
	 */
	@Test
	public void testDeterministicMultiTargetAStar() {
		Point start = new Point(40, 30);
		List<Point> points = new LinkedList<>();
		points.add(new Point(0, 0));
		points.add(new Point(41, 35));
		points.add(new Point(40, 60));
		points.add(new Point(20, 40));
		points.add(new Point(36, 88));
		points.add(new Point(79, 81));

		// Sort points by distance
		for (int i = 0; i < points.size() - 1; i++) {
			List<Point> sublist = points.subList(i, points.size());
			int minValue = _pathfinder.findPath(start, sublist.get(0), _bigMap).size();
			int minIndex = 0;

			for (int j = 1; j < sublist.size(); j++) {
				int value = _pathfinder.findPath(start, sublist.get(j), _bigMap).size();

				if (value < minValue) {
					minValue = value;
					minIndex = j;
				}
			}

			Point tmp = sublist.get(0);
			sublist.set(0, sublist.get(minIndex));
			sublist.set(minIndex, tmp);
		}

		for (int i = 0; i < points.size(); i++) {
			List<Point> sublist = points.subList(i, points.size());

			assertEquals(_pathfinder.findPath(start, sublist.get(0), _bigMap).size(), _pathfinder.findPath(start, sublist, _bigMap).size());
		}
	}
}