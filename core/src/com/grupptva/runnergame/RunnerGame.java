package com.grupptva.runnergame;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.grupptva.runnergame.debug.GeneratorVisualizer_DEBUG;
import com.grupptva.runnergame.modulesystem.GameModuleAdapter;
import com.grupptva.runnergame.modulesystem.MenuModuleAdapter;
import com.grupptva.runnergame.modulesystem.ModuleAdapter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Responsibility: Runs the application.
 *
 * Used by:
 * @see DesktopLauncher
 *
 * Uses:
 * @see com.grupptva.runnergame.debug.GeneratorVisualizer_DEBUG
 * @see com.grupptva.runnergame.modulesystem.GameModuleAdapter
 * @see com.grupptva.runnergame.modulesystem.MenuModuleAdapter
 * @see com.grupptva.runnergame.modulesystem.ModuleAdapter
 * @see com.badlogic.gdx.graphics.glutils.ShapeRenderer
 *
 *
 * @author Mattias
 * Revised by Karl
 */
public class RunnerGame extends ApplicationAdapter {
	ShapeRenderer sr;
	SpriteBatch batch;

	final double fastestTimeStep = 0.0041666; //240 fps
	final double slowestTimeStep = 0.0166666; //60 fps
	double currentTimeStep = slowestTimeStep;
	double timeAccumulator = 0;

	MenuModuleAdapter mainMenu;
	GeneratorVisualizer_DEBUG gv;
	
	GameModuleAdapter game;

	ModuleAdapter activeModule;
	
	Sound sound;

	@Override
	public void create() {
		sound = Gdx.audio.newSound(Gdx.files.internal("core" + System.getProperty("file.separator") + "assets" + System.getProperty("file.separator") + "music.mp3"));
		sound.loop();
		sound.play();
		batch = new SpriteBatch();
		sr = new ShapeRenderer();
		mainMenu = new MenuModuleAdapter();
		gv = new GeneratorVisualizer_DEBUG();
		activeModule = mainMenu;
	}
	
	private void initGameLogic()
	{
		game = new GameModuleAdapter();
		activeModule = game;
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
		
		//Gdx.graphics.setTitle(String.valueOf(Gdx.graphics.getRawDeltaTime()));

		
		while (timeAccumulator > currentTimeStep) { //Fixed timestep.
			timeAccumulator -= currentTimeStep;

			//--------------------Do logic here-------------------
			activeModule.update();
		}

		//Debug code for visualizing the world generation.
		if (Gdx.input.isKeyJustPressed(Keys.G)) {
			if (activeModule != gv)
				activeModule = gv;
			else
				activeModule = mainMenu;
		}

		Gdx.gl.glClearColor(0.3f, 0.6f, 1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//--------------------Do rendering here----------------------
		activeModule.render(batch, sr);
	
	
		if(activeModule.inactive())
		{
			if(activeModule == game)
				initMenu();
			else
				initGameLogic();
		}
	}
	private void initMenu()
	{
		mainMenu = new MenuModuleAdapter();
		activeModule=mainMenu;
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