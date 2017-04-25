package com.grupptva.runnergame;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class MenuButton{

    public int x,y,width,height;

    //public MenuButton((Gdx.graphics.getWidth() - width/2), (Gdx.graphics.getHeight() - width/2), int width, int height){'
    public MenuButton(int x, int y, int width, int height){
    	//menuButtons.add(this);
    	this.x = x;
    	this.y = y;
    	this.width = width;
    	this.height = height;
    }

    public void render(ShapeRenderer sr){
        sr.rect(x,y,width, height);
        sr.setColor(0.3f, 0.6f, 1f, 1);
        //.setCenter(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
    }

}