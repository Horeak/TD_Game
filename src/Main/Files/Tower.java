package Main.Files;


import Entities.GameEntity;
import Map.World;
import PathFinding.Utils.Node;
import Projectiles.Projectile;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

public abstract class Tower extends Node implements Cloneable{
	public World world;
	public Tower( World world, int x, int y){
		super(x,y);
		this.world = world;
	}


	//TODO Add description
	//TODO Display a level text below tower (Lv. 1, Lv. 2, Lv. 3 .....)
	//TODO Add something that displayes the attack timer (A bar at the bottom that fills up maybe)

	private int delay;
	private int killedByTurret = 0;
	private int level = 1;

	public abstract String getTowerName();
	public abstract String getTowerDescription();

	public abstract int getTowerMaxLevel();
	public int getTowerLevel(){return level;}
	public abstract boolean canUpgrade();
	public void upgradeTower(){level += 1;}
	public void setTowerLevel(int level){
		this.level = level;
	}

	public abstract int getTowerCost();
	
	public boolean canAttackEntity(GameEntity gameEntity){
		return true;
	}
	
	public int getEnemiesKilledByTower(){return killedByTurret;}
	public int getEnemiesInSight(){return world.getEntitiesNearTurret(this).size();}

	public abstract int getTowerRange();
	public abstract int getTowerDamage( GameEntity gameEntity );

	public boolean attackEntity( GameEntity ent){
		if(ent != null) {
			if(ent.damage(getTowerDamage(ent), true)){
				killedByTurret += 1;
			}
			return true;
		}

		return false;
	}
	public abstract int getAttackDelay();

	public int getCurrentDelay(){
		return delay;
	}
	public void setCurrentDelay(int i){
		delay = i;
	}

	public boolean attackAll(){return false;}


	public abstract void renderTower( Graphics g2, int renderX, int renderY, int sizeX, int sizeY);
	

	//TODO Add toggle button for this!
	public TowerTargetMode getTargetMode(){
		return TowerTargetMode.NORMAL;
	}

	//TODO Currently targetting the entity with the highest health.(Pherhaps make it level based. (For example the harder the enemy the higher the level the higher the targeting chance))
	public GameEntity getTarget(){
		if(world == null || !world.hasGenerated || world.getEntitiesNearTurret(this) == null){
			return null;
		}
		
		
		GameEntity ent = null;

		try {
			for (GameEntity entt : world.getEntitiesNearTurret(this)) {
				if(ent != null && !canAttackEntity(entt) || ent instanceof Projectile){
					continue;
				}
				
				if(getTargetMode() == TowerTargetMode.NORMAL){
					if(ent == null || new Vector2f(entt.x, entt.y).distance(new Vector2f(x, y)) < new Vector2f(ent.x, ent.y).distance(new Vector2f(x, y))){
						ent = entt;
					}

				}else if(getTargetMode() == TowerTargetMode.FASTEST){
					if(ent == null || entt.getMovementSpeed() > ent.getMovementSpeed()){
						ent = entt;
					}

				}else if(getTargetMode() == TowerTargetMode.STRONGEST){
					if(ent == null || entt.getEntityMaxHealth() > ent.getEntityMaxHealth()){
						ent = entt;
					}

				}else if(getTargetMode() == TowerTargetMode.MOST_WORTH){
					if(ent == null || entt.getMoneyDropped() > ent.getMoneyDropped()){
						ent = entt;
					}
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
