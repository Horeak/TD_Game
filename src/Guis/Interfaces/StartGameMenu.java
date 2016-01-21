package Guis.Interfaces;


import Guis.Button.DifficultyButton;
import Guis.GuiObject;
import Guis.Menu;
import Main.Game;
import Utils.Difficulty;
import Utils.RenderUtil;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.FontUtils;

import java.awt.*;

public class StartGameMenu extends BackgroundMenu {

	//TODO Loading StartGameMenu takes a long time on mac
	public StartGameMenu(){
		int width = ((Game.gameWindowX + 20) / 3);
		guiObjects.add(new DifficultyButton(20, 100, width, 300, this, Difficulty.EASY));
		guiObjects.add(new DifficultyButton(40 + width, 100, width, 300, this, Difficulty.NORMAL));
		guiObjects.add(new DifficultyButton(60 + (width * 2), 100, width, 300, this, Difficulty.HARD));

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

