package us.rescyou.crumble.state;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

import us.rescyou.crumble.Assets;
import us.rescyou.crumble.Game;
import us.rescyou.crumble.entity.Entity;
import us.rescyou.crumble.entity.Player;
import us.rescyou.crumble.room.Room;

public class RunningState extends BaseState {

	// General Variables
	public Room[] room = new Room[3]; // Previous, Current, Next
	public Player player;
	public float xOffs = 20, yOffs = 20;
	public int shakeTimer = 0;

	// Transition Variables
	private float transitionDelay;
	private int transitionTimer;
	private float transitionDuration;
	private int transitionedLines;
	private boolean passedDelay;
	private Direction transitionDirection = Direction.RIGHT;
	// private Direction transitionDirection = Direction.LEFT;
	private boolean goalReached = false;
	private int leadingMap = 1;

	// Reloading
	public float playerStartX;
	public float playerStartY;
	public float onionTimer = 0;
	
	// Enums
	public enum Direction {
		RIGHT, LEFT;
	}

	// Initialization
	public RunningState(int id) {
		super(id);
	}

	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		// Set up the maps
		Room one = new Room(this, Assets.MAPS + leadingMap + ".tmx");
		Room two = new Room(this, Assets.MAPS + (leadingMap + 1) + ".tmx");
		leadingMap += 1;
		room[1] = one;
		room[2] = two;

		// If the player still doesn't exist, create one (for debugging)
		if (player == null) {
			player = new Player(this, 248, 135);
		}

		// Set up transition details
		setTransitionDetails(5, 10);
		
