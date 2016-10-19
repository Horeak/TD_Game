package Entities;

import Main.Game;
import World.WorldBase;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class TinyMonster extends GameEntity {
	public TinyMonster( WorldBase world, float x, float y) {
		super(world, x, y);
	}

	@Override
	public int getMoneyDropped() {
		return 5;
	}

	public float movementSpeed = 8;

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
		g2.setColor(Color.green);
		Rectangle rect = new Rectangle(x + (blockSizeX * 0.35F), y + (blockSizeY * 0.35F), blockSizeX * 0.25F, blockSizeY * 0.25F);
		g2.fill(rect);

		g2.setColor(Color.black);
		g2.draw(rect);
	}

	@Override
	protected void updateEntity() {

	}

	@Override
	public int getEntityMaxHealth() {
		return Game.game.player.getHealthScaled(10);
	}

	@Override
	public String getEntityName() {
		return "Tiny Mob";
	}
}
