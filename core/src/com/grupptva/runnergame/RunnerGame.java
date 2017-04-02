package com.grupptva.runnergame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.grupptva.runnergame.character.CharacterController;

public class RunnerGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	
	CharacterController cc = new CharacterController();
	
	
	@Override
	public void create () {
		batch = new SpriteBatch();	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.3f, 0.6f, 1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		
		cc.update();
		cc.render(batch);
		
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
