package com.grupptva.runnergame.game;

import com.grupptva.runnergame.game.view.GameRenderer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.grupptva.runnergame.ScenePlugin;
import com.grupptva.runnergame.game.model.GameLogic;

/**
 * A facade for the actual game. This is the class that should be run in order
 * to run the game.
 * 
 * @author Mattias
 *
 */
public class GamePlugin implements ScenePlugin {
	GameLogic gameLogic;
	GameRenderer gameRenderer;

	public GamePlugin() {
		gameLogic = new GameLogic();
		gameRenderer = new GameRenderer();
	}

	@Override
	public void update() {
		gameLogic.update();

	}

	@Override
	public void render(SpriteBatch batch, ShapeRenderer sr) {
		//Lets the view read the model without the model knowing about the view.
		gameRenderer.render(batch, sr, gameLogic);
	}

}
