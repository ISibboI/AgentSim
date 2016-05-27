package de.isibboi.agentsim.ui.opengl;

import java.awt.Dimension;

import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;

// TODO This comment is bad
/**
 * The JPanel that draws the OpenGL stuff.
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

		_panel.setPreferredSize(new Dimension(300, 300));
	}

	public JPanel getJPanel() {
		return _panel;
	}

	@Override
	public synchronized void init(GLAutoDrawable drawable) {
		LOG.info("Initialized GLJPanel");
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void display(GLAutoDrawable drawable) {
		LOG.info("Displayed GLJPanel");
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		// TODO Auto-generated method stub

	}
}