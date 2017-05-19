package com.grupptva.runnergame.game.services.worldgenerator;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.grupptva.runnergame.game.model.gamecharacter.GameCharacter;

public class HookStepTest {
	//A lot of the tests here depend on the JumpStep class, so make sure those pass before trubleshooting these.

	HookStep hs;

	@Before
	public void init() {
		hs = new HookStep(1.5f, 25, new GameCharacter(30f, 150f, 3f), new Random(), 50);
	}

	@Test
	public void calculateHookSwingOffsets() {
		float r = 4;
		int tileSize = 1;

		List<Integer[]> expectedList = new ArrayList<Integer[]>();
		expectedList.add(new Integer[] { 0, -4 });
		expectedList.add(new Integer[] { 1, -4 });
		expectedList.add(new Integer[] { 2, -4 });
		expectedList.add(new Integer[] { 2, -3 });
		expectedList.add(new Integer[] { 3, -3 });
		expectedList.add(new Integer[] { 3, -2 });
		expectedList.add(new Integer[] { 3, -1 });

		List<Integer[]> offsets = hs.calculateHookSwingOffsets(r, tileSize);
		offsets = JumpStep.mergeSort(offsets, 1);
		offsets = JumpStep.mergeSort(offsets, 0);

		for (int i = 0; i < offsets.size(); i++) {
			for (int u = 0; u < offsets.get(i).length; u++) {
				assertTrue(offsets.get(i)[u] == expectedList.get(i)[u]);
			}
		}
	}

	@Test
	public void getCircleXTest() {
		assertTrue(hs.getCircleX(5, -4) == 3);
	}

	@Test
	public void getCircleYTest() {
		assertTrue(hs.getCircleY(5, 3) == -4);
	}

	@Test
	public void calculateHookAttachOffsetsTest() {
		float angle = 0.820305f;
		float radius = 100f;
		int tileSize = 20;

		List<Integer[]> expectedList = new ArrayList<Integer[]>();
		expectedList.add(new Integer[] { 1, 2 });
		expectedList.add(new Integer[] { 2, 2 });
		expectedList.add(new Integer[] { 2, 3 });
		expectedList.add(new Integer[] { 3, 3 });

		List<Integer[]> offsets = hs.calculateHookAttachOffsets(angle, radius, tileSize);
		//Sort list to make it easier to compare to expected.
		offsets = JumpStep.mergeSort(offsets, 1);
		offsets = JumpStep.mergeSort(offsets, 0);

		//		for (Integer[] offset : offsets) {
		//			System.out.println(Arrays.toString(offset));
		//		}
		for (int i = 0; i < offsets.size(); i++) {
			for (int u = 0; u < offsets.get(i).length; u++) {
				assertTrue(offsets.get(i)[u] == expectedList.get(i)[u]);
			}
		}
	}

}
