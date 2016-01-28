package Main;

import Entities.Entity;
import Entities.TestEntity;
import Guis.Gui;
import Guis.Interfaces.MainMenu;
import Guis.Menu;
import Map.World;
import Render.AbstractWindowRender;
import Render.Renders.WorldRender;
import Settings.Config;
import Settings.Values.KeybindingAction;
import Utils.Player;
import Utils.Registrations;
import Utils.RenderUtil;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Game extends BasicGame implements InputListener{

//TODO Add entity "effects" (like minecraft potion effects) to allow thing like freeze and poison. (have it where it updates every second or so where it has acces to the entity instance and can change gameSpeed, damage...)
	//TODO Basic tower defence game. (Randomly generated maps? Maybe use the random value A* star path finding to generate the path)

	//TODO Loading new guis are taking too long! (Specificly the StartGameMenu and GuiIngame)

	public static int gameWindowX = 500;
	public static int gameWindowY = 500;
	public static int xWindowSize = gameWindowX + 100;
	public static int yWindowSize = gameWindowY + 100;
	public static String Title = "TD Game";

	public static Menu menu;
	public static boolean ingame = false;

	//TODO Find a smooth resizable method
	public static boolean resizable = false;

	public static AppGameContainer gameContainer;
	public static Game file = new Game(Title);
	public static Random rand = new Random();

	public static int default_x_size  = 50;
	public static int default_y_size  = 50;

	public static World world;
	public static Player player = new Player();

	public static int gameSpeed = 1;

	public Game( String title ) {
		super(title);
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		Display.setResizable(resizable);

		Registrations.registerWindowRenders();
		addKEybindings();

		menu = new MainMenu();

		container.getInput().addMouseListener(new MouseListener() {
			public void mouseWheelMoved( int change ) {
				/**if (menu  == null) {
					if (!hasScrolled) {
						HotbarRender.slotSelected += (change / -120);

						if (HotbarRender.slotSelected > 10) {
							HotbarRender.slotSelected = 1;
						}
						if (HotbarRender.slotSelected <= 0) {
							HotbarRender.slotSelected = 10;
						}

						hasScrolled = true;
					} else {
						hasScrolled = false;
					}
				}
				 **/

				if (menu  != null) {
					menu .onMouseWheelMoved(change);
				}

			}

			public void mouseClicked( int button, int x, int y, int clickCount ) {
			}


			public void mousePressed( int button, int x, int y ) {
				for (AbstractWindowRender render : Registrations.windowRenders) {
					if (menu  == null || render.canRenderWithWindow())
						render.mouseClick(button, x, y);
				}

				if (menu != null) {
					menu.mouseClick(button, x, y);
				}
			}

			public void mouseReleased( int button, int x, int y ) {
			}

			public void mouseMoved( int oldx, int oldy, int newx, int newy ) {
			}

			public void mouseDragged( int oldx, int oldy, int newx, int newy ) {
			}

			public void setInput( Input input ) {
			}

			public boolean isAcceptingInput() {
				return true;
			}

			public void inputEnded() {
			}

			public void inputStarted() {
			}
		});
		
	}


	public static ArrayList<KeybindingAction> keybindingActions = new ArrayList<>();
	public static void addKEybindings(){
		keybindingActions.add(new KeybindingAction(Config.getKeybindFromID("pause")) {
			@Override
			public void performAction() {
				Game.gameSpeed = 0;
			}
		});
		keybindingActions.add(new KeybindingAction(Config.getKeybindFromID("speed.1")) {
			@Override
			public void performAction() {
				Game.gameSpeed = 1;
			}
		});
		keybindingActions.add(new KeybindingAction(Config.getKeybindFromID("speed.2")) {
			@Override
			public void performAction() {
				Game.gameSpeed = 2;
			}
		});
		keybindingActions.add(new KeybindingAction(Config.getKeybindFromID("speed.3")) {
			@Override
			public void performAction() {
				Game.gameSpeed = 3;
			}
		});

		keybindingActions.add(new KeybindingAction(Config.getKeybindFromID("spawnEnt")) {
			@Override
			public void performAction() {
				Game.world.entities.add(new TestEntity(Game.world, Game.world.getStartNode().x, Game.world.getStartNode().y));
			}
		});
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
	}

	@Override
	public void render(GameContainer container, Graphics g2) throws SlickException {
		if (g2.getFont() instanceof AngelCodeFont) {
			g2.setFont(RenderUtil.getFont(new java.awt.Font("Arial", 0, 0)));
		}

		for (AbstractWindowRender render : Registrations.windowRenders) {
			if ((menu instanceof Gui) || menu == null || render.canRenderWithWindow()) {
				if (render.canRender()) {
					render.render(g2);
				}
			}
		}

		if(Game.world != null && ingame){
			for(Entity ent : Game.world.entities){
				if(ent != null)
					ent.renderEntity(g2, WorldRender.renderX, WorldRender.renderY);
			}
		}

		if (menu != null) {
			if (menu.canRender()) {
				menu.render(g2);
				menu.renderObject(g2);

				if (menu instanceof Gui) {
					((Gui) menu).renderPost(g2);
				}
			}
		}


	}


	@Override
	public void keyPressed( int key, char c ) {
		if(ingame){
			for(KeybindingAction ac : keybindingActions){
				if(key == ac.key.getKey()){
					ac.performAction();
					return;
				}
			}
		}

		for (AbstractWindowRender render : Registrations.windowRenders) {
			if ((menu instanceof Gui) || menu == null || render.canRenderWithWindow()) {
				render.keyPressed(key, c);
			}
		}

		if (menu != null) {
			menu .keyPressed(key, c);
		}
	}

	@Override
	public void keyReleased( int key, char c ) {
		for (AbstractWindowRender render : Registrations.windowRenders) {
			if ((menu instanceof Gui) || menu == null || render.canRenderWithWindow()) {
				render.keyReleased(key, c);
			}
		}

		if (menu != null) {
			menu .keyReleased(key, c);
		}
	}

	public static void main( String[] args ) {
		try {

			gameContainer = new AppGameContainer(file);
			gameContainer.setDisplayMode(xWindowSize, yWindowSize, false);

			gameContainer.setAlwaysRender(true);
			gameContainer.setShowFPS(false);
			gameContainer.setVSync(true);

			gameContainer.start();
		} catch (SlickException ex) {
			Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
