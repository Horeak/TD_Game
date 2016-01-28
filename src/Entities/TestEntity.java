package Entities;

import Main.Game;
import Map.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class TestEntity extends Entity {

	public TestEntity(World world, float x, float y) {
		super(world, x, y);
	}

	@Override
	public int getEntityMaxHealth() {
		return (int)(10 * Game.world.difficulty.healthModifier);
	}

	@Override
	public String getEntityName() {
		return "Test";
	}

	@Override
	public int getMoneyDropped() {
		return 10;
	}

	public boolean isMouseOver(int mouseX, int mouseY, float blockSizeX, float blockSizeY){
		rect = new Rectangle(x * blockSizeX + (blockSizeX * 0.25F), y * blockSizeY + (blockSizeY * 0.25F), blockSizeX * 0.5F, blockSizeY * 0.5F);
		return rect.contains(mouseX, mouseY);
	}

	@Override
	public void renderEntity(Graphics g2, float blockSizeX, float blockSizeY) {
		g2.setColor(Color.red);

		if(health <= (health * 0.5F))
		g2.draw(new Rectangle(x * blockSizeX, y * blockSizeY, blockSizeX, blockSizeY));

		g2.fill(new Rectangle(x * (blockSizeX) + (blockSizeX * 0.25F), y * (blockSizeY) + (blockSizeY * 0.25F), blockSizeX * 0.5F, blockSizeY * 0.5F));
	}
}
