package com.grupptva.runnergame.game.model;

import com.grupptva.runnergame.game.model.gamecharacter.GameCharacter;
import com.grupptva.runnergame.game.model.gamecharacter.Point;
import com.grupptva.runnergame.game.model.world.Tile;
import com.grupptva.runnergame.game.model.world.WorldModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created on 2017-05-20.
 *
 * Responsibility: Handle logic for casting the hook.
 *
 * Used by:
 * @see GameLogic
 *
 * Uses:
 * @see GameCharacter
 * @see Point
 * @see Tile
 * @see WorldModel
 *
 *
 * @author Agnes and Karl
 * Revised by Mattias
 */
public class HookLogic {
	private GameCharacter gameCharacter;
	private WorldModel world;
	private final int tileSize;

	List<Integer[]> attachOffsets;

	public HookLogic(GameCharacter gameCharacter, WorldModel world, int tileSize) {
		this.gameCharacter = gameCharacter;
		this.world = world;
		this.tileSize = tileSize;

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

		//By sorting the offsets by the distance to the offset
		//they can be looped through in order in order to 
		//quickly find the first one that is Tile.OBSTACLE.
		offsets = mergeSortByDistance(offsets);

		return offsets;
	}

	static List<Integer[]> mergeSortByDistance(List<Integer[]> list) {
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
		left = mergeSortByDistance(left);
		right = mergeSortByDistance(right);

		return mergeByDistance(left, right);
	}

	private static List<Integer[]> mergeByDistance(List<Integer[]> left,
			List<Integer[]> right) {
		List<Integer[]> result = new ArrayList<Integer[]>();

		//Loops until either list is empty.
		while (left.size() > 0 && right.size() > 0) {
			//Appends the lowest distance, from either list, to the result list.
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
		for (int i = charColumnIndex + minXOffset; i <= charColumnIndex
				+ maxXOffset; i++) {
			relevantColumns.add(world.getColumn(i));
			Arrays.toString(relevantColumns.get(relevantColumns.size() - 1));
		}
		for (Integer[] offset : attachOffsets) {
			int x = offset[0] - minXOffset;
			Tile[] column = relevantColumns.get(x);
			int y = charRowIndex + offset[1] - 1;
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

				}
			}
		}
	}
}