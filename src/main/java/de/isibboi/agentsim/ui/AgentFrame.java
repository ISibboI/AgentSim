package de.isibboi.agentsim.ui;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import de.isibboi.agentsim.Environment;
import de.isibboi.agentsim.Settings;

@SuppressWarnings("serial")
public class AgentFrame extends JFrame {
	private final DrawPane drawPane;
	private final Settings settings;
	
	public AgentFrame(Settings settings) {
		super("Agent Sim version " + Environment.VERSION);
		this.settings = settings;
		
		drawPane = new DrawPane(settings.getInt(Settings.UI_WIDTH), settings.getInt(Settings.UI_HEIGHT));
		setContentPane(drawPane);
		
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		pack();
	}
}
