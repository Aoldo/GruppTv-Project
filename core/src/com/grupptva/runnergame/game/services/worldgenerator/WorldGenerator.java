package com.grupptva.runnergame.game.services.worldgenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.grupptva.runnergame.game.model.gamecharacter.GameCharacter;
import com.grupptva.runnergame.game.model.world.Chunk;

/**
 * 
 * @author Mattias
 */
public class WorldGenerator {
	/**
	 * The width of generated chunks.
	 */
	private final int chunkWidth;
	/**
	 * The height of generated chunks.
	 */
	private final int chunkHeight;

	private Random rng;

	//These are the chances of different types of generation happening.
	private int jumpStepChance = 100;
	private int hookStepChance = 30;

	/**
	 * The leftmost Y coordinate of the next chunk being generated.
	 */
	private int initY;

	/**
	 * Determines the maximum size of platforms created. Smaller values make the
	 * platforms smaller and by exension the game harder.
	 */
	private int currentTileExtraBuffer = 3;

	/**
	 * Used to select predefined values for {@see currentTileExtraBuffer}, which
	 * is used to determine the maximum size of the platforms created during
	 * generation. Larger values give the player more leeway during platforming,
	 * ie make the game easier.
	 * 
	 * @author Mattias
	 *
	 */
	public enum BufferPresets {
		NONE, SMALL, MEDIUM, HUGE;
	}

	/**
	 * Different kinds of tiles used in generation of chunks. Some are used for
	 * visualizing the generation, and the rest represent values used in the
	 * actual world.
	 * 
	 * @author Mattias
	 */
	public enum Tile {
		EMPTY, FULL, POSSIBLEHOOK, POSSIBLESTAND, HOOKTARGET;
	}

	/**
	 * The offsets compared to the "current tile" that the character can attach
	 * its hook to.
	 */
	private List<Integer[]> hookAttachOffsets = new ArrayList<Integer[]>(); //TODO: Create object to store this and hookLandingOffsetList?
	/**
	 * The offsets compared to the "current tile" that the character can jump
	 * and land on.
	 */
	private List<Integer[]> jumpOffsets = new ArrayList<Integer[]>();

	/**
	 * A list of lists that have the offsets the character can land on after
	 * using the hook the offset at the exact same index inside @see
	 * hookAttachoffsets.
	 */
	private List<List<Integer[]>> hookLandingOffsetList = new ArrayList<List<Integer[]>>(); //TODO: Create object to store this and hookAttachOffset?

	/**
	 * Creates a new WorldGenerator, parameters are the coordinates in the grid
	 * that the character can reach in different ways, in relation the the tile
	 * the character is currently standing on, or attached to. The offset should
	 * be in integers that represent the index of the tiles in question inside
	 * of a chunk.
	 * 
	 * @param hookAttachOffsets
	 * @param jumpOffsets
	 * @param hookJumpOffsets
	 * @param seed
	 */
	public WorldGenerator(float vx, int tileSize, Long seed, int chunkWidth, int chunkHeight, int initY,
			GameCharacter character) {

		float v0y = character.getJumpInitialVelocity();
		float a = character.getGravity();
		float angle = 1;
		float radius = 75f;

		rng = new Random(seed);
		this.initY = initY;

		this.chunkHeight = chunkHeight;
		this.chunkWidth = chunkWidth;

		jumpOffsets = calculateJumpLandingOffsets(v0y, a, tileSize, vx);

		initHookOffsets(v0y, a, vx, tileSize, angle, radius, character);
	}

