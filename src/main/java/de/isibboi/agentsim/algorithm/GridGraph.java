package de.isibboi.agentsim.algorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.isibboi.agentsim.game.map.Point;

/**
 * Represents a grid graph that is dynamically built as nodes are requested.
 * A grid graph is a graph whose vertices can be accessed via a two dimensional integer coordinate.
 * Every vertex has four neighbours, the vertices above, under, left and right from it.
 * The grid graph supports holes. The location of the holes is provided by the {@link BlockadeMap}.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 *
 * @param <T> The data type stored in this graph.
 */
public class GridGraph<T> {
	private final Map<Point, T> _graph = new HashMap<>();
	private final BlockadeMap _blockadeMap;
	private final VertexDataFactory<T> _vertexDataFactory;

	/**
	 * Creates a new empty grid graph with the given blockade map.
	 * @param blockadeMap The blockade map.
	 * @param vertexDataFactory The factory used to create new vertex data.
	 */
	public GridGraph(final BlockadeMap blockadeMap, final VertexDataFactory<T> vertexDataFactory) {
		_blockadeMap = blockadeMap;
		_vertexDataFactory = vertexDataFactory;
	}

	/**
	 * Returns the vertex data at the given location.
	 * @param location The location.
	 * @return The data.
	 */
	public T getData(final Point location) {
		T data = _graph.get(location);

		if (data == null && !_blockadeMap.isBlocked(location)) {
			data = _vertexDataFactory.createVertexData(location);
			_graph.put(location, data);
		}

		return data;
	}

	/**
	 * Sets the vertex data at the given location.
	 * @param location The location.
	 * @param data The data.
	 */
	public void setVertex(final Point location, final T data) {
		if (!_blockadeMap.isBlocked(location)) {
			_graph.put(location, data);
		}
	}

	/**
	 * Returns a collection of the neighbours of the given vertex position.
	 * @param location The location.
	 * @return The neighbours of node.
	 */
	public Collection<T> getNeighbours(final Point location) {
		Collection<T> neighbours = new ArrayList<>(4);

		for (Point point : location.getNeighbours()) {
			T data = getData(point);

			if (data != null) {
				neighbours.add(data);
			}
		}

		return neighbours;
	}
}