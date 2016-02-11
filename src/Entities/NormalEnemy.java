package Entities;

import Main.Game;
import World.WorldBase;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class NormalEnemy extends GameEntity {
	public NormalEnemy(WorldBase world, float x, float y) {
		super(world, x, y);
	}

	@Override
	public int getMoneyDropped() {
		return 10;
	}

	@Override
	public float getMovementSpeed() {
		return 10;
	}

	@Override
	public void renderEntity(Graphics g2, float x, float y, float blockSizeX, float blockSizeY) {
		g2.setColor(Color.yellow);

		Rectangle rect = new Rectangle(x + (blockSizeX * 0.25F), y + (blockSizeY * 0.25F), blockSizeX * 0.5F, blockSizeY * 0.5F);
		g2.fill(rect);

		g2.setColor(Color.black);
		g2.draw(rect);
	}

	@Override
	protected void updateEntity() {

	}

	@Override
	public int getEntityMaxHealth() {
		return Game.game.player.getHealthScaled(15);
	}

	@Override
	public String getEntityName() {
		return "Normal Enemy";
	}
}
