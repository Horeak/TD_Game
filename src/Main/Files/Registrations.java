package Main.Files;


import Towers.BasicTower;
import Towers.HeavyTower;
import Towers.PoisonTower;
import Towers.SlowTower;


public class Registrations {
	public static Tower[] towers = new Tower[]{
			new BasicTower(),
			new SlowTower(),
			new PoisonTower(),
	        new HeavyTower(),
	};
}
