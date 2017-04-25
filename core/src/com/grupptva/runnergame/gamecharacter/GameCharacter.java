package com.grupptva.runnergame.gamecharacter;

import java.awt.*;

/**
 * Created by agnesmardh on 2017-04-21.
 */
public class GameCharacter {
	private Point position; 
	public Rectangle box;


	public Point getPosition() {
		return position;
	}

	private void setPosition(Point position) {
		this.position = position;
	}
}
