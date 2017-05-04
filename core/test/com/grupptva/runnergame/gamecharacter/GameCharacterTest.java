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
		float newY = 1f;
		gameCharacter.moveY(newY);
		Assert.assertTrue(gameCharacter.getPosition().getY() == newY);
		Assert.assertTrue(gameCharacter.getPosition().getX() == 0);
	}

	@Test
	public void testFall() {
		float yVelocity = gameCharacter.getyVelocity();
		float y = gameCharacter.getPosition().getY();
		gameCharacter.fall();
		Assert.assertTrue(gameCharacter.getyVelocity() == yVelocity + gameCharacter.getGravity());
		Assert.assertTrue(gameCharacter.getPosition().getY() == y + yVelocity);
	}

	@Test
	public void testJump() {
		float x = gameCharacter.getPosition().getX();
		float yvelocity = gameCharacter.getyVelocity();
		float jumpVelocity = gameCharacter.getJumpInitialVelocity();gameCharacter.jump();
		float newX = gameCharacter.getPosition().getX();
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
