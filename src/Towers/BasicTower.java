package Towers;

import Entities.GameEntity;
import Main.Files.CostCaluculator;
import Main.Files.Tower;
import Main.Files.TowerRarity;
import Map.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Path;
import org.newdawn.slick.geom.Rectangle;

public class BasicTower extends Tower {
	public BasicTower(){
		super(null, 0, 0);
	}


	public BasicTower(World world, int x, int y) {
		super(world, x, y);
	}

	@Override
	public String getTowerName() {
		return "Basic Tower";
	}

	@Override
	public String getTowerDescription() {
		return "Basic attack tower. Attacks nearby monsters.";
	}

	@Override
	public int getTowerMaxLevel() {
		return 10;
	}

	@Override
	public boolean canUpgrade() {
		return getTowerLevel() < getTowerMaxLevel();
	}

	@Override
	public int getTowerCost() {
		return CostCaluculator.getCostFromValue(TowerRarity.COMMON, 0.5F);
	}

	@Override
	public int getTowerRange() {
		return 3 + (((getTowerLevel() - 1)) / 4);
	}

	@Override
	public int getTowerDamage( GameEntity gameEntity ) {
		return 2 + getTowerLevel();
	}

	@Override
	public int getAttackDelay() {
		return 3 + getTowerLevel();
	}

	double angle = 0;
	
	@Override
	public void renderTower(Graphics g2, int renderX, int renderY, int sizeX, int sizeY) {
		Rectangle rectangle = new Rectangle(renderX, renderY, sizeX, sizeY);
		
		GameEntity target = getTarget();

		//Tower Base
		g2.setColor(Color.yellow);
		g2.fill(rectangle);
		g2.setColor(Color.black);
		g2.draw(rectangle);

		g2.pushTransform();
		
		Path Triangle = new Path(rectangle.getMinX() + (sizeX * 0.25F), rectangle.getMaxY() - (sizeY * 0.25F));
		Triangle.lineTo(rectangle.getCenterX(), rectangle.getY() + (sizeY * 0.25F));
		Triangle.lineTo(rectangle.getMaxX() - (sizeX * 0.25F), rectangle.getMaxY() - (sizeY * 0.25F));
		Triangle.close();
		
		if(target != null && target.rect != null) {
			angle = Math.atan2(Triangle.getCenterX() - target.rect.getCenterX(), Triangle.getCenterY() - target.rect.getCenterY());
			angle = (float)Math.toDegrees(angle);
		}
		
		g2.rotate(Triangle.getCenterX(), Triangle.getCenterY(), -(float)angle);
		
		g2.setColor(Color.orange);
		g2.fill(Triangle);
		g2.setColor(Color.black);
		g2.draw(Triangle);
		
		g2.popTransform();
	}
}
