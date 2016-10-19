package Guis.Interfaces;

import Entities.GameEntity;
import EntityFiles.Entity;
import Interface.Gui;
import Interface.GuiObject;
import Interface.UIMenu;
import Main.Files.BaseNode;
import Main.Files.Registrations;
import Main.Files.Tower;
import Main.Game;
import Main.GameConfig;
import Render.Renders.WorldRender;
import Utilities.FontHandler;
import Utilities.TimeTaker;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.FontUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class GuiIngame extends Gui {

	public static Tower selectedTower;

	public GuiIngame(  ) {
		super(Game.game.gameContainer, false);
	}
	public boolean overrideKeybindigs(){return false;}
	@Override
	public void render( Graphics g2 ) {
		for(GuiObject ob : guiObjects) ob = null;
		
		guiObjects.clear();
		guiObjects = null;
		guiObjects = new CopyOnWriteArrayList<>();

		guiObjects.add(new upgradeButton(250, Display.getHeight() - 25, 200, 20, this));
		guiObjects.add(new sellButton(250, Display.getHeight() - 50, 200, 20, this));

		for(int i = 0; i < 4; i++)
		guiObjects.add(new speedButton(5 + (i * 25), Display.getHeight() - 25, 20, 20, this, i));

		//TODO Add scrolling or "pages"
		int i = 0;
		for(Tower t : Registrations.towers){
			guiObjects.add(new shopButton(Display.getWidth() - 95, 5 + (i * 52), 90, 42, this, t));
			i += 1;
		}

		Rectangle tangle = new Rectangle(Display.getWidth() - 100, 0, 100, Display.getHeight());
		g2.setColor(FontHandler.getColorToSlick(new java.awt.Color(116, 116, 116)));
		g2.fill(tangle);

		g2.setColor(g2.getColor().darker());
		g2.draw(tangle);


		Rectangle tangle1 = new Rectangle(0, Display.getHeight() - 100, Display.getWidth() - 100, 100);

		g2.setColor(FontHandler.getColorToSlick(new java.awt.Color(151, 151, 151)));
		g2.fill(tangle1);

		g2.setColor(g2.getColor().darker());
		g2.draw(tangle1);

		g2.setColor(Color.black);
		g2.fill(new Rectangle(5, Display.getHeight() - 95, 100, 30));

		g2.setColor(Color.yellow);
		g2.draw(new Rectangle(5, Display.getHeight() - 95, 100, 30));

		FontHandler.resizeFont(g2, 16);
		FontHandler.changeFontStyle(g2, Font.BOLD);
		org.newdawn.slick.util.FontUtils.drawRight(g2.getFont(), "$" + Game.player.money, 0, Display.getHeight() - 89, 100, g2.getColor());
		FontHandler.resetFont(g2);

		g2.setColor(Color.black);
		FontHandler.resizeFont(g2, 16);
		FontHandler.changeFontStyle(g2, Font.BOLD);
		g2.drawString("Round: " + Game.player.round + "\nWave: " + Game.player.wave + "/" + Game.player.waveMax + "\n\n\nLives:" + Game.player.lives, 115, Display.getHeight() - 100);
		FontHandler.resetFont(g2);

		g2.setColor(Color.black);
		FontHandler.resizeFont(g2, 16);
		FontHandler.changeFontStyle(g2, Font.BOLD);
		g2.drawString("Game speed: ", 5, Display.getHeight() - 45);
		FontHandler.resetFont(g2);

		Rectangle tangle2 = new Rectangle(240, Display.getHeight() - 100, 259, 100);

		g2.setColor(Color.darkGray);
		g2.fill(tangle2);

		g2.setColor(Color.darkGray.darker());
		g2.draw(tangle2);

		Tower tower = WorldRender.towerSelected;

		String towerName = tower != null ? tower.getTowerName() : "N/A";

		String towerDamage = tower != null ? Game.player.getTowerDamage(tower) + "" : "N/A";
		String towerRange = tower != null ? Game.player.getTowerRange(tower) + "" : "N/A";

		String towerLefel = tower != null ? tower.getTowerLevel() + "/" + tower.GetTowerMaxLevel() : "N/A";

		g2.setColor(Color.black);
		FontHandler.resizeFont(g2, 16);
		FontHandler.changeFontStyle(g2, Font.BOLD);
		g2.drawString("Selected: " + towerName, 250, Display.getHeight() - 100);
		FontHandler.resetFont(g2);

		g2.setColor(Color.black);
		FontHandler.resizeFont(g2, 11);
		FontHandler.changeFontStyle(g2, Font.BOLD);
		g2.drawString("Damage: " + towerDamage + "\n" +
				"Range: " + towerRange, 250, Display.getHeight() - 85);

		g2.drawString("Level: " + towerLefel + "\n", 350, Display.getHeight() - 85);
		FontHandler.resetFont(g2);


		if(GameConfig.debugMode){
			g2.setColor(Color.black);
			FontHandler.resizeFont(g2, 14);
			FontHandler.changeFontStyle(g2, Font.BOLD);
			g2.drawString("Debug mode", Display.getWidth() - 100, Display.getHeight() - 20);
			FontHandler.resetFont(g2);

			FontHandler.resizeFont(g2, 10);
			g2.drawString("Time: " + TimeTaker.getText("timeStarted", "<days>, <hours>, <mins>, <secs>", true), Display.getWidth() - 100, Display.getHeight() - 30);
			FontHandler.resetFont(g2);

			FontHandler.resizeFont(g2, 10);
			g2.drawString("FPS: " + Game.game.gameContainer.getFPS(), Display.getWidth() - 100, Display.getHeight() - 40);
			FontHandler.resetFont(g2);
		}

		if(Game.world.getTower(WorldRender.mX, WorldRender.mY) != null){
			Tower turret = Game.world.getTower(WorldRender.mX, WorldRender.mY);
			renderTooltip(WorldRender.mouseX + 5, WorldRender.mouseY + 5, 0,0, new String[]{turret.getTowerName(),"Level: " + turret.getTowerLevel() + "/" + turret.GetTowerMaxLevel(), "Damage: " + Game.player.getTowerDamage(turret), "Range: " + Game.player.getTowerRange(turret), "", "Enemies killed: " + turret.getEnemiesKilledByTower(), GameConfig.debugMode ? "Enemies in sight: " + turret.getEnemiesInSight() : null, GameConfig.debugMode ? "Attack time: " + turret.getCurrentDelay()/100 + "/" + turret.getAttackDelay() : null});
		}

		if(GameConfig.renderDebug)
		if(Game.world.getNode(WorldRender.mX, WorldRender.mY) != null){
			BaseNode nd = (BaseNode)Game.world.getNode(WorldRender.mX, WorldRender.mY);
			renderTooltip(WorldRender.mouseX + 5, WorldRender.mouseY + 5, 0,0, new String[]{"Value: " + nd.getValue()});
		}



		if(Game.world.entities != null)
		for(Entity ent : Collections.synchronizedList(Game.world.entities)){
			if(ent == null) continue;

			if(((GameEntity)ent).isMouseOver(WorldRender.mouseX, WorldRender.mouseY, WorldRender.renderX, WorldRender.renderY)){
				renderTooltip(WorldRender.mouseX + 5, WorldRender.mouseY + 5, 0,0, new String[]{ent.getEntityName(), "Health: " + ent.getEntityHealth() + "/" + ent.getEntityMaxHealth(), GameConfig.debugMode ? "Effects: " + ((GameEntity) ent).activeEffects.values().toString() : "", GameConfig.debugMode ? " " : null, GameConfig.debugMode ? "Money Dropped: " + ((GameEntity) ent).getMoneyDropped() : null, GameConfig.debugMode ? "Speed: " + ((GameEntity) ent).getMovementSpeed() : null});
			}
		}

		Rectangle rectangle = new Rectangle(0, Display.getHeight() - 150, Display.getWidth() - 100, 50);

		g2.setColor(Color.lightGray);
		g2.fill(rectangle);

		g2.setClip(new Rectangle(rectangle.getX(), rectangle.getY(), rectangle.getWidth() / 2, rectangle.getHeight()));

		g2.setColor(Color.darkGray);
		FontHandler.resizeFont(g2, 10);
		FontHandler.changeFontStyle(g2, Font.BOLD);

		if(Game.world.startDelay < Game.world.delayToStart){
			g2.drawString("Current wave:      " + ("[" + ((float)((Game.world.startDelay - Game.world.delayToStart) / 1000F)) + "s until first wave]"), 2, Display.getHeight() - 148);
		}else {
			g2.drawString("Current wave:      " + (Game.world.delayNextRound != 0 ? "[" + ((float) ((Game.world.timeToNextRound - Game.world.delayNextRound) / 1000F)) + "s until next wave]" : ""), 2, Display.getHeight() - 148);
		}

		FontHandler.resetFont(g2);

		//TODO Can use Game.player.getEntitiesForRound() to get original list to compare!
		if(Game.world.mobs != null) {
			renderEnts(g2, Game.world.mobs, 5, Display.getHeight() - 135);
		}


		g2.setClip(new Rectangle(rectangle.getX() + (rectangle.getWidth() / 2), rectangle.getY(), rectangle.getWidth() / 2, rectangle.getHeight()));

		g2.setColor(Color.darkGray);
		FontHandler.resizeFont(g2, 10);
		FontHandler.changeFontStyle(g2, Font.BOLD);
		g2.drawString("Next wave: ", rectangle.getX() + (rectangle.getWidth() / 2) + 2, Display.getHeight() - 148);
		FontHandler.resetFont(g2);

		ArrayList<GameEntity> list = Game.player.getEntitiesForRound(Game.player.wave == Game.player.waveMax ? 1 : Game.player.wave + 1, Game.player.wave == Game.player.waveMax ? Game.player.round + 1 : Game.player.round);
		renderEnts(g2, list, (rectangle.getX() + (rectangle.getWidth() / 2) + 5) - (Game.player.getEnemyTypes(list) >= 4 ? offSet : 0), Display.getHeight() - 135);

		if(Game.player.getEnemyTypes(list) >= 4){
			float t = Game.player.getEnemyTypes(list);
			offSetMax = (t - (3.5F)) * 69F;

			if(reverse){
				if(offSet > 0){
					offSet -= change;
				}else{
					reverse = false;
				}
			}else{
				if(offSet >= offSetMax && offSet > 0){
					reverse = true;
				}else{
					offSet += change;
				}
			}
		}

		g2.setClip(null);

		g2.setColor(Color.darkGray);
		g2.drawLine(rectangle.getX() + (rectangle.getWidth() / 2), Display.getHeight() - 150, rectangle.getX() + (rectangle.getWidth() / 2), Display.getHeight() - 100);
		g2.draw(rectangle);
	}

	float offSet = 0, offSetMax = 0, change = 0.5F;
	boolean reverse = false;

	@Override
	public boolean canRender() {
		return Game.ingame;
	}

	public void renderEnts(Graphics g2, ArrayList<GameEntity> entss, float x, float y){
		HashMap<String, Integer> ents = new HashMap<>();
		ArrayList<GameEntity> entts = new ArrayList<>();

		if(entss != null && entss.size() > 0)
		for (GameEntity ent : new ArrayList<GameEntity>(entss)) {
			if(ents.containsKey(ent.getEntityName())){
				ents.put(ent.getEntityName(), ents.get(ent.getEntityName()) + 1);
			}else{
				ents.put(ent.getEntityName(), 1);
				entts.add(ent);
			}
		}

		//TODO Somehow slowly translate it to the left either when one mob is done spawning or as it gets closer
		int iH = 0;
		for (GameEntity ent : entts) {
			if(ent == null || ent == null) continue;

//			Rectangle rectangle1 = new Rectangle(5 + (69 * iH), Display.getHeight() - 135, 64, 32);
			Rectangle rectangle1 = new Rectangle(x + (69 * iH), y, 64, 32);

			if(rectangle1.contains(WorldRender.mouseX, WorldRender.mouseY)){
				if(g2.getClip().contains(WorldRender.mouseX, WorldRender.mouseY)) {
					renderTooltip(WorldRender.mouseX + 5, WorldRender.mouseY + 5, 0, 0, new String[]{ ents.get(ent.getEntityName()) + "x " + ent.getEntityName() });
				}
			}

			g2.setColor(Color.gray);
			g2.fill(rectangle1);

			g2.setColor(Color.darkGray);
			g2.draw(rectangle1);

			g2.setColor(Color.black);
			FontHandler.resizeFont(g2, 12);
			FontUtils.drawRight(g2.getFont(), ents.get(ent.getEntityName()) + "x", (int)(rectangle1.getX()), (int)(rectangle1.getY() + (rectangle1.getHeight() / 4)), 28, g2.getColor());
			FontHandler.resetFont(g2);


			Rectangle temp = new Rectangle(rectangle1.getCenterX(), rectangle1.getY() + 4, 25, 25);

			g2.setColor(Color.lightGray);
			g2.fill(temp);

			g2.setColor(Color.darkGray);
			g2.draw(temp);

			ent.renderEntity(g2, temp.getX(), temp.getY(), temp.getWidth(), temp.getHeight());

			iH += 1;
		}
	}

}

