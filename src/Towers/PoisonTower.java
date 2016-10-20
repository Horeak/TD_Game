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
import org.newdawn.slick.geom.Path;
import org.newdawn.slick.geom.Rectangle;

import java.util.Map;

//TODO Add proper visual and animation
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
		return "Applies a damage overtime poison effect to nearby monsters\nPoison can be stacked from multiple towers";
	}

	@Override
	public int getTowerMaxLevel() {
		return 5;
	}

	@Override
	public boolean canUpgrade() {
		return getTowerLevel() < getTowerMaxLevel();
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
	
	double angle = 0;

	@Override
	public void renderTower(Graphics g2, int renderX, int renderY, int sizeX, int sizeY) {
		Rectangle rectangle = new Rectangle(renderX, renderY, sizeX, sizeY);
		
		GameEntity target = getTarget();
		
		g2.setColor(Color.green);
		g2.fill(rectangle);
		g2.setColor(Color.black);
		g2.draw(rectangle);
		
		g2.pushTransform();
		
		Path Shape = new Path(rectangle.getMinX() + (sizeX * 0.25F), rectangle.getMaxY() - (sizeY * 0.25F));
		
		Shape.lineTo(rectangle.getMinX() + (sizeX * 0.25F), rectangle.getY() + (sizeY * 0.25F));
		Shape.lineTo(rectangle.getMinX() + (sizeX * 0.5F), rectangle.getY() + (sizeY * 0.12F));
		Shape.lineTo(rectangle.getMaxX() - (sizeX * 0.25F), rectangle.getY() + (sizeY * 0.25F));
		Shape.lineTo(rectangle.getMaxX() - (sizeX * 0.25F), rectangle.getMaxY() - (sizeY * 0.25F));
		Shape.close();
		
		
		if(target != null && target.rect != null) {
			angle = Math.atan2(Shape.getCenterX() - target.rect.getCenterX(), Shape.getCenterY() - target.rect.getCenterY());
			angle = (float)Math.toDegrees(angle);
		}
		
		g2.rotate(Shape.getCenterX(), Shape.getCenterY(), -(float)angle);
		
		g2.setColor(Color.green.darker());
		g2.fill(Shape);
		g2.setColor(Color.black);
		g2.draw(Shape);
		
		g2.popTransform();
	}
}
