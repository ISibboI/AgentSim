package de.isibboi.agentsim.ui;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.isibboi.agentsim.Environment;
import de.isibboi.agentsim.Settings;
import de.isibboi.agentsim.game.entities.Entity;
import de.isibboi.agentsim.game.entities.Goblin;
import de.isibboi.agentsim.game.map.GameMap;
import de.isibboi.agentsim.game.map.MapGenerator;

@SuppressWarnings("serial")
public class AgentFrame {
	private final Logger _log = LogManager.getLogger(getClass());
	
	private final DrawFrame _drawFrame;
	private final Settings _settings;

	private final List<Entity> _entities = new ArrayList<>();
	private GameMap _map;

	public AgentFrame(final Settings settings) {
		this._settings = settings;

		_drawFrame = new DrawFrame("Agent Sim version " + Environment.VERSION, settings.getInt(Settings.UI_WIDTH), settings.getInt(Settings.UI_HEIGHT), settings.getInt(Settings.GAME_SCALE));

		MapGenerator mapGenerator = new MapGenerator(
				settings.getInt(Settings.UI_WIDTH) / settings.getInt(Settings.GAME_SCALE),
				settings.getInt(Settings.UI_HEIGHT) / settings.getInt(Settings.GAME_SCALE));
		_map = mapGenerator.generateMap();
	}

	private void spawnGoblin() {
		Goblin goblin = new Goblin(_map, _map.getRandomValidLocationNearSpawnPoint(Integer.MAX_VALUE, _settings.getInt(Settings.GAME_SPAWN_RADIUS)));
		_entities.add(goblin);
	}

	public void spawnGoblins(final int amount) {
		for (int i = 0; i < amount; i++) {
			spawnGoblin();
		}
		
		_log.info("Spawned " + amount + " Goblins");
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
