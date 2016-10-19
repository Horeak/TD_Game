package Main;

import Entities.GameEntity;
import EntityFiles.Entity;
import GameFiles.BaseGame;
import Guis.Interfaces.MainMenu;
import Main.Files.Player;
import Map.World;
import Render.Renders.DirectionRender;
import Render.Renders.WorldRender;
import Rendering.AbstractWindowRender;
import Settings.Config;
import Utilities.LoggerUtil;
import Utilities.TimeTaker;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.InputListener;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Game extends BaseGame implements InputListener {
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

	//TODO Game is too easy. Once you get to a certain point which you get to fast it is basicly impossible to lose

	//TODO Game starts lagging alot after being run for a long time (Tested until 1h and 30min)(Pherhaps looking at some sort of garbage collection will help?)
	//TODO Perhaps make it where the path is not a part of the node storeage and instead draw the path by dynamicly getting it each tick (maybe run it on a seperate thread) so that the path can change if it forexample gets blocked
	//TODO Try to improve performance. (CHanging array lists seems to have improved it a bit)

	@Override
	public void initGame(GameContainer container) throws SlickException {
		LoggerUtil.activate("TD_Game");
		TimeTaker.startTimeTaker("timeStarted");
		setCurrentMenu(new MainMenu());
	}
	

	public static GameConfig cf = new GameConfig();
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


	public static String IntegerToRoman(int n){
		String roman="";
		int repeat;
		int magnitude[]={1000,900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
		String symbol[]={"M","CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
		repeat=n/1;
		for(int x=0; n>0; x++){
			repeat=n/magnitude[x];
			for(int i=1; i<=repeat; i++){
				roman=roman + symbol[x];
			}
			n=n%magnitude[x];
		}
		return roman;
	}
}