		// Loop the background music
		Assets.themeMusic.loop(1f, .5f);
	}

	// Updating
	public void tick(GameContainer container, StateBasedGame game) throws SlickException {
		// Decrement the shake timer
		if (shakeTimer >= 0) {
			shakeTimer -= 1;
			shakeScreen(Game.SHAKE_MAGNITUDE);
		} else {
			xOffs = 0;
			yOffs = 0;
		}

		// See if we've reached the goal if we haven't already
		if (!goalReached && player.boundingBox.intersects(getCurrentMap().goal.boundingBox)) {
			goalReached = true;
			passedDelay = true;
			transitionTimer = 0;
			
			Assets.goalSound.play();
			
			// Shake the screen
			shakeTimer = 10;
			
			if(leadingMap == 14) {
				game.enterState(Game.STATE_OVER);
			}
		}

		// Wait out the delay
		checkDelay();

		// See if we've fallen off the map
		checkFallOff();

		// Never get passed the delay if we're holding transitions
		if (Game.NO_TRANSITION) {
			passedDelay = false;
		}

		// Transition stuff
		transitionIteration();
		checkTransition();

		// Entity Stuff
		player.tick(container, game);
	}

	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		g.setColor(Color.green);

		// Scale the graphics
		g.scale(Game.SCALE, Game.SCALE);
		

		Room currentMap = getCurrentMap();
		Room nextMap = getNextMap();
		
		
		switch (transitionDirection) {
		case RIGHT:
			nextMap.render(Math.round(xOffs), Math.round(yOffs));
			nextMap.goal.render(container, game, g, xOffs, yOffs);

			currentMap.render(Math.round(xOffs + (transitionedLines * Game.TILE_SIZE)), Math.round(yOffs), transitionedLines, 0, Game.WIDTH - transitionedLines, Game.HEIGHT);
			currentMap.goal.render(container, game, g, xOffs, yOffs);

			player.render(container, game, g, xOffs, yOffs);
			break;
		case LEFT:
			nextMap.render(Math.round(xOffs), (Math.round(yOffs)));
			nextMap.goal.render(container, game, g, xOffs, yOffs);

			currentMap.render(Math.round(xOffs), Math.round(yOffs), 0, 0, Game.WIDTH - (Game.WIDTH - transitionedLines), Game.HEIGHT);
			currentMap.goal.render(container, game, g, xOffs, yOffs);

			player.render(container, game, g, xOffs, yOffs);
			break;
		}
		
		// Onioning
		if(onionTimer > 0) {
			System.out.println("onioning");
			nextMap.render(0, 0);
			onionTimer -= 1;
		}

		// Drawing bounding boxesDDD
		if (Game.DRAW_BOUNDING_BOXES) {
			for (Rectangle rect : getBoundingBoxes()) {
				g.draw(rect);
			}
		}
	}

	// Update Helpers
	private void checkFallOff() {
		boolean right = player.getX() > Game.WIDTH_PIXEL + Game.FALLOFF_BUFFER;
		boolean left = player.getX() + Game.TILE_SIZE < 0 - Game.FALLOFF_BUFFER;
		boolean down = player.getY() > Game.HEIGHT_PIXEL + Game.FALLOFF_BUFFER;
		boolean up = player.getY() + Game.TILE_SIZE < 0 - Game.FALLOFF_BUFFER;

		if (right || left || down || up) {
			reload();
		}
	}

	private void transitionIteration() {
		if (passedDelay) {
			int millisPerLine = (int) (transitionDuration / Game.WIDTH);

			if (goalReached) {
				// Triple the speed
				millisPerLine /= 3;
			}

			if (transitionDirection == Direction.RIGHT && transitionedLines < Game.WIDTH) {
				if (transitionTimer >= millisPerLine) {
					// Reset the transition timer
					transitionTimer -= millisPerLine;
					transitionedLines += 1;

					// Make transition sound
					Assets.crunchSound.play();
					
					// Shake the screen a little bit
					shakeTimer = 1;
				} else {
					transitionTimer += (1000f / BaseState.TICKS_PER_SECOND);
				}
			} else if (transitionDirection == Direction.LEFT && transitionedLines > 0) {
				if (transitionTimer >= millisPerLine) {
					// Reset transition timer
					transitionTimer -= millisPerLine;
					transitionedLines -= 1;

					// Make transition sound
					Assets.crunchSound.play();
					
					// Shake the screen a little bit
					shakeTimer = 1;
				} else {
					transitionTimer += (1000f / BaseState.TICKS_PER_SECOND);
				}
			}
		}
	}

	private void checkDelay() {
		if (!passedDelay) {
			if (transitionTimer >= transitionDelay) {
				transitionTimer = 0;
				passedDelay = true;
			} else {
				transitionTimer += (1000 / BaseState.TICKS_PER_SECOND);
			}
		}
	}

	private void checkTransition() throws SlickException {
		// See if we've transitioned the whole level
		if (transitionDirection == Direction.RIGHT) {
			if (transitionedLines >= Game.WIDTH) {
				moveNextRoom();
				setTransitionDetails((transitionDelay / 1000) * Game.QUICKENING_FACTOR * Game.QUICKENING_FACTOR, (((float) transitionDuration / 1000) * Game.QUICKENING_FACTOR));
			}
		} else if (transitionDirection == Direction.LEFT) {
			if (transitionedLines <= 0) {
				moveNextRoom();
				setTransitionDetails((transitionDelay / 1000) * Game.QUICKENING_FACTOR * Game.QUICKENING_FACTOR, (((float) transitionDuration / 1000) * Game.QUICKENING_FACTOR));
			}
		}
	}

	// Collision Detection
	public ArrayList<Rectangle> getBoundingBoxes() {
		ArrayList<Rectangle> output = new ArrayList<Rectangle>();

		// Go through the current map
		for (Rectangle rect : getCurrentMap().boundingBoxes) {
			// See if it's on screen
			switch (transitionDirection) {
			case RIGHT:
				if (rect.getX() / Game.TILE_SIZE > transitionedLines - 1) {
					output.add(rect);
				}
				break;
			case LEFT:
				if (rect.getX() / Game.TILE_SIZE < transitionedLines) {
					output.add(rect);
				}
				break;
			}
		}

		for (Rectangle rect : getNextMap().boundingBoxes) {
			switch (transitionDirection) {
			case RIGHT:
				if (rect.getX() / Game.TILE_SIZE < transitionedLines) {
					output.add(rect);
				}
				break;
			case LEFT:
				if (rect.getX() / Game.TILE_SIZE > transitionedLines - 1) {
					output.add(rect);
				}
				break;
			}
		}

		// Add bounding boxes around the goal if we've reached it
		if (goalReached) {
			for (int x = -1; x <= 2; x++) {
				for (int y = -1; y <= 2; y++) {
					if ((x == 0 && y == 0) || (x == 0 && y == 1) || (x == 1 && y == 0) || (x == 1 && y == 1)) {
						continue;
					}
					output.add(new Rectangle(getCurrentMap().goal.getX() + (Game.TILE_SIZE * x), getCurrentMap().goal.getY() + (Game.TILE_SIZE * y), Game.TILE_SIZE, Game.TILE_SIZE));
				}
			}
		}

		return output;
	}

	public boolean collides(Entity entity) {
		// Run through each collision box on the map
		for (Rectangle rect : getBoundingBoxes()) {
			if (rect != entity.boundingBox && (rect.intersects(entity.boundingBox))) {
				return true;
			}
		}
		return false;
	}

	public Rectangle getCollided(Entity entity) {
		// Run through each collision box on the map
		for (Rectangle rect : getBoundingBoxes()) {
			if (rect != entity.boundingBox && (rect.intersects(entity.boundingBox))) {
				return rect;
			}
		}
		return null;
	}

	public boolean touches(Entity entity, float touchDistance) {
		// Make the entity's bounding box bigger by touchDistance
		entity.expandBoundingBox(touchDistance);

		// See if it now collides with anything
		boolean touched = collides(entity);

		// Set the bounding box back
		entity.expandBoundingBox(-touchDistance);

		// Return
		return touched;
	}

	public Rectangle getTouched(Entity entity, float touchDistance) {
		// Make the entity's bounding box bigger by touchDistance
		entity.expandBoundingBox(touchDistance);

		// See if it now collides with anything
		Rectangle touched = getCollided(entity);

		// Set the bounding box back
		entity.expandBoundingBox(-touchDistance);

		// Return
		return touched;
	}

	// Miscellaneous
	private void shakeScreen(float magnitude) {
		xOffs =  (float) (-magnitude + (Math.random() * (magnitude * 2f)));
		yOffs =  (float) (-magnitude + (Math.random() * (magnitude * 2f)));
	}

	private void moveNextRoom() throws SlickException {
		// Check death
		if (!goalReached) {
			reload();
			return;
		}

		// Reset the player last position
		playerStartX = player.getX();
		playerStartY = player.getY();

		// Swap the direction
		if (transitionDirection == Direction.RIGHT) {
			transitionDirection = Direction.LEFT;
		} else if (transitionDirection == Direction.LEFT) {
			transitionDirection = Direction.RIGHT;
		}

		room[0] = room[1];
		room[1] = room[2];

		// Load the next Room
		if(leadingMap != 13) {			
			room[2] = new Room(this, Assets.MAPS + ++leadingMap + ".tmx");
		} else {
//			game.enterState(Game.STATE_OVER);
			setTransitionDetails(99999999, 9999999);
			leadingMap += 1;
		}
	}

	private void setTransitionDetails(float delay, float duration) {
		transitionDelay = delay * 1000;
		transitionTimer = 0;
		transitionDuration = duration * 1000;
		goalReached = false;

		if (transitionDirection == Direction.RIGHT) {
			transitionedLines = 0;
		} else if (transitionDirection == Direction.LEFT) {
			transitionedLines = Game.WIDTH;
		}
		passedDelay = false;
	}

	private void reload() {
		setTransitionDetails(transitionDelay / 1000, transitionDuration / 1000);
		player.setX(playerStartX);
		player.setY(playerStartY);

		// Play the death sound
		Assets.deathSound.play();
		
		// Shake the screen
		shakeTimer = 10;
	}

	// Getters
	public Room getCurrentMap() {
		return room[1];
	}

	public Room getNextMap() {
		return room[2];
	}

}
