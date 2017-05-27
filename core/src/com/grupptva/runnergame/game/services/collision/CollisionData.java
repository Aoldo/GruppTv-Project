package com.grupptva.runnergame.game.services.collision;

/**
 * Responsibility: Represents the data of a possible collision.
 *
 * Used by:
 * @see CollisionChecker
 * @see ICollisionChecker
 * @see com.grupptva.runnergame.game.model.CollisionLogic
 *
 * Uses:
 * @see CollisionDirection
 *
 *
 * @author Karl and Agnes
 */
public class CollisionData {

	private boolean hasCollided;
	private CollisionDirection direction;

	CollisionData(boolean hasCollided, CollisionDirection direction){
		setHasCollided(hasCollided);
		setDirection(direction);
	}

	public boolean hasCollided() {
		return hasCollided;
	}

	private void setHasCollided(boolean hasCollided) {
		this.hasCollided = hasCollided;
	}

	public CollisionDirection getDirection() {
		return direction;
	}

	private void setDirection(CollisionDirection direction) {
		this.direction = direction;
	}
}
