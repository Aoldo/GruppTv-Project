package com.grupptva.runnergame.highscores;

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
	
}
