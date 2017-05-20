package com.grupptva.runnergame.game.model.gamecharacter;

import com.grupptva.runnergame.game.model.gamecharacter.hook.AbstractHook;
import com.grupptva.runnergame.game.model.gamecharacter.hook.Hook;

/**
 * Created by agnesmardh on 2017-04-21.
 */
public class GameCharacter {
	private Point position;
	private float gravity = -0.4f;
	private float yVelocity;
	private float jumpInitialVelocity = 7f;
	private boolean collidingWithGround = false;
	private boolean attachedWithHook = false;
	private final float hookAngle = 1;
	private final float pixelsPerFrame;
	private boolean isDead = false;
	private AbstractHook hook;

	public GameCharacter(float x, float y, float pixelsPerFrame) {
		position = new Point(x, y);
		this.pixelsPerFrame = pixelsPerFrame;
		setHook(new Hook());
	}

	public void update() {
		if(attachedWithHook) {
			if (!collidingWithGround && !hook.isHookExtended()) {
				fall();
			}
		}else{
			if(!collidingWithGround){
				fall();
			}
		}
		if(attachedWithHook){
			hook.setLength(position.getDistance(hook.getPosition()));
			if(hook.isHookExtended()){
				setyVelocity(0);
				swing();
			}
			hook.moveHook(pixelsPerFrame);
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
		float newY = hook.getYPosAfterSwing(position.getX());
		position.setY(newY);
		if(Math.abs(newY - hook.getPosition().getY()) < 10) {
			removeHook();
		}
	}

	public void handleCollisionFromBelow(float yCoordinate) {
		setyVelocity(0);
		position.setLocation(position.getX(), yCoordinate);
	}

	public void initHook(Point hookPosition) {
		attachedWithHook = true;
		float hookLength = position.getDistance(hookPosition);
		hook.initHook(hookPosition, hookLength);
	}

	public void removeHook() {
		float characterYPos = position.getY();
		float characterXPos = position.getX();
		yVelocity = hook.getReleaseVelocity(characterXPos, characterYPos, jumpInitialVelocity);
		attachedWithHook = false;
		position.setY(position.getY() + 1);
		collidingWithGround = false;
	}

	public float getReleaseVelocity(float hookXPos, float hookYPos, float characterXPos, float characterYPos) {
		double angle = Math.atan((characterYPos - hookYPos)/(characterXPos - hookXPos))+Math.PI/2;
		return (float) Math.sin(angle) * jumpInitialVelocity;
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

	public AbstractHook getHook() {
		return hook;
	}

	public void setHook(AbstractHook hook) {
		this.hook = hook;
	}

	public float getHookAngle() {
		return hookAngle;
	}
}