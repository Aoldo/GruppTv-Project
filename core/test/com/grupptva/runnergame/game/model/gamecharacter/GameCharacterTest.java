package com.grupptva.runnergame.game.model.gamecharacter;

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
		gameCharacter = new GameCharacter(30, 150, 1.5f);
	}

	@Test
	public void testMoveY() {
		gameCharacter.getPosition().setY(2);
		float newY = -1;
		gameCharacter.moveY(newY);
		Assert.assertTrue(gameCharacter.getPosition().getY() == 1);
		Assert.assertTrue(gameCharacter.getPosition().getX() == 30);
	}

	@Test
	public void testFall() {
		float yVelocity = 7f;
		gameCharacter.setyVelocity(yVelocity);
		float gravity = gameCharacter.getGravity();
		gameCharacter.getPosition().setY(20);
		gameCharacter.fall();
		Assert.assertTrue(gameCharacter.getyVelocity() == yVelocity + gravity);
		Assert.assertTrue(gameCharacter.getPosition().getY() == 20 + gameCharacter.getyVelocity());
	}

	@Test
	public void testJump() {
		gameCharacter.setCollidingWithGround(true);
		float x = gameCharacter.getPosition().getX();
		float yVelocity = 5f;
		gameCharacter.setyVelocity(yVelocity);
		float jumpVelocity = 7f;
		gameCharacter.jump();
		float newX = gameCharacter.getPosition().getX();
		Assert.assertEquals(yVelocity + jumpVelocity, gameCharacter.getyVelocity(), 0.0001);
		Assert.assertTrue(x == newX);
	}

	@Test
	public void testHandleCollisionFromBelow() {
		gameCharacter.handleCollisionFromBelow(0);
		Assert.assertTrue(gameCharacter.getyVelocity() == 0);
		Assert.assertTrue(gameCharacter.getPosition().getY() == 0);
	}
}
