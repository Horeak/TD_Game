package Map;

import EntityFiles.Entity;
import Main.Game;
import NoiseGenerator.PerlinNoiseGenerator;
import PathFinding.Interfaces.MovingObject;
import PathFinding.Interfaces.NodeMap;
import PathFinding.NodeMapPathFinder;
import PathFinding.Utils.Node;
import PathFinding.Utils.Path;
import Towers.BaseNode;
import Towers.Tower;
import Utils.Difficulty;
import Utils.UpdateThread;
import World.WorldBase;
import org.newdawn.slick.geom.Circle;

import javax.swing.*;
import java.util.ArrayList;

public class World  extends WorldBase implements NodeMap{
	public UpdateThread updateThread = new UpdateThread();

	public Node[][] nodes;
	public Tower[][] towers;

	public int xSize, ySize;

	public Node startNode, endNode;

	public Difficulty difficulty;

	public World( int xSize, int ySize, Difficulty difficulty){
		nodes = new Node[xSize][ySize];
		towers = new Tower[xSize][ySize];

		this.xSize = xSize;
		this.ySize = ySize;

		this.difficulty = difficulty;

		updateThread.start();
	}

	public ArrayList<Entity> getEntitiesNearTurret( Tower tower ){
		ArrayList<Entity> ents = new ArrayList<>();

		try {
			for (Entity ent : entities) {
				Circle cir = new Circle(tower.x, tower.y, Game.player.getTowerRange(tower));

				if (cir.contains(ent.x, ent.y)) {
					ents.add(ent);
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}


		return ents;
	}

	public void setTower( Tower Tower, int x, int y){
		if (x >= 0 && y >= 0 && x < xSize && y < ySize){
			if(Tower != null) {
				Tower.x = x;
				Tower.y = y;

				Tower.world = this;
			}

			towers[ x ][ y ] = Tower;
		}
	}

	public void setNode(Node node, int x, int y){
		if (x >= 0 && y >= 0 && x < xSize && y < ySize){
			if(node != null) {
				node.x = x;
				node.y = y;
			}

			nodes[ x ][ y ] = node;
		}
	}

	public Tower getTower( int x, int y ) {
		if (x >= 0 && y >= 0 && x < xSize && y < ySize){
			return towers[x][y];
		}
		return null;
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
			setNode(node, node.x, node.y);
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
		//TODO Improve world decoration
		float res = (((xSize / 2) + (Game.rand.nextInt(xSize / 2)) + ((ySize / 2) + (Game.rand.nextInt(ySize / 2)))) / 2F);

		PerlinNoiseGenerator generator = new PerlinNoiseGenerator(Game.rand.nextLong());

		for (int x = 0; x < xSize; x++) {
			for (int y = 0; y < ySize; y++) {
//				float tile = ((float)SimplexNoise.noise((x / res), (y / res)) * 8) / 2;
				double tile = (generator.noise((x / res), (y / res)) * 8);

				if(getNode(x,y) instanceof BaseNode){
					((BaseNode)getNode(x,y)).value = (float)tile;
				}
			}
		}

		SwingUtilities.invokeLater(() -> {
			int genAttempts = 0, attemptsToReset = 10;

			generatePath:
			for(int i = 0; i < attemptsToReset; i++){

				for (int x = 0; x < xSize; x++) {
					for (int y = 0; y < ySize; y++) {
						if(getNode(x,y) instanceof BaseNode){
							((BaseNode)getNode(x,y)).isPath = false;
						}
					}
				}

				int gen = Game.rand.nextInt(2);

				ArrayList<Integer> topStart = new ArrayList<>();
				ArrayList<Integer> leftStart = new ArrayList<>();

				ArrayList<Integer> bottomEnd = new ArrayList<>();
				ArrayList<Integer> rightEnd = new ArrayList<>();

				for(int x = 0; x < xSize - 1; x++){
					if(validNode(null, x, 0)){
						topStart.add(x);
					}
					if(validNode(null, 0, x)){
						leftStart.add(x);
					}
					if(validNode(null, x, xSize - 1)){
						bottomEnd.add(x);
					}
					if(validNode(null, xSize - 1, x)){
						rightEnd.add(x);
					}
				}

				if(gen == 0){
					setStartNode(createNode(topStart.get(Game.rand.nextInt(topStart.size())), 0));
					setEndNode(createNode(bottomEnd.get(Game.rand.nextInt(bottomEnd.size())), ySize - 1));
				}else{
					setStartNode(createNode(0, leftStart.get(Game.rand.nextInt(leftStart.size()))));
					setEndNode(createNode(xSize - 1, rightEnd.get(Game.rand.nextInt(rightEnd.size()))));
				}

				try {
					NodeMapPathFinder finder = new NodeMapPathFinder(this, (xSize * ySize), false);
					Path path = finder.findPath(null);


					if (path == null || path.steps == null || path.getLength() <= 1) {
						if(genAttempts >= attemptsToReset){
							initMap();
						}else{
							genAttempts += 1;
							continue;
						}
					}

					if(path != null && path.steps != null && path.steps.size() > 0) {
						for (Path.Step step : path.steps) {
							((BaseNode) getNode(step.getX(), step.getY())).isPath = true;
						}
						enemyPath = path;
					}

					if(enemyPath == null){
						throw new Exception("ERROR: path failed to generate!");
					}

					break;
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

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

	public void addedToOpenList(Node node){
		node.addValue("openPathNode", true);
	}
	public void addedToClosedList(Node node){
		node.addValue("closedPathNode", true);
	}


	public void removedFromOpenList(Node node){
		node.removeValue("openPathNode");
	}
	public void removedFromClosedList(Node node){
		node.removeValue("closedPathNode");
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
		return getNode(x, y) instanceof BaseNode && !((BaseNode)getNode(x,y)).isPath && ((BaseNode)getNode(x,y)).value <= 1 && ((BaseNode)getNode(x,y)).value >= -2 && getTower(x, y) == null;
	}

	@Override
	public Node createNode( int x, int y ) {
		BaseNode nd = new BaseNode(this, x, y);
		return nd;
	}

	//TODO Start working on wave system. (Get the amount of enemies, the delay between them, delay between the waves....)
	//TODO When wave system is done start actuly spawning the enemies after about 20-60sec after getting into the game or add somekind of start button. (Perhaps start on gameSpeed 0? and start by selecting another speed)
	public void updateGame(){
		if(enemyPath == null || enemyPath.steps == null) return;

		for(int x = 0; x < xSize; x++){
			for(int y = 0; y < ySize; y++){
				if(getTower(x, y) != null){
					Tower tower = getTower(x,y);

					//TODO Fix towerAttackDelay
					if(tower.delay >= (tower.getAttackDelay() * (100))){
						if(tower.getTarget() != null) {
							tower.delay = 0;
							tower.attackEntity(tower.getTarget());
						}
					}else{
						tower.delay += 1 * Game.gameSpeed;
					}

				}
			}
		}

		ArrayList<Entity> removeEnt = new ArrayList<>(entities);

		//ConcurrentModificationException...
		for(Entity ent : entities){
			boolean found = false;

			if(ent.getEntityHealth() <= 0){
				removeEnt.remove(ent);
				continue;
			}

			for(Path.Step step : enemyPath.steps) {
				if (!found) {
					if (Math.round(ent.x) == step.getX()) {
						if (Math.round(ent.y) == step.getY()) {
							found = true;
							continue;
						}
					}
				} else {
					float xx = step.getX() - ent.x;
					float yy = step.getY() - ent.y;

					//TODO Improve movement amount. It seems to be almost random now.
					ent.x += (xx * 0.08F) / (10 / Game.gameSpeed);
					ent.y += (yy * 0.08F) / (10 / Game.gameSpeed);

					break;
				}
			}

			if(Math.round(ent.x) == Game.world.getEndNode().x && Math.round(ent.y) == Game.world.getEndNode().y){
				removeEnt.remove(ent);
				Game.player.lives -= 1;
				loseCheck();
			}

		}

		entities = removeEnt;
		loseCheck();
	}

	public void loseCheck(){
		if(Game.player.lives <= 0){
			//GameFiles Over!
			System.out.println("Game over!");
			Game.player.lives = 0;
		}
	}
}
