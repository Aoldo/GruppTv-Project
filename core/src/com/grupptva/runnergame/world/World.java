package com.grupptva.runnergame.world;

import com.grupptva.runnergame.character.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class World {
	public List<Rectangle> obstacles = new ArrayList<Rectangle>();

	public World() {
		obstacles.add(new Rectangle(0, 0, 250, 25));
		

		obstacles.add(new Rectangle(500, 0, 250, 25));

		obstacles.add(new Rectangle(350, 0, 50, 25));

		obstacles.add(new Rectangle(400, 50, 50, 25));
	}

}
