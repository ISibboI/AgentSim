package de.isibboi.agentsim;

import de.isibboi.agentsim.ui.AgentFrame;

public class AgentSim implements Runnable {
	public static void main(final String[] args) {
		AgentSim sim = new AgentSim();
		sim.run();
	}

	private final AgentFrame _frame;
	private final Settings _settings;

	public AgentSim() {
		_settings = new Settings("/.settings");

		_frame = new AgentFrame(_settings);
	}

	@Override
	public void run() {
		while (true) {
			_frame.update();
			_frame.render();
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}