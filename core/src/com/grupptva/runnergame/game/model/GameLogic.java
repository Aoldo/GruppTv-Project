package com.grupptva.runnergame.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.grupptva.runnergame.ScenePlugin;
import com.grupptva.runnergame.game.model.gamecharacter.GameCharacter;
import com.grupptva.runnergame.game.model.gamecharacter.Point;
import com.grupptva.runnergame.game.model.world.Chunk;
import com.grupptva.runnergame.game.model.world.Tile;
import com.grupptva.runnergame.game.model.world.WorldModel;
import com.grupptva.runnergame.game.services.collision.CollisionChecker;
import com.grupptva.runnergame.game.services.worldgenerator.WorldGenerator;
import com.grupptva.runnergame.game.services.collision.ICollisionChecker;
import com.grupptva.runnergame.game.view.GameRenderer;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

/**
 * @author Mattias revised by Karl and Agnes
 */
public class GameLogic implements ScenePlugin, InputProcessor {
	GameRenderer gameRenderer;
	// private character
	private GameCharacter character;
	private WorldModel world;
	private WorldGenerator generator;
	private ICollisionChecker collisionChecker;
	private CollisionLogic collisionLogic;
	private HookLogic hookLogic;

	private int chunkWidth = 40;
	private int chunkHeight = 20;

	Chunk c = new Chunk(chunkWidth, chunkHeight);
	Chunk d = new Chunk(chunkWidth, chunkHeight);

	private int tileSize = 20;

	private float pixelsPerFrame = 3f;

	private final int jumpKeyCode = Input.Keys.SPACE;
	private final int hookKeyCode = Input.Keys.H;
	private final int resetKeyCode = Input.Keys.R;

	//
	public GameLogic() {
		Gdx.input.setInputProcessor(this);

		gameRenderer = new GameRenderer();
		character = new GameCharacter(30, 150, pixelsPerFrame);
		world = new WorldModel();

		collisionChecker = new CollisionChecker();
		collisionLogic = new CollisionLogic(character, world, tileSize, collisionChecker);

		hookLogic = new HookLogic(character, world, tileSize, chunkWidth, chunkHeight);

		generator = new WorldGenerator(pixelsPerFrame, tileSize, 4l, chunkWidth, chunkHeight, 0, character);
		//generator = new WorldGenerator(character.getJumpInitialVelocity(),
		//		character.getGravity(), pixelsPerFrame, tileSize, 4l, chunkWidth, chunkHeight, 0, 1, 75);

		for (int x = 0; x < c.getTiles().length; x++) {
			for (int y = 0; y < c.getTiles()[0].length; y++) {
				if (y == 0) {
					c.getTiles()[x][y] = Tile.OBSTACLE;
				} else {
					c.getTiles()[x][y] = Tile.EMPTY;
				}
			}
		}
		for (int x = 0; x < c.getTiles().length; x++) {
			for (int y = 0; y < c.getTiles()[0].length; y++) {
				if (y == 4) {
					d.getTiles()[x][y] = Tile.OBSTACLE;
				} else {
					d.getTiles()[x][y] = Tile.EMPTY;
				}
			}
		}
		world.setChunks(new Chunk[]{c, d, c});

		//TODO: First 3 chunks should be a tutorial.
		world.setChunks(
				new Chunk[]{c, generator.generateChunk(), generator.generateChunk()});
	}

	public void update() {
		if (character.isDead()) {
			character.setDead(false);
			reset();
		} else {
			world.moveLeft(pixelsPerFrame);
			collisionLogic.handlePossibleCollision();
			character.update();
			if (world.getPosition() < -tileSize * chunkWidth) {
				world.incrementStartIndex();
				world.setPosition(0);
				world.updateChunk(generator.generateChunk());
			}
		}
		//move world here or world.update()?
		//world.moveLeft(pixelsPerFrame);
	}

	public void render(SpriteBatch batch, ShapeRenderer sr) {
		sr.begin(ShapeType.Filled);
		gameRenderer.renderCharacter(tileSize, character, sr);
		gameRenderer.renderWorld(tileSize, getWorld(), sr);
		sr.end();
	}

	public WorldModel getWorld() {
		return world;
	}

	public void setWorld(WorldModel world) {
		this.world = world;
	}

