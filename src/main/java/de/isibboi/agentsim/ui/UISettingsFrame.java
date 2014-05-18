package de.isibboi.agentsim.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

import de.isibboi.agentsim.Messages;
import de.isibboi.agentsim.Settings;
import de.isibboi.agentsim.ui.event.UIActionListener;
import de.isibboi.agentsim.ui.event.UserActionEvent;

/**
 * A frame for editing the game settings.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
@SuppressWarnings("serial")
public class UISettingsFrame extends JFrame implements WindowListener, ActionListener {
	private class DisplaySize {
		private final int _width;
		private final int _height;

		/**
		 * Creates a new {@link DisplaySize}.
		 * 
		 * @param width the width of the display.
		 * @param height the height of the display.
		 */
		public DisplaySize(final int width, final int height) {
			_width = width;
			_height = height;
		}

		/**
		 * @return the width.
		 */
		public int getWidth() {
			return _width;
		}

		/**
		 * @return the height.
		 */
		public int getHeight() {
			return _height;
		}

		@Override
		public String toString() {
			return _width + "x" + _height;
		}

		@Override
		public int hashCode() {
			return _width ^ _height << 16 ^ _height >>> 16;
		}

		@Override
		public boolean equals(final Object o) {
			if (o instanceof DisplaySize) {
				DisplaySize s = (DisplaySize) o;
				return s._width == _width && s._height == _height;
			} else {
				return false;
			}
		}
	}

	private final Settings _settings;
	private final UIActionListener _listener;

	private final JLabel _restartLabel;
	private final JTabbedPane _sectionsPane;

	private final JPanel _uiPanel;
	private final JLabel _uiSizeLabel;
	private final JComboBox<DisplaySize> _uiSizeValue;

	/**
	 * Creates a new {@link UISettingsFrame}.
	 * 
	 * @param settings the settings to edit.
	 * @param listener a listener to listen for the closing of this window.
	 */
	public UISettingsFrame(final Settings settings, final UIActionListener listener) {
		_settings = settings;
		_listener = listener;

		final Insets insets = new Insets(4, 4, 4, 4);
		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = insets;

		setLayout(new GridBagLayout());

		_restartLabel = new JLabel(Messages.getString(Messages.UI_SETTINGS_FRAME_RESTART_LABEL));
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(_restartLabel, gbc);

		/////////////////////////////////////////////////////////////////////////////////
		////  UI  ///////////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////

		_uiPanel = new JPanel(new GridBagLayout());

		_uiSizeLabel = new JLabel(Messages.getString(Messages.UI_SETTINGS_FRAME_UI_SIZE_LABEL));
		gbc.gridx = 0;
		gbc.gridy = 0;
		_uiPanel.add(_uiSizeLabel, gbc);

		DisplaySize[] displaySizes = new DisplaySize[] {
				new DisplaySize(640, 480),
				new DisplaySize(800, 600),
				new DisplaySize(1280, 720),
				new DisplaySize(1920, 1080),
				new DisplaySize(2560, 1440) };
		int selectedIndex = 0;
		DisplaySize currentDisplaySize = new DisplaySize(_settings.getInt(Settings.UI_WIDTH), _settings.getInt(Settings.UI_HEIGHT));
		
		for (int i = 0; i < displaySizes.length; i++) {
			if (displaySizes[i].equals(currentDisplaySize)) {
				selectedIndex = i;
				break;
			}
		}
		
		_uiSizeValue = new JComboBox<>(displaySizes);
		_uiSizeValue.setSelectedIndex(selectedIndex);
		gbc.gridx = 1;
		gbc.gridy = 0;
		_uiPanel.add(_uiSizeValue, gbc);
		_uiSizeValue.addActionListener(this);

		_sectionsPane = new JTabbedPane();
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(_sectionsPane, gbc);

		_sectionsPane.addTab(Messages.getString(Messages.UI_SETTINGS_FRAME_UI_TAB), _uiPanel);

		pack();
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		addWindowListener(this);
		setVisible(true);
	}

	@Override
	public void windowActivated(final WindowEvent e) {
	}

	@Override
	public void windowClosed(final WindowEvent e) {
	}

	@Override
	public void windowClosing(final WindowEvent e) {
		_listener.userAction(new UserActionEvent(this));
	}

	@Override
	public void windowDeactivated(final WindowEvent e) {
	}

	@Override
	public void windowDeiconified(final WindowEvent e) {
	}

	@Override
	public void windowIconified(final WindowEvent e) {
	}

	@Override
	public void windowOpened(final WindowEvent e) {
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		if (e.getSource() == _uiSizeValue) {
			_settings.set(Settings.UI_WIDTH, ((DisplaySize) _uiSizeValue.getSelectedItem()).getWidth());
			_settings.set(Settings.UI_HEIGHT, ((DisplaySize) _uiSizeValue.getSelectedItem()).getHeight());
		}
	}
}