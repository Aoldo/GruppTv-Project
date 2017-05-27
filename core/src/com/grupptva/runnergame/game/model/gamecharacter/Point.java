package com.grupptva.runnergame.game.model.gamecharacter;

/**
 * Responsibility: Represents a point in two dimensions.
 *
 * Used by:
 * @see GameCharacter
 * @see com.grupptva.runnergame.game.model.gamecharacter.hook.AbstractHook
 * @see com.grupptva.runnergame.game.model.gamecharacter.hook.Hook
 * @see com.grupptva.runnergame.game.model.HookLogic
 * @see com.grupptva.runnergame.game.model.gamecharacter.hook.IHook
 *
 * @Author Karl and Agnes
 */

public class Point {
	private float x;
	private float y;

	public Point(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void setLocation(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float getDistance(Point point) {
		float dX = this.x - point.x;
		float dY = this.y - point.y;
		return (float) Math.sqrt((dX * dX) + (dY * dY));
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
}
