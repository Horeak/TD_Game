package Entities;

import Map.World;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.font.effects.Effect;
import org.newdawn.slick.geom.Rectangle;

import java.util.HashMap;

public abstract class Entity {

	//TODO getEntityMovementSpeed();

	public float x, y;
	public World world;
	public int health = getEntityMaxHealth();

	public HashMap<Integer, Effect> activeEffects = new HashMap<>();

	public Entity(World world, float x, float y){
		this.x = x;
		this.y = y;

		this.world = world;
	}

	public int getEntityHealth(){return  health;}

	public void setEntityHealth(int i ){
		health = i;
	}

	public abstract int getEntityMaxHealth();
	public abstract String getEntityName();
	public abstract int getMoneyDropped();

	public abstract void renderEntity(Graphics g2, float blockSizeX, float blockSizeY);

	public void applyEffect(Effect effect){
		activeEffects.put(activeEffects.size(), effect);
	}

	public void removeEffect(int id){
		activeEffects.remove(id);
	}

	Rectangle rect = null;
	public boolean isMouseOver(int mouseX, int mouseY, float blockSizeX, float blockSizeY){
		rect = new Rectangle(x * blockSizeX, y * blockSizeY, blockSizeX, blockSizeY);
		return rect.contains(mouseX, mouseY);
	}
}
