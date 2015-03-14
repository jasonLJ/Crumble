package us.rescyou.crumble.room;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.tiled.TiledMap;

import us.rescyou.crumble.Game;
import us.rescyou.crumble.entity.Goal;
import us.rescyou.crumble.entity.Player;
import us.rescyou.crumble.state.RunningState;

public class Room extends TiledMap {

	// General Variables
	public RunningState state;
	public List<Rectangle> boundingBoxes = new ArrayList<Rectangle>();
	public Goal goal;

	// Initialization
	public Room(RunningState state, String path) throws SlickException {
		super(path);
		this.state = state;

		// Find the collidable layer
		int layer = 1;

		// Read through the map for collision boxes
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				// Read the tile at that location
				int tileID = getTileId(x, y, layer);

				if (tileID != 0) { // Exists on this layer
					boundingBoxes.add(new Rectangle(x * Game.TILE_SIZE, y * Game.TILE_SIZE, Game.TILE_SIZE, Game.TILE_SIZE));
				}
			}
		}

		// Finding the Goal and Player
		int objectGroupCount = getObjectGroupCount();
		for (int gi = 0; gi < objectGroupCount; gi++) // gi = object group index
		{
			int objectCount = getObjectCount(gi);
			for (int oi = 0; oi < objectCount; oi++) // oi = object index
			{
				String objectName = getObjectName(gi, oi);
				if (objectName.equals("Goal")) {
					goal = new Goal(state, getObjectX(gi, oi), getObjectY(gi, oi));
				} else if (objectName.equals("Player")) {
					state.player = new Player(state, getObjectX(gi, oi), getObjectY(gi, oi));
				}
			}
		}
	}

}
