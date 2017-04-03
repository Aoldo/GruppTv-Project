package com.grupptva.runnergame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.grupptva.runnergame.gamecore.GameCore;

public class RunnerGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;

	GameCore GC = new GameCore();

	final double lowestTimePerLogicFrame = 0.0041666; //240fps
	final double highestTimePerLogicFrame = 0.0166666; //60fps

	double currentTimePerLogicFrame = highestTimePerLogicFrame;
	double timeAccumulator = 0;

	@Override
	public void create() {
		batch = new SpriteBatch();
	}

	@Override
	public void render() {
		Gdx.graphics.setTitle(String.valueOf(Gdx.graphics.getFramesPerSecond()));

		//Temporary code to change execution speed.
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			if(currentTimePerLogicFrame > lowestTimePerLogicFrame)
			{
				currentTimePerLogicFrame -= 0.0001f;
			}
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			if(currentTimePerLogicFrame < highestTimePerLogicFrame)
			{
				currentTimePerLogicFrame += 0.0001f;
			}
		}

		
		timeAccumulator += Gdx.graphics.getDeltaTime();		
		
		while (timeAccumulator > 0) {	
			timeAccumulator -= currentTimePerLogicFrame;
			
			//TODO: Do logic in here.
			GC.update();
		}

		Gdx.gl.glClearColor(0.3f, 0.6f, 1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();

		//TODO: Do rendering here.
		GC.render(batch);

		batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
	}
}
