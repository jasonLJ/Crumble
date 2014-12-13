package us.rescyou.crumble.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import us.rescyou.crumble.Assets;
import us.rescyou.crumble.Game;
import us.rescyou.crumble.state.RunningState;

public class Goal extends Entity {

	Image[][] images = new Image[2][2];

	public Goal(RunningState state, int x, int y) {
		super(state, x, y, Assets.entitySheet, 0, 2, false);

		// Set up the array of images
		for (int xi = 0; xi < images.length; xi++) {
			for (int yi = 0; yi < images[0].length; yi++) {
				images[xi][yi] = spriteSheet.getSubImage((subImageX + xi) * Game.TILE_SIZE, (subImageY + yi) * Game.TILE_SIZE, Game.TILE_SIZE, Game.TILE_SIZE);
			}
		}

		// Edit the boundingbox so it's directly in the middle
		boundingBox.setX(boundingBox.getX() + (Game.TILE_SIZE / 2f));
		boundingBox.setY(boundingBox.getY() + Game.TILE_SIZE);
	}

	public void tick(GameContainer container, StateBasedGame game) throws SlickException {
		// Do nothing
	}

	public void render(GameContainer container, StateBasedGame game, Graphics g, float xOffs, float yOffs) {
		// Render each of the images
		for (int xi = 0; xi < images.length; xi++) {
			for (int yi = 0; yi < images[0].length; yi++) {
				images[xi][yi].draw(xOffs + getX() + (xi * Game.TILE_SIZE), yOffs + getY() + (yi * Game.TILE_SIZE));
			}
		}
		
		if(Game.DRAW_BOUNDING_BOXES) {
			g.draw(boundingBox);
		}
	}
}