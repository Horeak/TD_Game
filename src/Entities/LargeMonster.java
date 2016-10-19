package Entities;

import Main.Game;
import World.WorldBase;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class LargeMonster extends GameEntity {
	public LargeMonster( WorldBase world, float x, float y) {
		super(world, x, y);
	}

	@Override
	public int getMoneyDropped() {
		return 20;
	}

	public float movementSpeed = 10;

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
		g2.setColor(Color.red);

		Rectangle rect = new Rectangle(x, y, blockSizeX * 1F, blockSizeY * 1F);
		g2.fill(rect);

		g2.setColor(Color.black);
		g2.draw(rect);
	}

	@Override
	protected void updateEntity() {

	}

	@Override
	public int getEntityMaxHealth() {
		return Game.game.player.getHealthScaled(25);
	}

	@Override
	public String getEntityName() {
		return "Large Mob";
	}
}
