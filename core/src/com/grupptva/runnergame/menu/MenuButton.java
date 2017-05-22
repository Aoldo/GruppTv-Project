package com.grupptva.runnergame.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

/**
 * 
 * @author Luka
 *
 */
public class MenuButton {

	public int x, y, width, height;
	public Color pressed, notPressed;

	public MenuButton(int x, int y, int width, int height, Color pressed,
			Color notPressed) {
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

	public boolean collides(float x, float y, MainMenu mainMenu) {

		if (((Gdx.input.getX() < (mainMenu.getScreenWidth() / 2 + x + 160))
				&& !(Gdx.input.getX() < mainMenu.getScreenWidth() / 2 + x))
				&& (((Gdx.input.getY() < ((mainMenu.getScreenHeight() / 2 - y))))
						&& !(Gdx.input.getY() < (((mainMenu.getScreenHeight() / 2 - y)))
								- 40))) {
			return true;
		} else {
			return false;
		}
	}
}