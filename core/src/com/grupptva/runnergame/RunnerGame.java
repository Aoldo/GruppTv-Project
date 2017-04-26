package com.grupptva.runnergame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class RunnerGame extends ApplicationAdapter {
	ShapeRenderer sr;
	SpriteBatch batch;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		sr = new ShapeRenderer();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.3f, 0.6f, 1f, 0.4f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		batch.draw(img, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();
		
		sr.begin(ShapeType.Filled);
		//sr.begin(ShapeType.Line);
		startGame.render(sr);
		highScores.render(sr);
		quitGame.render(sr);
		sr.end();
		batch.begin();
		startGameBF.draw(batch, startGameString, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2); 
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
