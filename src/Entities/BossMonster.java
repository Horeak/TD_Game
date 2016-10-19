package Entities;

import Main.Game;
import World.WorldBase;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Ellipse;

public class BossMonster extends GameEntity {
	public BossMonster( WorldBase world, float x, float y) {
		super(world, x, y);
	}

	@Override
	public int getMoneyDropped() {
		return 200;
	}

	public float movementSpeed = 3;

	@Override
	public float getMovementSpeed() {
		return movementSpeed;
	}

	@Override
	public void setMovementSpeed(float f) {
		movementSpeed = f;
	}

	@Override
	public void renderEntity(Graphics g2, float x, float y, float blockSizeX, float blockSizeY) {
		g2.setColor(new Color(70, 0, 70));

		Ellipse e = new Ellipse(x + (blockSizeX * 0.5F), y + (blockSizeY * 0.5F), (blockSizeX * 0.75F), (blockSizeY * 0.5F));
		Ellipse e1 = new Ellipse(x + (blockSizeX * 0.5F), y + (blockSizeY * 0.5F), (blockSizeX * 0.5F), (blockSizeY * 0.75F));

		g2.fill(e);
		g2.fill(e1);

		g2.setColor(Color.black);
		g2.draw(e);
		g2.draw(e1);

	}

	@Override
	protected void updateEntity() {

	}

	@Override
	public int getEntityMaxHealth() {
		return Game.game.player.getHealthScaled(200);
	}

	@Override
	public String getEntityName() {
		return "Boss Monster";
	}
}
