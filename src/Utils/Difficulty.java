package Utils;

public enum Difficulty {

	EASY("Easy", 0.5F, 0.5F, 0.5F, 30, 2),
	NORMAL("Normal", 1F, 1F, 1F, 20, 1),
	HARD("Hard", 2F, 2F, 2F, 10, 0.5F);


	public String name;
	public int lives;
	public float costModifer, healthModifier, enemyCountModier, sizeMultiplier;
	Difficulty(String name, float costModifier, float healthModifier, float enemyCountModifer, int lives, float sizeMultiplier){
		this.name = name;

		this.lives = lives;

		this.costModifer = costModifier;
		this.healthModifier = healthModifier;
		this.enemyCountModier = enemyCountModifer;
		this.sizeMultiplier = sizeMultiplier;
	}
}
