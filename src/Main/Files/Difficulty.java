package Main.Files;


public enum Difficulty {

	EASY("Easy", 0.5F, 0.5F, 0.5F, 30),
	NORMAL("Normal", 1F, 1F, 1F, 20),
	HARD("Hard", 2F, 2F, 2F, 10);


	public String name;
	public int lives;
	public float costModifer, healthModifier, enemyCountModier;
	Difficulty(String name, float costModifier, float healthModifier, float enemyCountModifer, int lives){
		this.name = name;

		this.lives = lives;

		this.costModifer = costModifier;
		this.healthModifier = healthModifier;
		this.enemyCountModier = enemyCountModifer;
	}
}
