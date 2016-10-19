package Entities;

import Effects.Effect;
import EntityFiles.Entity;
import Main.Game;
import Render.Renders.WorldRender;
import World.WorldBase;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class GameEntity extends Entity {
	public GameEntity(WorldBase world, float x, float y) {
		super(world, x, y);
	}

	public ConcurrentHashMap<Integer, Effect> activeEffects = new ConcurrentHashMap<>();

	public abstract int getMoneyDropped();
	public abstract float getMovementSpeed();
	public abstract void setMovementSpeed(float f);

	public abstract void renderEntity(Graphics g2, float x, float y, float blockSizeX, float blockSizeY);


	public void applyEffect(Effect effect){
		if(!hasEffect(effect.getClass()) || hasEffect(effect.getClass()) && effect.allowMultiple()) {
			activeEffects.put(activeEffects.size(), effect);
			effect.applyToEntity(this);
		}
	}


	public void removeEffect(int id){
		activeEffects.get(id).finishEffect(this);
		activeEffects.remove(id);
	}

	public boolean hasEffect(Class<? extends Effect> c){
		for(Effect e : activeEffects.values()){
			if(e.getClass().equals(c)){
				return true;
			}
		}

		return false;
	}

	public boolean damage(int damage, boolean drop){
		setEntityHealth(getEntityHealth() - damage);

		if (getEntityHealth() <= 0) {

			if(drop) {
				Game.player.money += getMoneyDropped();
			}

			return true;
		}

		return false;
	}

	public int getIdFromEffect(Effect effect){
		for(Map.Entry<Integer, Effect> ent : activeEffects.entrySet()){
			if(ent.getValue() == effect){
				return ent.getKey();
			}

			if(ent.getValue().time == effect.time && ent.getValue().timeLasted == effect.timeLasted){
				if(ent.getValue().getClass().getName().equals(effect.getClass().getName())){
					return ent.getKey();
				}
			}
		}

		return -1;
	}

	protected abstract void updateEntity();
	public void updateEntityBase(){
		for(Effect eff : activeEffects.values()){
			eff.update(this);

			eff.timeLasted += 1;

			if(eff.timeLasted >= eff.time || ((eff.time - eff.timeLasted) / 1000) <= 0){
				removeEffect(getIdFromEffect(eff));
			}
		}

		updateEntity();
	}

	public void renderEntity(Graphics g2, int renderX, int renderY){
		renderEntity(g2, x * WorldRender.renderX, y * WorldRender.renderY, WorldRender.renderX, WorldRender.renderY);
	}

	public Rectangle rect = null;
	public boolean isMouseOver(int mouseX, int mouseY, float blockSizeX, float blockSizeY){
		rect = new Rectangle(x * blockSizeX, y * blockSizeY, blockSizeX, blockSizeY);
		return rect.contains(mouseX, mouseY);
	}

}
