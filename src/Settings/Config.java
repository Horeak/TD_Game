package Settings;

import Settings.Values.Keybinding;
import org.newdawn.slick.Input;

public class Config {
	public static Keybinding[] keybindings = new Keybinding[]{
			new Keybinding("Pause/Resume game", "pause", Input.KEY_SPACE, "Speed control"),
			new Keybinding("Speed 1x", "speed.1", Input.KEY_1, "Speed control"),
			new Keybinding("Speed 2x", "speed.2", Input.KEY_2, "Speed control"),
			new Keybinding("Speed 3x", "speed.3", Input.KEY_3, "Speed control")};


	public static Keybinding getKeybindFromID( String id ) {
		for (Keybinding keybinding : keybindings) {
			if (keybinding.getId().equals(id)) {
				return keybinding;
			}
		}

		return null;
	}

}
