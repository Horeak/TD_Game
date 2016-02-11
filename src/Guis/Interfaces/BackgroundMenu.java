package Guis.Interfaces;

import Interface.UIMenu;
import Main.Game;
import Map.World;
import PathFinding.Utils.Node;
import Towers.BaseNode;
import Utils.Difficulty;
import Utils.EnumWorldSize;
import Utils.FontHandler;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import java.awt.*;

public class BackgroundMenu extends UIMenu {
	public static World world;

	@Override
	public void render( Graphics g2 ) {
		if(world == null) {
			world = new World(EnumWorldSize.MEDIUM.xSize, EnumWorldSize.MEDIUM.ySize, Difficulty.NORMAL);
			world.initMap();
		}

		if(world != null) {
			for (int x = 0; x < world.xSize; x++) {
				for (int y = 0; y < world.ySize; y++) {
					Node node = world.getNode(x, y);

					float renderX = Game.xWindowSize / world.xSize;
					float renderY = Game.yWindowSize / world.ySize;

					if (node != null && node instanceof BaseNode) {
						((BaseNode) node).renderNode(g2, (int) (x * renderX), (int) (y * renderY), (int) renderX, (int) renderY);
					}
				}
			}
		}

		g2.setColor(FontHandler.getColorToSlick(new Color(194, 194, 194, 70)));
		g2.fill(new Rectangle(0,0,Display.getWidth(), Display.getHeight()));
	}

	@Override
	public boolean canRender() {
		return true;
	}
}
