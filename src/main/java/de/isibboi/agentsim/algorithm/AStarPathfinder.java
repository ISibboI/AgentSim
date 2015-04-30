package de.isibboi.agentsim.algorithm;

import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import de.isibboi.agentsim.game.entities.Movement;
import de.isibboi.agentsim.game.map.Point;

/**
 * Calculates a shortest path from start to target using the A* algorithm.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 *
 */
public class AStarPathfinder implements PathfindingAlgorithm {
	private class AStarNode implements Comparable<AStarNode> {
		private final Point _location;
		private final int _distanceToTarget;

		/**
		 * Creates a new graph node.
		 * @param location The location of the node.
		 * @param distanceToTarget The shortest possible distance to target.
		 */
		public AStarNode(final Point location, final int distanceToTarget) {
			_location = location;
			_distanceToTarget = distanceToTarget;
		}

		@Override
		public boolean equals(final Object o) {
			if (o instanceof AStarNode) {
				return _location.equals(((AStarNode) o)._location);
			}

			return false;
		}

		@Override
		public int hashCode() {
			return _location.hashCode();
		}

		@Override
		public int compareTo(final AStarNode o) {
			// Less than <=> Shorter distance to target.
			return _distanceToTarget - o._distanceToTarget;
		}
	}

	private final Set<AStarNode> _graph;

	/**
	 * Creates a new object.
	 */
	public AStarPathfinder() {
		_graph = new HashSet<>();
	}

	@Override
	public List<Movement> findPath(final Point start, final Point target, final PathfindingMap map) {
		Set<AStarNode> closedList = new HashSet<>();
		PriorityQueue<AStarNode> openList = new PriorityQueue<>(); // Represents a min-heap.
	}
}