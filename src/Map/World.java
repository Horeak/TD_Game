package Map;

import Entities.GameEntity;
import Main.Files.BaseNode;
import Main.Files.Difficulty;
import Main.Files.Tower;
import Main.Files.UpdateThread;
import Main.Game;
import NoiseGenerator.PerlinNoiseGenerator;
import PathFinding.Interfaces.MovingObject;
import PathFinding.Interfaces.NodeMap;
import PathFinding.NodeMapPathFinder;
import PathFinding.Utils.Node;
import PathFinding.Utils.Path;
import Projectiles.Projectile;
import Utilities.LoggerUtil;
import Utilities.TimeTaker;
import World.WorldBase;
import com.sun.javafx.geom.Vec2d;
import org.newdawn.slick.geom.Vector2f;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;

public class World  extends WorldBase implements NodeMap{
	public CopyOnWriteArrayList<GameEntity> entities = new CopyOnWriteArrayList<>();

	public CopyOnWriteArrayList<GameEntity> getEntities(){
		return  entities;
	}

	public UpdateThread updateThread = new UpdateThread();

	public Node[][] nodes;
	public Tower[][] towers;

	public int xSize, ySize;

	public Node startNode, endNode;

	public Difficulty difficulty;

	public boolean hasGenerated = false;

	public World( int xSize, int ySize, Difficulty difficulty){
		nodes = new Node[xSize][ySize];
		towers = new Tower[xSize][ySize];

		this.xSize = xSize;
		this.ySize = ySize;

		this.difficulty = difficulty;

		updateThread.start();

		Game.player.money = (int)(difficulty.costModifer * 100);
	}

