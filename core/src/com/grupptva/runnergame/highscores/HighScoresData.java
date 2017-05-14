package com.grupptva.runnergame.highscores;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.grupptva.runnergame.RunnerGame;

public class HighScoresData {
	
	int playerScore;
	
	String[] names;
	
	public BitmapFont font;
	
	final int maxScoreCount = 10;
	
	Preferences prefs = Gdx.app.getPreferences("Highscore List");
	
	public void sortHighScores() {
		
	}
	
	//public HighScoresData(int score, String names) {
	//	this.score = score;
	//	this.names = names;
	//}

	//public static void saveHighscore(Preferences prefs, int[] x) {
	//	prefs.putString("name", "Donald Duck");
	//}
		
	// public static Highscores loadHighscore() { }
	
	// int namn 4 bokstäver namn
	
	// Preferences prefs = Gdx.app.getPreferences("My Preferences");

}
