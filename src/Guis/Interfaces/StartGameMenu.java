package Guis.Interfaces;

import Guis.Button.DifficultyButton;
import Interface.GuiObject;
import Interface.UIMenu;
import Main.Files.Difficulty;
import Main.Files.EnumWorldSize;
import Main.Game;
import Map.World;
import Utilities.FontHandler;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import java.awt.*;

public class StartGameMenu extends BackgroundMenu {

	public int selectedXsize = Game.default_x_size, selectedYsize = Game.default_y_size;

	//TODO Loading StartGameMenu takes a long time on mac

	boolean added = false;
	public void add(){
		int width = ((Game.gameWindowX + 20) / 3);
		guiObjects.add(new DifficultyButton(20, 80, width, 170, this, Difficulty.EASY));
		guiObjects.add(new DifficultyButton(40 + width, 80, width, 170, this, Difficulty.NORMAL));
		guiObjects.add(new DifficultyButton(60 + (width * 2), 80, width, 170, this, Difficulty.HARD));

		guiObjects.add(new worldSizeButton(20, 280, width, 150, this, EnumWorldSize.SMALL));
		guiObjects.add(new worldSizeButton(40 + width, 280, width, 150, this, EnumWorldSize.MEDIUM));
		guiObjects.add(new worldSizeButton(60 + (width * 2), 280, width, 150, this, EnumWorldSize.BIG));

		guiObjects.add(new backButton(40 + width, 500, width, 40, this));
		guiObjects.add(new startButton(40 + width, 450, width, 40, this));
	}

	@Override
	public void render( Graphics g2 ) {
		super.render(g2);

		if(!added){
			add();
			added = true;
		}

		g2.setColor(FontHandler.getColorToSlick(new java.awt.Color(194, 194, 194, 100)));
		g2.fill(new org.newdawn.slick.geom.Rectangle(0,0, Display.getWidth(), Display.getHeight()));

		for(GuiObject ob : guiObjects){
			if(ob instanceof startButton){
				boolean mapSize = false, difficulty = false;

				for(GuiObject ob1 : guiObjects){
					if(ob1 instanceof worldSizeButton){
						if(((worldSizeButton)ob1).selected){
							mapSize = true;
							continue;
						}
					}else if (ob1 instanceof DifficultyButton){
						if(((DifficultyButton)ob1).selected){
							difficulty = true;
							continue;
						}
					}
				}
				((startButton)ob).enabled = mapSize && difficulty;
			}
		}
	}
}

class backButton extends GuiObject {

	int i = 0;
	public backButton( int x, int y, int width, int height, UIMenu menu ) {
		super(Game.game,x, y, width, height, menu);
	}

	@Override
	public void onClicked( int button, int x, int y, UIMenu menu ) {
		if(i > 3) {
			Game.game.setCurrentMenu(new MainMenu());
		}
	}

	@Override
	public void renderObject( Graphics g2, UIMenu menu ) {
		i += 1;

		g2.setColor(isMouseOver() ? FontHandler.getColorToSlick(new java.awt.Color(200, 200, 200, 255)) : FontHandler.getColorToSlick(new java.awt.Color(194, 194, 194, 200)));
		g2.fill(new Rectangle(x, y, width, height));

		g2.setColor(org.newdawn.slick.Color.black);
		g2.draw(new Rectangle(x,y, width, height));

		FontHandler.resizeFont(g2, 16);
		FontHandler.changeFontStyle(g2, Font.BOLD);
		org.newdawn.slick.util.FontUtils.drawCenter(g2.getFont(), "Back", x, y + (height / 4), width - 2, g2.getColor());
		FontHandler.resetFont(g2);
	}
}

class startButton extends GuiObject{

	public startButton( int x, int y, int width, int height, UIMenu menu ) {
		super(Game.game,x, y, width, height, menu);
	}

