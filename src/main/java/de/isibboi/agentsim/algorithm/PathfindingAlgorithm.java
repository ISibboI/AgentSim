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

	/**
	 * Finds the shortest path from start to the closest target with the information provided by map.
	 * @param start The start point.
	 * @param targets The target points.
	 * @param map The map.
	 * @return The shortest path from start to the closest target, or null, if no target is reachable from start.
	 */
	Queue<Movement> findPath(Point start, Iterable<Point> targets, BlockadeMap map);

	/**
	 * Finds the shortest paths from start to the closest n targets with the information provided by map.
	 * The algorithm returns one of the paths with equal probability.
	 * 
	 * @param start The start point.
	 * @param targets The target points.
	 * @param map The map.
	 * @param max The maximum amount of targets to reach.
	 * @param maxDistanceFactor The maximum distance of targets to check.
	 * 		When the closest target has a distance of {@code x}, no targets further away than {@code maxDistanceFactor * x} will be reached.
	 * @return The shortest path from start to the closest target, or null, if no target is reachable from start.
	 */
	Queue<Movement> findPath(Point start, Iterable<Point> targets, BlockadeMap map, int max, double maxDistanceFactor);
}
