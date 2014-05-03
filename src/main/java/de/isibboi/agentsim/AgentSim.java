package de.isibboi.agentsim;

import javax.swing.JFrame;

import de.isibboi.agentsim.ui.AgentFrame;

public class AgentSim {
	public static void main(String[] args) {
		AgentSim sim = new AgentSim();
	}
	
	private final AgentFrame frame;
	private final Settings settings;
	
	public AgentSim() {
		settings = new Settings("/.settings");
		
		frame = new AgentFrame(settings);
		frame.setVisible(true);
	}
}