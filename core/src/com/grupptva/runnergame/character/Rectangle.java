package com.grupptva.runnergame.character;

public class Rectangle {
	Point position = new Point(0,0);
	//TODO: protected, by adding a get method in character
	public Point dimensions = new Point(0,0);
	
	public Rectangle(float x, float y, float width, float height)
	{
		this.position.setPosition(x, y);
		this.dimensions.setPosition(width,height);
	}
}