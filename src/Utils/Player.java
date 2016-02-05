package Utils;

import Main.Game;
import Towers.Tower;

public class Player {
	public int money = 500; //500 = start
	public int round = 0;

	public int wave = 0, waveMax = 0;

	public int lives;

	public int getCostFromTower( Tower tower ){
		return (int) (tower.getTurretCost() * Game.world.difficulty.costModifer);
	}
	public int getTowerRange( Tower tower ) {
		float f = (((Game.world.xSize + Game.world.ySize) / 2));
		float ff = ((Game.default_x_size + Game.default_y_size) / 2);
		float fff = f / ff;

		return (int)(tower.getTurretRange() * fff);
	}
	public int getTowerDamage( Tower tower ){return tower.getTurretDamage();}

	public boolean canAffordTower( Tower tower ){
		return money >= getCostFromTower(tower);
	}
}