	private void reset() {
		character = new GameCharacter(30, 150, pixelsPerFrame);

		generator = new WorldGenerator(pixelsPerFrame, tileSize, 5l, chunkWidth, chunkHeight, 0, character);

		world.setChunks(new Chunk[]{c, generator.generateChunk(), generator.generateChunk()});
		world.setPosition(0);
		world.setStartIndex(0);
		collisionLogic.setGameCharacter(character);
	}

	public void castHook() {
		Point hookEndPos = getPositionWhereHookExitsWorld();
		List<Tile[]> columnsToCheck = getColumnsToCheckWhenCastingHook(hookEndPos);
		handleCastHook(columnsToCheck, hookEndPos);
	}

	private void handleCastHook(List<Tile[]> columnsToCheck, Point hookEndPos) {
		for (int col = 0; col < columnsToCheck.size(); col++) {
			for(int row = 0; row < columnsToCheck.get(col).length; row++) {
				Tile tile = columnsToCheck.get(col)[row];
				if(tile != Tile.EMPTY) {
					out.println("Not empty!!");
					float columnCharacterIsIn = Math.abs(getWorld().getPosition() - character.getPosition().getX()) / tileSize + 1;
					float tileXPos = columnCharacterIsIn + col * tileSize;
					out.printf("dx: %.3f%n", character.getPosition().getX() - tileXPos);
					float tileYPos = row * tileSize;
					boolean intersectsTile = checkIfLineIntersectsTile(hookEndPos, tileXPos, tileYPos);
					if(intersectsTile) {
						out.println("Intersected!!");
						Point hookPosition = new Point(tileXPos + tileSize, tileYPos + tileSize);
						character.initHook(hookPosition);
					}
				}
			}
		}
	}

	boolean checkIfLineIntersectsTile(Point hookEndPos, float tileXPos, float tileYPos) {
		float x = character.getPosition().getX() + tileSize/2;
		float y = character.getPosition().getY() + tileSize/2;
		float dx = hookEndPos.getX() - x;
		float dy = hookEndPos.getY() - y;
		float left = x - tileXPos;
		float right = tileXPos + tileSize - x;
		float top = y - tileYPos + tileSize;
		float bottom = tileYPos - y;
		float[] p = {-dx, dx, -dy, dy};
		float[] q  = {x-left, right-x, y-top, bottom-y};
		float u1 = Float.NEGATIVE_INFINITY;
		float u2 = Float.POSITIVE_INFINITY;

		for(int i = 0; i < 4; i++) {
			if(p[i] == 0) {
				if(q[i] < 0) {
					return false;
				}
			} else {
				float t = q[i] / p[i];
				if(p[i] < 0 && u1 < t) {
					u1 = t;
				}
				else if(p[i] > 0 && u2 > t) {
					u2 = t;
				}
			}
		}
		if(u1 > u2 || u1 > 1 || u1 < 0) {
			return false;
		}
		return true;
	}

	private List<Tile[]> getColumnsToCheckWhenCastingHook(Point hookEndPos) {
		List<Tile[]> columns = new ArrayList<Tile[]>();
		int columnHookIsIn = (int) (hookEndPos.getX() - world.getPosition()) / tileSize + 1;
		int columnCharacterIsIn = (int) (character.getPosition().getX() - world.getPosition()) / tileSize + 1;
		for(int i = columnCharacterIsIn; i <= columnHookIsIn; i++){
			columns.add(world.getColumn(i));
		}
		return columns;
	}

	private Point getPositionWhereHookExitsWorld() {
		Point hookStart = new Point(character.getPosition().getX() + tileSize / 2,
				character.getPosition().getX() + tileSize / 2);
		float endX = hookStart.getX() + (chunkHeight * tileSize - hookStart.getY()) / (float) Math.tan(character.getHookAngle());
		if (endX < chunkWidth) {
			return new Point(endX, chunkHeight);
		} else {
			float endY = hookStart.getY() + (chunkWidth - hookStart.getX()) * (float) Math.tan(character.getHookAngle());
			return new Point(chunkWidth, endY);
		}

	}

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
			case jumpKeyCode:
				character.jump();
				return true;
			case hookKeyCode:
				//character.initHook(character.getPosition().getOffsetPoint(75, character.getHookAngle()));
				hookLogic.castHook();
				return true;
			case resetKeyCode:
				reset();
				return true;
			default:
				return false;
		}
	}

	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
			case hookKeyCode:
				if (character.isAttachedWithHook())
					character.removeHook();
				return true;
			default:
				return false;
		}
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}