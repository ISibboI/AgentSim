package de.isibboi.agentsim.game.map;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MaterialFactory {
	private final Map<Integer, Material> _colorToMaterial = new HashMap<>();
	
	public void addMaterial(final int color, final GenerationParameters generationParameters) {
		if (_colorToMaterial.containsKey(color)) {
			throw new IllegalArgumentException("Map already contains a material with color: " + color);
		}
		
		_colorToMaterial.put(color, new Material(color, generationParameters));
	}
	
	public Material getMaterial(final int color) {
		Material result = _colorToMaterial.get(color);
		
		if (result == null) {
			throw new IllegalArgumentException("Material not found: " + color);
		}
		
		return result;
	}
	
	public Collection<Material> getAllMaterials() {
		return _colorToMaterial.values();
	}
}