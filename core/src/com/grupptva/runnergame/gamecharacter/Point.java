package com.grupptva.runnergame.gamecharacter;

/**
 * Created by agnesmardh on 2017-04-26.
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

	public Point getOffsetPoint(float length, float angle) {
		float newX = x + (float) Math.cos(angle) * length;
		float newY = y + (float) Math.sin(angle) * length;
		return new Point(newX, newY);
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
