package com.grupptva.runnergame;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class MainMenu {

    private static ArrayList<MenuButton> menuButtons = new ArrayList<MenuButton>();
	
	public MainMenu() {
		menuButtons.add(new MenuButton(0, 0, 0, 0));
	}
	
    public static void renderAllMenuButtons(ShapeRenderer sr){
    	menuButtons.forEach(item -> {
            item.render(sr);
        });
    }
    
	public void render (ShapeRenderer sr) {
		
	}

}
