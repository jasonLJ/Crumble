package us.rescyou.crumble.entity;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import us.rescyou.crumble.Assets;
import us.rescyou.crumble.Game;
import us.rescyou.crumble.state.RunningState;

public class Player extends Entity {

	// Configuration
	public static final float SPEED = 2.5f;
	public static final float SPRINT_SPEED = 2.5f;
	public static final float JUMP_HEIGHT = -Game.GRAVITY - 4.5f;
	public static final int TICKS_PER_STEP = 35;

	// General Variables
	private float deltaJump; // For jumping and stuff
	private boolean canJump = true;
	private int stepTimer = 0;
	private int previousY;

	// Initialization
	public Player(RunningState state, int x, int y) {
		super(state, x, y, Assets.entitySheet, 0, 0, true);
		state.playerStartX = x;
		state.playerStartY = y;
		previousY = y;
	}

	// Updating
	public void tick(GameContainer container, StateBasedGame game) throws SlickException {
		// Add movement
		Vector2f horizontalVector = new Vector2f(0, 0);

		if (Keyboard.isKeyDown(Input.KEY_A)) {
			horizontalVector.x = -1;
			flipped = false;
		}

		if (Keyboard.isKeyDown(Input.KEY_D)) {
			horizontalVector.x = 1;
			flipped = true;
		}

		if (Keyboard.isKeyDown(Input.KEY_SPACE) && canJump) {
			deltaJump = JUMP_HEIGHT;
			canJump = false;

			// Play jump sound
			Assets.jumpSound.play();
		}

		// Make the movement vector only SPEED pixels long
		float currentSpeed = SPEED;
		if (Keyboard.isKeyDown(Input.KEY_LSHIFT)) {
			currentSpeed = SPRINT_SPEED;
		}

		horizontalVector = horizontalVector.normalise().scale(currentSpeed);

		// Manipulate jump vector
		float deltaY = (deltaJump + Game.GRAVITY);
		deltaJump *= .995f;

		move(horizontalVector.x, deltaY);

		// Handling step sounds
		if (horizontalVector.x != 0 && (int) getY() == previousY) {
			if (stepTimer >= TICKS_PER_STEP / currentSpeed) {
				// Reset the timer
				stepTimer = 0;

				// Make a step
				Assets.stepSound.play(1, .25f);
				;
			} else {
				stepTimer++;
			}
		}
		

		previousY = (int) getY();
	}

	// Movement
	public void moveY(float deltaY) {
		// Move the bounding box to the new position
		boundingBox.setY(boundingBox.getY() + deltaY);

		// See if it collides with anything
		Rectangle collided = state.getCollided(this);

		if (collided != null) { // Collided with something
			if (collided.getY() > getY()) {
				canJump = true;
			}

			deltaJump = -Game.GRAVITY;

			// Set ent's position as close as we can get it
			if (collided.getY() > getY()) { // Collided is Below
				float distance = collided.getY() - getY() - Game.TILE_SIZE - Game.COLLISION_BUFFER;
				setY(getY() + distance);
			} else { // Collided is Above
				float distance = getY() - collided.getY() - Game.TILE_SIZE - Game.COLLISION_BUFFER;
				setY(getY() - distance);
			}
		} else {
			// If it won't collide, let it move normally
			setY(getY() + deltaY);
		}
	}

}
