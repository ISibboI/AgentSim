package de.isibboi.agentsim.game.map;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Sebastian Schmidt
 * @since 0.0.0
 */
public class MaterialFactory {
	private final Map<Integer, Material> _colorToMaterial = new HashMap<>();
	
	/**
	 * Adds a new material.
	 * @param name
	 * @param color
	 * @param solid
	 * @param durability
	 * @param generationParameters
	 */
	public void addMaterial(final String name, final int color, final boolean solid, final int durability, final GenerationParameters generationParameters) {
		if (_colorToMaterial.containsKey(color)) {
			throw new IllegalArgumentException("Map already contains a material with color: " + color);
		}
		
		_colorToMaterial.put(color, new Material(name, color, solid, durability, generationParameters));
	}
	
	public Material getMaterial(final int color) {
		int maskedColor = color & 0xffffff;
		
		Material result = _colorToMaterial.get(maskedColor);
		
		if (result == null) {
			throw new IllegalArgumentException("Material not found: " + maskedColor);
		}
		
		return result;
	}
	
	public Collection<Material> getAllMaterials() {
		return _colorToMaterial.values();
	}
}