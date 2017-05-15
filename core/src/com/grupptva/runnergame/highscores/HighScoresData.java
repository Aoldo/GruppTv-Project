package com.grupptva.runnergame.highscores;

import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.grupptva.runnergame.RunnerGame;

	public class HighScoresData implements Comparator<HighScoresData> {
			
	public BitmapFont font;
	
	final int maxScoreCount = 10;
	
	int highScores[] = new int[10];
	String[] names = new String[10];
	
	Preferences prefs = Gdx.app.getPreferences("Highscore List");
	
	////////////////////////////////////
    String name;
    int score;

    public HighScoresData(String name, int score) {
        this.name = name;
        this.score = score; 
    }
    
    public void updateScore(int score){
        this.score += score;
    }	
	////////////////////////////////////

	public void sortHighScores() {
		for (int i = 0; i < highScores.length; i++) {
			
		}
	}
	
	public void addScore() {
		
	}
	
	public void removeScore() {
		
	}
	
	@Override
	public int compare(HighScoresData o1, HighScoresData o2) {
		if ((o1.score) > (o2.score)) {
			return 1;
		} else if ((o1.score) < (o2.score)) {
			return -1;
		} else {
			return 0;
		}
	}

	//public static void saveHighscore(Preferences prefs, int[] x) {
	//	prefs.putString("name", "Donald Duck");
	//}
		
	// public static Highscores loadHighscore() { }
	
	// int namn 4 bokstäver namn
	
	// Preferences prefs = Gdx.app.getPreferences("My Preferences");

}
