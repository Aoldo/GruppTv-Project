package com.grupptva.runnergame.modulesystem;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.grupptva.runnergame.menu.MenuController;

public class MenuModuleAdapter implements ModuleAdapter {
	MenuController controller;
	
	public MenuModuleAdapter() {
		controller = new MenuController();
	}
	@Override
	public boolean inactive()
	{
		return controller.startGame;
	}

	@Override
	public void update() {
		controller.update();
	}

	@Override
	public void render(SpriteBatch batch, ShapeRenderer sr) {
		controller.render(batch, sr);
	}

}
