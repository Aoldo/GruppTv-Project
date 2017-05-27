package com.grupptva.runnergame.game.services.collision;

/**
 * Responsibility: Provides a method head for collision checkers.
 *
 * Used by:
 * @see CollisionChecker
 *
 * Uses:
 * @see CollisionData
 *
 * @author Karl and Agnes
 */
public interface ICollisionChecker {

	/**
	 * Checks collision between character and a tile.
	 * <p>
	 * NOTE: A tile is a square and the character has the same size as a tile.
	 *
	 * @param tileSize      is the size of a tile.
	 * @param tileXPos      is the x-position of the bottom left corner of the tile.
	 * @param tileYPos      is the y-position of the bottom left corner of the tile.
	 * @param characterXPos is the x-position of the bottom left corner of the character.
	 * @param characterYPos is the y-position of the bottom left corner of the character.
	 */
	CollisionData checkCollisionWithTile(int tileSize, float tileXPos, float tileYPos,
	                            float characterXPos, float characterYPos);
}
