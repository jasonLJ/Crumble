package us.rescyou.crumble.state;

import java.awt.GraphicsEnvironment;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import us.rescyou.crumble.Assets;
import us.rescyou.crumble.Game;
import us.rescyou.crumble.room.Room;

public class OverState extends BaseState {

	// General Variables
	private Room finalRoom;
	
	// Initialization
	public OverState(int id) {
		super(id);
	}

	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		finalRoom = new Room(null, Assets.MAPS + "13.tmx");
	}

	public void tick(GameContainer container, StateBasedGame game) throws SlickException {
	}

	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		g.scale(Game.SCALE, Game.SCALE);
		
		finalRoom.render(0, 0);
		
		Assets.vignette.draw();
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		
		g.setColor(Color.white);
	}
	

}
