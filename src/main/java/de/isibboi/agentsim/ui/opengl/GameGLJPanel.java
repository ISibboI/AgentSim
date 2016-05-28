package de.isibboi.agentsim.ui.opengl;

import java.nio.FloatBuffer;
import java.util.Collection;
import java.util.HashSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jogamp.opengl.GL2ES3;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.GLBuffers;

import de.isibboi.agentsim.game.entities.Drawable;
import de.isibboi.agentsim.ui.renderer.Renderer;

/**
 * The JPanel that draws the game content using OpenGL.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 *
 */
public class GameGLJPanel implements GLEventListener {
	private static final Logger LOG = LogManager.getLogger(GameGLJPanel.class);

	private final GLJPanel _panel;
	private final HashSet<Drawable> _content = new HashSet<>();
	private Renderer _renderer;

	// Clear values
	private FloatBuffer _clearColor = GLBuffers.newDirectFloatBuffer(new float[] { 0, 0, 0, 1 });
	private FloatBuffer _clearDepth = GLBuffers.newDirectFloatBuffer(new float[] { 1 });

	public GameGLJPanel() {
		GLProfile glProfile = GLProfile.getGL4ES3();
		GLCapabilities glCapabilities = new GLCapabilities(glProfile);
		_panel = new GLJPanel(glCapabilities);

		// We want to draw stuff manually here!
		_panel.setIgnoreRepaint(true);

		_panel.addGLEventListener(this);

		// Recommended for performance reasons.
		// We can use the projection matrix for that if necessary.
		_panel.setSkipGLOrientationVerticalFlip(true);
	}

	public GLJPanel getJPanel() {
		return _panel;
	}

	@Override
	public synchronized void init(final GLAutoDrawable drawable) {
		LOG.info("Initialized GLJPanel");
	}

	@Override
	public void dispose(final GLAutoDrawable drawable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void display(final GLAutoDrawable drawable) {
		if (_renderer == null) {
			LOG.warn("Cannot render " + this + ", since _renderer is null");
			return;
		}

		LOG.info("Displaying GLJPanel");

		// Configure OpenGL
		GL4 gl = drawable.getGL().getGL4();

		gl.glClearBufferfv(GL2ES3.GL_COLOR, 0, _clearColor);
		gl.glClearBufferfv(GL2ES3.GL_DEPTH, 0, _clearDepth);

		for (Drawable d : _content) {
			if (d == null) {
				LOG.error("Drawables container contains null element!");
			}

			d.accept(_renderer);
		}

		LOG.info("Displayed GLJPanel");
	}

	@Override
	public void reshape(final GLAutoDrawable drawable, final int x, final int y, final int width, final int height) {
		LOG.info("Reshaped GLJPanel: x = " + x + ", y = " + y + ", width = " + width + ", height = " + height);

		GL4 gl = drawable.getGL().getGL4();
		gl.glViewport(x, y, width, height);
	}

	/**
	 * Returns the drawables container.
	 * Use this to add or remove drawables. 
	 * @return The drawables container.
	 */
	public Collection<Drawable> getDrawablesContainer() {
		return _content;
	}

	/**
	 * Sets the renderer to use for rendering.
	 * @param renderer The renderer.
	 */
	public void setRenderer(final Renderer renderer) {
		_renderer = renderer;
	}
}