package de.isibboi.agentsim.ui;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;

import de.isibboi.agentsim.Settings;
import de.isibboi.agentsim.game.entities.Drawable;
import de.isibboi.agentsim.game.map.GameMap;
import de.isibboi.agentsim.game.map.Point;

/**
 * Represents the game ui.
 * 
 * @author Sebastian Schmidt
 * @since 0.2.0
 */
public class UI implements Drawable, MouseListener {
	private final Settings _settings;
	private final GameMap _map;
	
	private int _width;
	private int _height;
	
	private final FrequencyMeter _frameRateMeter;
	
	
	private final Renderer _renderer;
	private final Collection<Drawable> _drawables = new ArrayList<>();
	
	private UINumberLabel _frameRateLabel;
	private UINumberLabel _entityCountLabel;

	/**
	 * Creates a new ui with the given settings.
	 * 
	 * @param renderer The renderer used to draw the ui.
	 * @param settings The settings.
	 * @param map The game map.
	 */
	public UI(final Renderer renderer, final Settings settings, final GameMap map) {
		_settings = settings;
		_renderer = renderer;
		_map = map;
		
		_width = settings.getInt(Settings.UI_WIDTH);
		_height = settings.getInt(Settings.UI_HEIGHT);
		
		_frameRateMeter = new FrequencyMeter(0, 0.1);
		
		initUI();
	}

	/**
	 * Creates the ui.
	 */
	private void initUI() {
		_frameRateLabel = new UINumberLabel(_renderer, new Point(_width - 270, 10), 250, "Framerate: ", "", 1, 0);
		_drawables.add(_frameRateLabel);
		
		_entityCountLabel = new UINumberLabel(_renderer, new Point(_width - 270, 50), 250, "Entity count: ", "", 0, _settings.getInt(Settings.GAME_INITIAL_GOBLIN_COUNT));
		_drawables.add(_entityCountLabel);
	}

	@Override
	public void draw(final Graphics2D g) {
		// Measure frame rate.
		_frameRateMeter.update();
		_frameRateLabel.setValue(_frameRateMeter.getValue());
		
		// Measure entity count.
		_entityCountLabel.setValue(_map.getEntityCount());
		
		_renderer.setGraphics(g);
		for (Drawable d : _drawables) {
			d.draw(g);
		}
	}

	@Override
	public void mouseClicked(final MouseEvent e) {
	}

	@Override
	public void mouseEntered(final MouseEvent e) {
	}

	@Override
	public void mouseExited(final MouseEvent e) {
	}

	@Override
	public void mousePressed(final MouseEvent e) {
	}

	@Override
	public void mouseReleased(final MouseEvent e) {
	}

}
