package com.grupptva.runnergame.game.services.worldgenerator;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.grupptva.runnergame.game.model.gamecharacter.GameCharacter;

public class JumpStepTest {

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
		sortList = JumpStep.mergeSort(sortList, 1);
		sortList = JumpStep.mergeSort(sortList, 0);

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
		expectedList.add(new Integer[] { 2, 7 });
		expectedList.add(new Integer[] { 2, 6 });
		expectedList.add(new Integer[] { 3, 5 });
		expectedList.add(new Integer[] { 3, 3 });
		expectedList.add(new Integer[] { 4, 2 });
		expectedList.add(new Integer[] { 4, 1 });

		List<Integer[]> sortList = new ArrayList<Integer[]>();
		sortList.add(new Integer[] { 4, 1 });
		sortList.add(new Integer[] { 0, 2 });
		sortList.add(new Integer[] { 1, 5 });
		sortList.add(new Integer[] { 2, 6 });
		sortList.add(new Integer[] { 1, 3 });
		sortList.add(new Integer[] { 3, 5 });
		sortList.add(new Integer[] { 3, 3 });
		sortList.add(new Integer[] { 4, 2 });
		sortList.add(new Integer[] { 2, 7 });
		sortList.add(new Integer[] { 0, 1 });

		sortList = JumpStep.sortJumpIndexes(sortList);

		for (int i = 0; i < sortList.size(); i++) {
			for (int u = 0; u < sortList.get(i).length; u++) {
				assertTrue(sortList.get(i)[u] == expectedList.get(i)[u]);
			}
		}
	}

	@Test
	public void calculateJumpLandingOffsetsTest() {
		List<Integer[]> landingOffsets = JumpStep.calculateJumpLandingOffsets(4, -2, 1, 1);
		List<Integer[]> expectedOffsets = new ArrayList<Integer[]>();

		expectedOffsets.add(new Integer[] { 4, 2 });
		expectedOffsets.add(new Integer[] { 4, 1 });
		expectedOffsets.add(new Integer[] { 4, 0 });
		expectedOffsets.add(new Integer[] { 5, -1 });
		expectedOffsets.add(new Integer[] { 5, -2 });
		expectedOffsets.add(new Integer[] { 5, -3 });
		expectedOffsets.add(new Integer[] { 5, -4 });

		for (int i = 0; i < landingOffsets.size(); i++) {
			for (int u = 0; u < landingOffsets.get(i).length; u++) {
				assertTrue(landingOffsets.get(i)[u] == expectedOffsets.get(i)[u]);
			}
		}

	}

	@Test
	public void getFramesToApexOfJumpTest() {
		assertTrue(JumpStep.getFramesToApexOfJump(9.82f, -19.64f) == .5f);
	}

	@Test
	public void getRelativeHeightOfApexTest() {
		assertEquals(JumpStep.getRelativeHeightOfApex(860f, -9.8f), 37734.6939f, 0.1);
	}

	@Test
	public void getFramesToYValueTest() {
		assertTrue(JumpStep.getFramesToYValue(10, -10, 0, 0) == JumpStep.getFramesToApexOfJump(10, 10) * 2);
		assertEquals(JumpStep.getFramesToYValue(4, -2, -5, 1), 5.15f, .1f);
	}

	@Test
	public void getJumpYTest() {
		assertTrue(JumpStep.getJumpY(4, -2, 3, 1) == 4f);
	}
	@Test
	public void calculateJumpGridTest() {
		//Don't worry about it.
		boolean[][] result1 = new boolean[][] { { false, false, false, false, true },
				{ false, false, false, false, true }, { false, false, false, false, true },
				{ false, false, false, false, true }, { true, false, false, true, true },
				{ true, false, false, true, false }, { true, true, false, true, false },
				{ true, true, true, true, false } };

		assertArrayEquals(JumpStep.calculateJumpGrid(4, -2, 1, 1), result1);

		boolean[][] result2 = new boolean[][] { { false, false, false, false, false, false, false, true },
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
		assertArrayEquals(JumpStep.calculateJumpGrid(6, -2, 1, 1), result2);
	}
}
