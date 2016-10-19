package Main.Files;

import Main.Game;
import Main.GameConfig;
import Map.World;
import PathFinding.Utils.Node;
import Utilities.FontHandler;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class BaseNode extends Node {
	public World world;
	public boolean isPath = false;
	private float value;

	private float noise = 1F;

	public BaseNode( World world, int x, int y){
		super(x,y);
		this.world = world;
		noise -= ((float)Game.rand.nextInt(2) / 100F);
	}

	public void renderNode( Graphics g2, int x, int y, int sizeX, int sizeY){
		g2.setColor(GameConfig.renderDebug ? getColor() :  getColor().scaleCopy(noise));
		g2.fill(new Rectangle(x, y, sizeX, sizeY));
	}

	public void setValue(float x){
		value = x;
	}
	public float getValue(){
		return value;
	}

	public Color getColor(){
		if(GameConfig.renderDebug){
			if(world.getStartNode() != null && x == world.getStartNode().x && y == world.getStartNode().y){
				return Color.blue;
			}

			if(world.getEndNode() != null && x == world.getEndNode().x && y == world.getEndNode().y){
				return Color.cyan;
			}

			if(isPath || (getValue("openPathNode") != null && (boolean)getValue("openPathNode") == true)){
				return Color.yellow;
			}else{
				if(Game.world.validNode(null, x, y)){
					return value > 0 ? Color.green.darker(value / 5) : Color.green.brighter(value / 5);
				}else{
					return value > 0 ? Color.red.darker(value / 8) : Color.red.brighter(value / 7);
				}
			}
		}

		if(getValue("openPathNode") != null && (boolean)getValue("openPathNode") == true && !isPath) return FontHandler.getColorToSlick(new java.awt.Color(195, 133, 39)).darker(0.15f);

		if(isPath){
			return FontHandler.getColorToSlick(new java.awt.Color(140, 96, 19));
		}

		if(value <= -3) return new Color(0.1F, 0.1F, 1F).brighter(0.5F).brighter(value / 10);
		if(value <= -2) return FontHandler.getColorToSlick(new java.awt.Color(255, 211, 134)).brighter(value / 10);

		if(value > -2){
			if(value > 3){
				return Color.gray.brighter().darker(value / 8);
			}

			return Color.green.darker().darker(value / 10);
		}

		return Color.white;
	}

}
