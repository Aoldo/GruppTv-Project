package com.grupptva.runnergame.highscores;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.grupptva.runnergame.RunnerGame;
import com.grupptva.runnergame.menu.HighScoreMenu;

//public class HighScoresData implements Comparable<HighScoresData> {
public class HighScoresData {

	final int maxScoreCount = 10;

	// int highScores[] = new int[maxScoreCount];
	// String[] names = new String[maxScoreCount];

	Preferences prefs = Gdx.app.getPreferences("HookITHighScores");

	// HighScore[] highscores = new HighScore[maxScoreCount];
	List<HighScore> highscores = new ArrayList<HighScore>();

	public HighScoresData() {
		// initScores();
		// initHighScore();
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
            if (left.get(0).getScore() <= right.get(0).getScore()) {
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
		printScores();
	}
	
	public void printScores() {
      for(HighScore d : highscores) {
          System.out.println(d.score + "  "+ d.name);
      }		
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
