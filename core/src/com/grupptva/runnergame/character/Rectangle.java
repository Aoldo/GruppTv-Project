package com.grupptva.runnergame.character;

public class Rectangle {
	Point position = new Point(0,0);
	Point dimensions = new Point(0,0);
	
	public Rectangle(float x, float y, float width, float height)
	{
		this.position.setPosition(x, y);
		this.dimensions.setPosition(width,height);
	}
}
