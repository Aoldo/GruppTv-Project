package com.grupptva.runnergame.character;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class CharacterController {
	CharacterModel model;
	public CharacterController()
	{
		model = new CharacterModel(0, 200);
	}
	
	
	public void update()
	{
		//model.update();
	}
	
	//ub3r l33t h4x
	public void render(	SpriteBatch batch)
	{
		ShapeRenderer testRenderer = new ShapeRenderer();
		testRenderer.begin(ShapeType.Filled);
		testRenderer.rect(model.box.position.getX(),model.box.position.getY(),model.box.dimensions.getX(),model.box.dimensions.getY());
		testRenderer.end();
	}
}