class shopButton extends GuiObject{

	Tower tower;

	public shopButton( int x, int y, int width, int height, UIMenu menu, Tower tower ) {
		super(Game.game,x, y, width, height, menu);

		this.tower = tower;
	}

	boolean b = false;

	@Override
	public void onClicked( int button, int x, int y, UIMenu menu ) {
		if(Game.player.canAffordTower(tower)) {
			if (b) {
				GuiIngame.selectedTower = null;
			} else {
				GuiIngame.selectedTower = tower;
			}
		}else{
			GuiIngame.selectedTower = null;
		}
	}

	@Override
	public void renderObject( Graphics g2, UIMenu menu ) {
		b = GuiIngame.selectedTower != null;

		Color c = isMouseOver() ? GuiIngame.selectedTower == tower ? Color.blue : Game.player.canAffordTower(tower) ? Color.gray : Color.gray.darker() : GuiIngame.selectedTower == tower ? Color.blue.darker().darker() : Game.player.canAffordTower(tower) ? Color.darkGray : Color.darkGray.darker();

		if(isMouseOver()){
			String[] tt = new String[]{ tower.getTowerName(), "Range: " + Game.player.getTowerRange(tower), "Damage: " + Game.player.getTowerDamage(tower), "Cost: " + Game.player.getCostFromTower(tower)};
			((Gui)menu).renderTooltip(x, y + height, width, height, tt);
		}

		g2.setColor(c);
		g2.fill(new Rectangle(x, y, width, height));

		g2.setColor(c.darker());
		g2.draw(new Rectangle(x, y, width, height));

		g2.setClip(x + 1, y + 1, width - 2, height - 2);

		tower.renderTower(g2, x + 5, y + 5, 32, 32);

		g2.setColor(Color.yellow);
		FontHandler.resizeFont(g2, 11);
		FontHandler.changeFontStyle(g2, Font.BOLD);
		g2.drawString("$" + Game.player.getCostFromTower(tower), x + 40, y + 4);
		FontHandler.resetFont(g2);

		g2.setColor(Color.orange);
		FontHandler.resizeFont(g2, 10);
		FontHandler.changeFontStyle(g2, Font.BOLD);
		g2.drawString("RN: " + Game.player.getTowerRange(tower), x + 40, y + 18);
		g2.drawString("DMG: " + Game.player.getTowerDamage(tower), x + 40, y + 26);
		FontHandler.resetFont(g2);

		g2.setClip(null);
	}
}

