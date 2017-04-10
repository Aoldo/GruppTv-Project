package com.grupptva.runnergame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.grupptva.runnergame.worldgenerator.GeneratorVisualizer;

public class RunnerGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;

	@Override
	public void create() {
		batch = new SpriteBatch();

	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0.3f, 0.6f, 1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();

		worldGenVisualization();

		batch.end();
	}

	//This area is for testing the world generator and also for visualizing it.
	GeneratorVisualizer gv = new GeneratorVisualizer();

	private void worldGenVisualization() {
		gv.update();
		gv.render(batch);
	}

	// /worldgen

	@Override
	public void dispose() {
		batch.dispose();
	}
}
