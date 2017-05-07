package com.grupptva.runnergame.gamecharacter;

import java.awt.*;

/**
 * Created by agnesmardh on 2017-04-21.
 */
public class GameCharacter {
	private Point position;
	private float gravity = -0.4f;
	private float yVelocity;
	private float jumpInitialVelocity = 7f;
	private float maxHookVelocity = 14f;
	private float xVelocity = 1.5f;
	private boolean collidingWithGround = false;
	private boolean attachedWithHook = false;
	private Point hookPosition;
	private final float hookAngle = 1;

	public GameCharacter(float x, float y) {
		position = new Point(x, y);
	}

	public void update(float pixelsPerFrame) {
		fall();
		if(attachedWithHook) {
			moveHook(pixelsPerFrame);
		}
	}

	void moveY(float distance) {
		float newY = position.getY() + distance;
		position.setLocation(position.getX(), newY);
	}

	void fall() {
		if (!collidingWithGround) {
			yVelocity += gravity;
			moveY(yVelocity);
		}

	}

	public void jump() {
		if (collidingWithGround) {
			position.setY(position.getY()+1);
			yVelocity += jumpInitialVelocity;
			collidingWithGround = false;
		}
	}

	public Point getHookPosition() {
		return hookPosition;
	}

	public void handleCollisionFromBelow(float yCoordinate) {

		setyVelocity(0);
		position.setLocation(position.getX(), yCoordinate);
	}

	public void initHook(float length) {
		attachedWithHook = true;
		hookPosition = position.getOffsetPoint(length, hookAngle);
	}

	public void removeHook() {
		attachedWithHook = false;
		position.setY(position.getY()+1);
		yVelocity += maxHookVelocity;
		collidingWithGround = false;
	}

	private void moveHook(float pixelsPerFrame) {
		hookPosition.setX(hookPosition.getX() - pixelsPerFrame);
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public float getyVelocity() {
		return yVelocity;
	}

	private void setyVelocity(float yVelocity) {
		this.yVelocity = yVelocity;
	}

	public float getJumpInitialVelocity() {
		return jumpInitialVelocity;
	}

	public boolean isCollidingWithGround() {
		return collidingWithGround;
	}

	public void setCollidingWithGround(boolean collidingWithGround) {
		this.collidingWithGround = collidingWithGround;
	}

	public float getGravity() {
		return gravity;
	}

	public boolean isAttachedWithHook() {
		return attachedWithHook;
	}

}
