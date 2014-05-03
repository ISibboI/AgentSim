package de.isibboi.agentsim.ui;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import de.isibboi.agentsim.Environment;
import de.isibboi.agentsim.Settings;
import de.isibboi.agentsim.game.Entity;
import de.isibboi.agentsim.game.GameMap;
import de.isibboi.agentsim.noise.MapGenerator;

@SuppressWarnings("serial")
public class AgentFrame {
	private final DrawFrame _drawFrame;
	private final Settings _settings;

	private final List<Entity> _entities = new ArrayList<>();
	private GameMap _map;
	
	public AgentFrame(final Settings settings) {
		this._settings = settings;

		_drawFrame = new DrawFrame("Agent Sim version " + Environment.VERSION, settings.getInt(Settings.UI_WIDTH), settings.getInt(Settings.UI_HEIGHT));
		
		MapGenerator mapGenerator = new MapGenerator(settings.getInt(Settings.UI_WIDTH), settings.getInt(Settings.UI_HEIGHT));
		_map = mapGenerator.getMap();
	}
	
	public void update() {
		
	}
	
	public void render() {
		Graphics2D g = _drawFrame.startRender();
		
		_map.draw(g);
		
		for (Entity entity : _entities) {
			entity.draw(g);
		}
		
		_drawFrame.stopRender();
	}
}
