package us.rescyou.crumble.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

import us.rescyou.crumble.Game;
import us.rescyou.crumble.state.RunningState;

public abstract class Entity {

	// General Variables
	public RunningState state;
	private float x, y;
	public boolean collidable;
	public Image spriteSheet;
	public int subImageX, subImageY;

	public Image image;
	public Image originalImage;
	public boolean flipped;
	public Rectangle boundingBox;

	// Initialization
	public Entity(RunningState state, int x, int y, Image spriteSheet, int subImageX, int subImageY, boolean collidable) {
		this.state = state;
		this.x = x;
		this.y = y;
		this.spriteSheet = spriteSheet;
		this.subImageX = subImageX;
		this.subImageY = subImageY;
		this.collidable = collidable;

		// Load up the image
		image = spriteSheet.getSubImage(subImageX * Game.TILE_SIZE, subImageY * Game.TILE_SIZE, Game.TILE_SIZE, Game.TILE_SIZE);
		originalImage = image;

		// Create the bounding box
		boundingBox = new Rectangle(x, y, Game.TILE_SIZE, Game.TILE_SIZE);
	}

	// Updating
	public abstract void tick(GameContainer container, StateBasedGame game) throws SlickException;

	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		render(container, game, g, 0, 0);

		if (Game.DRAW_BOUNDING_BOXES) {
			g.draw(boundingBox);
		}
	}

	public void render(GameContainer container, StateBasedGame game, Graphics g, float xOffs, float yOffs) throws SlickException {
		// Draw the image at the coordinates + offset
		image.getFlippedCopy(flipped, false).draw(xOffs + x, yOffs + y);
	}

	// Movement
	public void move(float deltaX, float deltaY) {
		moveX(deltaX);
		moveY(deltaY);
	}

	public void moveX(float deltaX) {
		// Move the bounding box to the new position
		boundingBox.setX(boundingBox.getX() + deltaX);

		// See if it collides with anything
		Rectangle collided = state.getCollided(this);
		if (collided != null) { // Collided with something
			// Set ent's position as close as we can get it
			if (collided.getX() > getX()) { // Collided Right
				float distance = collided.getX() - getX() - Game.TILE_SIZE - Game.COLLISION_BUFFER;
				setX(getX() + distance);
			} else { // Collided Left
				float distance = getX() - collided.getX() - Game.TILE_SIZE - Game.COLLISION_BUFFER;
				setX(getX() - distance);
			}
		} else {
			// If it won't collide, let it move
			setX(getX() + deltaX);
		}
	}

	public void moveY(float deltaY) {
		// Move the bounding box to the new position
		boundingBox.setY(boundingBox.getY() + deltaY);

		// See if it collides with anything
		Rectangle collided = state.getCollided(this);
		if (collided != null) { // Collided with something
			// Set ent's position as close as we can get it
			if (collided.getY() > getY()) { // Collided Below
				float distance = collided.getY() - getY() - Game.TILE_SIZE - Game.COLLISION_BUFFER;
				setY(getY() + distance);
			} else { // Collided Above
				float distance = getY() - collided.getY() - Game.TILE_SIZE - Game.COLLISION_BUFFER;
				setY(getY() - distance);
			}
		} else {
			// If it won't collide, let it move
			setY(getY() + deltaY);
		}
	}

	// Miscellaneous
	public void expandBoundingBox(float amount) {
		// Make the entity's bounding box bigger by touchDistance
		boundingBox.setX(boundingBox.getX() - amount);
		boundingBox.setY(boundingBox.getY() - amount);
		boundingBox.setWidth(boundingBox.getWidth() + 2 * amount);
		boundingBox.setHeight(boundingBox.getHeight() + 2 * amount);
	}

	// Getters
	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	// Setters
	public void setX(float x) {
		this.x = x;
		boundingBox.setX(x);
	}

	public void setY(float y) {
		this.y = y;
		boundingBox.setY(y);
	}
}
