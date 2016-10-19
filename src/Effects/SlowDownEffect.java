package Effects;

import Entities.GameEntity;
import EntityFiles.Entity;

public class SlowDownEffect extends Effect {
	public int level, time;
	public float speedPre;

	public SlowDownEffect(int timeToLast, int level) {
		super(timeToLast);

		time = timeToLast;
		this.level = level;
	}

	@Override
	public String getEffectName() {
		return "Slowness";
	}



	@Override
	public void applyToEntity(GameEntity ent) {

		speedPre = ent.getMovementSpeed();
		ent.setMovementSpeed(speedPre - level);

		if(ent.getMovementSpeed() <= 0){
			ent.setMovementSpeed(1);
		}
	}

	@Override
	public void update(GameEntity ent) {

	}

	@Override
	public void finishEffect(GameEntity ent) {
		ent.setMovementSpeed(speedPre);
	}

	@Override
	public boolean allowMultiple() {
		return false;
	}
}