class upgradeButton extends GuiObject{

	public upgradeButton( int x, int y, int width, int height, UIMenu menu ) {
		super(Game.game,x, y, width, height, menu);
	}

	@Override
	public void onClicked( int button, int x, int y, UIMenu menu ) {
		if(WorldRender.towerSelected != null)
		if(WorldRender.towerSelected.canUpgrade()){
			if(Game.player.money >= Game.player.getUpgradeCostFromTower(WorldRender.towerSelected) || GameConfig.debugMode){

				if(!GameConfig.debugMode) {
					Game.player.money -= Game.player.getUpgradeCostFromTower(WorldRender.towerSelected);
				}

				WorldRender.towerSelected.upgradeTower();
			}
		}
	}

	@Override
	public void renderObject( Graphics g2, UIMenu menu ) {
		Rectangle tangle = new Rectangle(x, y, width, height);

		int mouseX = Game.game.gameContainer.getInput().getMouseX();
		int mouseY = Game.game.gameContainer.getInput().getMouseY();

		boolean hasUpgrade = WorldRender.towerSelected != null && WorldRender.towerSelected.canUpgrade();
		boolean canAfford = hasUpgrade && Game.player.money >= Game.player.getUpgradeCostFromTower(WorldRender.towerSelected);

		g2.setColor(isMouseOver() && canAfford  ? Color.lightGray : canAfford ? Color.gray : Color.darkGray);
		g2.fill(tangle);

		g2.setColor(canAfford ? Color.yellow : Color.red.darker());
		g2.draw(tangle);

		if(isMouseOver()){
			if(hasUpgrade){
				((Gui)menu).renderTooltip(mouseX, mouseY - 20,0,0, new String[]{canAfford ? "Buy upgrade" : "Cant afford"});
			}
		}

		g2.setClip(tangle);

		if(WorldRender.towerSelected != null) {
			FontHandler.resizeFont(g2, 14);
			FontHandler.changeFontStyle(g2, Font.BOLD);
			g2.drawString(hasUpgrade ? "Upgrade cost: $" + Game.player.getUpgradeCostFromTower(WorldRender.towerSelected) : "", x + 5, y + 3);
			FontHandler.resetFont(g2);
		}

		g2.setClip(null);
	}
}

