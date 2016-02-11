package Map;

import Entities.GameEntity;
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
import Utils.LoggerUtil;
import Utils.TimeTaker;
import Utils.UpdateThread;
import World.WorldBase;
import org.newdawn.slick.geom.Circle;

import javax.swing.*;
import java.util.ArrayList;
import java.util.logging.Level;

public class World  extends WorldBase implements NodeMap{
	public ArrayList<GameEntity> entities = new ArrayList<>();

	public UpdateThread updateThread = new UpdateThread();//TODO: Add wave control thread

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

	public ArrayList<GameEntity> getEntitiesNearTurret( Tower tower ){
		ArrayList<GameEntity> ents = new ArrayList<>();

		try {
			for (GameEntity ent : new ArrayList<GameEntity>(entities)) {
				Circle cir = new Circle(tower.x, tower.y, Game.player.getTowerRange(tower));

				if(cir != null && ent != null)
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
		TimeTaker.startTimeTaker("worldGenerate");

		for(int x = 0; x < xSize; x++){
			for(int y = 0; y < ySize; y++){
				setNode(createNode(x, y));
			}
		}
		//TODO: Add random variation to each empty node to make it look better
		float res = (((xSize / 2) + (Game.rand.nextInt(xSize / 2)) + ((ySize / 2) + (Game.rand.nextInt(ySize / 2)))) / 2F);

		PerlinNoiseGenerator generator = new PerlinNoiseGenerator(Game.rand.nextLong());

		for (int x = 0; x < xSize; x++) {
			for (int y = 0; y < ySize; y++) {
//				float tile = ((float)SimplexNoise.noise((x / res), (y / res)) * 8) / 2;
				double tile = (generator.noise((x / res), (y / res)) * 8);

				if(getNode(x,y) instanceof BaseNode){
					((BaseNode)getNode(x,y)).setValue((float)tile);
				}
			}
		}

		//TODO: World is still being generated without a path
		SwingUtilities.invokeLater(() -> {
			int genAttempts = 0, attemptsToReset = 10;//TODO Attempts=worldxsize + worldysize / 2

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
					if(topStart.size() <= 0 || bottomEnd.size() <= 0) continue generatePath;

					int startX = topStart.get(Game.rand.nextInt(topStart.size())), startY = 0;
					int endX = bottomEnd.get(Game.rand.nextInt(bottomEnd.size())), endY = ySize - 1;

					float v1 = ((BaseNode)getNode(startX, startY)).getValue(), v2 = ((BaseNode)getNode(endX, endY)).getValue();

					setStartNode(createNode(startX, startY));
					setEndNode(createNode(endX, endY));

					((BaseNode)getNode(startX, startY)).setValue(v1);
					((BaseNode)getNode(endX, endY)).setValue(v2);
				}else{
					if(leftStart.size() <= 0 || rightEnd.size() <= 0) continue generatePath;

					int startX = 0, startY = leftStart.get(Game.rand.nextInt(leftStart.size()));
					int endX = xSize - 1, endY = rightEnd.get(Game.rand.nextInt(rightEnd.size()));

					float v1 = ((BaseNode)getNode(startX, startY)).getValue(), v2 = ((BaseNode)getNode(endX, endY)).getValue();

					setStartNode(createNode(startX, startY));
					setEndNode(createNode(endX, endY));

					((BaseNode)getNode(startX, startY)).setValue(v1);
					((BaseNode)getNode(endX, endY)).setValue(v2);
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
						System.err.println("Was unable to generate path within limit of " + attemptsToReset + " tries.");

						setStartNode(null);
						setEndNode(null);

						return;
					}

					{

						LoggerUtil.out.log(Level.INFO, "World generation sucsessfull. (" + TimeTaker.getText("worldGenerate", "<days>, <hours>, <mins>, <secs>, <ms>", true) + ")");
						LoggerUtil.out.log(Level.INFO, "World was generated in " + (gen + 1) + " tries.");
						LoggerUtil.out.log(Level.INFO, "World path was: " + enemyPath);
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
		return getNode(x, y) instanceof BaseNode && !((BaseNode)getNode(x,y)).isPath && ((BaseNode)getNode(x,y)).getValue() <= 1 && ((BaseNode)getNode(x,y)).getValue() >= -2 && getTower(x, y) == null;
	}

	@Override
	public Node createNode( int x, int y ) {
		BaseNode nd = new BaseNode(this, x, y);
		return nd;
	}

	//TODO Start working on wave system. (Get the amount of enemies, the delay between them, delay between the waves....)
	//TODO When wave system is done start actuly spawning the enemies after about 20-60sec after getting into the game or add somekind of start button. (Perhaps start on gameSpeed 0? and start by selecting another speed)


	//delayNextRound=Delay between changing waves/rounds
	//delaySpawn=Delay between each mob being spawned from the current wave spawn
	public int delayNextRound, delaySpawn = 0;
	public final int timeToSpawn = 200, timeToNextRound = 1000;
	public ArrayList<GameEntity> mobs;

	public synchronized void updateGame(){
		//TODO Add a longer delay then for normal rounds
		if(Game.player.round == 0){
			Game.player.round = 1;
			Game.player.wave = 1;
			Game.player.waveMax = 10;

			mobs = Game.player.getEntitiesForRound(Game.player.wave, Game.player.round);
		}


		//TODO Make time between waves shorter and make time between rounds either remain the same or increase it
		//Spawn the mobs from the mobs list and then remove them from the list
		if(mobs != null) {
			if (mobs.size() >= 1) {
					if (delaySpawn >= timeToSpawn) {
						if(mobs.get(0) != null) {
							entities.add(mobs.get(0));
							mobs.remove(0);
							delaySpawn = 0;
						}
					} else {
						delaySpawn += Game.gameSpeed;
					}

				}
			}

		//Increase wave/round number when all mobs are dead
			if (mobs != null && mobs.size() <= 0 && entities.size() <= 0) {
				if (delayNextRound >= timeToNextRound) {
					delayNextRound = 0;

					if (Game.player.wave < Game.player.waveMax) {
						Game.player.wave += 1;
					} else {
						Game.player.round += 1;
						Game.player.wave = 1;
					}

					mobs = Game.player.getEntitiesForRound(Game.player.wave, Game.player.round);
				} else {
					delayNextRound += Game.gameSpeed;
				}
			} else {
				delayNextRound = 0;
			}


		if(enemyPath == null || enemyPath.steps == null) return;

		for(int x = 0; x < xSize; x++){
			for(int y = 0; y < ySize; y++){
				if(getTower(x, y) != null){
					Tower tower = getTower(x,y);

					//TODO Fix towerAttackDelay
					if(tower.getCurrentDelay() >= (tower.getAttackDelay() * (100))){
						if(tower.getTarget() != null) {
							tower.setCurrentDelay(0);
							tower.attackEntity(tower.getTarget());
						}
					}else{
						tower.setCurrentDelay(tower.getCurrentDelay() + 1 * Game.gameSpeed);
					}

				}
			}
		}

		ArrayList<GameEntity> removeEnt = new ArrayList<>(entities);

		//ConcurrentModificationException...
		for(GameEntity ent : new ArrayList<>(entities)){
			if(ent == null) continue;

			ent.updateEntityBase();

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

					if(Game.gameSpeed != 0) {
						//TODO Improve movement amount. It seems to be almost random now.
						ent.x += (xx * (ent.getMovementSpeed() / 100)) / (10 / Game.gameSpeed);
						ent.y += (yy * (ent.getMovementSpeed() / 100)) / (10 / Game.gameSpeed);
					}

					break;
				}
			}

			if(Math.round(ent.x) == Game.world.getEndNode().x && Math.round(ent.y) == Game.world.getEndNode().y){
				removeEnt.remove(ent);
				Game.player.lives -= ent.getEntityHealth() >= 10 ? ent.getEntityHealth() / 10 : 1;
				loseCheck();
			}

		}

		entities = removeEnt;
	}

	public void loseCheck(){
		if(Game.player.lives <= 0){
			//TODO: Add gameover menu
			LoggerUtil.out.log(Level.WARNING, "Game over!");
			Game.player.lives = 0;
		}
	}
}
