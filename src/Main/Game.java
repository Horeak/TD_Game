package Main;

import Entities.BossEnemy;
import Entities.GameEntity;
import EntityFiles.Entity;
import GameFiles.BaseGame;
import Guis.Interfaces.MainMenu;
import Map.World;
import Render.Renders.DirectionRender;
import Render.Renders.WorldRender;
import Rendering.AbstractWindowRender;
import Settings.Config;
import Settings.Values.KeybindingAction;
import Utils.Player;
import Utils.TimeTaker;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Game extends BaseGame {
	//TODO Add entity "effects" (like minecraft potion effects) to allow thing like freeze and poison. (have it where it updates every second or so where it has acces to the entity instance and can change gameSpeed, damage...)
	//TODO Loading new guis are taking too long! (Specificly the StartGameMenu and GuiIngame)

	//TODO: Improve rendering times. (It seems to take a long time to change what is rendering and to start rendering)
	//TODO Perhaps make it where the path is not a part of the node storeage and instead draw the path by dynamicly getting it each tick (maybe run it on a seperate thread) so that the path can change if it forexample gets blocked

	public static int gameWindowX = 500;
	public static int gameWindowY = 500;
	public static int xWindowSize = gameWindowX + 100;
	public static int yWindowSize = gameWindowY + 150;
	public static String Title = "TD Game";

	public static boolean ingame = false;
	public static Game game = new Game(Title, xWindowSize, yWindowSize, false);

	public static Random rand = new Random();

	public static int default_x_size  = 50;
	public static int default_y_size  = 50;

	public static World world;
	public static Player player = new Player();

	public static int gameSpeed = 0;

	public Game(String title, int xSize, int ySize, boolean fullscreen) {
		super(title, xSize, ySize, fullscreen);
	}


	//TODO Game starts lagging alot after being run for a long time (Tested until 1h and 30min)(Pherhaps looking at some sort of garbage collection will help?)

	@Override
	public void initGame(GameContainer container) throws SlickException {
		TimeTaker.startTimeTaker("timeStarted");
		setCurrentMenu(new MainMenu());
	}

	public void addKeybindings(){
		keybindingActions.add(new KeybindingAction(getConfig().getKeybindFromID("pause")) {
			@Override
			public void performAction() {
				if(ingame)
				Game.gameSpeed = 0;
			}
		});
		keybindingActions.add(new KeybindingAction(getConfig().getKeybindFromID("speed.1")) {
			@Override
			public void performAction() {
				if(ingame)
				Game.gameSpeed = 1;
			}
		});
		keybindingActions.add(new KeybindingAction(getConfig().getKeybindFromID("speed.2")) {
			@Override
			public void performAction() {
				if(ingame)
				Game.gameSpeed = 2;
			}
		});
		keybindingActions.add(new KeybindingAction(getConfig().getKeybindFromID("speed.3")) {
			@Override
			public void performAction() {
				if(ingame)
				Game.gameSpeed = 3;
			}
		});

		keybindingActions.add(new KeybindingAction(getConfig().getKeybindFromID("spawnEnt")) {
			@Override
			public void performAction() {
				if(ingame)
				Game.world.entities.add(new BossEnemy(Game.world, Game.world.getStartNode().x, Game.world.getStartNode().y));
			}
		});

		keybindingActions.add(new KeybindingAction(getConfig().getKeybindFromID("debugTogg")) {
			@Override
			public void performAction() {
				GameConfig.debugMode ^= true;
			}
		});
	}

	GameConfig cf = new GameConfig();
	@Override
	public Config getConfig() {
		return cf;
	}

	@Override
	public String getTextureLocation() {
		return null;
	}

	@Override
	public String getFilesSaveLocation() {
		return null;
	}


	public AbstractWindowRender[] renders = new AbstractWindowRender[]{new WorldRender(), new DirectionRender()};
	@Override
	public AbstractWindowRender[] getAbstractWindowRenderers() {
		return renders;
	}

	@Override
	public void updateGame(GameContainer container, int delta) throws SlickException {
	}

	@Override
	public void renderGame(GameContainer container, Graphics g2) throws SlickException {
		if(Game.world != null && ingame){
			for(Entity ent : new ArrayList<GameEntity>(Game.world.entities)){
				if(ent != null) {
					ent.renderEntity(g2, 0, 0);
				}
			}
		}
	}

	@Override
	public void loadGame() {

	}

	@Override
	public void closeGame() {

	}

	public static void main( String[] args ) {
		try {
			game.gameContainer.setAlwaysRender(true);
			game.gameContainer.setShowFPS(false);
			game.gameContainer.setVSync(true);

			game.gameContainer.start();
		} catch (SlickException ex) {
			Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
