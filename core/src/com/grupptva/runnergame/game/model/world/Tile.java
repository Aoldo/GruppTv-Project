package com.grupptva.runnergame.game.model.world;

/**
 * Created on 4/24/17.
 *
 * Responsibility: The Tile enum represents the data of one element in the grid
 * specified in {@link WorldModel}.
 *
 * Used by:
 * @see WorldModel
 * @see Chunk
 * @see com.grupptva.runnergame.game.model.CollisionLogic
 * @see com.grupptva.runnergame.game.model.GameLogic
 * @see com.grupptva.runnergame.game.model.HookLogic
 * @see com.grupptva.runnergame.game.model.worldgenerator.WorldGenerator
 *
 *
 * @author Karl 'NaN' Wikström
 */
public enum Tile {
	EMPTY,
	OBSTACLE
}
