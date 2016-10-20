package Main.Files;

import Entities.*;
import Main.Game;
import Main.GameConfig;

import java.util.ArrayList;

public class Player {
	public int money = 100; //100 = start
	public int round = 0;
	public int wave = 0, waveMax = 0;

	public int lives;

	//TODO Add scaled money drop

	public int getCostFromTower( Tower tower ){
		return (int) (tower.getTowerCost() * Game.world.difficulty.costModifer);
	}
	public int getUpgradeCostFromTower(Tower tower){
		return (int)((getCostFromTower(tower) * 1.75F) * (tower.getTowerLevel() * 1.25F));
	}

	public int getTowerSellAmount(Tower tower){
		return getCostFromTower(tower) + (int)((getCostFromTower(tower) * 0.5F) * (tower.getTowerLevel() - 1));
	}

	//TODO: Tower range seems too small on small map
	public int getTowerRange( Tower tower ) {
		if(tower == null) return 0;
		
		float f = (((Game.world.xSize + Game.world.ySize)));
		float ff = ((Game.default_x_size + Game.default_y_size));
		float fff = f / ff;

		return (int)(tower.getTowerRange() * fff);
	}
	public int getTowerDamage( Tower tower ){return tower.getTowerDamage(tower.getTarget());}
	public boolean canAffordTower( Tower tower ){
		return money >= getCostFromTower(tower) || GameConfig.debugMode;
	}

	public int getHealthScaled(int i){
		return (int)(i * Game.game.world.difficulty.healthModifier) + ((Game.player.round + Game.player.wave) - 2); //TODO: Make higher wave have more enemies or enemies with more health?
	}

	//TODO Make sure every round and wave has enemies
	public ArrayList<GameEntity> getEntitiesForRound(int wave, int round){
		if(Game.world == null || Game.world.getStartNode() == null || Game.world.getEndNode() == null)return  null;

		ArrayList<GameEntity> ent = new ArrayList<>();

		int rounds = ((round - 1) * waveMax) + (wave);

		for(int i = 0; i < rounds; i++){
			ent.add(new TinyMonster(Game.world, Game.world.getStartNode().x, Game.world.getStartNode().y));
		}

		for(int i = 0; i < (rounds / 4); i++){
			ent.add(new MediumMonster(Game.world, Game.world.getStartNode().x, Game.world.getStartNode().y));
		}

		for(int i = 0; i < (rounds / 6); i++){
			ent.add(new LargeMonster(Game.world, Game.world.getStartNode().x, Game.world.getStartNode().y));
		}


		for(int i = 0; i < (rounds / 25); i++){
			ent.add(new SpeedyMonster(Game.world, Game.world.getStartNode().x, Game.world.getStartNode().y));
		}

		for(int i = 0; i < (rounds / 35); i++){
			ent.add(new HeavyMob(Game.world, Game.world.getStartNode().x, Game.world.getStartNode().y));
		}

		if(wave == waveMax){
			for(int i = 0; i < (rounds / 10); i++){
				ent.add(new BossMonster(Game.world, Game.world.getStartNode().x, Game.world.getStartNode().y));
			}
		}

		return ent;
	}

	public int getEnemyTypes(ArrayList<GameEntity> entList){
		ArrayList<String> ents = new ArrayList<>();
		int num = 0;

		if(entList != null)
		for(GameEntity ent : entList){
			if(!ents.contains(ent.getEntityName())){
				num += 1;
				ents.add(ent.getEntityName());
			}
		}



		return num;
	}
}
