package com.grupptva.runnergame.character;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class CharacterController {
	CharacterModel model;

	ShapeRenderer testRenderer;
	
	public CharacterController()
	{
		model = new CharacterModel(0, 200);
	}
	
	
	public void update()
	{
		testingMethodGround();
		
		
		model.update();
	}
	
	private void testingMethodGround()
	{
		if(model.getY() <= 50)
		{
			model.collideWithGround(50);
		}
		else
		{
			model.setGroundCollision(false);
		}
	}
	
	//ub3r l33t h4x
	public void render(	SpriteBatch batch)
	{
		testRenderer  = new ShapeRenderer();
		testRenderer.begin(ShapeType.Filled);
		//Draw playerCharacter box
		testRenderer.setColor(0f, 0f, 0f, 1f);
		testRenderer.rect(model.getX(),model.getY(),model.box.dimensions.getX(),model.box.dimensions.getY());
		
		//TESTING Draw fake ground.
		testRenderer.setColor(.1f, .1f, .5f, 1f);
		testRenderer.rect(0,0,700,50);
		
		
		testRenderer.end();
	}
}
