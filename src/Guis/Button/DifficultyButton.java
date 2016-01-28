package Guis.Button;

import Guis.GuiObject;
import Guis.Menu;
import Utils.Difficulty;
import Utils.RenderUtil;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.FontUtils;

import java.awt.*;

public class DifficultyButton extends GuiObject {

	int i = 0;
	Difficulty difficulty;
	public DifficultyButton( int x, int y, int width, int height, Menu menu, Difficulty difficulty ) {
		super(x, y, width, height, menu);
		this.difficulty = difficulty;
	}

boolean selected = false;

	@Override
	public void onClicked( int button, int x, int y, Menu menu ) {
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
	public void renderObject( Graphics g2, Menu menu ) {
		i += 1;

		g2.setColor(selected ? RenderUtil.getColorToSlick(new java.awt.Color(220, 220, 220, 255)) : isMouseOver() ? RenderUtil.getColorToSlick(new java.awt.Color(200, 200, 200, 255)) : RenderUtil.getColorToSlick(new java.awt.Color(194, 194, 194, 200)));
		g2.fill(new Rectangle(x, y, width, height));

		g2.setColor(selected ? org.newdawn.slick.Color.black : org.newdawn.slick.Color.darkGray);
		g2.draw(new Rectangle(x,y, width, height));

		g2.setColor(org.newdawn.slick.Color.black);
		g2.setClip(new Rectangle(x, y, width, height));

		RenderUtil.resizeFont(g2, 24);
		RenderUtil.changeFontStyle(g2, Font.BOLD);
		FontUtils.drawCenter(g2.getFont(), difficulty.name, x, y + 30, width - 2, g2.getColor());
		RenderUtil.resetFont(g2);

		RenderUtil.resizeFont(g2, 14);
		g2.drawString("- Prices: " +  (int)(difficulty.costModifer * 100) + "%" + "\n" +
						"- Enemy health: " + (int)(difficulty.healthModifier * 100) + "%" + "\n" +
						"- Enemy amount: " + (int)(difficulty.enemyCountModier * 100) + "%" + "\n" +
						"- Lives: " + difficulty.lives + "\n", x + 5, y + 70);

		g2.setClip(null);
	}
}
