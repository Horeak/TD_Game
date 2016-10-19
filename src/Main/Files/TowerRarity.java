package Main.Files;

public enum TowerRarity {
	COMMON(100),
	UNCOMMON(200),
	RARE(400),
	VERY_RARE(1000);
	
	
	public int baseCost;
	TowerRarity(int baseCost){
		this.baseCost = baseCost;
	}
}
