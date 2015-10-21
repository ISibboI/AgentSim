package de.isibboi.agentsim.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.font.TextAttribute;
import java.util.Collections;

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
	private final Font _buttonFont;
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
		_buttonFont = _font.deriveFont(Collections.singletonMap(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON));

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

	@Override
	public void drawButtonText(final String text, final int x, final int y) {
		_g.setFont(_buttonFont);
		_g.setColor(Color.BLACK);
		_g.drawString(text, x, y + _textVerticalOffset);
	}

	@Override
	public void drawHoveredBox(final int x, final int y, final int width, final int height) {
		_g.setColor(new Color(230, 230, 230));
		_g.fillRect(x, y, width, height);
		_g.setColor(Color.BLACK);
		_g.drawRect(x, y, width, height);
	}

	@Override
	public void drawClickedBox(final int x, final int y, final int width, final int height) {
		_g.setColor(new Color(230, 230, 230));
		_g.fillRect(x, y, width, height);
		_g.setColor(Color.BLACK);

		Stroke oldStroke = _g.getStroke();
		int additionalThickness = 1;
		_g.setStroke(new BasicStroke(additionalThickness + 1f));
		_g.drawRect(x + (int) Math.ceil(additionalThickness / 2.0f), y + (int) Math.ceil(additionalThickness / 2.0f),
				width - additionalThickness, height - additionalThickness);
		_g.setStroke(oldStroke);
	}

	@Override
	public void drawEntitySelectionRectangle(final Rectangle rectangle) {
		_g.setColor(new Color(230, 20, 20));
		_g.draw(rectangle);
	}
}