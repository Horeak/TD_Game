package Towers;

import Main.Game;
import Map.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class TestTurret extends Turret {
	public TestTurret(){
		super(null, 0, 0);
	}

	public TestTurret(World world, int x, int y ) {
		super(world, x, y);
	}

	@Override
	public int getTurretCost() {
		return 100;
	}

	@Override
	public int getTurretSellAmount() {
		return getTurretCost() + (getTurretCost() * level);
	}

	@Override
	public String getTurretName() {
		return "Test Turret";
	}

	public int level = 1;

	@Override
	public int getTurretMaxLevel() {
		return 100;
	}

	@Override
	public int getTurretLevel() {
		return level;
	}

	@Override
	public int getUpgradeCost() {
		return (int)((getTurretCost() * 1.75F) * (getTurretLevel() * 1.25F));
	}

	@Override
	public boolean canUpgrade() {
		return getTurretLevel() < (getTurretMaxLevel());
	}

	@Override
	public void upgradeTurret() {
		level += 1;
		Game.player.money += getUpgradeCost();
	}

	@Override
	public int getEnemiesKilledByTurret() {
		return Integer.MAX_VALUE;
	}

	@Override
	public int getEnemiesInSight() {
		return 0;
	}

	@Override
	public int getTurretRange() {
		return 5 + ((level-1));
	}

	@Override
	public int getTurretDamage() {
		return 5 + (level * 5);
	}


	@Override
	public void renderTower( Graphics g2, int renderX, int renderY, int sizeX, int sizeY ) {
		Rectangle rectangle = new Rectangle(renderX, renderY, sizeX, sizeY);
		Rectangle rectangle1 = new Rectangle(renderX + (sizeX * 0.25F), renderY + 4, (sizeX * 0.5F), (sizeY * 0.75F));

		g2.setColor(Color.yellow);
		g2.fill(rectangle);
		g2.setColor(Color.black);
		g2.draw(rectangle);

		g2.setColor(Color.orange);
		g2.fill(rectangle1);
		g2.setColor(Color.black);
		g2.draw(rectangle1);

		g2.drawLine(rectangle1.getX(), rectangle1.getY() + 3, rectangle1.getX() + rectangle1.getWidth() - 1, rectangle1.getY() + 3);
		g2.drawLine(rectangle1.getX() + ((rectangle1.getWidth() - 1) / 2), rectangle1.getY(), rectangle1.getX() + ((rectangle1.getWidth() - 1) / 2), rectangle1.getY() + rectangle1.getHeight() - 1);

	}

}
