package com.grupptva.runnergame.game.services.collision;

import com.grupptva.runnergame.game.services.collision.CollisionChecker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Created by agnesmardh on 2017-05-16.
 */
public class CollisionCheckerTest {
	private CollisionChecker collisionChecker;
	private int tileSize = 20;


	@Before
	public void setUp() {
		collisionChecker = new CollisionChecker();
	}

	@Test
	public void testCheckCollisionWithTile() {
		float tileXPos = 45;
		float tileYPos = 30;
		float characterXPos = 30;
		float characterYPos = 30;
		CollisionData collisionData = collisionChecker.checkCollisionWithTile(tileSize, tileXPos, tileYPos, characterXPos, characterYPos);
		Assert.assertTrue(collisionData.hasCollided());
		tileYPos = 25;
		collisionChecker.checkCollisionWithTile(tileSize, tileXPos, tileYPos, characterXPos, characterYPos);
		Assert.assertTrue(collisionData.hasCollided());

	}

}
