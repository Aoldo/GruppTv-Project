package com.grupptva.runnergame.game.services;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.*;

import com.grupptva.runnergame.game.services.WorldGenerator;
import com.grupptva.runnergame.game.services.WorldGenerator.Tile;

/**
 * 
 * @author Mattias
 *
 */
public class WorldGeneratorTest {
	WorldGenerator wg;

	@Before
	public void init() {
		wg = new WorldGenerator(7, -.4f, 1.5f, 25, 2l, 40, 20, 0);
	}

	@Test
	public void getFramesToApexOfJumpTest() {
		assertTrue(wg.getFramesToApexOfJump(9.82f, -19.64f) == .5f);
	}

	@Test
	public void getRelativeHeightOfApexTest() {
		assertEquals(wg.getRelativeHeightOfApex(860f, -9.8f), 37734.6939f, 0.1);
	}

	@Test
	public void getFramesToYValueTest() {
		assertTrue(wg.getFramesToYValue(10, -10, 0, 0) == wg.getFramesToApexOfJump(10, 10)
				* 2);
		assertEquals(wg.getFramesToYValue(4, -2, -5, 1), 5.15f, .1f);
	}

	@Test
	public void getJumpY() {
		assertTrue(wg.getJumpY(4, -2, 3, 1) == 4f);
	}

	@Test
	public void mergeSortTest() {
		List<Integer[]> expectedList = new ArrayList<Integer[]>();
		expectedList.add(new Integer[] { 0, 1 });
		expectedList.add(new Integer[] { 0, 2 });
		expectedList.add(new Integer[] { 1, 3 });
		expectedList.add(new Integer[] { 1, 5 });
		expectedList.add(new Integer[] { 2, 1 });
		expectedList.add(new Integer[] { 2, 2 });
		expectedList.add(new Integer[] { 3, 3 });
		expectedList.add(new Integer[] { 3, 5 });
		expectedList.add(new Integer[] { 4, 1 });
		expectedList.add(new Integer[] { 4, 2 });

		List<Integer[]> sortList = new ArrayList<Integer[]>();
		sortList.add(new Integer[] { 4, 1 });
		sortList.add(new Integer[] { 0, 2 });
		sortList.add(new Integer[] { 1, 5 });
		sortList.add(new Integer[] { 2, 1 });
		sortList.add(new Integer[] { 1, 3 });
		sortList.add(new Integer[] { 3, 5 });
		sortList.add(new Integer[] { 3, 3 });
		sortList.add(new Integer[] { 4, 2 });
		sortList.add(new Integer[] { 2, 2 });
		sortList.add(new Integer[] { 0, 1 });

		//Sort by Y first and then X to get it sorted primarily by X and secondarily by Y
		//Works because of stable sorting method.
		sortList = wg.mergeSort(sortList, 1);
		sortList = wg.mergeSort(sortList, 0);

		for (int i = 0; i < sortList.size(); i++) {
			for (int u = 0; u < sortList.get(i).length; u++) {
				assertTrue(sortList.get(i)[u] == expectedList.get(i)[u]);
			}
		}
	}

	@Test
	public void sortJumpIndexesTest() {
		List<Integer[]> expectedList = new ArrayList<Integer[]>();
		expectedList.add(new Integer[] { 0, 1 });
		expectedList.add(new Integer[] { 0, 2 });
		expectedList.add(new Integer[] { 1, 3 });
		expectedList.add(new Integer[] { 1, 5 });
		expectedList.add(new Integer[] { 2, 2 });
		expectedList.add(new Integer[] { 2, 1 });
		expectedList.add(new Integer[] { 3, 5 });
		expectedList.add(new Integer[] { 3, 3 });
		expectedList.add(new Integer[] { 4, 2 });
		expectedList.add(new Integer[] { 4, 1 });

		List<Integer[]> sortList = new ArrayList<Integer[]>();
		sortList.add(new Integer[] { 4, 1 });
		sortList.add(new Integer[] { 0, 2 });
		sortList.add(new Integer[] { 1, 5 });
		sortList.add(new Integer[] { 2, 1 });
		sortList.add(new Integer[] { 1, 3 });
		sortList.add(new Integer[] { 3, 5 });
		sortList.add(new Integer[] { 3, 3 });
		sortList.add(new Integer[] { 4, 2 });
		sortList.add(new Integer[] { 2, 2 });
		sortList.add(new Integer[] { 0, 1 });

		sortList = wg.sortJumpIndexes(sortList);

		for (int i = 0; i < sortList.size(); i++) {
			for (int u = 0; u < sortList.get(i).length; u++) {
				assertTrue(sortList.get(i)[u] == expectedList.get(i)[u]);
			}
		}
	}

