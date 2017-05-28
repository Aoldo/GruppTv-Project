package com.grupptva.runnergame.utils;

/**
 * Creates an highscore object holding a name and a score.
 * 
 * @author lukamrkonjic
 *
 */

public class HighScore {

	int score;
	String name;
	
	public HighScore(int score, String name) {
		this.score = score;
		this.name = name;
	}

	public int getScore() {
		return this.score;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setScore(int s) {
		this.score = s;
	}
	
}
