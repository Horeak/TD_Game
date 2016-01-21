package Utils;

import Render.AbstractWindowRender;
import Render.Renders.WorldRender;
import Towers.TestTurret;
import Towers.Turret;

import java.util.ArrayList;


public class Registrations {

	public static Turret[] turrets = new Turret[]{new TestTurret()};

	public static ArrayList<AbstractWindowRender> windowRenders = new ArrayList<>();
	public static void registerWindowRenders() {
		windowRenders.add(new WorldRender());
	}
}
