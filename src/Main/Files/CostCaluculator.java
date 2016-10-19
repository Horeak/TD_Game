package Main.Files;

public class CostCaluculator {
	
	public static int getCostFromValue(TowerRarity rarity, float costMultiplier){
		return (int)(rarity.baseCost * costMultiplier);
	}
}
