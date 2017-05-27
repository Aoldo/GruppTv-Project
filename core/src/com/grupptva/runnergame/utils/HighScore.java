package com.grupptva.runnergame.utils;

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
