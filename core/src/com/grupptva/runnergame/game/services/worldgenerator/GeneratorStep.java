package com.grupptva.runnergame.game.services.worldgenerator;

import java.util.List;

import com.grupptva.runnergame.game.model.gamecharacter.GameCharacter;
import com.grupptva.runnergame.game.services.worldgenerator.WorldGenerator.Tile;

abstract class GeneratorStep {
	public GeneratorStep(float vx, int tileSize, int chunkWidth, int chunkHeight,
			int initY, GameCharacter character) {
	}

	public abstract void step(Tile[][] chunk, Integer[] currentTile);

	public abstract void step(Tile[][] chunk, Integer[] currentTile,
			List<Tile[][]> chunkLog);
}
