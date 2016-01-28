package Utils;

import Render.AbstractWindowRender;
import Render.Renders.DirectionRender;
import Render.Renders.WorldRender;
import Towers.TestTower;
import Towers.Tower;

import java.util.ArrayList;


public class Registrations {

	public static Tower[] towers = new Tower[]{new TestTower()};

	public static ArrayList<AbstractWindowRender> windowRenders = new ArrayList<>();
	public static void registerWindowRenders() {
		windowRenders.add(new WorldRender());
		windowRenders.add(new DirectionRender());
	}
}
