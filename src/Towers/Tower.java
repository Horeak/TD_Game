package Towers;

import Entities.GameEntity;
import EntityFiles.Entity;
import Main.Game;
import Map.World;
import PathFinding.Utils.Node;
import org.newdawn.slick.Graphics;

import java.util.ConcurrentModificationException;

public abstract class Tower extends Node implements Cloneable{
	public World world;
	public Tower( World world, int x, int y){
		super(x,y);
		this.world = world;
	}


	private int delay;
	private int killedByTurret = 0;
	private int level = 1;

	public abstract String getTurretName();

	public abstract int getTurretMaxLevel();
	public int getTurretLevel(){return level;}
	public abstract boolean canUpgrade();
	public void upgradeTurret(){level += 1;}

	public abstract int getTurretCost();

	public int getEnemiesKilledByTurret(){return killedByTurret;}
	public int getEnemiesInSight(){return world.getEntitiesNearTurret(this).size();}

	public abstract int getTurretRange();
	public abstract int getTurretDamage();

	public void attackEntity(GameEntity ent){
		if(ent != null) {
			ent.setEntityHealth(ent.getEntityHealth() - getTurretDamage());

			if (ent.getEntityHealth() <= 0) {
				killedByTurret += 1;
				Game.player.money += ent.getMoneyDropped();
			}
		}
	}
	public abstract int getAttackDelay();

	public int getCurrentDelay(){
		return delay;
	}
	public void setCurrentDelay(int i){
		delay = i;
	}


	public abstract void renderTower( Graphics g2, int renderX, int renderY, int sizeX, int sizeY);


	//TODO Currently targetting the entity with the highest health.(Pherhaps make it level based. (For example the harder the enemy the higher the level the higher the targeting chance))
	public GameEntity getTarget(){
		GameEntity ent = null;

		try {
			for (Entity entt : world.getEntitiesNearTurret(this)) {
				if (ent == null || entt.getEntityHealth() > ent.getEntityHealth()) {
					ent = (GameEntity)entt;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}

		return ent;
	}

	public Object clone(){
		Tower tower = null;

		try{
			tower = this.getClass().newInstance();

			tower.world = world;
			tower.x = x;
			tower.y = y;

		}catch (Exception e){
			e.printStackTrace();
		}

		return tower;
	}
}