	/**
	 * Initializes the offsets used in hookStep.
	 * 
	 * @param v0y
	 *            The initial Y velocity of the characters jump.
	 * @param a
	 *            The constant acceleration of the character.
	 * @param vx
	 *            The constant X velocity of the character.
	 * @param tileSize
	 *            The width and height of a tile.
	 * @param angle
	 *            The angle that the hook is launched at.
	 * @param maxRadius
	 *            The maximum radius of the hook.
	 */
	private void initHookOffsets(float v0y, float a, float vx, int tileSize, float angle, float maxRadius,
			GameCharacter character) {
		hookAttachOffsets = calculateHookAttachOffsets(angle, maxRadius, tileSize);
		for (Integer[] attachOffset : hookAttachOffsets) {
			hookLandingOffsetList.add(calculateHookLandingOffsets(attachOffset, tileSize, a, v0y, vx, character));
		}
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
	float getFramesToApexOfJump(float v0y, float a) {
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
	float getRelativeHeightOfApex(float v0y, float a) {
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
	float getFramesToYValue(float v0y, float a, float y, float y0) {
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
	private int[] getSizeOfPossibleJumpGrid(float v0y, float a, float tileSize, float vx) {
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
	private boolean[][] createEmptyJumpGrid(int[] size) {
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
	private float getJumpY(float v0y, float a, float t) {
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
	float getJumpY(float v0y, float a, float t, float xTranslation) {
		return (v0y * (t - xTranslation)) + ((a * (t - xTranslation) * (t - xTranslation)) / 2);
	}

	/**
	 * Creates a grid of booleans where every tile that the character intersects
	 * with during a jump has the value true, and all other tiles have the value
	 * false.
	 * 
	 * //TODO: Rewrite jump calculations to not need this, should be possible to
	 * just get indexes immediately, instead of converting them to booleans and
	 * then back to indexes.
	 * 
	 * @param v0y
	 * @param a
	 * @param tileSize
	 * @param vx
	 * @return
	 */
	boolean[][] calculateJumpGrid(float v0y, float a, float tileSize, float vx) {
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

	/**
	 * Calculates which tiles the character can land on by swinging from
	 * {@param attachOffset}.
	 * 
	 * @param attachOffset
	 * @param tileSize
	 * @param a
	 * @param v0y
	 * @param vx
	 * @return A list containing every single offset, in relation to the
	 *         attachOffset, that the character can land on.
	 */
	List<Integer[]> calculateHookLandingOffsets(Integer[] attachOffset, int tileSize, float a, float v0y, float vx,
			GameCharacter character) {
		List<Integer[]> landingOffsets = new ArrayList<Integer[]>();

		float r = (float) Math
				.sqrt(tileSize * tileSize * (attachOffset[0] * attachOffset[0] + attachOffset[1] * attachOffset[1])); //Radius of rope/distance to attachOffset

		List<Integer[]> swingOffsets = calculateHookSwingOffsets(r, tileSize); //Tiles character collide with while swinging.

		for (Integer[] swingOffset : swingOffsets) {
			float yVel = character.getReleaseVelocity(swingOffset[0], swingOffset[1], 0, 0);

			if (yVel > 0) {
				List<Integer[]> swingLandingOffsets = calculateJumpLandingOffsets(yVel, a, tileSize, vx);

				for (Integer[] landingOffset : swingLandingOffsets) {
					//Offset them by the swing position so that they are in relation to the attachment point
					//instead of being in relation to the swing tile.
					landingOffset[0] += swingOffset[0];
					landingOffset[1] += swingOffset[1];
				}

				landingOffsets.addAll(swingLandingOffsets);
			}
		}
		removeDuplicates(landingOffsets);

		return landingOffsets;
	}

	/**
	 * Draws a circle with radius {@param r} around origin and checks which
	 * tiles in the 4th quadran(bottom right) the circle intersects with.
	 * 
	 * @param r
	 *            The radius of the circle.
	 * @return A list containing every overlapped tile's offset.
	 */
	List<Integer[]> calculateHookSwingOffsets(float r, int tileSize) {
		//Circle equation: r^2 = x^2+y^2
		List<Integer[]> offsets = new ArrayList<Integer[]>();

		int normR = (int) (r / tileSize);
		//Check vertical lines
		for (float x = 0; x < r; x += tileSize) {
			float y = getCircleY(r, x);
			int normX = (int) (x / tileSize);
			int normY = (int) Math.round(y / tileSize);

			if (normY < 0 && normX >= 0 && normY >= -normR && normX < normR) {
				offsets.add(new Integer[] { normX, normY });
			}
			if (normY < 0 && normX >= 0 && normY >= -normR && normX + 1 < normR) {
				offsets.add(new Integer[] { normX + 1, normY });
			}
		}

		//Check horizontal lines
		for (float y = 0; y >= -r; y -= tileSize) {
			float x = getCircleX(r, y);
			int normX = (int) (x / tileSize);
			int normY = (int) Math.round(y / tileSize);

			if (normY < 0 && normX >= 0 && normY >= -normR && normX < normR) {
				offsets.add(new Integer[] { normX, normY });
			}
			if (normY < 0 && normX >= 0 && normY - 1 >= -normR && normX < normR)
				offsets.add(new Integer[] { normX, normY - 1 });
		}
		removeDuplicates(offsets);

		return offsets;
	}

	/**
	 * Returns the bottom/lower Y value that corresponds to {@param x} in a
	 * circle.
	 * 
	 * @param r
	 *            The radius of the circle.
	 * @param x
	 *            X value whose Y value is wanted.
	 * @return Lower/bottom Y value at X.
	 */
	float getCircleY(float r, float x) {
		return (float) -Math.sqrt((r * r) - (x * x));
	}

	/**
	 * Returns the right/higher X value that corresponds to {@param y} in a
	 * circle.
	 * 
	 * @param r
	 *            The radius of the circle.
	 * @param y
	 *            Y value whose X value is wanted.
	 * @return Right/higher X value at Y.
	 */
	float getCircleX(float r, float y) {
		return (float) Math.sqrt((r * r) - (y * y));
	}

	/**
	 * Draws a line of length {@param maxRadius} at angle {@param angle} from
	 * origin, then calculates which tiles the line overlaps and returns their
	 * offset compared to the intial tile the line started at.
	 * 
	 * @param angle
	 *            The angle of the hook/line.
	 * @param maxRadius
	 *            The maximum length of the line.
	 * @param tileSize
	 *            The width/height of every tile.
	 * @return A list containing every overlapped tile's offset.
	 */
	List<Integer[]> calculateHookAttachOffsets(float angle, float maxRadius, int tileSize) {
		List<Integer[]> offsets = new ArrayList<Integer[]>();

		//TODO: similar to calculateJumpGrid, refactor?

		//Doubles used to prevent having to cast all the time. 
		//Faster than float? 

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
		removeDuplicates(offsets);

		return offsets;
	}

	/**
	 * Removes duplicates from {@param offsets}.
	 * 
	 * @param offsets
	 *            List whose duplicate values should be removed.
	 */
	private void removeDuplicates(List<Integer[]> offsets) {
		//Remove duplicates
		for (int i = 0; i < offsets.size(); i++) {
			for (int u = i + 1; u < offsets.size(); u++) {
				if (offsets.get(i)[0] == offsets.get(u)[0] && offsets.get(i)[1] == offsets.get(u)[1]) {
					offsets.remove(u);
					u--;
				}
			}
		}
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
	List<Integer[]> calculateJumpLandingOffsets(float v0y, float a, int tileSize, float vx) {
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
	 * Returns the indexes of every true value in {@param tiles}.
	 * 
	 * @param tiles
	 * @return List of indexes.
	 */
	List<Integer[]> getTrueIndexes(boolean[][] tiles) {
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
	 * Sorts primarily by ascending X (index[0]), a secondarily by Y (index[1]).
	 * Secondary is ascending for the left half of the list and descending for
	 * the right half. This is done because it's used to sort the jump which is
	 * a negative parabola.
	 * 
	 * @param jumpIndexes
	 *            List to be sorted.
	 * @return Sorted list.
	 */
	List<Integer[]> sortJumpIndexes(List<Integer[]> jumpIndexes) {
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
	List<Integer[]> mergeSort(List<Integer[]> list, int index) {
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
	private List<Integer[]> merge(List<Integer[]> left, List<Integer[]> right, int index) {
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
	List<Integer[]> getLandingIndexes(List<Integer[]> jumpIndexes) {
		jumpIndexes = sortJumpIndexes(jumpIndexes);

		List<Integer[]> landingIndexes = new ArrayList<Integer[]>();
		for (int i = jumpIndexes.size() / 2; i < jumpIndexes.size(); i++) {
			//TODO: Currently adds diagonal as landing, check if viable, if not: change to vertical by checking x= x
			if (jumpIndexes.get(i)[1] < jumpIndexes.get(i - 1)[1]) {
				landingIndexes.add(jumpIndexes.get(i));
			}
		}
		return landingIndexes;
	}

	/**
	 * Converts a chunk made out of WorldGenerator.Tile s to a chunk used in the
	 * actual world. This is needed because the generator has special tiles used
	 * for the visualization of the generation, that don't exist in the actual
	 * world.
	 * 
	 * @param chunk
	 * @return
	 */
	private com.grupptva.runnergame.game.model.world.Tile[][] convertChunkToWorldModel(Tile[][] chunk) {

		com.grupptva.runnergame.game.model.world.Tile[][] newChunk = new com.grupptva.runnergame.game.model.world.Tile[chunk[0].length][chunk.length];

		for (int y = 0; y < chunk.length; y++) {
			for (int x = 0; x < chunk[0].length; x++) {
				if (chunk[y][x] == Tile.FULL || chunk[y][x] == Tile.HOOKTARGET) {
					newChunk[x][y] = com.grupptva.runnergame.game.model.world.Tile.OBSTACLE;
				} else {
					newChunk[x][y] = com.grupptva.runnergame.game.model.world.Tile.EMPTY;
				}
			}
		}

		return newChunk;
	}

	/**
	 * Creates the next chunk of the world.
	 * 
	 * @return The created chunk.
	 */
	public Chunk generateChunk() {
		Tile[][] chunk = new Tile[chunkHeight][chunkWidth]; //The generated chunk. Currently filled with null.
		initEmptyChunk(chunk); //Initialize every tile inside of the chunk.

		Integer[] currentTile = { 0, initY }; //Represents current position of the "crawler" that simulates possible character movement through the chunk.

		// Keep crawling forward step by step until the end of the chunk has been reached.
		// Inside this loop is where the magic happens.
		while (currentTile[0] != chunk[0].length - 1) {
			chunk[currentTile[1]][currentTile[0]] = Tile.FULL; //Creates solid ground at the crawlers current position.

			int stepValue = rng.nextInt(jumpStepChance + hookStepChance); //Randomize which kind of movement should be used next.

			if (stepValue < jumpStepChance) //Select jumpStep
				jumpStep(chunk, currentTile);
			else if (stepValue < jumpStepChance + hookStepChance) //Select hookStep
				hookStep(chunk, currentTile);
		}
		chunk[currentTile[1]][currentTile[0]] = Tile.FULL; //Create solid ground at the end of the chunk.
		initY = currentTile[1]; //Save final Y value of the chunk so that it can seamlessly connect to the next one.

		return new Chunk(convertChunkToWorldModel(chunk)); //Convert the chunk into one that is usable by the world, and return it.
	}

	/**
	 * Initializes an empty chunk by filling it with tile.EMPTY, to prevent
	 * NullPointerExceptions.
	 * 
	 * @param chunk
	 *            The chunk is being initialized.
	 */
	private void initEmptyChunk(Tile[][] chunk) {
		for (int y = 0; y < chunk.length; y++) {
			for (int x = 0; x < chunk[0].length; x++) {
				chunk[y][x] = Tile.EMPTY;
			}
		}
	}

	/**
	 * Generates a chunk, but unlike generateChunk this method returns every
	 * single iteration of the chunks generation. Used for visualization
	 * purposes, SHOULD NOT BE CALLED IN THE GAME.
	 * 
	 * @param initY
	 *            The Y value of the leftmost tile, the starting point.
	 * @return A list containing every iteration of the generated chunk.
	 */
	public List<Tile[][]> generateChunkLog(int initY) {
		List<Tile[][]> chunkLog = new ArrayList<Tile[][]>();
		Tile[][] chunk = new Tile[chunkHeight][chunkWidth]; //The generated chunk. Currently empty.
		initEmptyChunk(chunk); //Initialize every tile inside of the chunk to prevent nullpointer exceptions.

		Integer[] currentTile = { 0, initY }; //Represents current position of the "crawler" that simulates possible character movement through the chunk.

		// Keep crawling forward step by step until the end of the chunk has been reached.
		// Inside this loop is where the magic happens.
		while (currentTile[0] != chunk[0].length - 1) {
			chunk[currentTile[1]][currentTile[0]] = Tile.FULL; //Creates solid ground at the crawlers current position.

			chunkLog.add(deepCopyChunk(chunk));
			clearPossibilities(chunk); //Used for visualization only!
			int stepValue = rng.nextInt(jumpStepChance + hookStepChance); //Randomize which kind of movement should be used next.

			if (stepValue < jumpStepChance) //Select jumpStep
				jumpStep(chunk, currentTile, chunkLog);
			else if (stepValue < jumpStepChance + hookStepChance) //Select hookStep
				hookStep(chunk, currentTile, chunkLog);

			chunkLog.add(deepCopyChunk(chunk));
		}

		chunk[currentTile[1]][currentTile[0]] = Tile.FULL; //Create solid ground at the end of the chunk.
		initY = currentTile[1]; //Save final Y value of the chunk so that it can seamlessly connect to the next one.

		chunkLog.add(deepCopyChunk(chunk));
		return chunkLog;
	}

	/**
	 * Called if the next step of generation is a step where the character has
	 * to hook a tile and then jump to another. Detects all possible locations
	 * the character can attach its hook to, from {@param currentTile}. This is
	 * followed by selecting one of them and detecting every location the
	 * character can jump to, while swinging form the select tile, and finally
	 * selecting one of those. Updates {@param chunk} to include all possible
	 * hook locations and landing locations. Sets {@param currentTile} to the
	 * final select tile, the one the character lands on after the hook.
	 * 
	 * @param chunk
	 *            The chunk currently being generated.
	 * @param currentTile
	 */
	private void hookStep(Tile[][] chunk, Integer[] currentTile) {
		List<Integer> validHookAttachIndexes = getValidOffsetIndexes(hookAttachOffsets, currentTile);

		//In order to prevent changes to currentTile in case there is no valid path after the hook attachment point has been set.
		Integer[] currentTileCopy = new Integer[] { currentTile[0], currentTile[1] };

		if (!hookStepPart1(chunk, currentTile, validHookAttachIndexes, currentTileCopy))
			return;

		hookStepPart2(chunk, currentTile, validHookAttachIndexes, currentTileCopy);
		createPlatform(chunk, currentTile);
	}

	/**
	 * A copy of hookStep that is used for visualization, only difference is
	 * that it logs important changes to the chunk in {@param chunkLog}.
	 * 
	 * @see hookStep
	 * @param chunkLog
	 */
	private void hookStep(Tile[][] chunk, Integer[] currentTile, List<Tile[][]> chunkLog) {
		List<Integer> validHookAttachIndexes = getValidOffsetIndexes(hookAttachOffsets, currentTile);

		//In order to prevent changes to currentTile in case there is no valid path after the hook attachment point has been set.
		Integer[] currentTileCopy = new Integer[] { currentTile[0], currentTile[1] };

		if (!hookStepPart1(chunk, currentTile, validHookAttachIndexes, currentTileCopy))
			return;

		chunkLog.add(deepCopyChunk(chunk));

		hookStepPart2(chunk, currentTile, validHookAttachIndexes, currentTileCopy);
		createPlatform(chunk, currentTile);
	}

	/**
	 * The first half of the hookStep method. Split into two for easy
	 * implementation of logging the generation
	 * {@See hookStep(..),hookStep(..,chunkLog)}.
	 * 
	 * @param chunk
	 * @param currentTile
	 * @param validHookAttachIndexes
	 * @param currentTileCopy
	 * @return
	 */
	private boolean hookStepPart1(Tile[][] chunk, Integer[] currentTile, List<Integer> validHookAttachIndexes,
			Integer[] currentTileCopy) {
		if (validHookAttachIndexes.size() == 0) {
			//Failsafe to prevent infinite loop, by setting currentTile[0] to the final point in the chunk the loop that calls this method will break.
			//TODO: Better solution.
			currentTile[0] = chunkWidth - 1;
			return false;
		}
		setValidOffsetsToValue(chunk, hookAttachOffsets, validHookAttachIndexes, currentTileCopy, Tile.POSSIBLEHOOK);
		return true;
	}

	/**
	 * The second half of the hookStep method. Split into two for easy
	 * implementation of logging the generation
	 * {@See hookStep(..),hookStep(..,chunkLog)}.
	 * 
	 * @param chunk
	 * @param currentTile
	 * @param validHookAttachIndexes
	 * @param currentTileCopy
	 */
	private void hookStepPart2(Tile[][] chunk, Integer[] currentTile, List<Integer> validHookAttachIndexes,
			Integer[] currentTileCopy) {
		int randomIndex = rng.nextInt(validHookAttachIndexes.size());
		Integer[] offset = hookAttachOffsets.get(validHookAttachIndexes.get(randomIndex));
		List<Integer[]> landOffsets = hookLandingOffsetList.get(randomIndex);

		currentTileCopy[0] += offset[0];
		currentTileCopy[1] += offset[1];

		List<Integer> validLandIndexes = getValidOffsetIndexes(landOffsets, currentTileCopy);
		if (validLandIndexes.size() == 0) {
			//Failsafe to prevent infinite loop, by setting currentTile[0] to the final point in the chunk the loop that calls this method will break.
			//TODO: Better solution.
			currentTile[0] = chunkWidth - 1;
			return;
		}
		chunk[currentTileCopy[1]][currentTileCopy[0]] = Tile.HOOKTARGET;
		setValidOffsetsToValue(chunk, landOffsets, validLandIndexes, currentTileCopy, Tile.POSSIBLESTAND);

		offset = landOffsets.get(validLandIndexes.get(rng.nextInt(validLandIndexes.size())));

		currentTile[0] = currentTileCopy[0] + offset[0];
		currentTile[1] = currentTileCopy[1] + offset[1];
	}

	/**
	 * Sets all tiles that aren't FULL or HOOKTARGET to EMPTY, used to clear
	 * previous possibilities for the visualization.
	 * 
	 * @param chunk
	 *            The chunk currently being generated.
	 */
	private void clearPossibilities(Tile[][] chunk) {
		for (int y = 0; y < chunk.length; y++) {
			for (int x = 0; x < chunk[0].length; x++) {
				if (chunk[y][x] != Tile.FULL && chunk[y][x] != Tile.HOOKTARGET) {
					chunk[y][x] = Tile.EMPTY;
				}
			}
		}
	}

	/**
	 * Sets currentTileExtraBuffer to a preset value. currentTileExtraBuffer's
	 * value decides the max width of the platforms generated during steps, to
	 * give the player some more leeway.
	 * 
	 * @param size
	 */
	private void setBufferSize(BufferPresets size) {
		switch (size) {
		case SMALL:
			currentTileExtraBuffer = 1;
			break;
		case MEDIUM:
			currentTileExtraBuffer = 3;
			break;
		case HUGE:
			currentTileExtraBuffer = 5;
			break;
		default:
			currentTileExtraBuffer = 0;
			break;
		}
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
	private void jumpStep(Tile[][] chunk, Integer[] currentTile, List<Tile[][]> chunkLog) {
		if (!jumpStepPart1(chunk, currentTile)) //Ends the jumpStep if there are no viable places to jump to.
			return;

		chunkLog.add(deepCopyChunk(chunk));
		chunk[currentTile[1]][currentTile[0]] = Tile.FULL;
		chunkLog.add(deepCopyChunk(chunk));

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
	private void jumpStep(Tile[][] chunk, Integer[] currentTile) {
		if (!jumpStepPart1(chunk, currentTile)) //Ends the jumpStep if there are no viable places to jump to.
			return;

		chunk[currentTile[1]][currentTile[0]] = Tile.FULL;

		createPlatform(chunk, currentTile);
	}

	/**
	 * The first half of the jumpStep method. Split into two for easy
	 * implementation of logging the generation {@See jumpStep(..) &
	 * jumpStep(..,chunkLog)}.
	 * 
	 * @param chunk
	 * @param currentTile
	 */
	private boolean jumpStepPart1(Tile[][] chunk, Integer[] currentTile) {
		List<Integer> validJumpIndexes = getValidOffsetIndexes(jumpOffsets, currentTile);
		if (validJumpIndexes.size() == 0) {
			//Failsafe to prevent infinite loop, by setting currentTile[0] to the final point in the chunk the loop that calls this method will break.
			//TODO: Better solution.
			currentTile[0] = chunkWidth - 1;

			return false;
		}
		setValidOffsetsToValue(chunk, jumpOffsets, validJumpIndexes, currentTile, Tile.POSSIBLESTAND);

		Integer[] offset = jumpOffsets.get(validJumpIndexes.get(rng.nextInt(validJumpIndexes.size())));

		//If to close to the bottom of the chunk: used weighted random selection instead
		//where tiles above the character are weighted higher and thus have an increased chance 
		//being selected, to prevent the generation from becoming stuck at the bottom.
		if (currentTile[1] < (chunk.length * .40)) { //.4 arbitrary height
			int[] indexWeights = new int[validJumpIndexes.size()];
			int totalWeights = 0;
			for (int i = 0; i < validJumpIndexes.size(); i++) {
				indexWeights[i] = 10;
				if (jumpOffsets.get(i)[1] > 0) {
					indexWeights[i] *= jumpOffsets.get(i)[1] * 5; //5 arbitrary weight amount.
				}
				totalWeights += indexWeights[i];
			}
			int r = rng.nextInt(totalWeights);
			int weightSum = 0;
			for (int i = 0; i < indexWeights.length; i++) {
				weightSum += indexWeights[i];
				if (r <= weightSum) {
					offset = jumpOffsets.get(validJumpIndexes.get(i));
					break;
				}
			}
		}

		currentTile[0] += offset[0];
		currentTile[1] += offset[1];
		return true;
	}

	/**
	 * Adds a platform to the chunk, using currentTile as the leftmost tile.
	 * Size depends on currentTileExtraBuffer.
	 * 
	 * @param chunk
	 *            Chunk being generated.
	 * @param currentTile
	 *            Leftmost position of platform. Updates to a tile inside the
	 *            platform.
	 */
	private void createPlatform(Tile[][] chunk, Integer[] currentTile) {
		if (currentTileExtraBuffer > 0) {
			int extraOffset = rng.nextInt(currentTileExtraBuffer) + 1;
			for (int i = 0; i <= extraOffset; i++) {
				if (isValidIndex(currentTile[0] + i, currentTile[1])) {
					chunk[currentTile[1]][currentTile[0] + i] = Tile.FULL;
				}
			}
			if (isValidIndex(currentTile[0] + extraOffset, currentTile[1])) {
				currentTile[0] += extraOffset;
				chunk[currentTile[1]][currentTile[0]] = Tile.POSSIBLEHOOK; //Used for visualization, turns tile purple

				extraOffset = rng.nextInt(2);
				for (int i = 1; i <= extraOffset; i++) {
					if (isValidIndex(currentTile[0] + i, currentTile[1])) {
						chunk[currentTile[1]][currentTile[0] + i] = Tile.FULL;
					}
				}
			}
		}
	}

	/**
	 * Checks if a given index is inside of the bounds of chunkHeight &
	 * chunkWidth.
	 * 
	 * @param x
	 *            the x coordinate of the index (second dimension).
	 * @param y
	 *            the y coordinate of the index (first dimension).
	 * @return True if the given index is inside of the chunks bounds, otherwise
	 *         false.
	 */
	private boolean isValidIndex(int x, int y) {
		return (y < chunkHeight && x < chunkWidth && x >= 0 && y >= 0);
	}

	/**
	 * Takes every valid index from {@param offsets}, listed in
	 * {@param validIndexes}, individually adds them to {@param currentTile} and
	 * then changes the new index, represented by said sum, inside of
	 * {@param chunk} to {@param value}.
	 * 
	 * @param chunk
	 *            The chunk whose values should be changed.
	 * @param offsets
	 *            A list containing offsets in relation to {@param currentTile}.
	 * @param validIndexes
	 *            A list containing every index inside {@param offsets} that is
	 *            valid, and should be used.
	 * @param currentTile
	 *            The position that every offset should be in relation to.
	 * @param value
	 *            The type of tile to set every valid chunk index to.
	 */
	private void setValidOffsetsToValue(Tile[][] chunk, List<Integer[]> offsets, List<Integer> validIndexes,
			Integer[] currentTile, Tile value) {
		for (Integer index : validIndexes) {
			int x = offsets.get(index)[0] + currentTile[0];
			int y = offsets.get(index)[1] + currentTile[1];

			if (chunk[y][x] != Tile.FULL)
				chunk[y][x] = value;
		}
	}

	/**
	 * Returns a list of integers containing every the index of every offset, in
	 * relation to {@param currentTile}, that is inside the bounds of the
	 * WorldGenerator's chunk size.
	 * 
	 * @See chunkWidth, chunkHeight
	 * @param offsets
	 *            A list of offsets whose validity should be checked.
	 * @param currentTile
	 *            The position that the offsets are in relation to.
	 * @return An integer list with the index of every valid offset.
	 */
	private List<Integer> getValidOffsetIndexes(List<Integer[]> offsets, Integer[] currentTile) {
		List<Integer> validIndexes = new ArrayList<Integer>();

		for (int i = 0; i < offsets.size(); i++) {
			int x = offsets.get(i)[0] + currentTile[0];
			int y = offsets.get(i)[1] + currentTile[1];

			if (isValidIndex(x, y)) {
				validIndexes.add(i);
			}
		}
		return validIndexes;
	}

	/**
	 * Creates a copy of {@param chunk} that doesn't reference the initial
	 * chunk's values.
	 * 
	 * @param chunk
	 *            The Tile[][] to copy.
	 * @return The copy.
	 */
	Tile[][] deepCopyChunk(Tile[][] chunk) {
		Tile[][] newChunk = new Tile[chunk.length][chunk[0].length];
		for (int y = 0; y < chunk.length; y++) {
			newChunk[y] = Arrays.copyOf(chunk[y], chunk[y].length);
		}
		return newChunk;
	}
}