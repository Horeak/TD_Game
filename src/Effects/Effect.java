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
}
