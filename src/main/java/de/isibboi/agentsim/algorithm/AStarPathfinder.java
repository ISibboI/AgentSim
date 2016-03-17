package de.isibboi.agentsim.algorithm;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

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
	 * @author Sebastian Schmidt
	 * @since 0.3.0
	 */
	private class AStarNode {
		private final Point _location;
		private int _distanceToTarget;
		private int _distanceFromStart;
		private int _costs;
		private boolean _closed = false;
		private AStarNode _predecessor;

		/**
		 * True if this node is a target.
		 */
		private boolean _target;

		/**
		 * Creates a new graph node.
		 * @param location The location of the node.
		 * @param distanceToTarget The shortest possible distance to target.
		 * @param distanceFromStart The distance from the start vertex to this vertex.
		 * @param predecessor The predecessor of this node.
		 */
		AStarNode(final Point location, final int distanceToTarget, final int distanceFromStart, final AStarNode predecessor) {
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
	private final Random _random = new Random();

	/**
	 * Creates a new object.
	 */
	public AStarPathfinder() {
	}

	@Override
	public Queue<Movement> findPath(final Point start, final Point target, final BlockadeMap map) {
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

	@Override
	public Queue<Movement> findPath(final Point start, final Iterable<Point> targets, final BlockadeMap map) {
		// 11 is taken from the OpenJDK implementation.
		_openList = new PriorityQueue<AStarNode>(11, new AStarNodeComparator());
		_graph = new GridGraph<>(map, new VertexDataFactory<AStarNode>() {
			@Override
			public AStarNode createVertexData(final Point location) {
				return new AStarNode(location, minimalManhattanDistance(location, targets), -1, null);
			}
		});

		final AStarNode startNode = new AStarNode(start, minimalManhattanDistance(start, targets), 0, null);
		startNode._closed = true;
		_openList.add(startNode);

		for (Point target : targets) {
			_graph.getData(target)._target = true;
		}

		AStarNode firstNodeReached = null;

		while (!_openList.isEmpty() && firstNodeReached == null) {
			AStarNode node = updateNode();

			if (node._target) {
				firstNodeReached = node;
			}
		}

		if (firstNodeReached != null) {
			LinkedList<Movement> result = getPath(firstNodeReached._location);
			Collections.reverse(result);
			return result;
		} else {
			return null;
		}
	}

	@Override
	public Queue<Movement> findPath(final Point start, final Iterable<Point> targets, final BlockadeMap map, final int max, final double maxDistanceFactor) {
		// 11 is taken from the OpenJDK implementation.
		_openList = new PriorityQueue<AStarNode>(11, new AStarNodeComparator());
		_graph = new GridGraph<>(map, new VertexDataFactory<AStarNode>() {
			@Override
			public AStarNode createVertexData(final Point location) {
				return new AStarNode(location, minimalManhattanDistance(location, targets), -1, null);
			}
		});

		final AStarNode startNode = new AStarNode(start, minimalManhattanDistance(start, targets), 0, null);
		startNode._closed = true;
		_openList.add(startNode);

		for (Point target : targets) {
			_graph.getData(target)._target = true;
		}

		List<AStarNode> targetsReached = new LinkedList<>();
		int maxCosts = -1;

		while (!_openList.isEmpty() && targetsReached.size() < max && (maxCosts == -1 || _openList.peek()._costs <= maxCosts)) {
			AStarNode node = updateNode();

			if (node._target) {
				targetsReached.add(node);

				if (maxCosts == -1) {
					maxCosts = (int) (node._distanceFromStart * maxDistanceFactor);
				}
			}
		}

		if (!targetsReached.isEmpty()) {
			LinkedList<Movement> result = getPath(targetsReached.get(_random.nextInt(targetsReached.size()))._location);
			Collections.reverse(result);
			return result;
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
	private LinkedList<Movement> getPath(final Point start) {
		return getPath(_graph.getData(start));
	}

	/**
	 * Traverses the graph starting with the start vertex following the predecessor-pointers.
	 * Stores all traversed vertices as movements.
	 * 
	 * @param start The start node.
	 * @return The path from start to target.
	 */
	private LinkedList<Movement> getPath(final AStarNode start) {
		LinkedList<Movement> path = new LinkedList<>();

		AStarNode last = start;

		while (last._predecessor != null) {
			AStarNode current = last._predecessor;
			path.add(Movement.getMovement(current._location.subtract(last._location)));
			last = current;
		}

		return path;
	}

	/**
	 * Updates the first node in the openList.
	 * @return The node that was updated.
	*/
	private AStarNode updateNode() {
		AStarNode node = _openList.poll();
		Collection<AStarNode> neighbours = _graph.getNeighbours(node._location);

		for (AStarNode neighbour : neighbours) {
			if (neighbour._predecessor == null || (!neighbour._closed && neighbour._distanceFromStart > node._distanceFromStart + 1)) {
				neighbour._distanceFromStart = node._distanceFromStart + 1;
				neighbour._predecessor = node;
				neighbour.updateCosts();

				_openList.remove(neighbour);
				_openList.add(neighbour);
			}
		}

		node._closed = true;
		return node;
	}

	/**
	 * Calculates the distance from the given location to the closest given target.
	 * 
	 * @param location The location.
	 * @param targets The targets to check.
	 * @return The distance to the closest target.
	 */
	private static int minimalManhattanDistance(final Point location, final Iterable<Point> targets) {
		int minDistance = Integer.MAX_VALUE;
		boolean error = true;

		for (Point target : targets) {
			int distance = location.manhattanDistance(target);
			error = false;

			if (distance < minDistance) {
				minDistance = distance;
			}
		}

		if (error) {
			throw new RuntimeException("targets must not be empty!");
		}

		return minDistance;
	}
}