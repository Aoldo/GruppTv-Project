package com.grupptva.runnergame.controller;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class InputHandler {
	int jumpKey;
	boolean jumpDownPreviousFrame = false;

	int hookKey;
	boolean hookDownPreviousFrame = false;

	List<InputListener> listeners = new ArrayList<InputListener>();

	public InputHandler() {
		jumpKey = Keys.S;
		hookKey = Keys.D;
	}

	public void addListener(InputListener listener) {
		listeners.add(listener);
		System.out.println("Listeners size: " + listeners.size());
	}

	public void removeListener(InputListener listener) {
		listeners.remove(listener);
		System.out.println("Listeners size: " + listeners.size());
	}

	public void update() {
		if (Gdx.input.isKeyPressed(jumpKey)) {
			if (!jumpDownPreviousFrame)
				jumpPressed();
			jumpDownPreviousFrame = true;
		} else {
			if (jumpDownPreviousFrame)
				jumpReleased();
			jumpDownPreviousFrame = false;
		}

		if (Gdx.input.isKeyPressed(hookKey)) {
			if (!hookDownPreviousFrame)
				hookPressed();
			hookDownPreviousFrame = true;
		} else {
			if (hookDownPreviousFrame)
				hookReleased();
			hookDownPreviousFrame = false;
		}
	}

	private void hookPressed() {
		for (InputListener listener : listeners) {
			listener.hookPressed();
		}
	}

	private void hookReleased() {
		for (InputListener listener : listeners) {
			listener.hookReleased();
		}
	}

	private void jumpPressed() {
		for (InputListener listener : listeners) {
			listener.jumpPressed();
		}
	}

	private void jumpReleased() {
		for (InputListener listener : listeners) {
			listener.jumpReleased();
		}
	}
}