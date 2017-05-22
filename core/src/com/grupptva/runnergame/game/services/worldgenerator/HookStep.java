package com.grupptva.runnergame.game.services.worldgenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.grupptva.runnergame.game.model.gamecharacter.GameCharacter;
import com.grupptva.runnergame.game.services.worldgenerator.GeneratorChunk.Tile;

class HookStep extends GeneratorStep {
	List<Integer[]> hookAttachOffsets;
	List<List<Integer[]>> hookLandingOffsetList = new ArrayList<List<Integer[]>>();

	public HookStep(float vx, int tileSize, GameCharacter character,
			Random rng, int chance) {
		float v0y = character.getJumpInitialVelocity();
		float a = character.getGravity();
		float angle = 1f; //TODO: Replace with character get methods when implemented.
		float radius = 75f;

		this.rng = rng;
		this.chance = chance;

		initHookOffsets(v0y, a, vx, tileSize, angle, radius, character);
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
				List<Integer[]> swingLandingOffsets = JumpStep.calculateJumpLandingOffsets(yVel, a, tileSize, vx);

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

			if (normY < 0 && normX >= 1 && normY >= -normR && normX < normR) {
				offsets.add(new Integer[] { normX, normY });
			}
			if (normY < 0 && normX >= 1 && normY >= -normR && normX + 1 < normR) {
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
	@Override
	public void step(GeneratorChunk chunk, Integer[] currentTile) {
		List<Integer> validHookAttachIndexes = chunk.getValidOffsetIndexes(hookAttachOffsets, currentTile);

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
	@Override
	public void step(GeneratorChunk chunk, Integer[] currentTile, List<Tile[][]> chunkLog) {
		List<Integer> validHookAttachIndexes = chunk.getValidOffsetIndexes(hookAttachOffsets, currentTile);

		//In order to prevent changes to currentTile in case there is no valid path after the hook attachment point has been set.
		Integer[] currentTileCopy = new Integer[] { currentTile[0], currentTile[1] };

		if (!hookStepPart1(chunk, currentTile, validHookAttachIndexes, currentTileCopy))
			return;

		chunkLog.add(chunk.deepCopyTiles());

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
	private boolean hookStepPart1(GeneratorChunk chunk, Integer[] currentTile, List<Integer> validHookAttachIndexes,
			Integer[] currentTileCopy) {
		if (validHookAttachIndexes.size() == 0) {
			//Failsafe to prevent infinite loop. This code is pretty much identical to failsafes in other places
			//but turning it into a boolean method doesn't remove the if statement since it would return true
			//if it passes the check, and that would require a new if statement in order to prevent it from ending this method early.
			createPlatform(chunk, currentTile);	
			return false;
		}
		chunk.setValidOffsetsToValue(hookAttachOffsets, validHookAttachIndexes, currentTileCopy, Tile.POSSIBLEHOOK);
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
	private void hookStepPart2(GeneratorChunk chunk, Integer[] currentTile, List<Integer> validHookAttachIndexes,
			Integer[] currentTileCopy) {
		int randomIndex = rng.nextInt(validHookAttachIndexes.size());
		Integer[] offset = hookAttachOffsets.get(validHookAttachIndexes.get(randomIndex));
		List<Integer[]> landOffsets = hookLandingOffsetList.get(randomIndex);

		currentTileCopy[0] += offset[0];
		currentTileCopy[1] += offset[1];

		List<Integer> validLandIndexes = chunk.getValidOffsetIndexes(landOffsets, currentTileCopy);
		if (validLandIndexes.size() == 0) {
			//Failsafe to prevent infinite loop. This code is pretty much identical to failsafes in other places
			//but turning it into a boolean method doesn't remove the if statement since it would return true
			//if it passes the check, and that would require a new if statement in order to prevent it from ending this method early.
			createPlatform(chunk, currentTile);			
			return;
		}
		chunk.tiles[currentTileCopy[1]][currentTileCopy[0]] = Tile.HOOKTARGET;
		chunk.setValidOffsetsToValue(landOffsets, validLandIndexes, currentTileCopy, Tile.POSSIBLESTAND);

		offset = landOffsets.get(validLandIndexes.get(rng.nextInt(validLandIndexes.size())));

		currentTile[0] = currentTileCopy[0] + offset[0];
		currentTile[1] = currentTileCopy[1] + offset[1];
	}
}
