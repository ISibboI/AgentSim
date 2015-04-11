package de.isibboi.agentsim.game.map;


/**
 * Represents a block on the map. Can be changed by the entities.
 * @author Sebastian Schmidt
 * @since 0.2.0
 */
public class Block {
	private final Material _material;

	/**
	 * Creates a new block from the given material. The material contains the initial state of the block.
	 * @param material The material the block is made of.
	 */
	public Block(final Material material) {
		_material = material;
	}

	/**
	 * Returns the material of this block.
	 * @return The material.
	 */
	public Material getMaterial() {
		return _material;
	}
}