package Entities;

import EntityFiles.Entity;
import Render.Renders.WorldRender;
import World.WorldBase;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.font.effects.Effect;
import org.newdawn.slick.geom.Rectangle;

import java.util.HashMap;

public abstract class GameEntity extends Entity {
	public GameEntity(WorldBase world, float x, float y) {
		super(world, x, y);
	}

	public HashMap<Integer, Effect> activeEffects = new HashMap<>();

	public abstract int getMoneyDropped();

	public abstract void renderEntity(Graphics g2, float blockSizeX, float blockSizeY);
	public void applyEffect(Effect effect){
		activeEffects.put(activeEffects.size(), effect);
	}
	public void removeEffect(int id){
		activeEffects.remove(id);
	}

	public void renderEntity(Graphics g2, int renderX, int renderY){
		renderEntity(g2, WorldRender.renderX, WorldRender.renderY);
	}

	public Rectangle rect = null;
	public boolean isMouseOver(int mouseX, int mouseY, float blockSizeX, float blockSizeY){
		rect = new Rectangle(x * blockSizeX, y * blockSizeY, blockSizeX, blockSizeY);
		return rect.contains(mouseX, mouseY);
	}
}
