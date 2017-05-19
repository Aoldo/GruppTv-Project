package com.grupptva.runnergame.game.services.worldgenerator;

import java.util.List;

import com.grupptva.runnergame.game.model.gamecharacter.GameCharacter;
import com.grupptva.runnergame.game.services.worldgenerator.Chunk.Tile;;

abstract class GeneratorStep {

	public abstract void step(Chunk chunk, Integer[] currentTile);

	public abstract void step(Chunk chunk, Integer[] currentTile,
			List<Tile[][]> chunkLog);
}
