package Towers;

import Effects.Effect;
import Effects.PoisonEffect;
import Entities.GameEntity;
import Main.Files.CostCaluculator;
import Main.Files.Tower;
import Main.Files.TowerRarity;
import Map.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;

import java.util.Map;

public class PoisonTower extends Tower {
	public PoisonTower(){
		super(null, 0, 0);
	}


	public PoisonTower( World world, int x, int y) {
		super(world, x, y);
	}

	@Override
	public String getTowerName() {
		return "Poison Tower";
	}

	@Override
	public String getTowerDescription() {
		return "Applies a damage overtime poison effect to nearby monsters";
	}

	@Override
	public int GetTowerMaxLevel() {
		return 5;
	}

	@Override
	public boolean canUpgrade() {
		return getTowerLevel() < GetTowerMaxLevel();
	}

	@Override
	public int getTowerCost() {
		return CostCaluculator.getCostFromValue(TowerRarity.RARE, 1F);
	}

	@Override
	public int getTowerRange() {
		return 2 + (getTowerLevel() / 2);
	}

	@Override
	public int getTowerDamage( GameEntity gameEntity ) {
		return 0;
	}

	@Override
	public int getAttackDelay() {
		return 30 - (getTowerLevel() * 2);
	}

	@Override
	public boolean attackAll() {
		return true;
	}

	@Override
	public boolean attackEntity( GameEntity ent) {
		if(!ent.hasEffect(PoisonEffect.class)) {
			ent.applyEffect(new PoisonEffect(getTowerLevel() * 2, getTowerLevel(), 1));
			return true;

		}else{
			int stack = 1, amount = 0;

			for(Map.Entry<Integer, Effect> e : ent.activeEffects.entrySet()){
				if(e.getValue() instanceof PoisonEffect){

					if(((PoisonEffect) e.getValue()).stack > stack){
						stack = ((PoisonEffect) e.getValue()).stack;
					}

					amount += 1;
					ent.removeEffect(e.getKey());
				}
			}

			ent.applyEffect(new PoisonEffect(getTowerLevel() * 2, getTowerLevel(), stack + 1));

			return true;
		}
	}

	@Override
	public void renderTower(Graphics g2, int renderX, int renderY, int sizeX, int sizeY) {
		Rectangle rectangle = new Rectangle(renderX, renderY, sizeX, sizeY);

		g2.setColor(Color.green);
		g2.fill(rectangle);
		g2.setColor(Color.black);
		g2.draw(rectangle);

		//TODO Make Poison tower have a different render then normal one
		Circle circle = new Circle(rectangle.getCenterX(), rectangle.getCenterY(), ((sizeX + sizeY) / 6));

		g2.setColor(Color.green.darker());
		g2.fill(circle);
		g2.setColor(Color.black);
		g2.draw(circle);
	}
}