	@Override
	public void onClicked( int button, int x, int y, UIMenu menu ) {
		if(enabled){
			EnumWorldSize worldSize = null;
			Difficulty diff = null;

			for(GuiObject ob1 : menu.guiObjects){
				if(diff != null && worldSize != null) break;

				if(ob1 instanceof worldSizeButton){
					if(((worldSizeButton)ob1).selected){
						worldSize = ((worldSizeButton)ob1).worldSize;
						continue;
					}
				}else if (ob1 instanceof DifficultyButton){
					if(((DifficultyButton)ob1).selected){
						diff = ((DifficultyButton)ob1).difficulty;
						continue;
					}
				}
			}

			if(diff != null && worldSize != null){
				Game.world = new World(worldSize.xSize, worldSize.ySize, diff);
				Game.world.initMap();

				Game.player.lives = diff.lives;

				Game.ingame = true;
				Game.game.setCurrentMenu(new GuiIngame());
			}
		}
	}

	@Override
	public void renderObject( Graphics g2, UIMenu menu ) {
		g2.setColor(!enabled ? isMouseOver() ? FontHandler.getColorToSlick(new java.awt.Color(200, 200, 200, 255)) : FontHandler.getColorToSlick(new java.awt.Color(194, 194, 194, 200)) : FontHandler.getColorToSlick(new java.awt.Color(230, 230, 230, 200)));
		g2.fill(new Rectangle(x, y, width, height));

		g2.setColor(org.newdawn.slick.Color.black);
		g2.draw(new Rectangle(x,y, width, height));

		FontHandler.resizeFont(g2, 16);
		FontHandler.changeFontStyle(g2, Font.BOLD);
		org.newdawn.slick.util.FontUtils.drawCenter(g2.getFont(), "Start", x, y + (height / 4), width - 2, g2.getColor());
		FontHandler.resetFont(g2);
	}
}

class worldSizeButton extends GuiObject{
	boolean selected = false;
	EnumWorldSize worldSize;
	public worldSizeButton( int x, int y, int width, int height, UIMenu menu, EnumWorldSize enumWorldSize) {
		super(Game.game, x, y, width, height, menu);
		this.worldSize = enumWorldSize;

	}

	@Override
	public void onClicked(int button, int x, int y, UIMenu menu) {
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
	public void renderObject(Graphics g2, UIMenu menu) {
		g2.setColor(selected ? FontHandler.getColorToSlick(new java.awt.Color(220, 220, 220, 255)) : isMouseOver() ? FontHandler.getColorToSlick(new java.awt.Color(200, 200, 200, 255)) : FontHandler.getColorToSlick(new java.awt.Color(194, 194, 194, 200)));
		g2.fill(new Rectangle(x, y, width, height));

		g2.setColor(selected ? org.newdawn.slick.Color.black : org.newdawn.slick.Color.darkGray);
		g2.draw(new Rectangle(x,y, width, height));


		g2.setColor(org.newdawn.slick.Color.black);
		FontHandler.resizeFont(g2, 16);
		FontHandler.changeFontStyle(g2, Font.BOLD);
		org.newdawn.slick.util.FontUtils.drawCenter(g2.getFont(), worldSize.name(), x, y + 10, width, g2.getColor());
		FontHandler.resetFont(g2);

		Rectangle rect = new Rectangle(x + (width / 4), y + (width / 4), width / 2, height / 2);
		g2.draw(rect);
		g2.drawLine(rect.getX() - 10, rect.getY() - 1, rect.getX() - 10, rect.getMaxY() - 1);

	//	g2.pushTransform();
	//	g2.rotate(rect.getX() - 15, rect.getCenterY() - 1, -90);
		FontHandler.resizeFont(g2, 12);
		g2.drawString(worldSize.ySize + "", rect.getX() - 25 - ((worldSize.ySize + "").length() * 2), rect.getCenterY() - 10);
		FontHandler.resetFont(g2);
	//	g2.popTransform();

		g2.drawLine(rect.getX() - 1, rect.getMaxY() + 10, rect.getMaxX() - 1, rect.getMaxY() + 10);
		FontHandler.resizeFont(g2, 12);
		g2.drawString(worldSize.xSize + "", rect.getCenterX() - 10, rect.getMaxY() + 10);
		FontHandler.resetFont(g2);

	}
}

