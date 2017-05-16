package com.grupptva.runnergame.game.model.gamecharacter;

/**
 * Created by agnesmardh on 2017-04-21.
 */
public class GameCharacter {
	private Point position;
	private float gravity = -0.4f;
	private float yVelocity;
	private float jumpInitialVelocity = 7f;
	private float hookVelocity = 3f;
	private float xVelocity = 1.5f;
	private boolean collidingWithGround = false;
	private boolean attachedWithHook = false;
	private boolean hookExtended = false;
	private Point hookPosition;
	private float hookLength;
	private final float hookAngle = 1;
	private final float pixelsPerFrame;
	private boolean isDead = false;

	public GameCharacter(float x, float y, float pixelsPerFrame) {
		position = new Point(x, y);
		this.pixelsPerFrame = pixelsPerFrame;
	}

	public void update() {
		if(attachedWithHook){
			hookExtended = hookLength <= position.getDistance(hookPosition);
			if(hookExtended){
				setyVelocity(0);
				hookVelocity += 0.1f;
				swing();
			}
			moveHook();
		}
		if(!collidingWithGround && !hookExtended){
			fall();
		}
	}

	void moveY(float distance) {
		float newY = position.getY() + distance;
		position.setLocation(position.getX(), newY);
	}

	void fall() {
		yVelocity += gravity;
		moveY(yVelocity);
	}

	public void jump() {
		if (collidingWithGround) {
			position.setY(position.getY() + 1);
			yVelocity += jumpInitialVelocity;
			collidingWithGround = false;
		}
	}

	private void swing() {
		float newY = hookPosition.getY() - (float) Math.sqrt(hookLength * hookLength - ((position.getX() + 1 - hookPosition.getX()) *
				(position.getX() + 1 - hookPosition.getX())));
		position.setY(newY);
		if(Math.abs(newY - hookPosition.getY()) < 10) {
			removeHook();
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
		hookLength = length;
	}

	public void removeHook() {
		attachedWithHook = false;
		position.setY(position.getY() + 1);
		yVelocity += hookVelocity;
		hookVelocity = 0f;
		collidingWithGround = false;
		hookExtended = false;
	}

	private void moveHook() {
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

	public void setyVelocity(float yVelocity) {
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

	public boolean isDead() {
		return isDead;
	}

	public void setDead(boolean dead) {
		isDead = dead;
	}
}