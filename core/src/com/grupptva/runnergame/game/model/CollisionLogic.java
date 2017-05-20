package com.grupptva.runnergame.game.model;

import com.grupptva.runnergame.game.model.gamecharacter.GameCharacter;
import com.grupptva.runnergame.game.model.world.Tile;
import com.grupptva.runnergame.game.model.world.WorldModel;
import com.grupptva.runnergame.game.services.collision.CollisionData;
import com.grupptva.runnergame.game.services.collision.ICollisionChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by agnesmardh on 2017-05-19.
 */
public class CollisionLogic {
	private GameCharacter gameCharacter;
	private WorldModel world;
	private final int tileSize;
	private ICollisionChecker collisionChecker;

	public CollisionLogic(GameCharacter gameCharacter, WorldModel world, int tileSize,
	                      ICollisionChecker collisionChecker) {
		this.gameCharacter = gameCharacter;
		this.world = world;
		this.tileSize = tileSize;
		this.collisionChecker = collisionChecker;
	}

	public void handlePossibleCollision() {
		getGameCharacter().setCollidingWithGround(false);
		// 1) Calculate which column the character is in
		int characterColumnNumber = calculateWhichColumnCharacterIsIn();
		// 2) Get columns to check
		List<Tile[]> columnsToCheck = getColumnsToCheck(characterColumnNumber);
		// 3) Check collision with those columns
		float firstColumnXValue = getWorld().getPosition() + characterColumnNumber * getTileSize();
		handleCollisionWithColumns(columnsToCheck, firstColumnXValue);
	}

	List<Tile[]> getColumnsToCheck(int characterColumnNumber) {
		// 1) Get the column that the character is in and the one after that
		List<Tile[]> columnsToCheck = new ArrayList<Tile[]>();
		columnsToCheck.add(getWorld().getColumn(characterColumnNumber + 1));
		columnsToCheck.add(getWorld().getColumn(characterColumnNumber + 2));
		return columnsToCheck;
	}

	int calculateWhichColumnCharacterIsIn() {
		return (int) Math.abs(getWorld().getPosition() - getGameCharacter().getPosition().getX()) / getTileSize();
	}

	void handleCollisionWithColumns(List<Tile[]> columns, float
			firstColumnXValue) {
		for (int col = 0; col < columns.size(); col++) {
			for (int row = 0; row < columns.get(col).length; row++) {
				Tile tile = columns.get(col)[row];
				if(tile != Tile.EMPTY) {
					float tileXPos = firstColumnXValue + col * getTileSize();
					float tileYPos = row * getTileSize();
					float characterXPos = getGameCharacter().getPosition().getX();
					float characterYPos = getGameCharacter().getPosition().getY();
					CollisionData collisionData = collisionChecker.checkCollisionWithTile(getTileSize(), tileXPos, tileYPos,
							characterXPos, characterYPos);

					if (collisionData.hasCollided()) {
						switch (collisionData.getDirection()) {
							case TOP:
								handleTopCollision();
								break;
							case BOTTOM:
								handleBottomCollision(tileYPos);
								break;
							case RIGHT:
								handleRightCollision();
								break;
							case LEFT:
								handleLeftCollision(tileYPos);
								break;
							default:
								break;
						}
					}
				}
			}
		}

	}

	void handleTopCollision() {
		getGameCharacter().setDead(true);
	}

	void handleLeftCollision(float tileYPos) {
		handleBottomCollision(tileYPos);
	}

	void handleRightCollision() {
		getGameCharacter().setDead(true);
	}

	void handleBottomCollision(float tileYPos) {
		getGameCharacter().handleCollisionFromBelow(tileYPos + getTileSize());
		getGameCharacter().setCollidingWithGround(true);
	}


	public GameCharacter getGameCharacter() {
		return gameCharacter;
	}

	public void setGameCharacter(GameCharacter gameCharacter) {
		this.gameCharacter = gameCharacter;
	}

	public WorldModel getWorld() {
		return world;
	}

	void setWorld(WorldModel world) {
		this.world = world;
	}

	public int getTileSize() {
		return tileSize;
	}
}
