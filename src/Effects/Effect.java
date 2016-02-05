package Effects;

import EntityFiles.Entity;

public abstract class Effect {
	//The time in seconds the effect will last
	public int time = 0;

	public abstract void applyToEntity(Entity ent);
	public abstract void update(Entity ent);
	public abstract void finishEffect(Entity ent);
}
