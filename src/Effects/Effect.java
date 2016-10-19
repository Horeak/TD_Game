package Effects;

import Entities.GameEntity;
import EntityFiles.Entity;

public abstract class Effect {
	//The time in seconds the effect will last
	public int time = 0, timeLasted = 0;

	public Effect(int timeToLast){
		time = timeToLast * 1000;
	}

	public abstract String getEffectName();
	public abstract void applyToEntity(GameEntity ent);
	public abstract void update(GameEntity ent);
	public abstract void finishEffect(GameEntity ent);

	public abstract boolean allowMultiple();


	//TODO Add a way to where effects rendering on entities

	@Override
	public String toString() {
		return getEffectName() + ": " + ((time - timeLasted) / 1000) + "s";
	}


	@Override
	public boolean equals( Object o ) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Effect)) {
			return false;
		}

		Effect effect = (Effect) o;

		if (time != effect.time) {
			return false;
		}
		if (timeLasted != effect.timeLasted) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = time;
		result = 31 * result + timeLasted;
		return result;
	}
}
