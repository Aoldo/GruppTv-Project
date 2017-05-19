package com.grupptva.runnergame.game.services;

import com.grupptva.runnergame.game.model.gamecharacter.GameCharacter;
import com.grupptva.runnergame.game.model.world.Tile;
import com.grupptva.runnergame.game.model.world.WorldModel;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

/**
 * Created by agnesmardh on 2017-05-16.
 */
public class CollisionHandler {

	private GameCharacter gameCharacter;
	private WorldModel world;
	private final int tileSize;

	public CollisionHandler(GameCharacter gc, WorldModel world, int tileSize) {
		setGameCharacter(gc);
		setWorld(world);
		this.tileSize = tileSize;

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
				float tileXPos = firstColumnXValue + col * getTileSize();
				float tileYPos = row * getTileSize();
				handleCollisionWithTile(columns.get(col)[row], tileXPos, tileYPos);
			}
		}

	}

	void handleCollisionWithTile(Tile tile, float tileXPos, float tileYPos) {
		float dx = getGameCharacter().getPosition().getX() - tileXPos;
		float dy = getGameCharacter().getPosition().getY() - tileYPos;
		//out.printf("dx: %f%ndy: %f%n", dx, dy);
		//out.printf("X: %f%nY: %f%n%n", getGameCharacter().getPosition().getX(), getGameCharacter().getPosition().getY());
		if (tile != Tile.EMPTY) {
			if (Math.abs(dx) <= getTileSize() && Math.abs(dy) <= getTileSize()) { // is there a collision?
				//out.printf("Y: %f%n", getGameCharacter().getPosition().getY());
				//out.printf("tileXPos: %f%ntileYPos: %f%n", tileXPos, tileYPos);
				if (dy > dx) { // Is it
					//out.println("dy > dx");
					//out.printf("%f > %f%n", dy, dx);
					if (dy >= -dx) {
						//out.printf("%f > -%f%n", dy, dx);
						//bottom
						//out.println("bottom");
						handleBottomCollision(tileYPos);
					} else {
						//right
						//kill character
						//out.println("right");
						handleRightCollision();
					}
				} else {
					//out.println("dy <= dx");
					if (dy != dx) {
						if (dy > -dx) {
							//left
							//out.println("left");
							handleLeftCollision(tileYPos);
						} else {
							//top
							//kill character
							//out.println("top");
							handleTopCollision();
						}
					} else if (dy > 0 && dx > 0){
						//bottom
						//out.println("bottom");
						handleBottomCollision(tileYPos);
					} else {
						//out.println("top");
						handleTopCollision();
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
