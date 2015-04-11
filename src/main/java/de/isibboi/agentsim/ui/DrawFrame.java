package de.isibboi.agentsim.ui;

import java.awt.BufferCapabilities;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A JPanel implementation that supports drawing on it's background.
 * @author Sebastian Schmidt
 * @since 0.0.0
 */
@SuppressWarnings("serial")
public class DrawFrame extends JFrame {
	private final Logger _log = LogManager.getLogger(DrawFrame.class);

	private boolean _isRendering = false;
	private Graphics2D _renderGraphics;
	private final AffineTransform _contentTransformation;
	private final AffineTransform _uiTransformation;

	private final BufferStrategy _bufferStrategy;

	/**
	 * Creates a new pane with the given dimensions.
	 * 
	 * @param title The window title.
	 * @param width The width of the pane.
	 * @param height The height of the pane.
	 * @param scale The scale of the content.
	 */
	public DrawFrame(final String title, final int width, final int height, final int scale) {
		super(title);

		JPanel contentPane = new JPanel();
		contentPane.setPreferredSize(new Dimension(width, height));
		setContentPane(contentPane);

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setIgnoreRepaint(true);
		setResizable(false);
		pack();

		createBufferStrategy(2);
		_bufferStrategy = getBufferStrategy();
		logBufferInfo();

		Insets frameInsets = getInsets();
		_contentTransformation = new AffineTransform(scale, 0, 0, scale, frameInsets.left, frameInsets.top);
		_uiTransformation = new AffineTransform(1, 0, 0, 1, frameInsets.left, frameInsets.top);
		//		_contentTransformation = new AffineTransform(scale, 0, 0, scale, 1, 1);
		//		_uiTransformation = new AffineTransform(1, 0, 0, 1, 1, 1);

		setVisible(true);
	}

	/**
	 * Starts the rendering of a frame.
	 * @return The {@link Graphics2D} object used for rendering.
	 */
	public Graphics2D startRender() {
		if (_isRendering) {
			throw new IllegalStateException("Rendering already started");
		}

		_isRendering = true;
		_renderGraphics = (Graphics2D) _bufferStrategy.getDrawGraphics();
		_renderGraphics.setTransform(_contentTransformation);

		return getRenderGraphics();
	}

	/**
	 * Removes the content scaling.
	 */
	public void switchToUIRender() {
		_renderGraphics.setTransform(_uiTransformation);
	}

	/**
	 * Finishes the rendering process and shows the rendered image.
	 */
	public void finishRender() {
		if (!_isRendering) {
			throw new IllegalStateException("Rendering was not started");
		}

		_isRendering = false;
		_renderGraphics.dispose();
		_renderGraphics = null;

		_bufferStrategy.show();
	}

	/**
	 *  Closes the frame and releases all system resources.
	 */
	public void close() {
		if (_isRendering) {
			finishRender();
		}

		_bufferStrategy.dispose();
	}

	/**
	 * Logs info about the graphic buffers used.
	 */
	private void logBufferInfo() {
		BufferCapabilities capabilities = _bufferStrategy.getCapabilities();

		_log.info("Page flipping supported: " + capabilities.isPageFlipping());

		if (capabilities.isPageFlipping()) {
			_log.info("Requires full screen for page flipping: " + capabilities.isFullScreenRequired());
		}
	}

	/**
	 * Returns the render graphics for the current frame, or null, if the rendering process has not been started or already finished.
	 * @return The render graphics.
	 */
	public Graphics2D getRenderGraphics() {
		return _renderGraphics;
	}
}
