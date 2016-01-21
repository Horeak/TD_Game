package Map;

import Main.Game;
import PathFinding.Interfaces.MovingObject;
import PathFinding.Interfaces.NodeMap;
import PathFinding.NodeMapPathFinder;
import PathFinding.Utils.Node;
import PathFinding.Utils.Path;
import Towers.BaseNode;
import Utils.Difficulty;
import Utils.PerlinNoiseGenerator;
import Utils.RenderUtil;
import com.sun.javafx.geom.Vec2d;
import org.newdawn.slick.Color;

import java.util.ArrayList;

public class World  implements NodeMap{
	public Node[][] nodes;
	public int xSize, ySize;

	public Node startNode, endNode;

	public Difficulty difficulty;

	public World( int xSize, int ySize, Difficulty difficulty){
		nodes = new Node[xSize][ySize];

		this.xSize = xSize;
		this.ySize = ySize;

		this.difficulty = difficulty;
	}

	public void setTower( Node node, int x, int y){
		if (x >= 0 && y >= 0 && x < xSize && y < ySize){
			Node nd = null;

			if(nodes[x][y] instanceof BaseNode){
				nd = nodes[x][y];
			}

			nodes[ x ][ y ] = node != null ? node : createNode(x,y);

			if(nodes[x][y] instanceof BaseNode){
				((BaseNode)nodes[x][y]).x = x;
				((BaseNode)nodes[x][y]).y = y;

				((BaseNode)nodes[x][y]).world = this;
			}

			if(nd != null && nd instanceof BaseNode){
				((BaseNode)nodes[x][y]).value = ((BaseNode)nd).value;
				((BaseNode)nodes[x][y]).c = ((BaseNode)nd).c;
				((BaseNode)nodes[x][y]).isPath = ((BaseNode)nd).isPath;
			}
		}
	}


	@Override
	public Node getNode( int x, int y ) {
		if (x >= 0 && y >= 0 && x < xSize && y < ySize){
			return nodes[x][y];
		}
		return null;
	}

	@Override
	public void setNode( Node node ) {
		if(node != null)
		setTower(node, node.x, node.y);
	}


	public Path enemyPath = null;

	//TODO Optimize world generation to decrease loading times (For example do not regenerate world if path is not valid)
	@Override
	public void initMap() {
		for(int x = 0; x < xSize; x++){
			for(int y = 0; y < ySize; y++){
				setNode(createNode(x, y));
			}
		}
		float res = (((xSize / 2) + (Game.rand.nextInt(xSize / 2)) + ((ySize / 2) + (Game.rand.nextInt(ySize / 2)))) / 2F);

		PerlinNoiseGenerator generator = new PerlinNoiseGenerator(Game.rand.nextLong());

		for (int x = 0; x < xSize; x++) {
			for (int y = 0; y < ySize; y++) {
//				float tile = ((float)SimplexNoise.noise((x / res), (y / res)) * 8) / 2;
				double tile = (generator.noise((x / res), (y / res)) * 4);

				if(getNode(x,y) instanceof BaseNode){
					((BaseNode)getNode(x,y)).value = (float)tile;
				}
			}
		}

		int genAttempts = 0, attemptsToReset = 5;

		generatePath:
		for(int i = 0; i < Integer.MAX_VALUE; i++){

		for (int x = 0; x < xSize; x++) {
			for (int y = 0; y < ySize; y++) {
				if(getNode(x,y) instanceof BaseNode){
					((BaseNode)getNode(x,y)).isPath = false;
					((BaseNode)getNode(x,y)).c = null;
				}
			}
		}

			int sX, sY, g;
			//0 means to start at y
			//1 means to start with x
			if (Game.rand.nextInt(2) == 0) {
				sX = 0;
				sY = Game.rand.nextInt(ySize);
				g = 1;
			} else {
				sX = Game.rand.nextInt(xSize);
				sY = 0;
				g = 2;
			}

			setStartNode(createNode(sX, sY));

			boolean valid = false;
			int minDistance = ((xSize + ySize) / 2);

			while (!valid) {
				int eX = -1, eY = -1;

				if (g == 1) {
					eX = xSize - 1;
					eY = Game.rand.nextInt(ySize);

					if (eY == sY)
						continue;

				} else if (g == 2) {
					eX = Game.rand.nextInt(xSize);
					eY = ySize - 1;

					if (eX == sX)
						continue;
				}

				if (new Vec2d(eX, eY).distance(sX, sY) >= minDistance) {
					valid = true;
					setEndNode(createNode(eX, eY));
					break;
				} else {
					continue;
				}
			}

			try {
				NodeMapPathFinder finder = new NodeMapPathFinder(this, ((xSize + ySize) / 2) * 100, false);
				Path path = finder.findPath(null);

				Color c = RenderUtil.getColorToSlick(new java.awt.Color(255, 215, 109));

				if (path == null || path.steps == null || path.getLength() <= 1) {
					if(genAttempts >= attemptsToReset){
						initMap();
					}else{
						genAttempts += 1;
						continue generatePath;
					}
				}

				if(path != null && path.steps != null && path.steps.size() > 0)
				for (Path.Step step : path.steps) {
					((BaseNode) getNode(step.getX(), step.getY())).isPath = true;

					for (int x = -1; x < 1; x++) {
						for (int y = -1; y < 1; y++) {
							if (getNode(step.getX() + x, step.getY() + y) != null) {
								Node nd = getNode(step.getX() + x, step.getY() + y);

								if (nd instanceof BaseNode) {
									BaseNode bn = (BaseNode) nd;

									if (!bn.isPath) {
										bn.c = c.darker(0.07F);
										bn.value = 100;
									}
								}
							}
						}
					}

					enemyPath = path;
				}

				break;
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	@Override
	public ArrayList<Node> getNodes() {
		ArrayList<Node> lis = new ArrayList<>();

		for(Node[] ee : nodes){
			for(Node e : ee){
				lis.add(e);
			}
		}

		return lis;
	}

	@Override
	public Node getStartNode() {
		return startNode;
	}

	@Override
	public Node getEndNode() {
		return endNode;
	}

	@Override
	public void setStartNode( Node node ) {
		startNode = node;
		setNode(startNode);
	}

	@Override
	public void setEndNode( Node node ) {
		endNode = node;
		setNode(endNode);
	}

	@Override
	public boolean validNode( MovingObject movingObject, int x, int y ) {
		return getNode(x, y) instanceof BaseNode && !((BaseNode)getNode(x,y)).isPath && ((BaseNode)getNode(x,y)).value <= 1;
	}

	@Override
	public Node createNode( int x, int y ) {
		BaseNode nd = new BaseNode(this, x, y);
		return nd;
	}
}
