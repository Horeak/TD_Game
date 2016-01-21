package Guis.Interfaces;

import Main.Game;
import Map.World;
import PathFinding.Utils.Node;
import Towers.BaseNode;
import Utils.Difficulty;
import Utils.RenderUtil;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import java.awt.*;

public class BackgroundMenu extends Guis.Menu {
	public static World world;

	public BackgroundMenu(){
		if(world == null) {
			world = new World(Game.default_x_size, Game.default_y_size, Difficulty.NORMAL);
			world.initMap();
		}
	}

	@Override
	public void render( Graphics g2 ) {
		for(int x = 0; x < world.xSize; x++){
			for(int y = 0; y < world.ySize; y++){
				Node node = world.getNode(x, y);

				float renderX = Game.xWindowSize / world.xSize;
				float renderY = Game.yWindowSize / world.ySize;

				if(node != null && node instanceof BaseNode){
					((BaseNode)node).renderNode(g2, (int)(x * renderX), (int)(y * renderY), (int)renderX, (int)renderY);
				}
			}
		}

		g2.setColor(RenderUtil.getColorToSlick(new Color(194, 194, 194, 70)));
		g2.fill(new Rectangle(0,0,Display.getWidth(), Display.getHeight()));
	}

	@Override
	public boolean canRender() {
		return true;
	}
}
