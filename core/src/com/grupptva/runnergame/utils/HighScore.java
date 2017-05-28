package com.grupptva.runnergame.utils;

/**
 * Responsibility: Represent a high score with score and score holder.
 *
 * Used by:
 * @see HighScoresData
 * @see com.grupptva.runnergame.game.controller.GameController
 *
 *
 * @author Luka
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
