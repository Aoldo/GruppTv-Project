package com.grupptva.runnergame.gamecharacter;

import java.awt.*;

/**
 * Created by agnesmardh on 2017-04-21.
 */
public class GameCharacter {
	private Point position;
	public Rectangle box;
	private double gravity = 0.4;
	private double yVelocity;
	private double jumpInitialVelocity = 10;
	private boolean collidingWithGround = false;

	public GameCharacter(int x, int y) { 
		box = new Rectangle(x, y, 25, 25);
		 position = box.getLocation(); 
	}

	public void moveY(double distance) { 
		double newY = position.getY() + distance; 
		position.setLocation(position.getX(), newY); 
	}

	public void fall() { 
		if (!collidingWithGround) {
			 yVelocity += gravity; 
		} 
		moveY(yVelocity);
	 }

	public void jump() { 
		yVelocity += jumpInitialVelocity; 
	}

	public Point getPosition() {
		return position;
	}

	private void setPosition(Point position) {
		this.position = position;
	}

	public double getyVelocity() {
		return yVelocity;
	}

	private void setyVelocity(double yVelocity) {
		this.yVelocity = yVelocity;
	}

	public double getJumpInitialVelocity() {
		return jumpInitialVelocity;
	}

	public boolean isCollidingWithGround() {
		return collidingWithGround;
	}

	public void setCollidingWithGround(boolean collidingWithGround) {
		this.collidingWithGround = collidingWithGround;
	}
}
