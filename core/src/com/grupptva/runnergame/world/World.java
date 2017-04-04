package com.grupptva.runnergame.world;

import com.grupptva.runnergame.character.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class World {
	public List<Rectangle> obstacles = new ArrayList<Rectangle>();

	public World() {
		obstacles.add(new Rectangle(0, 0, 75, 25));
		obstacles.add(new Rectangle(150, 0, 75, 25));
		obstacles.add(new Rectangle(300, 0, 75, 25));
		obstacles.add(new Rectangle(450, 0, 75, 25));
	}
}