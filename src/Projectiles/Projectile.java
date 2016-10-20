package Projectiles;

import Entities.GameEntity;
import Main.Files.Tower;
import Main.Game;
import Utilities.FontHandler;
import World.WorldBase;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class Projectile extends GameEntity {
	public GameEntity entityTarget;
	public Tower towerSource;
	
	public Projectile( WorldBase world, float x, float y, GameEntity target, Tower towerSource ) {
		super(world, x, y);
		
		this.entityTarget = target;
		this.towerSource = towerSource;
	}
	
	@Override
	public int getEntityMaxHealth() {
		return 1;
	}
	
	@Override
	public boolean selectable() {
		return false;
	}
	
	@Override
	public String getEntityName() {
		return towerSource.getTowerName() + " Projectile";
	}
	
	
	
	@Override
	public int getMoneyDropped() {
		return 0;
	}
	
	@Override
	public float getMovementSpeed() {
		return entityTarget.getMovementSpeed() + 2;
	}
	
	@Override
	public void setMovementSpeed( float f ) {}
	
	@Override
	public void renderEntity( Graphics g2, float x, float y, float blockSizeX, float blockSizeY ) {
		Rectangle rectangle = new Rectangle(x + (blockSizeX * 0.5F), y + (blockSizeY * 0.5F), blockSizeX * 0.2F, blockSizeY * 0.2F);
		g2.setColor(FontHandler.getColorToSlick(new java.awt.Color(170, 170, 170, 100)));
		g2.fill(rectangle);
	}
	
	@Override
	protected void updateEntity() {
		if(entityTarget == null || entityTarget.health <= 0){
			world.entities.remove(this);
			return;
		}
		
		float targetX = entityTarget.x - x;
		float targetY = entityTarget.y - y;
		
		this.x += (targetX * (getMovementSpeed() / 100)) / (10 / Game.gameSpeed);
		this.y += (targetY * (getMovementSpeed() / 100)) / (10 / Game.gameSpeed);
	}
}
