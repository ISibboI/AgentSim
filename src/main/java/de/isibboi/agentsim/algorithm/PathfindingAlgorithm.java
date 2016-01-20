package de.isibboi.agentsim.algorithm;

import java.util.Queue;

import de.isibboi.agentsim.game.entities.Movement;
import de.isibboi.agentsim.game.map.Point;

/**
 * An algorithm that searches the shortest path between two points on the map.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public interface PathfindingAlgorithm {
	/**
	 * Finds the shortest path from start to target with the information provided by map.
	 * @param start The start point.
	 * @param target The target point.
	 * @param map The map.
	 * @return The shortest path from start to target, or null, if target is not reachable from start.
	 */
	Queue<Movement> findPath(Point start, Point target, BlockadeMap map);
}
