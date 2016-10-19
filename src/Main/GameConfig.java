package Main;

import Guis.Interfaces.GuiIngame;
import Guis.Interfaces.MainMenu;
import Main.Files.Player;
import Settings.Config;
import Settings.Values.ConfigOption;
import Settings.Values.Keybinding;
import Settings.Values.KeybindingAction;
import org.newdawn.slick.Input;

public class GameConfig extends Config {
	public static boolean debugMode = false;
	public static boolean renderDebug = false;

	public static Keybinding[] keybindings = new Keybinding[]{
	 new Keybinding("Pause/Resume game", "pause", Input.KEY_SPACE, "Speed control"){
		 @Override
		 public KeybindingAction getAction() {
			 return new KeybindingAction(this) {
				 @Override
				 public void performAction() {
					 if(Game.ingame)
						 Game.gameSpeed = 0;
				 }
			 };
		 }
	 },
	 new Keybinding("Speed 1x", "speed.1", Input.KEY_1, "Speed control"){
		 @Override
		 public KeybindingAction getAction() {
			 return new KeybindingAction(this) {
				 @Override
				 public void performAction() {
					 if(Game.ingame)
						 Game.gameSpeed = 1;
				 }
			 };
		 }
	 },
	 new Keybinding("Speed 2x", "speed.2", Input.KEY_2, "Speed control"){
		 @Override
		 public KeybindingAction getAction() {
			 return new KeybindingAction(this) {
				 @Override
				 public void performAction() {
					 if(Game.ingame)
						 Game.gameSpeed = 2;
				 }
			 };
		 }
	 },
	 new Keybinding("Speed 3x", "speed.3", Input.KEY_3, "Speed control"){
		 @Override
		 public KeybindingAction getAction() {
			 return new KeybindingAction(this) {
				 @Override
				 public void performAction() {
					 if(Game.ingame)
						 Game.gameSpeed = 3;
				 }
			 };
		 }
	 },

	 new Keybinding("Menu", "menu", Input.KEY_ESCAPE, "Menu"){
		 @Override
		 public KeybindingAction getAction() {
			 return new KeybindingAction(this) {
				 @Override
				 public void performAction() {
					 if(Game.ingame) {
						 GuiIngame.selectedTower = null;
						 Game.gameSpeed = 0;
						
						 Game.ingame = false;
						
						 Game.world = null;
						 Game.player = new Player();
						
						 Game.game.setCurrentMenu(new MainMenu());
					 }
				 }
			 };
		 }
	 },

	 new Keybinding("Debug mode", "debugTogg", Input.KEY_D, "Debug"){
		 @Override
		 public KeybindingAction getAction() {
			 return new KeybindingAction(this) {
				 @Override
				 public void performAction() {
					 if(Game.ingame)
						 GameConfig.debugMode ^= true;
				 }
			 };
		 }
	 },
	 new Keybinding("Debug render", "debugRend", Input.KEY_R, "Debug"){
		 @Override
		 public KeybindingAction getAction() {
			 return new KeybindingAction(this) {
				 @Override
				 public void performAction() {
					 if(Game.ingame)
						 GameConfig.renderDebug ^= true;
				 }
			 };
		 }
	 }};

	public static ConfigOption[] configOptions = new ConfigOption[]{
	};


	@Override
	public Keybinding[] getKeybindings() {
		return keybindings;
	}

	@Override
	public ConfigOption[] getConfigOptions() {
		return configOptions;
	}
}
