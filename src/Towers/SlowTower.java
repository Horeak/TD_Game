package Towers;

import Effects.SlowDownEffect;
import Entities.GameEntity;
import Main.Files.CostCaluculator;
import Main.Files.Tower;
import Main.Files.TowerRarity;
import Map.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Path;
import org.newdawn.slick.geom.Rectangle;

public class SlowTower extends Tower {
	public SlowTower(){
		super(null, 0, 0);
	}


	public SlowTower(World world, int x, int y) {
		super(world, x, y);
	}

	@Override
	public String getTowerName() {
		return "Freeze Tower";
	}

	@Override
	public String getTowerDescription() {
		return "Slows down monsters.";
	}

	@Override
	public int GetTowerMaxLevel() {
		return 5;
	}

	@Override
	public boolean canUpgrade() {
		return getTowerLevel() < GetTowerMaxLevel();
	}

	@Override
	public int getTowerCost() {
		return CostCaluculator.getCostFromValue(TowerRarity.UNCOMMON, 0.75F);
	}

	@Override
	public int getTowerRange() {
		return 2 + (getTowerLevel() - 1);
	}

	@Override
	public int getTowerDamage( GameEntity gameEntity ) {
		return 0;
	}

	@Override
	public int getAttackDelay() {
		return 20 - (getTowerLevel() * 2);
	}

	@Override
	public boolean attackEntity( GameEntity ent) {
		if(!ent.hasEffect(SlowDownEffect.class)) {
			ent.applyEffect(new SlowDownEffect(getTowerLevel() * 2, getTowerLevel()));
			return true;
		}

		return false;
	}

	@Override
	public void renderTower(Graphics g2, int renderX, int renderY, int sizeX, int sizeY) {
		Rectangle rectangle = new Rectangle(renderX, renderY, sizeX, sizeY);
		
		GameEntity target = getTarget();

		g2.setColor(Color.blue);
		g2.fill(rectangle);
		g2.setColor(Color.black);
		g2.draw(rectangle);
		
		g2.pushTransform();
		
		Path Shape = new Path(rectangle.getMinX() + (sizeX * 0.25F), rectangle.getMaxY() - (sizeY * 0.25F));
		
		Shape.lineTo(rectangle.getMinX() + (sizeX * 0.25F), rectangle.getY() + (sizeY * 0.25F));
		Shape.curveTo(rectangle.getMaxX() - (sizeX * 0.25F), rectangle.getY() + (sizeY * 0.25F), rectangle.getMinX() + (sizeX * 0.5F), rectangle.getY() + (sizeY * 0.5F), rectangle.getMaxX() - (sizeX * 0.5F), rectangle.getY() + (sizeY * 0.5F));
		Shape.lineTo(rectangle.getMaxX() - (sizeX * 0.25F), rectangle.getMaxY() - (sizeY * 0.25F));
		Shape.close();
		
		
		if(target != null && target.rect != null) {
			double angle = Math.atan2(Shape.getCenterX() - target.rect.getCenterX(), Shape.getCenterY() - target.rect.getCenterY());
			angle = (float)Math.toDegrees(angle);
			g2.rotate(Shape.getCenterX(), Shape.getCenterY(), -(float)angle);
		}
		
		g2.setColor(Color.cyan);
		g2.fill(Shape);
		g2.setColor(Color.black);
		g2.draw(Shape);
		
		g2.popTransform();
	}
}
