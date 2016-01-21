package Towers;

import Map.World;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.tests.xml.Entity;

public abstract class Turret extends BaseNode implements Cloneable{
	public Turret(World world, int x, int y){
		super(world, x,y);
	}

	//Add getTarget
	public abstract String getTurretName();

	public abstract int getTurretMaxLevel();
	public abstract int getTurretLevel();
	public abstract boolean canUpgrade();
	public abstract void upgradeTurret();

	public abstract int getUpgradeCost();
	public abstract int getTurretCost();
	public abstract int getTurretSellAmount();

	public abstract int getEnemiesKilledByTurret();
	public abstract int getEnemiesInSight();

	public abstract int getTurretRange();
	public abstract int getTurretDamage();

	public abstract void renderTower( Graphics g2, int renderX, int renderY, int sizeX, int sizeY);

	private Entity target;

	public void setTarget(Entity ent){
		this.target = ent;
	}

	//TODO Change to Entities
	public Entity getTarget(){
		return target;
	}

	public Object clone(){
		Turret turret = null;

		try{
			turret = this.getClass().newInstance();

			turret.world = world;
			turret.x = x;
			turret.y = y;
			turret.target = target;

		}catch (Exception e){
			e.printStackTrace();
		}

		return turret;
	}
}
