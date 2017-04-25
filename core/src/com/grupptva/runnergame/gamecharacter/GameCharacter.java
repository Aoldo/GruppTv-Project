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
}
