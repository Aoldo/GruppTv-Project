package com.grupptva.runnergame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class RunnerGame extends ApplicationAdapter {
	ShapeRenderer sr;
	SpriteBatch batch;
	Texture img;
	MenuButton mb;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		sr = new ShapeRenderer();
		//mb = new MenuButton(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2,20,20);
		//mb = new MenuButton((Gdx.graphics.getWidth() - width/2),(Gdx.graphics.getHeight() - height/2),20,20);
		mb = new MenuButton((Gdx.graphics.getWidth()-10),(Gdx.graphics.getHeight()-10),20,20);
		img = new Texture("mainmenubg.png");
		//this.setScreen(new StartScreen(this));
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.3f, 0.6f, 1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
		sr.begin(ShapeType.Line);
		mb.render(sr);
		sr.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
