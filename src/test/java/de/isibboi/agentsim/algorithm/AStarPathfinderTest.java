package de.isibboi.agentsim.algorithm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.isibboi.agentsim.game.entities.Movement;
import de.isibboi.agentsim.game.map.Point;

/**
 * @author Sebastian Schmidt
 * @since 
 *
 */
public class AStarPathfinderTest {
	private AStarPathfinder _pathfinder;
	private BlockadeMap _map;

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
	}

	/**
	 * Tests if null is returned if there is not path from start to target.
	 */
	@Test
	public void testUnreachable() {
		List<Movement> path = _pathfinder.findPath(new Point(3, 0), new Point(4, 4), _map);
		assertNull("Path is not null.", path);
	}

	/**
	 * Checks if the algorithm finds the shortest path.
	 */
	@Test
	public void testShortestPath() {
		List<Movement> path = _pathfinder.findPath(new Point(0, 0), new Point(4, 4), _map);
		assertNotNull("Path is null.", path);
		assertEquals("Path is not the shortest one.", 8, path.size());
	}
}
