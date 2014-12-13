package us.rescyou.crumble;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import us.rescyou.crumble.state.LoadingState;
import us.rescyou.crumble.state.OverState;
import us.rescyou.crumble.state.RunningState;

public class Game extends StateBasedGame {

	// Configuration
	public static final String TITLE = "Crumble";
	public static final int WIDTH = 32;
	public static final int HEIGHT = 18;
	public static final int SCALE = 2;
	public static final int TILE_SIZE = 16;
	public static final int WIDTH_PIXEL = WIDTH * TILE_SIZE;
	public static final int HEIGHT_PIXEL = HEIGHT * TILE_SIZE;
	public static final int FALLOFF_BUFFER = 100;
	public static final int SHAKE_MAGNITUDE = 3;
	public static final float GRAVITY = 25f;
	public static final float COLLISION_BUFFER = 0.1f;
	public static final float QUICKENING_FACTOR = 0.925f;

	// Debug Configuration
	public static final boolean NO_TRANSITION = false;
	public static final boolean DRAW_BOUNDING_BOXES = false;
	public static final boolean IGNORE_DEATH = false;

	// States
	public static final int STATE_LOADING = 0;
	public static final int STATE_RUNNING = 1;
	public static final int STATE_OVER = 2;

	// Initialization
	public static void main(String[] args) throws SlickException {
		AppGameContainer container = new AppGameContainer(new Game());
		container.setDisplayMode(WIDTH * SCALE * TILE_SIZE, HEIGHT * SCALE * TILE_SIZE, false);
		container.setAlwaysRender(true);
		container.setShowFPS(false);
		container.start();
	}

	public Game() {
		super(TITLE);
	}

	// Miscellaneous
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new LoadingState(STATE_LOADING));
		addState(new RunningState(STATE_RUNNING));
		addState(new OverState(STATE_OVER));
		enterState(STATE_LOADING);
	}
}