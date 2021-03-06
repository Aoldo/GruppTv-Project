package com.grupptva.runnergame.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

/**
 * Creates a button with desired width, height and length. Also handles button collision.
 * 
 * @author Luka
 *
 */
public class Button {

	public int x, y, width, height;
	public Color pressed, notPressed;

	public Button(int x, int y, int width, int height, Color pressed,
			Color notPressed) {
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

	public boolean collides(float mouseX, float mouseY, Integer screenWidthHalf,
			Integer screenHeightHalf) {
		if (mouseX < screenWidthHalf + x + width && mouseX > screenWidthHalf + x) {
			if (2 * screenHeightHalf - mouseY < screenHeightHalf + height + y
					&& 2 * screenHeightHalf - mouseY > screenHeightHalf + y) {
				return true;
			}
		}
		return false;
	}

	public boolean collides2(float x, float y, float xwidth, Integer screenWidth,
			Integer screenHeight) {
		if (((Gdx.input.getX() < (screenWidth / 2 + x + xwidth))
				&& !(Gdx.input.getX() < screenWidth / 2 + x))
				&& (((Gdx.input.getY() < ((screenHeight / 2 - y))))
						&& !(Gdx.input.getY() < (((screenHeight / 2 - y))) - 40))) {
			return true;
		} else {
			return false;
		}
	}

}