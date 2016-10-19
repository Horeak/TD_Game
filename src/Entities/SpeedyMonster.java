package Entities;

import Main.Game;
import World.WorldBase;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Path;

public class SpeedyMonster extends GameEntity {
	public SpeedyMonster( WorldBase world, float x, float y) {
		super(world, x, y);
	}

	@Override
	public int getMoneyDropped() {
		return 50;
	}

	public float movementSpeed = 30;

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
		g2.setColor(Color.orange);

		Path shape = new Path(x + (blockSizeX * 0.5F),y);
		shape.lineTo(x, y + blockSizeY);
		shape.lineTo(x + blockSizeX, y + blockSizeY);
		shape.close();

		g2.fill(shape);

		g2.setColor(Color.black);
		g2.draw(shape);
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
		return "Speedy Mob";
	}
}
