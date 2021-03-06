package com.grupptva.runnergame.game.model.worldgenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.grupptva.runnergame.game.model.gamecharacter.GameCharacter;
import com.grupptva.runnergame.game.model.worldgenerator.GeneratorChunk.Tile;

/**
 * Implementation of GeneratorStep that adds a step where the character has to
 * use the hook on a generated tile in order to swing over to the next platform
 * in the chunk.
 * 
 * This class depends on JumpStep's methods that calculate where the character
 * can land after jumping.
 * 
 * @see JumpStep
 * 
 * @author Mattias
 * 
 */
class HookStep extends GeneratorStep {
	List<Integer[]> hookAttachOffsets;
	List<List<Integer[]>> hookLandingOffsetList = new ArrayList<List<Integer[]>>();

	public HookStep(final float vx, final int tileSize, final GameCharacter character, final Random rng,
			final int chance) {
		final float v0y = character.getJumpInitialVelocity();
		final float a = character.getGravity();

		final float angle = 1f; //TODO: Replace with character get methods when implemented.
		final float radius = 75f;

		this.rng = rng;
		this.chance = chance;

		initHookOffsets(v0y, a, vx, tileSize, angle, radius, character);
	}

	/**
	 * Calculates which tiles the character can land on by swinging from
	 * {@param attachOffset}.
	 * 
	 * @param attachOffset
	 *            The offset of the tile the character is attached to with the
	 *            hook, in relation to the tile the character is on.
	 * @param tileSize
	 *            The width and height of a tile.
	 * @param a
	 *            The constant acceleration of the character.
	 * @param v0y
	 *            The initial Y velocity of the characters jump.
	 * 
	 * @param vx
	 *            The constant X velocity of the character.
	 * @return A list containing every single offset, in relation to the
	 *         attachOffset, that the character can land on.
	 */
	List<Integer[]> calculateHookLandingOffsets(final Integer[] attachOffset, final int tileSize, final float a,
			final float v0y, final float vx, final GameCharacter character) {
		List<Integer[]> landingOffsets = new ArrayList<Integer[]>();

		final float r = (float) Math
				.sqrt(tileSize * tileSize * (attachOffset[0] * attachOffset[0] + attachOffset[1] * attachOffset[1])); //Radius of rope/distance to attachOffset

		final List<Integer[]> swingOffsets = calculateHookSwingOffsets(r, tileSize); //Tiles character collide with while swinging.

		//For every tile the character touches while swinging, check which tiles the character 
		//can land on by releasing the hook while colliding with that tile.
		for (Integer[] swingOffset : swingOffsets) {
			final float yVel = character.getReleaseVelocity(swingOffset[0], swingOffset[1], 0, 0);

			if (yVel > 0) { //Prevent terrible things from happening.
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
	 * Initializes the offsets used in step.
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
	private void initHookOffsets(final float v0y, final float a, final float vx, final int tileSize, final float angle,
			final float maxRadius, final GameCharacter character) {
		hookAttachOffsets = calculateHookAttachOffsets(angle, maxRadius, tileSize);
		for (Integer[] attachOffset : hookAttachOffsets) {
			hookLandingOffsetList.add(calculateHookLandingOffsets(attachOffset, tileSize, a, v0y, vx, character));
		}
	}

	/**
	 * Creates a circle with radius {@param r} around origin and checks which
	 * tiles in the 4th quadran(bottom right) the circle intersects with.
	 * 
	 * @param r
	 *            The radius of the circle.
	 * @return A list containing every overlapped tile's offset.
	 */
	List<Integer[]> calculateHookSwingOffsets(float r, int tileSize) {
		//Circle equation: r^2 = x^2+y^2
		List<Integer[]> offsets = new ArrayList<Integer[]>();

		final int normR = (int) (r / tileSize);
		//Check vertical lines
		for (float x = 0; x < r; x += tileSize) {
			final float y = getCircleY(r, x);
			final int normX = (int) (x / tileSize);
			final int normY = (int) Math.round(y / tileSize);

			if (normY < 0 && normX >= 1 && normY >= -normR && normX < normR) {
				offsets.add(new Integer[] { normX, normY });
			}
			if (normY < 0 && normX >= 1 && normY >= -normR && normX + 1 < normR) {
				offsets.add(new Integer[] { normX + 1, normY });
			}
		}

		//Check horizontal lines
		for (float y = 0; y >= -r; y -= tileSize) {
			final float x = getCircleX(r, y);
			final int normX = (int) (x / tileSize);
			final int normY = (int) Math.round(y / tileSize);

			if (normY < 0 && normX >= 0 && normY >= -normR && normX < normR) {
				offsets.add(new Integer[] { normX, normY });
			}
			if (normY < 0 && normX >= 0 && normY - 1 >= -normR && normX < normR) {
				offsets.add(new Integer[] { normX, normY - 1 });
			}
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
	float getCircleY(final float r, final float x) {
		return (float) -Math.sqrt(r * r - x * x);
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
	float getCircleX(final float r, final float y) {
		return (float) Math.sqrt(r * r - y * y);
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
	List<Integer[]> calculateHookAttachOffsets(final float angle, final float maxRadius, final int tileSize) {
		List<Integer[]> offsets = new ArrayList<Integer[]>();

		//TODO: similar to calculateJumpGrid in JumpGrid, refactor?

		//Doubles used to prevent having to cast all the time. 
		//Faster than float? 

		//Check vertical lines
		final double maxX = maxRadius * Math.cos(angle);
		for (double x = 0; x < maxX; x += tileSize) {
			final double r = (float) (x / Math.cos(angle));
			final double y = r * Math.sin(angle);

			//Normalize the values to get an index/offset instead.
			final int normX = (int) (x / tileSize);
			final int normY = (int) (y / tileSize);
			//Add offsets (x,y) and (x-1,y)
			if (normX > 1 && normY > 1) {
				offsets.add(new Integer[] { normX, normY });
				offsets.add(new Integer[] { normX - 1, normY });
			}
		}
		//Check horizontal lines
		final float maxY = (float) (maxRadius * Math.sin(angle));
		for (double y = 0; y < maxY; y += tileSize) {
			final double r = (float) (y / Math.sin(angle));
			final double x = r * Math.cos(angle);

			//Normalize the values to get an index/offset instead.
			final int normX = (int) (x / tileSize);
			final int normY = (int) (y / tileSize);
			//Add offsets (x,y) and (x,y-1)
			if (normX > 1 && normY > 1) {
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
	 * implementation of logging the generation.
	 * 
	 * @param chunk
	 * @param currentTile
	 * @param validHookAttachIndexes
	 * @param currentTileCopy
	 * @return
	 */
	private boolean hookStepPart1(GeneratorChunk chunk, Integer[] currentTile, List<Integer> validHookAttachIndexes,
			Integer[] currentTileCopy) {
		if (validHookAttachIndexes.isEmpty()) {
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
		final int randomIndex = rng.nextInt(validHookAttachIndexes.size());
		Integer[] offset = hookAttachOffsets.get(validHookAttachIndexes.get(randomIndex));
		List<Integer[]> landOffsets = hookLandingOffsetList.get(randomIndex);

		currentTileCopy[0] += offset[0];
		currentTileCopy[1] += offset[1];

		List<Integer> validLandIndexes = chunk.getValidOffsetIndexes(landOffsets, currentTileCopy);
		if (validLandIndexes.isEmpty()) {
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
