package Guis.Interfaces;

import Guis.Menu;
import Guis.Objects.GuiButton;
import Main.Game;
import Utils.RenderUtil;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.FontUtils;

import java.awt.*;

public class MainMenu extends BackgroundMenu {

	public MainMenu(){
		guiObjects.add(new MainMenuButton(0, (int) (Display.getHeight() * 0.5F), 210, 50, "Start game", this) {
			@Override
			public void clicked( int button, int x, int y, Menu menu ) {
				Game.menu = new StartGameMenu();
			}
		});
		guiObjects.add(new MainMenuButton(0, (int) (Display.getHeight() * 0.5F) + 60, 210, 50, "Settings", this) {
			@Override
			public void clicked( int button, int x, int y, Menu menu ) {

			}
		});
		guiObjects.add(new MainMenuButton(0, (int) (Display.getHeight() * 0.5F) + 120, 210, 50, "Exit", this) {
			@Override
			public void clicked( int button, int x, int y, Menu menu ) {
				System.exit(0);
			}
		});
	}

	@Override
	public void render(Graphics g2) {
		super.render(g2);

		g2.setColor(RenderUtil.getColorToSlick(new java.awt.Color(159, 159, 159, 108)));
		g2.fill(new Rectangle(0, 50, Display.getWidth(), 100));

		g2.setColor(Color.orange);
		RenderUtil.resizeFont(g2, 52);
		RenderUtil.changeFontStyle(g2, Font.BOLD);
		FontUtils.drawCenter(g2.getFont(), Game.Title, 0, (int)(Display.getHeight() * 0.12F), Display.getWidth(), g2.getColor());
		RenderUtil.resetFont(g2);

	}
}

abstract class MainMenuButton extends GuiButton{
	public MainMenuButton(int x, int y, int width, int height, String text, Menu menu) {
		super(x, y, width, height, text, menu);
	}

	@Override
	public void onClicked(int button, int x, int y, Menu menu) {
		clicked(button, x, y, menu);
	}

	public abstract void clicked(int button, int x, int y, Menu menu);

	@Override
	public void renderObject(Graphics g2, Menu menu) {
		//super.renderObject(g2, menu);

		Rectangle tangle = new Rectangle(x, y, width + (isMouseOver() ? 20 : 0), height);
		Color c = RenderUtil.getColorToSlick(new java.awt.Color(174, 75, 45));

		if(isMouseOver())
			c = c .brighter();

		g2.fill(tangle, new GradientFill(x, y, c, x + width, y + height, c.darker()));

		g2.setColor(isMouseOver() ? Color.orange.brighter() : Color.orange);

		RenderUtil.resizeFont(g2, 18);
		RenderUtil.changeFontStyle(g2, Font.BOLD);
		g2.drawString(text, x + (isMouseOver() ? 20 : 0), y + (height / 3F));
		RenderUtil.resetFont(g2);
	}
}
