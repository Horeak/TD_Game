package Main;

import Settings.Config;
import Settings.Values.ConfigOption;
import Settings.Values.Keybinding;
import org.newdawn.slick.Input;

public class GameConfig extends Config {
	public static boolean debugMode = false;

	public static Keybinding[] keybindings = new Keybinding[]{
	 new Keybinding("Pause/Resume game", "pause", Input.KEY_SPACE, "Speed control"),
	 new Keybinding("Speed 1x", "speed.1", Input.KEY_1, "Speed control"),
	 new Keybinding("Speed 2x", "speed.2", Input.KEY_2, "Speed control"),
	 new Keybinding("Speed 3x", "speed.3", Input.KEY_3, "Speed control"),

	 new Keybinding("Spawn entity [DEBUG]", "spawnEnt", Input.KEY_ENTER, "Debug"),
	 new Keybinding("Debug mode", "debugTogg", Input.KEY_D, "Debug")};

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
