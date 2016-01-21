package Guis.Button;

import Guis.GuiObject;
import Guis.Interfaces.GuiIngame;
import Guis.Menu;
import Main.Game;
import Map.World;
import Utils.Difficulty;
import Utils.Player;
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


	@Override
	public void onClicked( int button, int x, int y, Menu menu ) {
		if(i > 3) {
			Game.world = new World((int)(Game.default_x_size * difficulty.sizeMultiplier), (int)(Game.default_y_size * difficulty.sizeMultiplier), difficulty);
			Game.world.initMap();

			Game.player = new Player();


			Game.ingame = true;
			Game.menu = new GuiIngame();
		}
	}

	@Override
	public void renderObject( Graphics g2, Menu menu ) {
		i += 1;

		g2.setColor(isMouseOver() ? RenderUtil.getColorToSlick(new java.awt.Color(200, 200, 200, 255)) : RenderUtil.getColorToSlick(new java.awt.Color(194, 194, 194, 200)));
		g2.fill(new Rectangle(x, y, width, height));

		g2.setColor(org.newdawn.slick.Color.black);
		g2.draw(new Rectangle(x,y, width, height));

		g2.setClip(new Rectangle(x, y, width, height));

		RenderUtil.resizeFont(g2, 24);
		RenderUtil.changeFontStyle(g2, Font.BOLD);
		FontUtils.drawCenter(g2.getFont(), difficulty.name, x, y + 30, width - 2, g2.getColor());
		RenderUtil.resetFont(g2);

		RenderUtil.resizeFont(g2, 14);
		g2.drawString("- Prices: " +  (int)(difficulty.costModifer * 100) + "%" + "\n" +
						"- Enemy health: " + (int)(difficulty.healthModifier * 100) + "%" + "\n" +
						"- Enemy amount: " + (int)(difficulty.enemyCountModier * 100) + "%" + "\n" +
						"- Lives: " + difficulty.lives + "\n" +
						"- World size: " + ((int)((Game.default_x_size * difficulty.sizeMultiplier))) + "x" + ((int)((Game.default_y_size * difficulty.sizeMultiplier))), x + 5, y + 80);
		RenderUtil.changeFontStyle(g2, Font.BOLD);
		g2.drawString("Render something cool \ndown here!\nPerhaps difficulty icon", x + 2, y + 200);
		RenderUtil.resetFont(g2);

		g2.setClip(null);
	}
}
