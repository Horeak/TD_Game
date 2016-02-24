package Effects;

import EntityFiles.Entity;

public abstract class Effect {
	//The time in seconds the effect will last
	public int time = 0, timeLasted = 0;

	public Effect(int timeToLast){
		time = timeToLast;
	}

	public abstract String getEffectName();
	public abstract void applyToEntity(Entity ent);
	public abstract void update(Entity ent);
	public abstract void finishEffect(Entity ent);


	@Override
	public String toString() {
		return "Effect{" +
				"name=" + getEffectName() +
				", time=" + time +
				", timeLasted=" + timeLasted +
				'}';
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
