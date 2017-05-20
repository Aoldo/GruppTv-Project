package com.grupptva.runnergame;

import java.awt.Color;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.grupptva.runnergame.game.services.worldgenerator.GeneratorVisualizer;
import com.grupptva.runnergame.menu.MainMenu;
import com.grupptva.runnergame.menu.MenuListener;
import com.grupptva.runnergame.pluginsystem.GamePlugin;
import com.grupptva.runnergame.pluginsystem.ScenePlugin;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * 
 * @author Mattias
 *
 */
public class RunnerGame extends ApplicationAdapter implements MenuListener {
	ShapeRenderer sr;
	SpriteBatch batch;
	Texture img;

	final double fastestTimeStep = 0.0041666; //240 fps
	final double slowestTimeStep = 0.0166666; //60 fps
	double currentTimeStep = slowestTimeStep;
	double timeAccumulator = 0;

	MainMenu mainMenu;
	GeneratorVisualizer gv;
	
	GamePlugin game;
	
	//InputHandler inputHandler;

	ScenePlugin activePlugin;

	@Override
	public void create() {
		//inputHandler = new InputHandler();
		batch = new SpriteBatch();
		sr = new ShapeRenderer();
		mainMenu = new MainMenu(this);
		gv = new GeneratorVisualizer();
		activePlugin = mainMenu;
	}
	
	private void initGameLogic()
	{
		//inputHandler.removeListener(gameLogic);
		game = new GamePlugin();
		//inputHandler.addListener(gameLogic);
		activePlugin = game;
	}

	public void startGameEvent() {
		initGameLogic();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0.3f, 0.6f, 1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		timeAccumulator += Gdx.graphics.getDeltaTime();

		debugTimeStep();
		
		Gdx.graphics.setTitle(String.valueOf(Gdx.graphics.getRawDeltaTime()));

		while (timeAccumulator > currentTimeStep) {
			timeAccumulator -= currentTimeStep;

			//--------------------Do logic here-------------------
			//inputHandler.update();
			activePlugin.update();
		}

		if (Gdx.input.isKeyJustPressed(Keys.G)) {
			if (activePlugin != gv)
				activePlugin = gv;
			else
				activePlugin = mainMenu;
		}

		Gdx.gl.glClearColor(0.3f, 0.6f, 1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//--------------------Do rendering here----------------------
		activePlugin.render(batch, sr);
	}

	private void debugTimeStep() {
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			if (currentTimeStep > fastestTimeStep) {
				currentTimeStep -= 0.0001f;
			}
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			if (currentTimeStep < slowestTimeStep) {
				currentTimeStep += 0.0001f;
			}
		}
	}

	@Override
	public void dispose() {
		batch.dispose();
	}
}