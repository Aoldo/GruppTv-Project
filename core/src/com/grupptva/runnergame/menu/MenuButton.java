package com.grupptva.runnergame.menu;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * 
 * @author Luka
 *
 */
public class MenuButton {

	public int x, y, width, height;
	public Color pressed, notPressed;

	public MenuButton(int x, int y, int width, int height, Color pressed, Color notPressed) {
		//menuButtons.add(this);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.pressed = pressed;
		this.notPressed = notPressed;
	}

	public int getButtonWidth() {
		return width;
	}

	public int getButtonHeight() {
		return height;
	}

	public boolean collides(float x, float y, Integer screenWidth, Integer screenHeight) {
		if (((Gdx.input.getX() < (screenWidth / 2 + x + 160))
				&& !(Gdx.input.getX() < screenWidth / 2 + x))
				&& (((Gdx.input.getY() < ((screenHeight / 2 - y))))
						&& !(Gdx.input.getY() < (((screenHeight/ 2 - y)))
								- 40))) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean collides2(float x, float y, float xwidth, Integer screenWidth, Integer screenHeight) {
		if (((Gdx.input.getX() < (screenWidth / 2 + x + xwidth))
				&& !(Gdx.input.getX() < screenWidth / 2 + x))
				&& (((Gdx.input.getY() < ((screenHeight / 2 - y))))
						&& !(Gdx.input.getY() < (((screenHeight/ 2 - y)))
								- 40))) {
			return true;
		} else {
			return false;
		}
	}
	
}