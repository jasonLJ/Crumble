package us.rescyou.crumble.state;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.state.StateBasedGame;

import us.rescyou.crumble.Assets;

public class LoadingState extends BaseState {

	// Initialization
	public LoadingState(int id) {
		super(id);
	}

	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		// Load images
		Assets.worldSheet = new Image(Assets.MAPS + "world.png", false, Image.FILTER_NEAREST);
		Assets.entitySheet = new Image(Assets.MAPS + "entities.png", false, Image.FILTER_NEAREST);
		Assets.vignette = new Image(Assets.MAPS + "vignette.png", false, Image.FILTER_NEAREST);
		
		// Load sounds
		Assets.deathSound = new Sound(Assets.SOUNDS + "death.wav");
		Assets.stepSound = new Sound(Assets.SOUNDS + "step.wav");
		Assets.jumpSound = new Sound(Assets.SOUNDS + "jump.wav");
		Assets.crunchSound = new Sound(Assets.SOUNDS + "crunch.wav");
		Assets.goalSound = new Sound(Assets.SOUNDS + "goal.wav");
		Assets.onionSound = new Sound(Assets.SOUNDS + "onion.wav");
		Assets.themeMusic = new Sound(Assets.SOUNDS + "theme.wav");
		
		// Move to next state
		game.enterState(getID() + 1);
	}

	// Updating
	public void tick(GameContainer container, StateBasedGame game) throws SlickException {
		// Do nothing
	}

	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		// Do nothing
	}

}
