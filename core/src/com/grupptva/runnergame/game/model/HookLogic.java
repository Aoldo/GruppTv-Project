package com.grupptva.runnergame.game.model;

import com.grupptva.runnergame.game.model.gamecharacter.GameCharacter;
import com.grupptva.runnergame.game.model.gamecharacter.Point;
import com.grupptva.runnergame.game.model.world.Tile;
import com.grupptva.runnergame.game.model.world.WorldModel;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

/**
 * Created by agnesmardh on 2017-05-20.
 */
public class HookLogic {
	private GameCharacter gameCharacter;
	private WorldModel world;
	private final int tileSize;
	private final int chunkWidth;
	private final int chunkHeight;

	public HookLogic(GameCharacter gameCharacter, WorldModel world, int tileSize, int chunkWidth, int chunkHeight) {
		this.gameCharacter = gameCharacter;
		this.world = world;
		this.tileSize = tileSize;
		this.chunkWidth = chunkWidth;
		this.chunkHeight = chunkHeight;
	}



	public void castHook() {
		Point hookEndPos = getPositionWhereHookExitsWorld();
		List<Tile[]> columnsToCheck = getColumnsToCheckWhenCastingHook(hookEndPos);
		handleCastHook(columnsToCheck, hookEndPos);
	}

	private void handleCastHook(List<Tile[]> columnsToCheck, Point hookEndPos) {
		for (int col = 0; col < columnsToCheck.size(); col++) {
			for(int row = 0; row < columnsToCheck.get(col).length; row++) {
				Tile tile = columnsToCheck.get(col)[row];
				if(tile != Tile.EMPTY) {
					out.println("Not empty!!");
					float columnCharacterIsIn = Math.abs(world.getPosition() - gameCharacter.getPosition().getX())
							/ tileSize + 1;
					float tileXPos = columnCharacterIsIn + col * tileSize;
					out.printf("dx: %.3f%n", gameCharacter.getPosition().getX() - tileXPos);
					float tileYPos = row * tileSize;
					boolean intersectsTile = checkIfLineIntersectsTile(hookEndPos, tileXPos, tileYPos);
					if(intersectsTile) {
						out.println("Intersected!!");
						Point hookPosition = new Point(tileXPos + tileSize, tileYPos + tileSize);
						gameCharacter.initHook(hookPosition);
					}
				}
			}
		}
	}

	boolean checkIfLineIntersectsTile(Point hookEndPos, float tileXPos, float tileYPos) {
		float x = gameCharacter.getPosition().getX() + tileSize/2f;
		float y = gameCharacter.getPosition().getY() + tileSize/2f;
		float dx = hookEndPos.getX() - x;
		float dy = hookEndPos.getY() - y;
		float left = x - tileXPos;
		float right = tileXPos + tileSize - x;
		float top = y - tileYPos + tileSize;
		float bottom = tileYPos - y;
		float[] p = {-dx, dx, -dy, dy};
		float[] q  = {x-left, right-x, y-top, bottom-y};
		float u1 = Float.NEGATIVE_INFINITY;
		float u2 = Float.POSITIVE_INFINITY;

		//Line2D line2D;
		//Rectangle2D rectangle2D;
		//line2D.intersects(rectangle2D);

		for(int i = 0; i < 4; i++) {
			if(p[i] == 0) {
				if(q[i] < 0) {
					return false;
				}
			} else {
				float t = q[i] / p[i];
				if(p[i] < 0 && u1 < t) {
					u1 = t;
				}
				else if(p[i] > 0 && u2 > t) {
					u2 = t;
				}
			}
		}
		if(u1 > u2 || u1 > 1 || u1 < 0) {
			return false;
		}
		return true;
	}

	private List<Tile[]> getColumnsToCheckWhenCastingHook(Point hookEndPos) {
		List<Tile[]> columns = new ArrayList<Tile[]>();
		int columnHookIsIn = (int) (hookEndPos.getX() - world.getPosition()) / tileSize + 1;
		int columnCharacterIsIn = (int) (gameCharacter.getPosition().getX() - world.getPosition()) / tileSize + 1;
		for(int i = columnCharacterIsIn; i <= columnHookIsIn; i++){
			columns.add(world.getColumn(i));
		}
		return columns;
	}

	private Point getPositionWhereHookExitsWorld() {
		Point hookStart = new Point(gameCharacter.getPosition().getX() + tileSize / 2,
				gameCharacter.getPosition().getX() + tileSize / 2);
		float endX = hookStart.getX() + (chunkHeight * tileSize - hookStart.getY())
				/ (float) Math.tan(gameCharacter.getHookAngle());
		if (endX < chunkWidth) {
			return new Point(endX, chunkHeight);
		} else {
			float endY = hookStart.getY() + (chunkWidth - hookStart.getX()) *
					(float) Math.tan(gameCharacter.getHookAngle());
			return new Point(chunkWidth, endY);
		}

	}

}
