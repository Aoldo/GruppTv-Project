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

	public void sortHighScores() {

	}

	public Object getHighScores() {
		return highscores;
	}

	public void addScore(HighScore h) {
		highscores.add(h);
		// sortHighScores();
		if (highscores.size() > maxScoreCount) {
			highscores.remove(highscores.size() - 1);
		}
		saveHighscores();	
	}
	
	public void printScores() {
      for(HighScore d : highscores) {
          System.out.println(d.score + "  "+ d.name);
      }		
	}

	public void removeScore(HighScore h) {
		highscores.remove(h);
		// sortHighscore();
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
	// int namn 4 bokstäver namn
}
