package com.grupptva.runnergame;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class MenuButton{

    public int x,y,width,height;

    private static ArrayList<MenuButton> menuButtons = new ArrayList<MenuButton>();

    public MenuButton(int x, int y, int width, int height){
    	menuButtons.add(this);
    }

    public void render(ShapeRenderer sr){
        sr.rect(x,y,x+width, y+height);
    }

    public static void renderAllMenuButtons(ShapeRenderer sr){
    	menuButtons.forEach(item -> {
            item.render(sr);
        });
    }

}