package de.isibboi.agentsim.algorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

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
	/**
	 * Note: this class has a natural ordering that is inconsistent with equals.
	 * 
	 * @author Sebastian Schmidt
	 * @since 0.3.0
	 *
	 */
	private class AStarNode {
		private final Point _location;
		private int _distanceToTarget;
		private int _distanceFromStart;
		private int _costs;
		private boolean _closed = false;
		private AStarNode _predecessor;

		/**
		 * Creates a new graph node.
		 * @param location The location of the node.
		 * @param distanceToTarget The shortest possible distance to target.
		 * @param distanceFromStart The distance from the start vertex to this vertex.
		 * @param predecessor The predecessor of this node.
		 */
		public AStarNode(final Point location, final int distanceToTarget, final int distanceFromStart, final AStarNode predecessor) {
			_location = location;
			_distanceToTarget = distanceToTarget;
			_distanceFromStart = distanceFromStart;
			updateCosts();
			_predecessor = predecessor;
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

		/**
		 * Updates the costs of this vertex.
		 */
		public void updateCosts() {
			_costs = _distanceFromStart + _distanceToTarget;
		}
	}

	private static class AStarNodeComparator implements Comparator<AStarNode> {
		@Override
		public int compare(final AStarNode a, final AStarNode b) {
			// Less than <=> Shorter distance to target.
			return a._costs - b._costs;
		}
	}

	private GridGraph<AStarNode> _graph;
	private PriorityQueue<AStarNode> _openList; // Represents a min-heap.

	/**
	 * Creates a new object.
	 */
	public AStarPathfinder() {
	}

	@Override
	public List<Movement> findPath(final Point start, final Point target, final BlockadeMap map) {
		// 11 is taken from the OpenJDK implementation.
		_openList = new PriorityQueue<AStarNode>(11, new AStarNodeComparator());
		_graph = new GridGraph<>(map, new VertexDataFactory<AStarNode>() {
			@Override
			public AStarNode createVertexData(final Point location) {
				return new AStarNode(location, location.manhattanDistance(start), -1, null);
			}
		});

		final AStarNode startNode = new AStarNode(target, start.manhattanDistance(target), 0, null);
		startNode._closed = true;
		_openList.add(startNode);

		while (!_openList.isEmpty() && !_graph.getData(start)._closed) {
			updateNode();
		}

		if (_graph.getData(start)._closed) {
			return getPath(start);
		} else {
			return null;
		}
	}

	/**
	 * Traverses the graph starting with the start vertex following the predecessor-pointers.
	 * Stores all traversed vertices as movements.
	 * 
	 * @param start The start vertex location.
	 * @return The path from start to target.
	 */
	private List<Movement> getPath(final Point start) {
		List<Movement> path = new ArrayList<>();

		AStarNode last = _graph.getData(start);

		while (last._predecessor != null) {
			AStarNode current = last._predecessor;
			path.add(Movement.getMovement(current._location.subtract(last._location)));
			last = current;
		}

		return path;
	}

	/**
	 * Updates the first node in the openList.
	*/
	private void updateNode() {
		AStarNode node = _openList.poll();
		Collection<AStarNode> neighbours = _graph.getNeighbours(node._location);

		for (AStarNode neighbour : neighbours) {
			if (neighbour._predecessor == null || !neighbour._closed && neighbour._distanceFromStart > node._distanceFromStart + 1) {
				neighbour._distanceFromStart = node._distanceFromStart + 1;
				neighbour._predecessor = node;
				neighbour.updateCosts();

				_openList.remove(neighbour);
				_openList.add(neighbour);
			}
		}

		node._closed = true;
	}
}