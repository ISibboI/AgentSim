package de.isibboi.agentsim.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import javax.swing.JFrame;

import de.isibboi.agentsim.Settings;

/**
 * Renders the default look and feel of the game.
 * 
 * @author Sebastian Schmidt
 * @since 0.2.0
 */
public class DefaultRenderer implements Renderer {
	private final Font _font;
	private final int _textHeight;
	private int _textVerticalOffset;

	private Graphics2D _g;

	/**
	 * Constructs a new default renderer.
	 * 
	 * @param frame The JFrame this renderer should draw on.
	 * @param settings The settings. 
	 */
	public DefaultRenderer(final JFrame frame, final Settings settings) {
		_font = new Font(settings.get(Settings.UI_FONT_FAMILY), Font.PLAIN, 20);
		
		_textHeight = frame.getFontMetrics(_font).getHeight();
		_textVerticalOffset = -frame.getFontMetrics(_font).getDescent();
	}

	@Override
	public void setGraphics(final Graphics2D g) {
		_g = g;
	}

	@Override
	public void drawBox(final int x, final int y, final int width, final int height) {
		_g.setColor(Color.WHITE);
		_g.fillRect(x, y, width, height);
		_g.setColor(Color.BLACK);
		_g.drawRect(x, y, width, height);
	}

	@Override
	public void drawText(final String text, final int x, final int y) {
		_g.setFont(_font);
		_g.setColor(Color.BLACK);
		_g.drawString(text, x, y + _textVerticalOffset);
	}

	@Override
	public int getTextHeight() {
		return _textHeight;
	}
}