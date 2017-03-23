package com.gruppTva.runnerGame;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;


/**
 * 
 * @author Mattias
 */
public class App extends BasicGame {
	public App(String gamename) {
		super(gamename);
	}

	public static void main(String[] args) {
		try {
			AppGameContainer appgc;
			appgc = new AppGameContainer(new App("TestGame v0.0"));
			appgc.setDisplayMode(1000, 1000, false);
			//appgc.setShowFPS(false);
			appgc.start();
		} catch (SlickException ex) {
			Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		gc.setAlwaysRender(true);
		gc.setTargetFrameRate(144);
	}

	/*
	 * Main loop, calls update, then render, then repeats.
	 */
	@Override
	public void update(GameContainer gc, int dt) throws SlickException {
		//TODO: Implement timestep stuff
			//TODO: Implement game logic
	}
	public void render(GameContainer gc, Graphics g) throws SlickException {
		//TODO: Implement rendering
	}
	
	
	//These methods are called when input events happen
	
	
	//Controller input
	@Override
	public void controllerButtonPressed(int controller, int button) {
		//System.out.println("+ " + controller + "   " + button);
	}
	@Override
	public void controllerButtonReleased(int controller, int button) {
		//System.out.println("- " + controller + "   " + button);
	}
	
	//Keyboard input
	@Override
	public void keyPressed(int key, char code) {
		//System.out.println("+ " + key + "   " + '\'' + code + '\'');
	}

	@Override
	public void keyReleased(int key, char code) {
		//System.out.println("- " + key + "   " + '\'' + code + '\'');
	}
}
