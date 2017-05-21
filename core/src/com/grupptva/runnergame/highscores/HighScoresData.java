package com.grupptva.runnergame.highscores;

import java.util.ArrayList;
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
		
	//	int highScores[] = new int[maxScoreCount];
	//	String[] names = new String[maxScoreCount];
		
		Preferences prefs = Gdx.app.getPreferences("HookITHighScores");

		//HighScore[] highscores = new HighScore[maxScoreCount];
		List<HighScore> highscores = new ArrayList<HighScore>();
				
//		public void initScores() {
//			for (int i = 0; i < maxScoreCount; i++) {
//				highscores.add(new HighScore());
//			}
//		}
		
		// Initiates the highscore menu with placeholders
//	    public void initHighScore() {
//			for (int i = 0; i < maxScoreCount; i++) {
//				//this.highscores[i].score = (int) (Math.random() * (50 - 0));
//				this.highscores.get(i).score = 0;
//				this.highscores.get(i).name = "---";
//			}  
//	    }
		
	    public HighScoresData() {
	    	//initScores();
	    	//initHighScore();
	    	loadHighscores();
	    }
	    
	    public int getHighScore(int index) {
	    	return highscores.get(index).getScore();
	    }
	    
	    public String getName(int index) {
	    	return highscores.get(index).getName();
	    }
	    	    
	//	public void sortHighScores() {
	//		for (int i = 0; i < highScores.length; i++) {
	//			
	//		}
	//	}
	    
	    public Object getHighScores() {
			return highscores;
	    }
		
		public void addScore(HighScore h) {
			highscores.add(h);
			//sortHighScores();
			if (highscores.size() > 10) {
				highscores.remove(highscores.size()-1);
			}
			saveHighscores();
		}
		
		public void removeScore(HighScore h) {
			highscores.remove(h);
			//sortHighscore();
			if (highscores.size() < 10) {
				loadHighscores();
			}
		}
		
		public int getLength() {
			return highscores.size();
		}

		public void saveHighscores() {
			for (int i = 0; i < maxScoreCount; i++) {
				prefs.putInteger("score"+i, getHighScore(i));
				prefs.putString("name"+i, getName(i));
			}
			prefs.flush();
		}
					
		public void loadHighscores() { 
			for (int i = 0; i < maxScoreCount; i++) {
				int s = prefs.getInteger("score"+i, 0);
				String n = prefs.getString("name"+i, "---");
				
				highscores.add(new HighScore(s, n));
			}
		}
		// int namn 4 bokstäver namn
}
