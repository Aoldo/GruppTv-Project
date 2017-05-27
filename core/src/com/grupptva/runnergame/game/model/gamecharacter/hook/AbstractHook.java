package com.grupptva.runnergame.game.model.gamecharacter.hook;

import com.grupptva.runnergame.game.model.gamecharacter.Point;

/**
 * Responsibility: Represents the core data for extension of this class.
 *
 * Used by:
 * @see Hook
 *
 * Uses:
 * @see Point
 * @see IHook
 *
 * @Author Karl and Agnes
 */
public abstract class AbstractHook implements IHook {

	private Point position;
	private float maxLength;
	private float length;

	public boolean isHookExtended(){
		return maxLength <= length;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public float getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(float maxLength) {
		this.maxLength = maxLength;
	}

	public float getLength() {
		return length;
	}

	public void setLength(float length) {
		this.length = length;
	}
}
