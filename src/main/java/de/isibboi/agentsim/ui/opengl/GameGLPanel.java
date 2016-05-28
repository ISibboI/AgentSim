package de.isibboi.agentsim.ui.opengl;

import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;

/**
 * The JPanel that draws the game content using OpenGL.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 *
 */
public class GameGLPanel implements GLEventListener {
	private static final Logger LOG = LogManager.getLogger(GameGLPanel.class);

	private final GLJPanel _panel;

	public GameGLPanel() {
		GLProfile glProfile = GLProfile.getDefault();
		GLCapabilities glCapabilities = new GLCapabilities(glProfile);
		_panel = new GLJPanel(glCapabilities);
		_panel.addGLEventListener(this);
	}

	public JPanel getJPanel() {
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
		LOG.info("Displayed GLJPanel");
	}

	@Override
	public void reshape(final GLAutoDrawable drawable, final int x, final int y, final int width, final int height) {
		LOG.info("Reshaped GLJPanel: x = " + x + ", y = " + y + ", width = " + width + ", height = " + height);
	}
}