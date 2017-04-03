package com.grupptva.runnergame.character;

/**
 * Represents a point in a 2-dimensional space, has a x and a y coordinate.
 * 
 * @author Mattias
 *
 */
public class Point {
	public Point(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Point(Point point) {
		this.x = point.x;
		this.y = point.y;
	}

	private float x, y;

	/**
	 * Calculates the angle between this point and {@param point} in radians.
	 * 
	 * @param point the point whose angle to should be calculated.
	 * @return the angle between the two points
	 */
	public float angleBetween(Point point) {
		//TODO: Make sure it works correctly, might need to *= -1 the variables.
		return (float) Math.atan2(y - point.y, x - point.x);
	}

	/**
	 * Sets the x coordinate.
	 * 
	 * @param x
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * Sets the y coordinate.
	 * 
	 * @param y
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * Faster method for only moving along the x axis.
	 * 
	 * @param distance
	 *            the distance to move the y coordinate.
	 */
	public void moveX(float distance) {
		x += distance;
	}

	/**
	 * Faster method for only moving along the y axis.
	 * 
	 * @param distance
	 *            the distance to move the y coordinate.
	 */
	public void moveY(float distance) {
		y += distance;
	}

	/**
	 * Moves the point {@code distance} towards {@code angle}
	 * 
	 * @param distance
	 *            Distance to move the point
	 * @param angleRad
	 *            Angle to move the point, in radians.
	 */
	public void moveInDirection(float distance, float angle) {
		x += distance * Math.cos(angle);
		y += distance * Math.sin(angle);
	}

	/**
	 * Sets this points coordinates to the same coordinates as {@code point}
	 * 
	 * @param point
	 *            the point whose coordinates should be copied.
	 */
	public void setPosition(Point point) {
		setPosition(point.x, point.y);
	}

	/**
	 * Sets this points coordinates to the coordinates the parameter values
	 * represent.
	 * 
	 * @param x
	 *            the x coordinate.
	 * @param y
	 *            the y coordinate.
	 */
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Calculates the distance between this point and the point represented by
	 * the parameter coordinates.
	 * 
	 * @param x
	 *            the x coordinate of the other point.
	 * @param y
	 *            the y coordinate of the other point.
	 * @return the distance between the points.
	 */
	public float distanceTo(float x, float y) {
		float dx = this.x - x;
		float dy = this.y - y;
		return (float) Math.sqrt(dx * dx + dy * dy);
	}

	/**
	 * Calculates the distance between this point and another point.
	 * 
	 * @param point
	 *            the other point
	 * @return the distance between the points.
	 */
	public float distanceTo(Point point) {
		return distanceTo(point.x, point.y);
	}

	/**
	 * 
	 * @return x coordinate of the point
	 */
	public float getX() {
		return x;
	}

	/**
	 * 
	 * @return y coordinate of the point.
	 */
	public float getY() {
		return y;
	}
}