package com.grupptva.runnergame.game.services.worldgenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.grupptva.runnergame.game.model.gamecharacter.GameCharacter;
import com.grupptva.runnergame.game.services.worldgenerator.GeneratorChunk;
import com.grupptva.runnergame.game.services.worldgenerator.GeneratorChunk.Tile;

/**
 * Implementation of GeneratorStep that adds a step where the character has to
 * jump from one platform to another.
 * 
 * @author Mattias
 *
 */
class JumpStep extends GeneratorStep {
	public JumpStep(float vx, int tileSize, GameCharacter character, Random rng, int chance) {
		float v0y = character.getJumpInitialVelocity();
		float a = character.getGravity();
		this.chance = chance;

		this.rng = rng;

		landingOffsets = calculateJumpLandingOffsets(v0y, a, tileSize, vx);
	}

	List<Integer[]> landingOffsets;

	/**
	 * The first half of the jumpStep method. Split into two for easy
	 * implementation of logging the generation {@See jumpStep(..) &
	 * jumpStep(..,chunkLog)}.
	 * 
	 * @param chunk
	 * @param currentTile
	 */
	private boolean jumpStepPart1(GeneratorChunk chunk, Integer[] currentTile) {
		List<Integer> validJumpIndexes = chunk.getValidOffsetIndexes(landingOffsets, currentTile);
		if (validJumpIndexes.size() == 0) {
			//Failsafe to prevent infinite loop. This code is pretty much identical to failsafes in other places
			//but turning it into a boolean method doesn't remove the if statement since it would return true
			//if it passes the check, and that would require a new if statement in order to prevent it from ending this method early.
			createPlatform(chunk, currentTile);

			return false;
		}
		chunk.setValidOffsetsToValue(landingOffsets, validJumpIndexes, currentTile, Tile.POSSIBLESTAND);

		Integer[] offset = landingOffsets.get(validJumpIndexes.get(rng.nextInt(validJumpIndexes.size())));

		//If to close to the bottom of the chunk: used weighted random selection instead
		//where tiles above the character are weighted higher and thus have an increased chance 
		//being selected, to prevent the generation from becoming stuck at the bottom.
		if (currentTile[1] < (chunk.height * .5)) { //.5 arbitrary height
			int[] indexWeights = new int[validJumpIndexes.size()];
			int totalWeights = 0;
			for (int i = 0; i < validJumpIndexes.size(); i++) {
				indexWeights[i] = 10;
				if (landingOffsets.get(i)[1] > 0) {
					indexWeights[i] *= landingOffsets.get(i)[1] * 5; //5 arbitrary weight amount.
				}
				totalWeights += indexWeights[i];
			}
			int r = rng.nextInt(totalWeights);
			int weightSum = 0;
			for (int i = 0; i < indexWeights.length; i++) {
				weightSum += indexWeights[i];
				if (r <= weightSum) {
					offset = landingOffsets.get(validJumpIndexes.get(i));
					break;
				}
			}
		}

		currentTile[0] += offset[0];
		currentTile[1] += offset[1];
		return true;
	}

	/**
	 * A copy of jumpStep, that also logs all important changes to the chunk for
	 * visualiation purposes.
	 * 
	 * @param chunk
	 *            The chunk currently being generated.
	 * @param currentTile
	 *            The "character's" current position.
	 * @param chunkLog
	 *            A list of every interation of the chunk being generated.ö
	 */
	@Override
	public void step(GeneratorChunk chunk, Integer[] currentTile, List<Tile[][]> chunkLog) {
		if (!jumpStepPart1(chunk, currentTile)) //Ends the jumpStep if there are no viable places to jump to.
			return;

		chunkLog.add(chunk.deepCopyTiles());
		chunk.tiles[currentTile[1]][currentTile[0]] = Tile.FULL;
		chunkLog.add(chunk.deepCopyTiles());

		createPlatform(chunk, currentTile);
	}

