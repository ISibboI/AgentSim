package de.isibboi.agentsim.ui;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;

import de.isibboi.agentsim.Settings;
import de.isibboi.agentsim.game.entities.Drawable;
import de.isibboi.agentsim.game.map.Point;

/**
 * Represents the game ui.
 * 
 * @author Sebastian Schmidt
 * @since 0.2.0
 */
public class UI implements Drawable, MouseListener {
	private final Settings _settings;
	
	private int _width;
	private int _height;
	
	private final FrequencyMeter _frameRateMeter;
	
	
	private final Renderer _renderer;
	private final Collection<Drawable> _drawables = new ArrayList<>();
	
	private UINumberLabel _frameRateLabel;

	/**
	 * Creates a new ui with the given settings.
	 * 
	 * @param renderer The renderer used to draw the ui.
	 * @param settings The settings.
	 */
	public UI(final Renderer renderer, final Settings settings) {
		_settings = settings;
		_renderer = renderer;
		
		_width = settings.getInt(Settings.UI_WIDTH);
		_height = settings.getInt(Settings.UI_HEIGHT);
		
		_frameRateMeter = new FrequencyMeter(0, 0.1);
		
		initUI();
	}

	/**
	 * Creates the ui.
	 */
	private void initUI() {
		_frameRateLabel = new UINumberLabel(_renderer, new Point(_width - 220, 10), 200, "Framerate: ", "", 1, 0);
		_drawables.add(_frameRateLabel);
	}

	@Override
	public void draw(final Graphics2D g) {
		// Measure frame rate.
		_frameRateMeter.update();
		_frameRateLabel.setValue(_frameRateMeter.getValue());
		
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
