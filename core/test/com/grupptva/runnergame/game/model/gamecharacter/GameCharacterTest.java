package com.grupptva.runnergame.game.model.gamecharacter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.grupptva.runnergame.game.model.gamecharacter.GameCharacter;

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
		float newY = -1;
		gameCharacter.moveY(newY);
		Assert.assertTrue(gameCharacter.getPosition().getY() == newY);
		Assert.assertTrue(gameCharacter.getPosition().getX() == 0);
	}

	@Test
	public void testFall() {
		float yVelocity = gameCharacter.getyVelocity();
		float y = gameCharacter.getPosition().getY();
		gameCharacter.fall();
		
		System.out.println("yVel " + yVelocity);
		System.out.println("y " + y);
		System.out.println("charYVel " + gameCharacter.getyVelocity());
		System.out.println("charY " + gameCharacter.getPosition().getY());
		
		Assert.assertTrue(gameCharacter.getyVelocity() == yVelocity + gameCharacter.getGravity());		
		Assert.assertEquals(gameCharacter.getPosition().getY(), y + yVelocity, .05f);
	}

	@Test
	public void testJump() {
		double x = gameCharacter.getPosition().getX();
		double yvelocity = gameCharacter.getyVelocity();
		double jumpVelocity = gameCharacter.getJumpInitialVelocity();
		gameCharacter.jump();
		double newX = gameCharacter.getPosition().getX();
		Assert.assertTrue(gameCharacter.getyVelocity() == yvelocity + jumpVelocity);
		Assert.assertTrue(x == newX);
	}

	@Test
	public void testHandleCollisionFromBelow() {
		gameCharacter.handleCollisionFromBelow(0);
		Assert.assertTrue(gameCharacter.getyVelocity() == 0);
		Assert.assertTrue(gameCharacter.getPosition().getY() == 0);
	}
}