	/**
	 * Called if the current step in generation is a jump. Detects all viable
	 * positions for the character to jump to, from the currentTile, and
	 * randomly selects on of them. Updates {@param chunk} with all the
	 * possiblities and changes {@param currentTile} to the select tile.
	 * 
	 * @param chunk
	 *            The chunk currently being generated.
	 * @param currentTile
	 *            The "character's" current position.
	 */
	@Override
	public void step(GeneratorChunk chunk, Integer[] currentTile) {
		if (!jumpStepPart1(chunk, currentTile)) //Ends the jumpStep if there are no viable places to jump to.
			return;

		chunk.tiles[currentTile[1]][currentTile[0]] = Tile.FULL;

		createPlatform(chunk, currentTile);
	}

	/**
	 * Returns the indexes of every true value in {@param tiles}.
	 * 
	 * @param tiles
	 * @return List of indexes.
	 */
	static List<Integer[]> getTrueIndexes(boolean[][] tiles) {
		List<Integer[]> indexes = new ArrayList<Integer[]>();
		//Loop through every index.
		for (int y = 0; y < tiles.length; y++) {
			for (int x = 0; x < tiles[0].length; x++) {
				if (tiles[y][x] == true)
					indexes.add(new Integer[] { x, y }); //Add index of true to return list.
			}
		}
		return indexes;
	}

	/**
	 * Calculates the offset of every tile the character can jump to and land
	 * on, based on the second grade equation that makes up its jump movement.
	 * 
	 * @param v0y
	 *            The initial upwards velocity that the character jumps with.
	 * @param a
	 *            The acceleration due to gravity. Should be negative.
	 * @param tileSize
	 *            The width&height of a tile.
	 * @param vx
	 *            The constant X velocity of the character.
	 * @return A list containing the offsets, in relation to the tile the
	 *         character is at, that the character can reach by jumping.
	 */
	static List<Integer[]> calculateJumpLandingOffsets(float v0y, float a, int tileSize, float vx) {
		boolean[][] jumpGrid = calculateJumpGrid(v0y, a, tileSize, vx); //Grid of tiles that the character might be able to reach by jumping.
		int halfGridHeight = jumpGrid.length / 2; //Save half of grid height, since grid height [-apexY, apexY] of jump, and character starts at 0.
		List<Integer[]> trueIndexes = getTrueIndexes(jumpGrid);
		List<Integer[]> landingIndexes = getLandingIndexes(trueIndexes);

		//Offset Y values so top half is positive and bottom half negative
		//And shift everything one tile to the right

		for (Integer[] index : landingIndexes) {
			index[1] -= halfGridHeight;
			index[0]++;
		}

		return landingIndexes;
	}

	/**
	 * Sorts primarily by ascending X (index[0]), a secondarily by Y (index[1]).
	 * Secondary is ascending for the left half of the list and descending for
	 * the right half. This is done because it's used to sort the jump which is
	 * a negative parabola.
	 * 
	 * @param jumpIndexes
	 *            List to be sorted.
	 * @return Sorted list.
	 */
	static List<Integer[]> sortJumpIndexes(List<Integer[]> jumpIndexes) {
		//Sort everything by ascending X, with equal X values to the left of the apex sorted by ascending Y
		//and equals to the right of the apex by descending Y.
		jumpIndexes = mergeSort(jumpIndexes, 0);

		//Split at apex	
		int currentX = 0;
		int currentY = 0;
		int currentIndex = 0;
		for (int i = 0; i < jumpIndexes.size(); i++) {
			if (jumpIndexes.get(i)[1] > currentY) {
				currentY = jumpIndexes.get(i)[1];
				currentX = jumpIndexes.get(i)[0];
				currentIndex = i;
			}
		}

		//Failsafe so that it splits between different X values, otherwise it wont sort properly
		int lowestMidXIndex = currentIndex;
		while (lowestMidXIndex > 1 && jumpIndexes.get(lowestMidXIndex - 1)[0] == currentX) {
			lowestMidXIndex--;
		}

		//Split the list.
		List<Integer[]> left = jumpIndexes.subList(0, lowestMidXIndex);
		List<Integer[]> right = jumpIndexes.subList(lowestMidXIndex, jumpIndexes.size());

		//Sort by Y
		left = mergeSort(left, 1);
		right = mergeSort(right, 1);
		Collections.reverse(right); //Reverse right half to get descending sort.

		left.addAll(right); //Add right half to left half to get everything in one list again.
		left = mergeSort(left, 0); //Sort entire list by ascending X. Sort is stable so ascending/descending Y is kept.

		return left;
	}

