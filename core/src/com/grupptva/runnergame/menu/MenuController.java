package com.grupptva.runnergame.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class MenuController implements InputProcessor {
	MainMenu model;
	MenuRenderer view;

	HighScoreMenu scoreModel;

	Integer width;
	Integer height;

	public MenuController() {
		Gdx.input.setInputProcessor(this);

		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		model = new MainMenu(width, height);
		view = new MenuRenderer();
		scoreModel = new HighScoreMenu(width, height);
	}

	public void update() {
		mouseX = Gdx.input.getX();
		mouseY = Gdx.input.getY();

		if (!inHighScore)
			model.update();
		else
			scoreModel.update();
	}

	public void render(SpriteBatch batch, ShapeRenderer sr) {
		if (!inHighScore)
			view.render(batch, sr, model, mouseX, mouseY);
		else
			scoreModel.render(batch, sr, mouseX, mouseY); //TODO: Split scoreModel into m&v
	}

	int mouseX, mouseY;

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	boolean hasPressed = false;

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		hasPressed = true;
		return false;
	}

	public boolean startGame = false;
	boolean inHighScore = false;

	private void sendClickToModel(int x, int y, int pointer, int button) {
		model.recieveClick(x, y, pointer, button);

		int widthHalf = width / 2;
		int heightHalf = height / 2;
		if (!inHighScore) {
			if (model.startGame.collides(x, y, widthHalf, heightHalf)) {
				//if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
				System.out.println("Start game pressed!");
				startGame = true;
				//sr.setColor(0.15f, 0.3f, 0.5f, 1);
				//}

			} else if (model.highScores.collides(x, y, widthHalf, heightHalf)) {
				//if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
				System.out.println("Highscores pressed!");
				inHighScore = true;
				//sr.setColor(0.15f, 0.3f, 0.5f, 1);
				//}

			} else if (model.quitGame.collides(x, y, widthHalf, heightHalf)) {
				//if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
				System.out.println("Quit game pressed!");
				//sr.setColor(0.15f, 0.3f, 0.5f, 1);
				Gdx.app.exit();
				//}
			}
		}
		else	//if in highscore
		{
			if(scoreModel.returnButton.collides(x, y,widthHalf, heightHalf))
			{
				System.out.println("Return pressed!");
				inHighScore = false;
			}
		}
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (hasPressed) {
			sendClickToModel(screenX, screenY, pointer, button);
		}
		System.out.println("Button released!");
		hasPressed = false;
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
