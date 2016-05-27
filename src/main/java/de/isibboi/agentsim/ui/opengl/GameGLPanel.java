package de.isibboi.agentsim.ui.opengl;

import javax.swing.JPanel;

import com.jogamp.opengl.GLCapabilities;
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
public class GameGLPanel {
	private final GLJPanel _panel;

	public GameGLPanel() {
		GLProfile glProfile = GLProfile.getDefault();
		GLCapabilities glCapabilities = new GLCapabilities(glProfile);
		_panel = new GLJPanel(glCapabilities);
	}

	public JPanel getJPanel() {
		return _panel;
	}
}