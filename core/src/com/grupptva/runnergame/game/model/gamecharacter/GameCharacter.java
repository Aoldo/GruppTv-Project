package com.grupptva.runnergame.game.model.gamecharacter;

import java.awt.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

/**
 * Created by agnesmardh on 2017-04-21. Revised by Mattias
 */
public class GameCharacter {
	private Point position;
	private float gravity = -0.4f;
	private float yVelocity;
	private float jumpInitialVelocity = 7f;
	private boolean collidingWithGround = false;
	private boolean attachedWithHook = false;
	private Point hookPosition;
	private final float hookAngle = 1;

	public GameCharacter(float x, float y) {
		position = new Point(x, y);
	}

	public float getFramesToApexOfJump() {
		//v=v_0+a*t, v = 0 => t=v_0/a
		return jumpInitialVelocity / (-gravity);
	}

	public float getRelativeHeightOfApex() {
		//integrate v=v_0+a*t dt <=> v_0*t-(a*t^2)/2
		float t = getFramesToApexOfJump();
		return (jumpInitialVelocity * t) - ((gravity * t * t) / 2);
	}

	public void update() {
		fall();
		debugReset();
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
			position.setY(position.getY() + 1);
			yVelocity += jumpInitialVelocity;
		}
	}

	public void handleCollisionFromBelow(float yCoordinate) {
		setyVelocity(0);
		position.setLocation(position.getX(), yCoordinate);
	}

	public void initHook(float length) {
		attachedWithHook = true;
		hookPosition = position.getOffsetPoint(length, hookAngle);
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

	private void debugReset() {
		if (Gdx.input.isKeyJustPressed(Keys.R))
			this.position.setLocation(position.getX(), 100);
	}
}