package us.rescyou.crumble.state;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public abstract class BaseState extends BasicGameState {

	// Configuration
	public static final int TICKS_PER_SECOND = 60;
	
	// General Variables
	private int id;
	private int timer = 0;
	
	// Initialization
	public BaseState(int id) {
		this.id = id;
	}
	
	public abstract void init(GameContainer container, StateBasedGame game) throws SlickException;

	// Updating
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		if(timer > (int) (1000f / (float) TICKS_PER_SECOND)) {
			timer -= (int) (1000f / (float) TICKS_PER_SECOND);
			tick(container, game);
		} else {
			timer += delta;
		}
	}
	
	public abstract void tick(GameContainer container, StateBasedGame game) throws SlickException;

	public abstract void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException;

	// Getters
	public int getID() {
		return id;
	}

}
