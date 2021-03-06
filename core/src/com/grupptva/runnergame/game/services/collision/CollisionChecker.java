package com.grupptva.runnergame.game.services.collision;

/**
 * Responsibility: It's used to check for collision between the GameCharacter and a Tile.
 *
 * Used by:
 * @see com.grupptva.runnergame.game.model.GameLogic
 *
 * Uses:
 * @see ICollisionChecker
 * @see CollisionData
 *
 * @author Karl and Agnes
 */
public class CollisionChecker implements ICollisionChecker {

	@Override
	public CollisionData checkCollisionWithTile(int tileSize, float tileXPos, float tileYPos,
	                                         float characterXPos, float characterYPos) {
		float dx = characterXPos - tileXPos;
		float dy = characterYPos - tileYPos;

		if (Math.abs(dx) <= tileSize && Math.abs(dy) <= tileSize) { // is there a collision?
			if (dy > dx) {
				if (dy >= -dx) {
					//bottom
					return new CollisionData(true, CollisionDirection.BOTTOM);
				} else {
					//right
					return new CollisionData(true, CollisionDirection.RIGHT);
				}
			} else {
				if (dy != dx) {
					if (dy > -dx) {
						//left
						return new CollisionData(true, CollisionDirection.LEFT);
					} else {
						//top
						return new CollisionData(true, CollisionDirection.TOP);
					}
				} else if (dy > 0 && dx > 0){
					//bottom
					return new CollisionData(true, CollisionDirection.BOTTOM);
				} else {
					//top
					return new CollisionData(true, CollisionDirection.TOP);
				}
			}
		}else{
			return new CollisionData(false, null);

		}
	}
}
