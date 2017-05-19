package com.grupptva.runnergame.game.services.worldgenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.grupptva.runnergame.game.model.gamecharacter.GameCharacter;
import com.grupptva.runnergame.game.model.world.Chunk;
import com.grupptva.runnergame.game.services.worldgenerator.GeneratorChunk.Tile;

public class WorldGenerator {
	private List<GeneratorStep> steps = new ArrayList<GeneratorStep>();

	int chunkWidth;
	int chunkHeight;
	int initY;
	Random rng;

	public WorldGenerator(float vx, int tileSize, Long seed, int chunkWidth, int chunkHeight, int initY,
			GameCharacter character) {

		this.rng = new Random(seed);
		this.chunkWidth = chunkWidth;
		this.chunkHeight = chunkHeight;
		this.initY = initY;

		steps.add(new HookStep(vx, tileSize, character, rng, 50));
		steps.add(new JumpStep(vx, tileSize, character, rng, 100));
	}

	private int getChanceSum() {
		int sum = 0;
		for (GeneratorStep s : steps) {
			sum += s.chance;
		}
		return sum;
	}

	/**
	 * Generates a chunk, but unlike generateChunk this method returns every
	 * single iteration of the chunks generation. Used for visualization
	 * purposes, SHOULD NOT BE CALLED IN THE GAME.
	 * 
	 * @param initY
	 *            The Y value of the leftmost tile, the starting point.
	 * @return A list containing every iteration of the generated chunk.
	 */
	public List<Tile[][]> generateChunkLog(int initY) {
		List<Tile[][]> chunkLog = new ArrayList<Tile[][]>();
		GeneratorChunk chunk = new GeneratorChunk(chunkWidth, chunkHeight); //The generated chunk. Currently empty.

		Integer[] currentTile = { 0, initY }; //Represents current position of the "crawler" that simulates possible character movement through the chunk.

		// Keep crawling forward step by step until the end of the chunk has been reached.
		// Inside this loop is where the magic happens.
		while (currentTile[0] != chunk.width - 1) {
			chunk.tiles[currentTile[1]][currentTile[0]] = Tile.FULL; //Creates solid ground at the crawlers current position.

			chunkLog.add(chunk.deepCopyTiles());
			chunk.clearPossibilities(); //Used for visualization, removes info from previous log, reduces visual bloat

			int stepValue = rng.nextInt(getChanceSum()); //Randomize which kind of movement should be used next.

			int chanceCounter = 0;
			for (int i = 0; i < steps.size(); i++) {
				if (stepValue < steps.get(i).chance + chanceCounter) {
					steps.get(i).step(chunk, currentTile);
					break;
				}
				chanceCounter += steps.get(i).chance;
			}

			chunk.clearPossibilities();
		}

		chunk.tiles[currentTile[1]][currentTile[0]] = Tile.FULL; //Create solid ground at the end of the chunk.
		initY = currentTile[1]; //Save final Y value of the chunk so that it can seamlessly connect to the next one.

		chunkLog.add(chunk.deepCopyTiles());
		return chunkLog;
	}

	/**
	 * Creates the next chunk of the world.
	 * 
	 * @return The created chunk.
	 */
	public Chunk generateChunk() {
		GeneratorChunk chunk = new GeneratorChunk(chunkWidth, chunkHeight); //The generated chunk. 

		Integer[] currentTile = { 0, initY }; //Represents current position of the "crawler" that simulates possible character movement through the chunk.

		// Keep crawling forward step by step until the end of the chunk has been reached.
		// Inside this loop is where the magic happens.
		while (currentTile[0] != chunk.width - 1) {
			chunk.tiles[currentTile[1]][currentTile[0]] = Tile.FULL; //Creates solid ground at the crawlers current position.

			int stepValue = rng.nextInt(getChanceSum()); //Randomize which kind of movement should be used next.

			int chanceCounter = 0;
			for (int i = 0; i < steps.size(); i++) {
				if (stepValue < steps.get(i).chance + chanceCounter) {
					steps.get(i).step(chunk, currentTile);
					break;
				}
				chanceCounter += steps.get(i).chance;
			}
			//___OLD___
			//if (stepValue < jumpStepChance) //Select jumpStep
			//	jumpStep(chunk, currentTile);
			//else if (stepValue < jumpStepChance + hookStepChance) //Select hookStep
			//	hookStep(chunk, currentTile);
		}
		chunk.tiles[currentTile[1]][currentTile[0]] = Tile.FULL; //Create solid ground at the end of the chunk.
		initY = currentTile[1]; //Save final Y value of the chunk so that it can seamlessly connect to the next one.

		return new Chunk(convertChunkToWorldModel(chunk.tiles)); //Convert the chunk into one that is usable by the world, and return it.
	}

	/**
	 * Converts a chunk made out of WorldGenerator.Tile s to a chunk used in the
	 * actual world. This is needed because the generator has special tiles used
	 * for the visualization of the generation, that don't exist in the actual
	 * world.
	 * 
	 * @param chunk
	 * @return
	 */
	private com.grupptva.runnergame.game.model.world.Tile[][] convertChunkToWorldModel(Tile[][] chunk) {

		com.grupptva.runnergame.game.model.world.Tile[][] newChunk = new com.grupptva.runnergame.game.model.world.Tile[chunk[0].length][chunk.length];

		for (int y = 0; y < chunk.length; y++) {
			for (int x = 0; x < chunk[0].length; x++) {
				if (chunk[y][x] == Tile.FULL || chunk[y][x] == Tile.HOOKTARGET) {
					newChunk[x][y] = com.grupptva.runnergame.game.model.world.Tile.OBSTACLE;
				} else {
					newChunk[x][y] = com.grupptva.runnergame.game.model.world.Tile.EMPTY;
				}
			}
		}
		return newChunk;
	}

}
