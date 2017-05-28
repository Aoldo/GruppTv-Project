package com.grupptva.runnergame.utils;

import com.badlogic.gdx.graphics.Color;

/**
 * Responsibility: Represents a button
 *
 * Used by:
 * @see com.grupptva.runnergame.game.model.GameOverMenu
 * @see com.grupptva.runnergame.menu.model.HighScoreMenu
 * @see com.grupptva.runnergame.menu.model.MainMenu
 * @see com.grupptva.runnergame.game.view.GameOverRenderer
 * @see com.grupptva.runnergame.menu.view.MenuRenderer
 *
 *
 * @author Luka
 * Revised bt Karl
 */
public class Button {

	public int x, y, width, height;
	public Color pressed, notPressed;

	public Button(int x, int y, int width, int height, Color pressed,
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

}