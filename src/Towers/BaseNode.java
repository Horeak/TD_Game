package Towers;

import Map.World;
import PathFinding.Utils.Node;
import Utils.FontHandler;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class BaseNode extends Node {
	public World world;
	public boolean isPath = false;
	public float value;

	public BaseNode( World world, int x, int y){
		super(x,y);
		this.world = world;
	}

	public void renderNode( Graphics g2, int x, int y, int sizeX, int sizeY){
		if(this instanceof Tower){
			((Tower)this).renderTower(g2, x, y, sizeX, sizeY);
			return;
		}

		g2.setColor(getColor());
		g2.fill(new Rectangle(x, y, sizeX, sizeY));
	}

	public Color getColor(){
		if(getValue("openPathNode") != null && (boolean)getValue("openPathNode") == true && !isPath) return FontHandler.getColorToSlick(new java.awt.Color(255, 215, 109)).darker(0.15f);

//		if(GameFiles.world != null && GameFiles.world.getStartNode() != null && GameFiles.world.getStartNode().x == x && GameFiles.world.getStartNode().y == y) return Color.green.darker();
//		if(GameFiles.world != null && GameFiles.world.getEndNode() != null && GameFiles.world.getEndNode().x == x && GameFiles.world.getEndNode().y == y) return Color.red.darker();

		if(isPath){
			return FontHandler.getColorToSlick(new java.awt.Color(255, 215, 109));
		}

		if(value < -3) return Color.blue.darker(0.25f);
		if(value < -2) return new Color(0.1F, 0.1F, 1F);
		if(value < -1) return Color.yellow.darker(0.4F);
		if(value < 0) return Color.green.darker(0.35f);
		if(value < 1) return Color.green.darker();
		if(value < 2) return Color.green.darker().darker(0.12F);
		if(value < 3) return Color.gray;
		if(value < 4) return Color.darkGray;
		if(value < 5) return Color.darkGray.darker();

		return Color.white;
	}

}
