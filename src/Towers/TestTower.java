package Towers;

import Main.GameConfig;
import Map.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;

public class TestTower extends Tower {
	public TestTower(){
		super(null, 0, 0);
	}

	public TestTower( World world, int x, int y ) {
		super(world, x, y);
	}

	@Override
	public int getTurretCost() {
		return 100;
	}

	@Override
	public String getTurretName() {
		return "Test Tower";
	}

	@Override
	public int getTurretMaxLevel() {
		return 100;
	}


	@Override
	public boolean canUpgrade() {
		return getTurretLevel() < (getTurretMaxLevel());
	}

	@Override
	public int getTurretRange() {
		return 5 + ((getTurretLevel()-1));
	}

	@Override
	public int getTurretDamage() {
		return 5 + (getTurretLevel() * 5);
	}

	@Override
	public int getAttackDelay() {
		return GameConfig.debugMode ? 0 : 10;
	}


	@Override
	public void renderTower( Graphics g2, int renderX, int renderY, int sizeX, int sizeY ) {
		Rectangle rectangle = new Rectangle(renderX, renderY, sizeX, sizeY);

		g2.setColor(Color.yellow);
		g2.fill(rectangle);
		g2.setColor(Color.black);
		g2.draw(rectangle);

		Circle circle = new Circle(rectangle.getCenterX(), rectangle.getCenterY(), ((sizeX + sizeY) / 6));

		g2.setColor(Color.orange);
		g2.fill(circle);
		g2.setColor(Color.black);
		g2.draw(circle);
	}

}
