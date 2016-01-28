package Guis.Interfaces;


import Guis.Button.DifficultyButton;
import Guis.GuiObject;
import Guis.Menu;
import Main.Game;
import Utils.Difficulty;
import Utils.EnumWorldSize;
import Utils.RenderUtil;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.FontUtils;

import java.awt.*;

public class StartGameMenu extends BackgroundMenu {

	public int selectedXsize = Game.default_x_size, selectedYsize = Game.default_y_size;

	//TODO Loading StartGameMenu takes a long time on mac
	public StartGameMenu(){
		int width = ((Game.gameWindowX + 20) / 3);
		guiObjects.add(new DifficultyButton(20, 80, width, 170, this, Difficulty.EASY));
		guiObjects.add(new DifficultyButton(40 + width, 80, width, 170, this, Difficulty.NORMAL));
		guiObjects.add(new DifficultyButton(60 + (width * 2), 80, width, 170, this, Difficulty.HARD));

		guiObjects.add(new worldSizeButton(20, 280, width, 150, this, EnumWorldSize.SMALL));
		guiObjects.add(new worldSizeButton(40 + width, 280, width, 150, this, EnumWorldSize.MEDIUM));
		guiObjects.add(new worldSizeButton(60 + (width * 2), 280, width, 150, this, EnumWorldSize.BIG));

		//TODO Add start button

		guiObjects.add(new backButton(40 + width, 500, width, 40, this));
	}

	@Override
	public void render( Graphics g2 ) {
		super.render(g2);

		g2.setColor(RenderUtil.getColorToSlick(new java.awt.Color(194, 194, 194, 100)));
		g2.fill(new org.newdawn.slick.geom.Rectangle(0,0, Display.getWidth(), Display.getHeight()));
	}
}

class backButton extends GuiObject {

	int i = 0;
	public backButton( int x, int y, int width, int height, Menu menu ) {
		super(x, y, width, height, menu);
	}

	@Override
	public void onClicked( int button, int x, int y, Menu menu ) {
		if(i > 3) {
			Main.Game.menu = new MainMenu();
		}
	}

	@Override
	public void renderObject( Graphics g2, Menu menu ) {
		i += 1;

		g2.setColor(isMouseOver() ? RenderUtil.getColorToSlick(new java.awt.Color(200, 200, 200, 255)) : RenderUtil.getColorToSlick(new java.awt.Color(194, 194, 194, 200)));
		g2.fill(new Rectangle(x, y, width, height));

		g2.setColor(org.newdawn.slick.Color.black);
		g2.draw(new Rectangle(x,y, width, height));

		RenderUtil.resizeFont(g2, 16);
		RenderUtil.changeFontStyle(g2, Font.BOLD);
		FontUtils.drawCenter(g2.getFont(), "Back", x, y + (height / 4), width - 2, g2.getColor());
		RenderUtil.resetFont(g2);
	}
}

class worldSizeButton extends GuiObject{
	boolean selected = false;

	EnumWorldSize worldSize;

	public worldSizeButton(int x, int y, int width, int height, Menu menu, EnumWorldSize enumWorldSize) {
		super(x, y, width, height, menu);
		this.worldSize = enumWorldSize;
	}

	@Override
	public void onClicked(int button, int x, int y, Menu menu) {
	selected ^= true;

		for(GuiObject ob : menu.guiObjects){
			if(ob instanceof worldSizeButton){
				if(ob != this){
					((worldSizeButton)ob).selected = false;
				}
			}
		}
	}

	@Override
	public void renderObject(Graphics g2, Menu menu) {
		g2.setColor(selected ? RenderUtil.getColorToSlick(new java.awt.Color(220, 220, 220, 255)) : isMouseOver() ? RenderUtil.getColorToSlick(new java.awt.Color(200, 200, 200, 255)) : RenderUtil.getColorToSlick(new java.awt.Color(194, 194, 194, 200)));
		g2.fill(new Rectangle(x, y, width, height));

		g2.setColor(selected ? org.newdawn.slick.Color.black : org.newdawn.slick.Color.darkGray);
		g2.draw(new Rectangle(x,y, width, height));

		g2.setColor(org.newdawn.slick.Color.black);

		g2.setColor(org.newdawn.slick.Color.black);
		RenderUtil.resizeFont(g2, 16);
		RenderUtil.changeFontStyle(g2, Font.BOLD);
		FontUtils.drawCenter(g2.getFont(), worldSize.name(), x, y + 10, width, g2.getColor());
		RenderUtil.resetFont(g2);

		Rectangle rect = new Rectangle(x + (width / 4), y + (width / 4), width / 2, height / 2);
		g2.draw(rect);

		g2.drawLine(rect.getX() - 10, rect.getY() - 1, rect.getX() - 10, rect.getMaxY() - 1);

		g2.pushTransform();
		g2.rotate(rect.getX() - 15, rect.getCenterY() - 1, -90);
		RenderUtil.resizeFont(g2, 12);
		g2.drawString(worldSize.ySize + "", rect.getX() - 25, rect.getCenterY() - 10);
		RenderUtil.resetFont(g2);
		g2.popTransform();

		g2.drawLine(rect.getX() - 1, rect.getMaxY() + 10, rect.getMaxX() - 1, rect.getMaxY() + 10);
		RenderUtil.resizeFont(g2, 12);
		g2.drawString(worldSize.xSize + "", rect.getCenterX() - 10, rect.getMaxY() + 10);
		RenderUtil.resetFont(g2);

	}
}

