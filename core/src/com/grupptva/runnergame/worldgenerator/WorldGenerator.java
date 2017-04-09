package com.grupptva.runnergame.worldgenerator;

import java.util.List;

public class WorldGenerator {
	/**
	 * The offsets compared to the "current tile" that the character can attach
	 * it's hook to.
	 */
	List<Integer[]> hookAttachOffsets;
	/**
	 * The offsets compared to the "current tile" that the character can jump
	 * and land on.
	 */
	List<Integer[]> jumpOffsets;
	/**
	 * The offsets compared to the "current tile"-which the hook is attached to
	 * that the character can land on.
	 * 
	 * TODO: This might depend on the radius of the hook and will probably cause
	 * issues later, need to figure out a fix.
	 */
	List<Integer[]> hookJumpOffsets;
	Long seed;

	/**
	 * Creates a new WorldGenerator, parameters are the coordinates in the grid
	 * that the character can reach in different ways, in relation the the tile
	 * the character is currently standing on, or attached to. The offset should
	 * be in integers that represent the index of the tiles in question inside
	 * of a chunk.
	 * 
	 * @param hookAttachOffsets
	 * @param jumpOffsets
	 * @param hookJumpOffsets
	 * @param seed
	 */
	public WorldGenerator(List<Integer[]> hookAttachOffsets, List<Integer[]> jumpOffsets,
			List<Integer[]> hookJumpOffsets, Long seed) {
		this.hookAttachOffsets = hookAttachOffsets;
		this.jumpOffsets = jumpOffsets;
		this.hookJumpOffsets = hookJumpOffsets;
		this.seed = seed;
	}

	public Integer[][] generateChunk(int y) {
		
		return null;
	}

	public List<Integer[][]> generateChunkLog(int y) {
		return null;
	}
}
