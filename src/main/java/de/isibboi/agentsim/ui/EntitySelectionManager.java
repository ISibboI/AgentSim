package de.isibboi.agentsim.ui;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import de.isibboi.agentsim.Settings;
import de.isibboi.agentsim.game.Game;
import de.isibboi.agentsim.game.entities.Drawable;
import de.isibboi.agentsim.game.entities.Entity;
import de.isibboi.agentsim.game.entities.MapEntity;
import de.isibboi.agentsim.game.map.Point;
import de.isibboi.agentsim.ui.event.MouseButton;
import de.isibboi.agentsim.ui.event.UIMouseInputListener;
import de.isibboi.agentsim.util.Util;

/**
 * Manages the selection of entities by the user.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public class EntitySelectionManager implements UIMouseInputListener, Drawable {
	/**
	 * The selection mode.
	 * Different possibilities of handling selection of a certain area.
	 * 
	 * @author Sebastian Schmidt
	 * @since 0.3.0
	 */
	public static enum SelectionMode {
		/**
		 * Every entity that is touched by the selection area is selected.
		 */
		TOUCH,

		/**
		 * Every entity whose center is inside the selection area is selected.
		 */
		CENTER,

		/**
		 * Every entity that is completely inside of the selection area is selected. 
		 */
		COMPLETE
	}

	/**
	 * Contains all selected entities.
	 * Handles the setting and clearing of the isSelected flag of the entities.
	 * 
	 * @author Sebastian Schmidt
	 * @since 0.3.0
	 */
	private static class SelectedEntities {
		private final Set<Entity> _selectedEntities = new HashSet<>();

		/**
		 * Adds the given entity to the selected entities.
		 * @param entity The entity.
		 */
		public void add(final Entity entity) {
			entity.setSelected(true);
			_selectedEntities.add(entity);
		}

		/**
		 * Removes the given entity from the selected entities.
		 * @param entity The entity.
		 */
		public void remove(final Entity entity) {
			entity.setSelected(false);
			_selectedEntities.remove(entity);
		}

		/**
		 * Adds the given entities to the selected entities.
		 * @param entities The entities.
		 */
		public void addAll(final Collection<Entity> entities) {
			for (Entity entity : entities) {
				entity.setSelected(true);
			}

			_selectedEntities.addAll(entities);
		}

		/**
		 * Removes all entities from the selected entities.
		 */
		public void removeAll() {
			for (Entity entity : _selectedEntities) {
				entity.setSelected(false);
			}

			_selectedEntities.clear();
		}

		/**
		 * Returns all selected entities.
		 * @return All selected entities.
		 */
		public Set<Entity> getAll() {
			return Collections.unmodifiableSet(_selectedEntities);
		}
	}

	private final Game _game;
	private final SelectedEntities _selectedEntities = new SelectedEntities();
	private final SelectionMode _selectionMode;
	private final Renderer _renderer;

	private Point _startingPoint;
	private Point _currentPoint;

	/**
	 * Creates a new object managing the selection of the given entities.
	 * @param game The game.
	 * @param settings The settings to read the selection mode from.
	 * @param renderer The renderer.
	 */
	public EntitySelectionManager(final Game game, final Settings settings, final Renderer renderer) {
		Objects.requireNonNull(game);
		Objects.requireNonNull(settings);
		Objects.requireNonNull(renderer);

		_game = game;
		_selectionMode = settings.getEnumConstant(Settings.UI_SELECTION_MODE, SelectionMode.class);
		_renderer = renderer;
	}

	@Override
	public void mouseMoved(final Point oldPosition, final Point newPosition) {
		_currentPoint = newPosition;
	}

	@Override
	public void mouseClicked(final Point position, final MouseButton button, final boolean buttonDown) {
		if (buttonDown) {
			_startingPoint = position;
		} else {
			if (_startingPoint.equals(position)) {
				selectPoint(position);
			} else {
				selectRectangle(_startingPoint, position);
			}

			_startingPoint = null;
		}
	}

	/**
	 * Selects all entities in the rectangle defined by a and b.
	 * 
	 * @param a Point a.
	 * @param b Point b.
	 */
	private void selectRectangle(final Point a, final Point b) {
		_selectedEntities.removeAll();
		_selectedEntities.addAll(getEntitiesInRectangle(a, b));
	}

	/**
	 * Returns all entities in the rectangle defined by a and b.
	 * 
	 * @param a Point a.
	 * @param b Point b.
	 * @return All entities in the rectangle defined by a and b.
	 */
	private Collection<Entity> getEntitiesInRectangle(final Point a, final Point b) {
		final int minX = Math.min(a.getX(), b.getX());
		final int maxX = Math.max(a.getX(), b.getX());
		final int minY = Math.min(a.getY(), b.getY());
		final int maxY = Math.max(a.getY(), b.getY());
		final Collection<Entity> result = new ArrayList<>();

		for (Entity entity : _game.getEntities()) {
			if (!(entity instanceof MapEntity)) {
				continue;
			}

			Rectangle2D.Double bounds = ((MapEntity) entity).getBounds();

			switch (_selectionMode) {
			case TOUCH:
				if (maxX >= bounds.getMinX() && minX <= bounds.getMaxX() && maxY >= bounds.getMinY() && minY <= bounds.getMaxY()) {
					result.add(entity);
				}

				break;

			case CENTER:
				if (maxX >= bounds.getCenterX() && minX <= bounds.getCenterX() && maxY >= bounds.getCenterY() && minY <= bounds.getCenterY()) {
					result.add(entity);
				}

				break;

			case COMPLETE:
				if (maxX >= bounds.getMaxX() && minX <= bounds.getMinX() && maxY >= bounds.getMaxY() && minY <= bounds.getMinY()) {
					result.add(entity);
				}

				break;

			default:
				throw new IllegalStateException("Unknown SelectionMode: " + _selectionMode);
			}
		}

		return result;
	}

	/**
	 * Selects the first entity found at the given position.
	 * @param position The position.
	 */
	private void selectPoint(final Point position) {
		_selectedEntities.removeAll();
		final Point2D.Double position2d = new Point2D.Double(position.getX(), position.getY());

		for (Entity entity : _game.getEntities()) {
			if (!(entity instanceof MapEntity)) {
				continue;
			}

			if (((MapEntity) entity).getBounds().contains(position2d)) {
				_selectedEntities.add(entity);

				System.out.println(((MapEntity) entity).getBounds());

				break;
			}
		}
	}

	/**
	 * Returns the currently selected entities.
	 * @return The selected entities.
	 */
	public Collection<Entity> getSelectedEntities() {
		return _selectedEntities.getAll();
	}

	/**
	 * Returns the entities are inside of the users currently unfinished selection, like a rectangle she is currently dragging.
	 * @return The entities in the current unfinished selection.
	 */
	public Collection<Entity> getCurrentUnfinishedSelection() {
		if (_startingPoint == null) {
			return Collections.emptySet();
		}

		return getEntitiesInRectangle(_startingPoint, _currentPoint);
	}

	@Override
	public void draw(final Graphics2D g, final double transition) {
		if (_startingPoint != null) {
			_renderer.drawEntitySelectionRectangle(Util.createRectangle(_startingPoint, _currentPoint));
		}
	}
}
