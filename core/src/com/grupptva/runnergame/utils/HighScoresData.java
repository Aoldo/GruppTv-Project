package com.grupptva.runnergame.utils;

/**
 * HighScoreData is responsible for handling all highscore data, in other words saving,
 * loading, adding removing highscore data in general.
 */

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class HighScoresData {

	final int maxScoreCount = 10;

	Preferences prefs = Gdx.app.getPreferences("HookITHighScores");

	List<HighScore> highscores = new ArrayList<HighScore>();

	public HighScoresData() {
		loadHighscores();
	}

	public int getHighScore(int index) {
		return highscores.get(index).getScore();
	}

	public String getName(int index) {
		return highscores.get(index).getName();
	}

	List<HighScore> mergeSort(List<HighScore> list) {
		if (list.size() <= 1) {
			return list; //A list with only one element is already sorted.
		}
		List<HighScore> left = new ArrayList<HighScore>();
		List<HighScore> right = new ArrayList<HighScore>();

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

	private List<HighScore> merge(List<HighScore> left, List<HighScore> right) {
		List<HighScore> result = new ArrayList<HighScore>();

		//Loops until either list is empty.
		while (left.size() > 0 && right.size() > 0) {
			//Appends the lowest value, from either list, to the result list.
			if (left.get(0).getScore() >= right.get(0).getScore()) {
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

	public void sortHighScores() {
		highscores = mergeSort(highscores);
	}

	public Object getHighScores() {
		return highscores;
	}

	public void addScore(HighScore h) {
		highscores.add(h);
		sortHighScores();
		if (highscores.size() > maxScoreCount) {
			highscores.remove(highscores.size() - 1);
		}
		saveHighscores();
	}

	
	public void removeScore(HighScore h) {
		highscores.remove(h);
		sortHighScores();
		if (highscores.size() < maxScoreCount) {
			loadHighscores();
		}
	}

	public int getLength() {
		return highscores.size();
	}

	public void saveHighscores() {
		for (int i = 0; i < maxScoreCount; i++) {
			prefs.putInteger("score" + i, getHighScore(i));
			prefs.putString("name" + i, getName(i));
		}
		prefs.flush();
	}

	public void loadHighscores() {
		for (int i = 0; i < maxScoreCount; i++) {
			int s = prefs.getInteger("score" + i, 0);
			String n = prefs.getString("name" + i, "---");

			highscores.add(new HighScore(s, n));
		}
	}
}