	/**
	 * Returns a new version of {@param list} sorted in ascending order, by the
	 * value of Integer[index]. {@See https://en.wikipedia.org/wiki/Merge_sort}
	 * 
	 * @param list
	 *            List of Integer[] to be sorted.
	 * @param index
	 *            the index of the Integer[] that is being used to determine the
	 *            sorting.
	 * @return A new sorted list.
	 */
	static List<Integer[]> mergeSort(List<Integer[]> list, int index) {
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
		left = mergeSort(left, index);
		right = mergeSort(right, index);

		return merge(left, right, index);
	}

	/**
	 * Merges two sorted lists into a sorted list. Should not be called outside
	 * of @see mergeSort
	 * 
	 * @param left
	 *            One of the two sorted lists being merged.
	 * @param right
	 *            One of the two sorted lists being merged.
	 * @param index
	 *            The index of the Integer[] that is being used to determine the
	 *            sorting.
	 * @return
	 */
	private static List<Integer[]> merge(List<Integer[]> left, List<Integer[]> right, int index) {
		List<Integer[]> result = new ArrayList<Integer[]>();

		//Loops until either list is empty.
		while (left.size() > 0 && right.size() > 0) {
			//Appends the lowest value, from either list, to the result list.
			if (left.get(0)[index] <= right.get(0)[index]) {
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

	/**
	 * Sorts the jumpIndexes list and then checks every tile, to the right of
	 * the apex, if it is below the previous tile. Adds all of those tiles to a
	 * new list and returns them.
	 * 
	 * @param jumpIndexes
	 *            List to be sorted and whose tiles should be investigated.
	 * @return A new list containing all tileIndexes who are below the previous
	 *         tile.
	 */
	static List<Integer[]> getLandingIndexes(List<Integer[]> jumpIndexes) {
		jumpIndexes = sortJumpIndexes(jumpIndexes);

		List<Integer[]> landingIndexes = new ArrayList<Integer[]>();
		for (int i = jumpIndexes.size() / 2; i < jumpIndexes.size(); i++) {
			if (jumpIndexes.get(i)[1] < jumpIndexes.get(i - 1)[1]) {
				landingIndexes.add(jumpIndexes.get(i));
			}
		}
		return landingIndexes;
	}

	/**
	 * Calculates the amount of frames, from origin, to reach the apex of the
	 * characters jump.
	 * 
	 * @param v0y
	 *            The initial Y velocity of the character.
	 * @param a
	 *            Constant acceleration of the character, should be negative.
	 * @return Frames to the apex.
	 */
	static float getFramesToApexOfJump(float v0y, float a) {
		//v=v_0+a*t, v = 0 => t=v_0/a
		return Math.abs(v0y / (a));
	}

	/**
	 * Calculates the height of the apex of a jump, relative to origin.
	 * 
	 * @param v0y
	 *            The initial Y velocity of the character.
	 * @param a
	 *            Constant acceleration of the character, should be negative.
	 * @return Height of the apex.
	 */
	static float getRelativeHeightOfApex(float v0y, float a) {
		//integrate v=v_0+a*t dt <=> y = v_0*t-(a*t^2)/2
		float t = getFramesToApexOfJump(v0y, a);
		return getJumpY(v0y, a, t);
	}

	/**
	 * Gets the amount of frames(time) that corresponds to the rightmost point
	 * of a jump with a Y value of {@param y}.
	 * 
	 * @param v0y
	 *            The initial Y velocity of the character.
	 * @param a
	 *            Constant acceleration of the character, should be negative.
	 * @param y
	 *            The Y value of the sought point.
	 * @param y0
	 *            The initial Y value of the character.
	 * @return The amount of frames, from the initial value {@param y0}, to the
	 *         sought value {@param y}.
	 */
	static float getFramesToYValue(float v0y, float a, float y, float y0) {
		float sqrt = (float) Math.sqrt(2 * a * y - 2 * a * y0 + v0y * v0y);
		//Get the furthest future time, ie rightmost point with that y value.
		if (sqrt - v0y > -sqrt - v0y)
			sqrt *= -1;

		return (sqrt - v0y) / a;
	}

	/**
	 * Calculates the width and height of a jump starting at y=0, then going up
	 * to y=apex and then going down to y=-apex. The dimensions are then
	 * normalized with {@param tileSize}.
	 * 
	 * @param v0y
	 *            The initial Y velocity of the character.
	 * @param a
	 *            Constant acceleration of the character, should be negative.
	 * @param tileSize
	 *            Size of each tile in the grid.
	 * @param vx
	 *            Constant X velocity of the character.
	 * @return The size of the jump grid.
	 */
	private static int[] getSizeOfPossibleJumpGrid(float v0y, float a, float tileSize, float vx) {
		int[] size = new int[] { 0, 0 };
		float maxY = getRelativeHeightOfApex(v0y, a);
		size[1] = (int) Math.ceil(maxY / tileSize) * 2; //*2 due to going up to the apex of the jump and then down to -apex
		float t = getFramesToYValue(v0y, a, -maxY + tileSize, 0);

		size[0] = (int) Math.ceil((t * vx) / tileSize);
		return size;
	}

	/**
	 * Returns an empty 2d array of booleans, with dimensions decided by
	 * {@param Size}
	 * 
	 * @param size
	 *            The dimensions of the 2d array, in the format [0] = X, [1] =
	 *            Y.
	 * @return An empty 2d array of booleans.
	 */
	private static boolean[][] createEmptyJumpGrid(int[] size) {
		return new boolean[size[1]][size[0]];
	}

	/**
	 * Given an initial Y velocity, a constant Y acceleration and a time/X
	 * value: returns the Y value that corresponds to the X value in the
	 * constant acceleration equation made out of the initial Y values.
	 * 
	 * @param v0y
	 *            The initial velocity
	 * @param a
	 *            The constant acceleration.
	 * @param t
	 *            The time/X value
	 * @return The corresponding Y value.
	 */
	private static float getJumpY(float v0y, float a, float t) {
		return (v0y * t) + ((a * t * t) / 2);
	}

	/**
	 * 
	 * Same as @see getJumpY except the equation/parabola is translated across
	 * the x-axis by {@param xTranslatio}
	 * 
	 * @param v0y
	 *            The initial velocity
	 * @param a
	 *            The constant acceleration.
	 * @param t
	 *            The time/X value
	 * @param xTranslation
	 *            The translation across X-axis
	 * @return
	 */
	static float getJumpY(float v0y, float a, float t, float xTranslation) {
		return (v0y * (t - xTranslation)) + ((a * (t - xTranslation) * (t - xTranslation)) / 2);
	}

	/**
	 * Creates a grid of booleans where every tile that the character intersects
	 * with during a jump has the value true, and all other tiles have the value
	 * false.
	 * 
	 * //TODO: Rewrite jump calculations to not need this, should be possible to
	 * just get indexes immediately, instead of converting them to booleans and
	 * then back to indexes. Upon rewriting this the method that removes
	 * duplicates will be needed, since currently the boolean grid takes care of
	 * that.
	 * 
	 * @param v0y
	 * @param a
	 * @param tileSize
	 * @param vx
	 * @return
	 */
	static boolean[][] calculateJumpGrid(float v0y, float a, float tileSize, float vx) {
		//Create a grid of false booleans. Size depends on how far the character can reach by jumping.
		boolean[][] jumpGrid = createEmptyJumpGrid(getSizeOfPossibleJumpGrid(v0y, a, tileSize, vx));

		//Calculations are done by simulating a point performing the jump and checking where it can land.
		//Starting y position of character is in the middle of the testing grid, since possible locations are [-yApex, yApex]
		//ie the character can gain or lose up to yApex in height.
		float y0 = jumpGrid.length * tileSize / 2;

		//In order to get every tile that the jump parabola intersects with, the (X or Y) coordinates between tiles are sent into the 
		//parabolas equation. The (Y or X respectively) solution is then used to select the who use the solution as a coordinate and 
		//who are connected to the initial "line between" coordinate. {@See http://i.imgur.com/6e25A1N.png} for graphical illustration.
		List<float[]> points = new ArrayList<float[]>(); //List containing all points.
		//Do x lines   | 
		for (int x = 0; x < jumpGrid[0].length * tileSize; x += tileSize / vx) {
			float y = getJumpY(v0y, a, x / vx) + y0;

			//Normalize the values so that they can be turned into indexes.
			int normY = (int) (y / tileSize);
			int normX = (int) (x / tileSize);

			points.add(new float[] { normX, normY }); //Save index.
		}

		//Add the tiles connected horizontally to every point.
		int index = 0;
		for (; index < points.size(); index++) {
			int x = (int) points.get(index)[0];
			int y = (int) points.get(index)[1];
			//Make sure it is inside bounds.
			if (x >= 0 && x < jumpGrid[0].length && y >= 0 && y < jumpGrid.length) {
				//Add tiles (x,y) and (x-1,y)
				jumpGrid[y][x] = true;
				if (x >= 1)
					jumpGrid[y][x - 1] = true;
			}
		}

		//Do y lines   _
		//Drawing a horizontal line of second grade equation, y=ax^2+bx+c, gives 2 results
		float framesToApex = getFramesToApexOfJump(v0y, a);
		float framesToZero = 2 * framesToApex;
		for (int y = 0; y < jumpGrid.length * tileSize; y += tileSize) {

			float adjustedY = y - y0; //TODO: Figure out why this was needed.

			float x = getFramesToYValue(v0y, a, adjustedY, 0); //The x value of the rightmost result from drawing the line.

			//Normalize the values so that they can be turned into indexes.
			int normY = (int) (y / tileSize);
			int normX = (int) (vx * x / tileSize);
			points.add(new float[] { normX, normY }); //Save index.

			//Make sure the leftmost result is inside bounds, since it looks something like this, any x past framesToZero is outside the left edge.
			//    _____
			//    |/\ |
			//    |  \|
			//    -----
			if (x < framesToZero) {
				float dx = x - framesToApex; //Get distance to apex from rightmost x, since leftmost x is on the opposite side of the apex.
				float leftX = framesToApex - dx; //The value of the leftmost x result form drawing the line.
				float normLeftX = (int) (leftX / tileSize);//Normalize the value so that it can be turned into an index.

				points.add(new float[] { normLeftX, normY }); //Save index.
			}
		}
		//Add the tiles connected vertically to every point.
		for (; index < points.size(); index++) {
			int x = (int) points.get(index)[0];
			int y = (int) points.get(index)[1];
			//Make sure it is inside bounds.
			if (x >= 0 && x < jumpGrid[0].length && y >= 0 && y < jumpGrid.length) {
				//Add tiles (x,y) and (x,y-1)
				jumpGrid[y][x] = true;
				if (y >= 1)
					jumpGrid[y - 1][x] = true;
			}
		}

		return jumpGrid;
	}
}
