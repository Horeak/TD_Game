package Towers;

import Entities.BossMonster;
import Entities.GameEntity;
import Main.Files.CostCaluculator;
import Main.Files.Tower;
import Main.Files.TowerRarity;
import Map.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;

public class HeavyTower extends Tower {
	public HeavyTower(){
		super(null, 0, 0);
	}


	public HeavyTower( World world, int x, int y) {
		super(world, x, y);
	}

	@Override
	public String getTowerName() {
		return "Heavy Tower";
	}

	@Override
	public String getTowerDescription() {
		return "Tower will do 5% of any enemies health. Will only attack boss monsters.";
	}

	@Override
	public int GetTowerMaxLevel() {
		return 1;
	}

	@Override
	public boolean canUpgrade() {
		return false;
	}

	@Override
	public int getTowerCost() {
		return CostCaluculator.getCostFromValue(TowerRarity.VERY_RARE, 1.25F);
	}

	@Override
	public int getTowerRange() {
		return 4;
	}

	@Override
	public int getTowerDamage( GameEntity gameEntity ) {
		return gameEntity != null ? (int)(gameEntity.getEntityMaxHealth() * 0.95F) : -1;
	}
	
	@Override
	public boolean canAttackEntity( GameEntity gameEntity ) {
		return gameEntity instanceof BossMonster;
	}
	
	@Override
	public int getAttackDelay() {
		return 20;
	}

	@Override
	public void renderTower(Graphics g2, int renderX, int renderY, int sizeX, int sizeY) {
		Rectangle rectangle = new Rectangle(renderX, renderY, sizeX, sizeY);

		g2.setColor(Color.red.darker());//Center Box
		g2.fill(rectangle);
		g2.setColor(Color.black);//Center Box Outline
		g2.draw(rectangle);

		//TODO Circle is not center when it is scaled down!
		Circle circle = new Circle(rectangle.getCenterX(), rectangle.getCenterY(), ((sizeX + sizeY) / 6));

		g2.setColor(Color.green.darker());
		g2.fill(circle);
		g2.setColor(Color.black);
		g2.draw(circle);
	}
}
