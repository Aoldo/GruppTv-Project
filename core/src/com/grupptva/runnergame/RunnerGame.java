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
	MenuButton startGame;
	MenuButton highScores;
	MenuButton quitGame;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		sr = new ShapeRenderer();
		
		int screenWidth = Gdx.graphics.getWidth();
		int screenHeight = Gdx.graphics.getHeight();
		
		//mb = new MenuButton(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2,20,20);
		//mb = new MenuButton((Gdx.graphics.getWidth() - width/2),(Gdx.graphics.getHeight() - height/2),20,20);
		startGame = new MenuButton(screenWidth/2-80,screenHeight/2+40,160,40);
		highScores = new MenuButton(screenWidth/2-80,screenHeight/2-20,160,40);
		quitGame = new MenuButton(screenWidth/2-80,screenHeight/2-80,160,40);
		img = new Texture("mainmenubg.png");
		//this.setScreen(new StartScreen(this));
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.3f, 0.6f, 1f, 0.4f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
		sr.begin(ShapeType.Filled);
		//sr.begin(ShapeType.Line);
		startGame.render(sr);
		highScores.render(sr);
		quitGame.render(sr);
		sr.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
