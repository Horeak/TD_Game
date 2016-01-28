package Towers;

import Entities.Entity;
import Main.Game;
import Map.World;
import org.newdawn.slick.Graphics;

public abstract class Tower extends BaseNode implements Cloneable{
	public Tower( World world, int x, int y){
		super(world, x,y);
	}

	public int delay;
	private int killedByTurret = 0;

	//Add getTarget
	public abstract String getTurretName();

	public abstract int getTurretMaxLevel();
	public abstract int getTurretLevel();
	public abstract boolean canUpgrade();
	public abstract void upgradeTurret();

	public abstract int getUpgradeCost();
	public abstract int getTurretCost();
	public abstract int getTurretSellAmount();

	public int getEnemiesKilledByTurret(){return killedByTurret;}
	public int getEnemiesInSight(){return world.getEntitiesNearTurret(this).size();}

	public abstract int getTurretRange();
	public abstract int getTurretDamage();

	public void attackEntity(Entity ent){
		if(ent != null) {
			ent.setEntityHealth(ent.getEntityHealth() - getTurretDamage());

			if (ent.getEntityHealth() <= 0) {
				killedByTurret += 1;
				Game.player.money += ent.getMoneyDropped();
			}
		}
	}
	public abstract int getAttackDelay();


	public abstract void renderTower( Graphics g2, int renderX, int renderY, int sizeX, int sizeY);


	//TODO Currently targetting the entity with the highest health.(Pherhaps make it level based. (For example the harder the enemy the higher the level the higher the targeting chance))
	public Entity getTarget(){
		Entity ent = null;

		for(Entity entt : world.getEntitiesNearTurret(this)){
			if(ent == null || entt.getEntityHealth() > ent.getEntityHealth()){
				ent = entt;
			}
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
