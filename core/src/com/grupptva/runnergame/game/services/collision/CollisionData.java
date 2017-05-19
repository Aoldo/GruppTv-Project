package com.grupptva.runnergame.game.services.collision;

/**
 * Created by agnesmardh on 2017-05-19.
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
