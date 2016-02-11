package Entities;

import Main.Game;
import World.WorldBase;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;

public class HeavyEnemy extends GameEntity {
	public HeavyEnemy(WorldBase world, float x, float y) {
		super(world, x, y);
	}

	@Override
	public int getMoneyDropped() {
		return 50;
	}

	@Override
	public float getMovementSpeed() {
		return 5;
	}

	@Override
	public void renderEntity(Graphics g2, float x, float y, float blockSizeX, float blockSizeY) {
		g2.setColor(Color.blue);

		Circle c = new Circle(x + (blockSizeX * 0.5F), y + (blockSizeY * 0.5F), ((blockSizeX * 0.5F) + (blockSizeY * 0.5F)) / 2);
		g2.fill(c);

		g2.setColor(Color.black);
		g2.draw(c);
	}

	@Override
	protected void updateEntity() {

	}

	@Override
	public int getEntityMaxHealth() {
		return Game.game.player.getHealthScaled(100);
	}

	@Override
	public String getEntityName() {
		return "Heavy Enemy";
	}
}
