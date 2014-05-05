package de.isibboi.agentsim.game.map;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * The material factory manages all materials. Materials have to have a unique color.
 * 
 * @author Sebastian Schmidt
 * @since 0.0.0
 */
public class MaterialFactory {
	private final Map<Integer, Material> _colorToMaterial = new HashMap<>();
	
	/**
	 * Adds a new material.
	 * 
	 * @param name The name of the material.
	 * @param color The color of the material.
	 * @param solid If this material is solid ( = entities can't walk over it).
	 * @param durability The durability of the material.
	 * @param generationParameters The generation parameters of the material.
	 * @return The newly added material.
	 */
	public Material addMaterial(final String name, final int color, final boolean solid, final int durability, final GenerationParameters generationParameters) {
		if (_colorToMaterial.containsKey(color)) {
			throw new IllegalArgumentException("Map already contains a material with color: " + color);
		}
		
		Material added = new Material(name, color, solid, durability, generationParameters);
		_colorToMaterial.put(color, added);
		return added;
	}
	
	/**
	 * Returns the material with the given colors.
	 * @param color The color.
	 * @return The material with the given color.
	 */
	public Material getMaterial(final int color) {
		int maskedColor = color & 0xffffff;
		
		Material result = _colorToMaterial.get(maskedColor);
		
		if (result == null) {
			throw new IllegalArgumentException("Material not found: " + maskedColor);
		}
		
		return result;
	}
	
	/**
	 * Returns a {@code Collection} of all materials.
	 * @return All materials.
	 */
	public Collection<Material> getAllMaterials() {
		return _colorToMaterial.values();
	}
}