class sellButton extends GuiObject{

	public sellButton( int x, int y, int width, int height, UIMenu menu ) {
		super(Game.game,x, y, width, height, menu);
	}

	@Override
	public void onClicked( int button, int x, int y, UIMenu menu ) {
		if(WorldRender.towerSelected != null) {
			Game.player.money += Game.player.getTowerSellAmount(WorldRender.towerSelected);
			Game.world.setTower(null, WorldRender.towerSelected.x, WorldRender.towerSelected.y);
			WorldRender.towerSelected = null;
			GuiIngame.selectedTower = null;
		}
	}

	@Override
	public void renderObject( Graphics g2, UIMenu menu ) {
		Rectangle tangle = new Rectangle(x, y, width, height);

		int mouseX = Game.game.gameContainer.getInput().getMouseX();
		int mouseY = Game.game.gameContainer.getInput().getMouseY();


		g2.setColor(WorldRender.towerSelected != null ? isMouseOver() ? Color.lightGray : Color.gray : Color.darkGray);
		g2.fill(tangle);

		g2.setColor(Color.red.darker());
		g2.draw(tangle);

		if(isMouseOver() && WorldRender.towerSelected != null){
			((Gui)menu).renderTooltip(mouseX, mouseY - 20,0,0, new String[]{"Sell tower"});
		}

		g2.setClip(tangle);

		if(WorldRender.towerSelected != null) {
			FontHandler.resizeFont(g2, 14);
			FontHandler.changeFontStyle(g2, Font.BOLD);
			g2.drawString("Sell tower for: $" + Game.player.getTowerSellAmount(WorldRender.towerSelected), x + 5, y + 3);
			FontHandler.resetFont(g2);
		}

		g2.setClip(null);
	}
}

class speedButton extends GuiObject{

	int speed;

	public speedButton( int x, int y, int width, int height, UIMenu menu, int speed) {
		super(Game.game,x, y, width, height, menu);
		this.speed = speed;
	}

	@Override
	public void onClicked(int button, int x, int y, UIMenu menu) {
		Game.gameSpeed = speed;
	}

	@Override
	public void renderObject(Graphics g2, UIMenu menu) {
		Rectangle rectangle = new Rectangle(x, y, width, height);

		g2.setColor(Game.gameSpeed == speed ? Color.gray : isMouseOver() ? Color.darkGray : Color.darkGray.darker());
		g2.fill(rectangle);

		g2.setColor(Color.orange.darker().darker());
		g2.draw(rectangle);

		g2.setColor(Color.white);

		FontHandler.resizeFont(g2, 12);
		FontHandler.changeFontStyle(g2, Font.BOLD);
		g2.drawString(speed == 0 ? "||" : (speed + "x"), x + (speed == 0 ? 6 : 2), y + (speed == 0 ? 2 : 3));
		FontHandler.resetFont(g2);

	}
}