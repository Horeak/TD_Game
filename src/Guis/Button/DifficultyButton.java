package Guis.Button;

import Interface.GuiObject;
import Interface.UIMenu;
import Main.Game;
import Utils.Difficulty;
import Utils.FontHandler;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import java.awt.*;

public class DifficultyButton extends GuiObject {

	int i = 0;
	public Difficulty difficulty;
	public boolean selected = false;

	public DifficultyButton( int x, int y, int width, int height, UIMenu menu, Difficulty difficulty ) {
		super(Game.game,x, y, width, height, menu);
		this.difficulty = difficulty;
	}


	@Override
	public void onClicked( int button, int x, int y, UIMenu menu ) {
		selected ^= true;

		for(GuiObject ob : menu.guiObjects){
			if(ob instanceof DifficultyButton){
				if(ob != this){
					((DifficultyButton)ob).selected = false;
				}
			}
		}
	}

	@Override
	public void renderObject( Graphics g2, UIMenu menu ) {
		i += 1;

		g2.setColor(selected ? FontHandler.getColorToSlick(new java.awt.Color(220, 220, 220, 255)) : isMouseOver() ? FontHandler.getColorToSlick(new java.awt.Color(200, 200, 200, 255)) : FontHandler.getColorToSlick(new java.awt.Color(194, 194, 194, 200)));
		g2.fill(new Rectangle(x, y, width, height));

		g2.setColor(selected ? org.newdawn.slick.Color.black : org.newdawn.slick.Color.darkGray);
		g2.draw(new Rectangle(x,y, width, height));

		g2.setColor(org.newdawn.slick.Color.black);
		g2.setClip(new Rectangle(x, y, width, height));

		FontHandler.resizeFont(g2, 24);
		FontHandler.changeFontStyle(g2, Font.BOLD);
		org.newdawn.slick.util.FontUtils.drawCenter(g2.getFont(), difficulty.name, x, y + 30, width - 2, g2.getColor());
		FontHandler.resetFont(g2);

		FontHandler.resizeFont(g2, 14);
		g2.drawString("- Prices: " +  (int)(difficulty.costModifer * 100) + "%" + "\n" +
						"- Enemy health: " + (int)(difficulty.healthModifier * 100) + "%" + "\n" +
						"- Enemy amount: " + (int)(difficulty.enemyCountModier * 100) + "%" + "\n" +
						"- Lives: " + difficulty.lives + "\n", x + 5, y + 70);

		g2.setClip(null);
	}
}