	public ArrayList<GameEntity> getEntitiesNearTurret( Tower tower ){
		ArrayList<GameEntity> ents = new ArrayList<>();

		try {
			for (GameEntity ent : Collections.synchronizedList(entities)) {
				if(ent != null)
				if (new Vector2f(ent.x, ent.y).distance(new Vector2f(tower.x, tower.y)) <= Game.player.getTowerRange(tower)) {
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
		NodeMapPathFinder finder = new NodeMapPathFinder(this, (xSize * ySize), false);

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
				double tile = (generator.noise((x / res), (y / res)) * 10);
				float t = (float)tile;

				if(getNode(x,y) instanceof BaseNode){
					((BaseNode)getNode(x,y)).setValue(t);
				}
			}
		}

		//TODO: World is still being generated without a path
		SwingUtilities.invokeLater(() -> {
			int genAttempts = 0, attemptsToReset = (xSize + ySize) / 2;

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
//					for(int t : topStart){
//						bottomEnd.removeIf(e -> (finder.findPath(null, t, 0, e, ySize - 1) == null));
//					}

					if(topStart.size() <= 0 || bottomEnd.size() <= 0) continue generatePath;

					int startX = topStart.get(Game.rand.nextInt(topStart.size())), startY = 0;
					int endX = bottomEnd.get(Game.rand.nextInt(bottomEnd.size())), endY = ySize - 1;

					float v1 = ((BaseNode)getNode(startX, startY)).getValue(), v2 = ((BaseNode)getNode(endX, endY)).getValue();

					setStartNode(createNode(startX, startY));
					setEndNode(createNode(endX, endY));

					((BaseNode)getNode(startX, startY)).setValue(v1);
					((BaseNode)getNode(endX, endY)).setValue(v2);
				}else{
//					for(int t : leftStart){
//						rightEnd.removeIf(e -> (finder.findPath(null, 0, t, xSize - 1, e) == null));
//					}

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

					if(enemyPath != null) {
						for (Path.Step step : path.steps) {
							for(int x = -1; x <= 1; x++){
								for(int y = -1; y <= 1; y++){
									if(getNode(x + step.getX(), y + step.getY()) != null) {
										if(Vec2d.distance(step.getX(), step.getY(), x + step.getX(), y + step.getY()) <= 1) {
											getNode(x + step.getX(), y + step.getY()).addValue("openPathNode", true);
										}
									}
								}
							}
						}

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

						hasGenerated = true;
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
		return getNode(x, y) instanceof BaseNode && !((BaseNode)getNode(x,y)).isPath && ((BaseNode)getNode(x,y)).getValue() <= 2.4F && ((BaseNode)getNode(x,y)).getValue() >= -2 && getTower(x, y) == null || getNode(x, y) instanceof BaseNode && getNode(x,y).getValue("openPathNode") != null && getNode(x,y).getValue("openPathNode").equals(true) && !((BaseNode) getNode(x, y)).isPath;
	}

	@Override
	public Node createNode( int x, int y ) {
		BaseNode nd = new BaseNode(this, x, y);
		return nd;
	}

	//delayNextRound=Delay between changing waves/rounds
	//delaySpawn=Delay between each mob being spawned from the current wave spawn
	public int delayNextRound, delaySpawn = 0, startDelay;
	public final int timeToSpawn = 500, timeToNextRound = 1500, delayToStart = 2000;
	public ArrayList<GameEntity> mobs;

	//TODO Longer deleay before new round then before new wave. And a way to detect when waiting for new wave(used for the DirectionRender)

	public synchronized void updateGame(){
		//TODO Add a longer delay then for normal rounds
		if(Game.player.round == 0){
			if(startDelay >= delayToStart) {
				Game.player.round = 1;
				Game.player.wave = 1;
				Game.player.waveMax = 10;

				mobs = Game.player.getEntitiesForRound(Game.player.wave, Game.player.round);
				resetTowerTimers();
			}else{
				startDelay += Game.gameSpeed;
			}
		}


		//TODO Make time between waves shorter and make time between rounds either remain the same or increase it
		//Spawn the mobs from the mobs list and then remove them from the list
		if(mobs != null) {
			if (mobs.size() >= 1) {
					if (delaySpawn >= timeToSpawn) {
						if(mobs.get(0) != null) {
							mobs.get(0).health = mobs.get(0).getEntityMaxHealth();

							entities.add(mobs.get(0));
							mobs.remove(0);
							delaySpawn = 0;

							mobs.trimToSize();
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
					resetTowerTimers();
				} else {
					delayNextRound += Game.gameSpeed;
				}
			} else {
				delayNextRound = 0;
			}


		if(enemyPath == null || enemyPath.steps == null) return;

		for(Tower[] t : towers){
			for(Tower tower : t){
				if(tower == null) continue;
				
				GameEntity entTarget = tower.getTarget();

				//TODO Improve towerAttackDelay (Does it scale properly with gameSpeed?)
				if(tower.getCurrentDelay() >= (tower.getAttackDelay() * (100))){
					if(tower.attackAll()){

						boolean tttt = false;

						for(GameEntity ent : getEntitiesNearTurret(tower)){
							if (tower.attackEntity(ent)) {
								tttt = true;
							}
						}

						if(tttt){
							tower.setCurrentDelay(0);
						}

					}else {
						if (entTarget != null) {
							if(entTarget != null && tower.canAttackEntity(entTarget)) {
								Projectile projectile = new Projectile(this, tower.x, tower.y, entTarget, tower);
								entities.add(projectile);
								tower.setCurrentDelay(0);
							}
						}
					}
				}else{
					tower.setCurrentDelay(tower.getCurrentDelay() + 1 * Game.gameSpeed);
				}
			}
		}


		for(GameEntity ent : entities){
			if(ent == null) continue;
			if(ent instanceof Projectile){
				GameEntity entTarget = ((Projectile)ent).entityTarget;
				
				if(entTarget == null || entTarget.health <= 0 || new Vector2f(ent.x, ent.y).distance(new Vector2f(entTarget.x, entTarget.y)) <= 0.5){
					entities.remove(ent);
					continue;
				}
			}

			for(int i = 0; i < Game.gameSpeed; i++)
			ent.updateEntityBase();

			boolean found = false;

			if(ent.getEntityHealth() <= 0){
				entities.remove(ent);
				continue;
			}

			//TODO Improve. (May cause performance issues) (Try to get the path instance closest to the entity?)
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
				entities.remove(ent);
				Game.player.lives -= ent.getEntityHealth() >= 10 ? ent.getEntityHealth() / 10 : 1;
				loseCheck();

				ent = null;
			}

		}
	}

	//Used to reset the attack delay on towers for each wave
	public void resetTowerTimers(){
		try {
			for (Tower[] t : towers) {
				for (Tower tt : t) {
					tt.setCurrentDelay(tt.getAttackDelay());
				}
			}
		}catch (Exception e){
			//e.printStackTrace();
		}
	}

	public void loseCheck(){
		if(Game.player.lives <= 0){
			//TODO: Add gameover menu
			LoggerUtil.out.log(Level.WARNING, "Game over!");
			Game.player.lives = 0;
		}
	}
}
