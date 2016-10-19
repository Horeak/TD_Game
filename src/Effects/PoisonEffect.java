package Effects;

import Entities.GameEntity;
import Main.Game;

public class PoisonEffect extends Effect {
	public int level, stack;

	public PoisonEffect( int timeToLast, int level, int stack) {
		super(timeToLast);
		this.level = level;
		this.stack = stack;
	}

	@Override
	public String getEffectName() {
		return "Poison";
	}



	@Override
	public void applyToEntity(GameEntity ent) {
	}


	int i = 0;

	@Override
	public void update(GameEntity ent) {
		if(i >= 1000) {
			i = 0;
			ent.damage(level * stack, true);
		}else{
			 i += 1;
		}
	}

	@Override
	public void finishEffect(GameEntity ent) {

	}

	@Override
	public boolean allowMultiple() {
		return false;
	}

	@Override
	public String toString() {
		return getEffectName() + " (" +  Game.IntegerToRoman(stack) +  ") : " + ((time - timeLasted) / 1000) + "s";
	}
}
