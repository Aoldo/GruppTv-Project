package com.grupptva.runnergame.highscores;

import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.grupptva.runnergame.RunnerGame;
import com.grupptva.runnergame.menu.HighScores;

	//public class HighScoresData implements Comparable<HighScoresData> {
	public class HighScoresData {
					
		final int maxScoreCount = 10;
		
	//	int highScores[] = new int[maxScoreCount];
	//	String[] names = new String[maxScoreCount];
		
		Preferences prefs = Gdx.app.getPreferences("HookITHighScores");

		HighScore[] highscores = new HighScore[maxScoreCount];
				
		public void initScores() {
			for (int i = 0; i < highscores.length; i++) {
				highscores[i] = new HighScore();
			}
		}
		
		// Initiates the highscore menu with placeholders
	    public void initHighScore() {
			for (int i = 0; i < highscores.length; i++) {
				//this.highscores[i].score = (int) (Math.random() * (50 - 0));
				this.highscores[i].score = 0;
				this.highscores[i].name = "---";
			}  
	    }
		
	    public HighScoresData() {
	    	initScores();
	    	initHighScore();
	    }
	    
	    public int getHighScore(int index) {
	    	return highscores[index].getScore();
	    }
	    
	    public String getName(int index) {
	    	return highscores[index].getName();
	    }
	    	    
	//	public void sortHighScores() {
	//		for (int i = 0; i < highScores.length; i++) {
	//			
	//		}
	//	}
	    
	    public Object getHighScores() {
			return highscores;
	    }
		
		public void addScore() {
			for (int i = 0; i < (getLength()); i++) {
				prefs.putInteger("highscore", getHighScore(i));
			}
			//sortHighScores();
		}
		
		public void removeScore() {
			
		}
		
		public int getLength() {
			return highscores.length;
		}
		
	//	@Override
	//	public int compare(HighScoresData o1, HighScoresData o2) {
	//		if ((o1.score) > (o2.score)) {
	//			return 1;
	//		} else if ((o1.score) < (o2.score)) {
	//			return -1;
	//		} else {
	//			return 0;
	//		}
	//	}
	
		public static void saveHighscore(Preferences prefs) {
		//prefs.putInteger(key, val);	
		//prefs.putString("name", h.getName());
		}
					
		// public static Highscores loadHighscore() { }
		
		// int namn 4 bokstäver namn
		
		// Preferences prefs = Gdx.app.getPreferences("My Preferences");

}
