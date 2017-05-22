package com.grupptva.runnergame.game.model;

import com.grupptva.runnergame.game.model.gamecharacter.GameCharacter;
import com.grupptva.runnergame.game.model.gamecharacter.Point;
import com.grupptva.runnergame.game.model.world.Tile;
import com.grupptva.runnergame.game.model.world.WorldModel;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
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

	List<Integer[]> attachOffsets;

	public HookLogic(GameCharacter gameCharacter, WorldModel world, int tileSize,
			int chunkWidth, int chunkHeight) {
		this.gameCharacter = gameCharacter;
		this.world = world;
		this.tileSize = tileSize;
		this.chunkWidth = chunkWidth;
		this.chunkHeight = chunkHeight;

		float angle = gameCharacter.getHookAngle();
		float radius = 100f;
		attachOffsets = calculateHookAttachOffsets(angle, radius, tileSize);
	}

	List<Integer[]> calculateHookAttachOffsets(float angle, float maxRadius,
			int tileSize) {
		List<Integer[]> offsets = new ArrayList<Integer[]>();

		//Check vertical lines
		double maxX = maxRadius * Math.cos(angle);
		for (double x = 0; x < maxX; x += tileSize) {
			double r = (float) (x / Math.cos(angle));
			double y = r * Math.sin(angle);

			//Normalize the values to get an index/offset instead.
			int normX = (int) (x / tileSize);
			int normY = (int) (y / tileSize);
			//Add offsets (x,y) and (x-1,y)
			if ((normX > 1 && normY > 1)) {
				offsets.add(new Integer[] { normX, normY });
				offsets.add(new Integer[] { normX - 1, normY });
			}
		}
		//Check horizontal lines
		float maxY = (float) (maxRadius * Math.sin(angle));
		for (double y = 0; y < maxY; y += tileSize) {
			double r = (float) (y / Math.sin(angle));
			double x = r * Math.cos(angle);

			//Normalize the values to get an index/offset instead.
			int normX = (int) (x / tileSize);
			int normY = (int) (y / tileSize);
			//Add offsets (x,y) and (x,y-1)
			if ((normX > 1 && normY > 1)) {
				offsets.add(new Integer[] { normX, normY });
				offsets.add(new Integer[] { normX, normY - 1 });
			}
		}
		//TODO: removeDuplicates(offsets);

		offsets = mergeSort(offsets);

		for (Integer[] i : offsets) {
			System.out.println(Arrays.toString(i));
		}

		return offsets;
	}

	static List<Integer[]> mergeSort(List<Integer[]> list) {
		if (list.size() <= 1) {
			return list; //A list with only one element is already sorted.
		}
		List<Integer[]> left = new ArrayList<Integer[]>();
		List<Integer[]> right = new ArrayList<Integer[]>();

		//Split list in half
		for (int i = 0; i < list.size(); i++) {
			if (i < list.size() / 2) {
				left.add(list.get(i));
			} else {
				right.add(list.get(i));
			}
		}
		//Recursively sort the list, in order to reach a point where it is made out of lists containing a single element
		//ie, made out of sorted lists.
		left = mergeSort(left);
		right = mergeSort(right);

		return merge(left, right);
	}

	private static List<Integer[]> merge(List<Integer[]> left, List<Integer[]> right) {
		List<Integer[]> result = new ArrayList<Integer[]>();

		//Loops until either list is empty.
		while (left.size() > 0 && right.size() > 0) {
			//Appends the lowest value, from either list, to the result list.
			double lSum = Math.sqrt(
					left.get(0)[0] * left.get(0)[0] + left.get(0)[1] * left.get(0)[1]);
			double rSum = Math.sqrt(right.get(0)[0] * right.get(0)[0]
					+ right.get(0)[1] * right.get(0)[1]);

			if (lSum <= rSum) {
				result.add(left.get(0));
				left.remove(0);
			} else {
				result.add(right.get(0));
				right.remove(0);
			}
		}
		//Adds the remaining values to the result, only the list with things left in its loop will run.
		while (left.size() > 0) {
			result.add(left.get(0));
			left.remove(0);
		}
		while (right.size() > 0) {
			result.add(right.get(0));
			right.remove(0);
		}
		return result;
	}

	public void castHook() {
		int charColumnIndex = (int) Math.abs(
				((world.getPosition() - gameCharacter.getPosition().getX()) / tileSize))
				+ 1;//ask NaN
		int charRowIndex = (int) (gameCharacter.getPosition().getY() / tileSize);

		List<Tile[]> relevantColumns = new ArrayList<Tile[]>();
		int maxXOffset = 0;
		int minXOffset = Integer.MAX_VALUE;

		for (Integer[] i : attachOffsets) {
			if (i[0] > maxXOffset) {
				maxXOffset = i[0];
			}
			if (i[0] < minXOffset) {
				minXOffset = i[0];
			}
		}
		System.out.println("minX: " + minXOffset + "   maxX: " + maxXOffset);
		for (int i = charColumnIndex + minXOffset; i <= charColumnIndex
				+ maxXOffset; i++) {
			relevantColumns.add(world.getColumn(i));
			System.out.println(
					Arrays.toString(relevantColumns.get(relevantColumns.size() - 1)));
		}
		System.out.println("ColCount: " + relevantColumns.size());
		for (Integer[] offset : attachOffsets) {
			int x = offset[0] - minXOffset;
			Tile[] column = relevantColumns.get(x);
			int y = charRowIndex + offset[1] - 1;
			System.out.println("X: " + x + " Y: " + y);
			if (y < column.length && y >= 0) { //Prevent OoB
				Tile tile = column[y];

				if (tile == Tile.OBSTACLE) {
					float charX = gameCharacter.getPosition().getX();
					float charXextra = charX % tileSize;

					float charY = gameCharacter.getPosition().getY();
					float charYextra = charY % tileSize;

					gameCharacter
							.initHook(new Point(charX - charXextra + offset[0] * tileSize,
									charY - charYextra + (offset[1] - 1) * tileSize));

					System.out.println("HIT");
				}
			}
		}
	}

	private void handleCastHook() {
	}

	List<Integer> colIndexes = new ArrayList();

	private List<Tile[]> getColumnsToCheckWhenCastingHook(Point hookEndPos) {
		List<Tile[]> columns = new ArrayList();
		colIndexes = new ArrayList();
		int columnHookIsIn = (int) Math.abs(world.getPosition() - hookEndPos.getX())
				/ tileSize;
		int columnCharacterIsIn = (int) Math
				.abs(world.getPosition() - gameCharacter.getPosition().getX()) / tileSize;
		for (int i = columnCharacterIsIn; i <= columnHookIsIn; i++) {
			columns.add(world.getColumn(i));
			colIndexes.add(i);
		}
		return columns;
	}

	Point getPositionWhereHookExitsWorld() {
		Point hookStart = new Point(gameCharacter.getPosition().getX() + tileSize / 2f,
				gameCharacter.getPosition().getY() + tileSize / 2f);

		float endX = hookStart.getX() + (chunkHeight * tileSize - hookStart.getY())
				/ (float) Math.tan(gameCharacter.getHookAngle());
		if (endX < chunkWidth * tileSize) {
			return new Point(endX, chunkHeight * tileSize);
		} else {
			float endY = hookStart.getY() + (chunkWidth * tileSize - hookStart.getX())
					* (float) Math.tan(gameCharacter.getHookAngle());
			return new Point(chunkWidth * tileSize, endY);
		}

	}

}
