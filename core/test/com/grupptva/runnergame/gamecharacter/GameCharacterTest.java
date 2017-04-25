package com.grupptva.runnergame.gamecharacter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by agnesmardh on 2017-04-21.
 */
public class GameCharacterTest {
	private GameCharacter gameCharacter;

	@Before
	public void setUp() { 
		gameCharacter = new GameCharacter(0, 0); 
	}

	@Test
	public void testMoveY() { 
		double newY = -1; 
		gameCharacter.moveY(newY); 
		Assert.assertTrue(gameCharacter.position.getY() == newY); 
		Assert.assertTrue(gameCharacter.position.getX() == 0); 
	}

	@Test
	public void testFall() { 
		double yVelocity = gameCharacter.getyVelocity();
		 double y = gameCharacter.position.getY(); 
		gameCharacter.fall(); 
		Assert.assertTrue(gameCharacter.getyVelocity() == yVelocity + gameCharacter.getGravity()); 
		Assert.assertTrue(gameCharacter.position.getY() == y + yVelocity); 
	}

	 
}