	@Test
	public void calculateJumpLandingOffsetsTest() {
		List<Integer[]> landingOffsets = wg.calculateJumpLandingOffsets(4, -2, 1, 1);
		List<Integer[]> expectedOffsets = new ArrayList<Integer[]>();

		expectedOffsets.add(new Integer[] { 3, 2 });
		expectedOffsets.add(new Integer[] { 3, 1 });
		expectedOffsets.add(new Integer[] { 3, 0 });
		expectedOffsets.add(new Integer[] { 4, -1 });
		expectedOffsets.add(new Integer[] { 4, -2 });
		expectedOffsets.add(new Integer[] { 4, -3 });
		expectedOffsets.add(new Integer[] { 4, -4 });

		for (int i = 0; i < landingOffsets.size(); i++) {
			for (int u = 0; u < landingOffsets.get(i).length; u++) {
				assertTrue(landingOffsets.get(i)[u] == expectedOffsets.get(i)[u]);
			}
		}

	}

	@Test
	public void calculateJumpGridTest() {
		//Don't worry about it.
		boolean[][] result1 = new boolean[][] { { false, false, false, false, true },
				{ false, false, false, false, true },
				{ false, false, false, false, true },
				{ false, false, false, false, true }, { true, false, false, true, true },
				{ true, false, false, true, false }, { true, true, false, true, false },
				{ true, true, true, true, false } };

		assertArrayEquals(wg.calculateJumpGrid(4, -2, 1, 1), result1);

		boolean[][] result2 = new boolean[][] {
				{ false, false, false, false, false, false, false, true },
				{ false, false, false, false, false, false, false, true },
				{ false, false, false, false, false, false, true, true },
				{ false, false, false, false, false, false, true, false },
				{ false, false, false, false, false, false, true, false },
				{ false, false, false, false, false, false, true, false },
				{ false, false, false, false, false, false, true, false },
				{ false, false, false, false, false, false, true, false },
				{ false, false, false, false, false, false, true, false },
				{ true, false, false, false, false, true, true, false },
				{ true, false, false, false, false, true, false, false },
				{ true, false, false, false, false, true, false, false },
				{ true, false, false, false, false, true, false, false },
				{ true, true, false, false, false, true, false, false },
				{ true, true, false, false, true, true, false, false },
				{ false, true, false, false, true, false, false, false },
				{ false, true, true, false, true, false, false, false },
				{ false, true, true, true, true, false, false, false } };
		assertArrayEquals(wg.calculateJumpGrid(6, -2, 1, 1), result2);
	}

	@Test
	public void deepCopyChunk_ShouldNotBeReference() {
		Tile[][] chunk = new Tile[][] { { Tile.EMPTY, Tile.EMPTY },
				{ Tile.EMPTY, Tile.EMPTY } };

		Tile[][] chunkCopy = wg.deepCopyChunk(chunk);
		chunk[0][0] = Tile.FULL;

		assertFalse(chunk[0][0] == chunkCopy[0][0]);
	}

	@Test
	public void testFinalTileOnMultipleSeeds() {
		boolean noFail = true;
		for (int i = 0; i < 50; i++) {
			if (!generateChunk_ShouldHaveTileAtRightmostColumn()) {
				noFail = false;
				break;
			}
		}
		assertTrue(noFail);
	}

	public boolean generateChunk_ShouldHaveTileAtRightmostColumn() {
		//TODO: Use generate chunk instead of chunklog, once implemented.
		List<Tile[][]> chunkLog = wg.generateChunkLog(10);
		Tile[][] chunk = chunkLog.get(chunkLog.size() - 1);

		int x = chunk[0].length - 1;
		for (int y = 0; y < chunk.length; y++) {
			if (chunk[y][x] == Tile.FULL) {
				return true;
			}
		}
		return false;
	}
}
