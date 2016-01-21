package Utils;

import Main.Game;
import Towers.Turret;

public class Player {
	public int money = 500; //500 = start
	public int round = 0;

	public int wave = 0, waveMax = 0;

	public int getCostFromTower( Turret turret){
		return (int) (turret.getTurretCost() * Game.world.difficulty.costModifer);
	}
	public int getTowerRange(Turret turret) {return (int)(turret.getTurretRange() * (Game.world.difficulty.sizeMultiplier));}
	public int getTowerDamage(Turret turret){return turret.getTurretDamage();}

	public boolean canAffordTower( Turret turret){
		return money >= getCostFromTower(turret);
	}
}
