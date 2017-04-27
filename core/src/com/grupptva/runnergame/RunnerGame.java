package com.grupptva.runnergame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.grupptva.runnergame.worldgenerator.GeneratorVisualizer;
/**
 * 
 * @author Mattias
 *
 */
public class RunnerGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;

	final double fastestTimeStep = 0.0041666; //240 fps
	final double slowestTimeStep = 0.0166666; //60 fps
	double currentTimeStep = slowestTimeStep;
	double timeAccumulator = 0;

	@Override
	public void create() {
		batch = new SpriteBatch();

	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0.3f, 0.6f, 1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		
		timeAccumulator += Gdx.graphics.getDeltaTime();
		
		debugTimeStep();
		
		while (timeAccumulator > 0) {
			timeAccumulator -= currentTimeStep;

			//--------------------Do logic here-------------------
		}

		Gdx.gl.glClearColor(0.3f, 0.6f, 1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		//--------------------Do SpriteBatch rendering here----------------------
		worldGenVisualization();
		batch.end();
	}

	//This area is for testing the world generator and also for visualizing it.
	GeneratorVisualizer gv = new GeneratorVisualizer();

	private void worldGenVisualization() {
		gv.update();
		gv.render(batch);
	}
	// end worldgen
		
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
