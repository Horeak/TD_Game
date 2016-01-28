package Guis.Interfaces;

import Entities.Entity;
import Guis.Gui;
import Guis.GuiObject;
import Guis.Menu;
import Main.Game;
import Render.Renders.WorldRender;
import Towers.Tower;
import Utils.Registrations;
import Utils.RenderUtil;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.FontUtils;

import java.awt.*;

public class GuiIngame extends Gui {

	public static Tower selectedTower;

	public GuiIngame(  ) {
		super(false);
	}

	@Override
	public void render( Graphics g2 ) {
		guiObjects.clear();

		guiObjects.add(new upgradeButton(250, Display.getHeight() - 25, 200, 20, this));
		guiObjects.add(new sellButton(250, Display.getHeight() - 50, 200, 20, this));

		for(int i = 0; i < 4; i++)
		guiObjects.add(new speedButton(5 + (i * 25), Display.getHeight() - 25, 20, 20, this, i));

		//TODO Add scrolling or "pages"
		int i = 0;
		for(Tower t : Registrations.towers){
			guiObjects.add(new shopButton(Display.getWidth() - 95, 5 + (i * 42), 90, 42, this, t));
			i += i;
		}

		Rectangle tangle = new Rectangle(Display.getWidth() - 100, 0, 100, Display.getHeight());
		g2.setColor(RenderUtil.getColorToSlick(new java.awt.Color(207, 130, 16)));
		g2.fill(tangle);

		g2.setColor(g2.getColor().darker());
		g2.draw(tangle);


		Rectangle tangle1 = new Rectangle(0, Display.getHeight() - 100, Display.getWidth() - 100, 100);

		g2.setColor(RenderUtil.getColorToSlick(new java.awt.Color(255, 227, 89)));
		g2.fill(tangle1);

		g2.setColor(g2.getColor().darker());
		g2.draw(tangle1);

		g2.setColor(Color.black);
		g2.fill(new Rectangle(5, Display.getHeight() - 95, 100, 30));

		g2.setColor(Color.yellow);
		g2.draw(new Rectangle(5, Display.getHeight() - 95, 100, 30));

		RenderUtil.resizeFont(g2, 16);
		RenderUtil.changeFontStyle(g2, Font.BOLD);
		FontUtils.drawRight(g2.getFont(), "$" + Game.player.money, 0, Display.getHeight() - 89, 100, g2.getColor());
		RenderUtil.resetFont(g2);

		g2.setColor(Color.black);
		RenderUtil.resizeFont(g2, 16);
		RenderUtil.changeFontStyle(g2, Font.BOLD);
		g2.drawString("Round: " + Game.player.round + "\nWave: " + Game.player.wave + "/" + Game.player.waveMax + "\nLives:" + Game.player.lives, 115, Display.getHeight() - 100);
		RenderUtil.resetFont(g2);

		g2.setColor(Color.black);
		RenderUtil.resizeFont(g2, 16);
		RenderUtil.changeFontStyle(g2, Font.BOLD);
		g2.drawString("Game speed: ", 5, Display.getHeight() - 45);
		RenderUtil.resetFont(g2);

		Rectangle tangle2 = new Rectangle(240, Display.getHeight() - 100, 259, 100);

		g2.setColor(Color.darkGray);
		g2.fill(tangle2);

		g2.setColor(Color.darkGray.darker());
		g2.draw(tangle2);

		Tower tower = WorldRender.towerSelected;

		String towerName = tower != null ? tower.getTurretName() : "N/A";

		String towerDamage = tower != null ? Game.player.getTowerDamage(tower) + "" : "N/A";
		String towerRange = tower != null ? Game.player.getTowerRange(tower) + "" : "N/A";

		String towerLefel = tower != null ? tower.getTurretLevel() + "/" + tower.getTurretMaxLevel() : "N/A";

		g2.setColor(Color.black);
		RenderUtil.resizeFont(g2, 16);
		RenderUtil.changeFontStyle(g2, Font.BOLD);
		g2.drawString("Selected: " + towerName, 250, Display.getHeight() - 100);
		RenderUtil.resetFont(g2);

		g2.setColor(Color.black);
		RenderUtil.resizeFont(g2, 11);
		RenderUtil.changeFontStyle(g2, Font.BOLD);
		g2.drawString("Damage: " + towerDamage + "\n" +
				"Range: " + towerRange, 250, Display.getHeight() - 85);

		g2.drawString("Level: " + towerLefel + "\n", 350, Display.getHeight() - 85);
		RenderUtil.resetFont(g2);

		if(Game.world.getNode(WorldRender.mX, WorldRender.mY) instanceof Tower){
			Tower turret = (Tower)Game.world.getNode(WorldRender.mX, WorldRender.mY);
			renderTooltip(WorldRender.mouseX + 5, WorldRender.mouseY + 5, 0,0, new String[]{turret.getTurretName(),"Level: " + turret.getTurretLevel() + "/" + turret.getTurretMaxLevel(), "Damage: " + Game.player.getTowerDamage(turret), "Range: " + Game.player.getTowerRange(turret), "", "Enemies killed: " + turret.getEnemiesKilledByTurret(), "Enemies in sight: " + turret.getEnemiesInSight(), "Attack time: " + turret.delay/100 + "/" + turret.getAttackDelay()});
		}

		for(Entity ent : Game.world.entities){
			if(ent.isMouseOver(WorldRender.mouseX, WorldRender.mouseY, WorldRender.renderX, WorldRender.renderY)){
				renderTooltip(WorldRender.mouseX + 5, WorldRender.mouseY + 5, 0,0, new String[]{ent.getEntityName(), "Health: " + ent.getEntityHealth() + "/" + ent.getEntityMaxHealth()});
			}
		}

	}

	@Override
	public boolean canRender() {
		return Game.ingame;
	}
}

class shopButton extends GuiObject{

	Tower tower;

	public shopButton( int x, int y, int width, int height, Menu menu, Tower tower ) {
		super(x, y, width, height, menu);

		this.tower = tower;
	}

	boolean b = false;

	@Override
	public void onClicked( int button, int x, int y, Menu menu ) {
		if(b) {
			GuiIngame.selectedTower = null;
		}else{
			GuiIngame.selectedTower = tower;
		}
	}

	@Override
	public void renderObject( Graphics g2, Menu menu ) {
		b = GuiIngame.selectedTower != null;

		Color c = isMouseOver() ? GuiIngame.selectedTower == tower ? Color.blue : Game.player.canAffordTower(tower) ? Color.gray : Color.gray.darker() : GuiIngame.selectedTower == tower ? Color.blue.darker().darker() : Game.player.canAffordTower(tower) ? Color.darkGray : Color.darkGray.darker();
		int mouseX = Game.gameContainer.getInput().getMouseX();
		int mouseY = Game.gameContainer.getInput().getMouseY();

		if(isMouseOver()){
			String[] tt = new String[]{ tower.getTurretName(), "Range: " + Game.player.getTowerRange(tower), "Damage: " + Game.player.getTowerDamage(tower), "Cost: " + Game.player.getCostFromTower(tower)};
			((Gui)menu).renderTooltip(x, y + height, width, height, tt);
		}

		g2.setColor(c);
		g2.fill(new Rectangle(x, y, width, height));

		g2.setColor(c.darker());
		g2.draw(new Rectangle(x, y, width, height));

		g2.setClip(x + 1, y + 1, width - 2, height - 2);

		tower.renderTower(g2, x + 5, y + 5, 32, 32);

		g2.setColor(Color.yellow);
		RenderUtil.resizeFont(g2, 11);
		RenderUtil.changeFontStyle(g2, Font.BOLD);
		g2.drawString("$" + Game.player.getCostFromTower(tower), x + 40, y + 4);
		RenderUtil.resetFont(g2);

		g2.setColor(Color.orange);
		RenderUtil.resizeFont(g2, 10);
		RenderUtil.changeFontStyle(g2, Font.BOLD);
		g2.drawString("RN: " + tower.getTurretRange(), x + 40, y + 18);
		g2.drawString("DMG: " + tower.getTurretDamage(), x + 40, y + 26);
		RenderUtil.resetFont(g2);

		g2.setClip(null);
	}
}

class upgradeButton extends GuiObject{

	public upgradeButton( int x, int y, int width, int height, Menu menu ) {
		super(x, y, width, height, menu);
	}

	@Override
	public void onClicked( int button, int x, int y, Menu menu ) {
		if(WorldRender.towerSelected != null)
		if(WorldRender.towerSelected.canUpgrade()){
			if(Game.player.money >= WorldRender.towerSelected.getUpgradeCost()){
				Game.player.money -= WorldRender.towerSelected.getUpgradeCost();
				WorldRender.towerSelected.upgradeTurret();
			}
		}
	}

	@Override
	public void renderObject( Graphics g2, Menu menu ) {
		Rectangle tangle = new Rectangle(x, y, width, height);

		int mouseX = Game.gameContainer.getInput().getMouseX();
		int mouseY = Game.gameContainer.getInput().getMouseY();

		boolean hasUpgrade = WorldRender.towerSelected != null && WorldRender.towerSelected.canUpgrade();
		boolean canAfford = hasUpgrade && Game.player.money >= WorldRender.towerSelected.getUpgradeCost();

		g2.setColor(isMouseOver() && canAfford  ? Color.lightGray : Color.gray);
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
			RenderUtil.resizeFont(g2, 14);
			RenderUtil.changeFontStyle(g2, Font.BOLD);
			g2.drawString(hasUpgrade ? "Upgrade cost: $" + WorldRender.towerSelected.getUpgradeCost() : "", x + 5, y + 3);
			RenderUtil.resetFont(g2);
		}

		g2.setClip(null);
	}
}

class sellButton extends GuiObject{

	public sellButton( int x, int y, int width, int height, Menu menu ) {
		super(x, y, width, height, menu);
	}

	@Override
	public void onClicked( int button, int x, int y, Menu menu ) {
		if(WorldRender.towerSelected != null) {
			Game.player.money += WorldRender.towerSelected.getTurretSellAmount();
			Game.world.setTower(null, WorldRender.towerSelected.x, WorldRender.towerSelected.y);
			WorldRender.towerSelected = null;
			GuiIngame.selectedTower = null;
		}
	}

	@Override
	public void renderObject( Graphics g2, Menu menu ) {
		Rectangle tangle = new Rectangle(x, y, width, height);

		int mouseX = Game.gameContainer.getInput().getMouseX();
		int mouseY = Game.gameContainer.getInput().getMouseY();


		g2.setColor(isMouseOver() ? Color.lightGray : Color.gray);
		g2.fill(tangle);

		g2.setColor(Color.red.darker());
		g2.draw(tangle);

		if(isMouseOver() && WorldRender.towerSelected != null){
			((Gui)menu).renderTooltip(mouseX, mouseY - 20,0,0, new String[]{"Sell tower"});
		}

		g2.setClip(tangle);

		if(WorldRender.towerSelected != null) {
			RenderUtil.resizeFont(g2, 14);
			RenderUtil.changeFontStyle(g2, Font.BOLD);
			g2.drawString("Sell tower for: $" + WorldRender.towerSelected.getTurretSellAmount(), x + 5, y + 3);
			RenderUtil.resetFont(g2);
		}

		g2.setClip(null);
	}
}

class speedButton extends GuiObject{

	int speed;

	public speedButton(int x, int y, int width, int height, Menu menu, int speed) {
		super(x, y, width, height, menu);
		this.speed = speed;
	}

	@Override
	public void onClicked(int button, int x, int y, Menu menu) {
		Game.gameSpeed = speed;
	}

	@Override
	public void renderObject(Graphics g2, Menu menu) {
		Rectangle rectangle = new Rectangle(x, y, width, height);

		g2.setColor(Game.gameSpeed == speed ? Color.yellow.darker() : isMouseOver() ? Color.orange.darker() : Color.orange.darker().darker());
		g2.fill(rectangle);

		g2.setColor(Color.orange.darker().darker());
		g2.draw(rectangle);

		g2.setColor(Color.yellow);

		RenderUtil.resizeFont(g2, 12);
		RenderUtil.changeFontStyle(g2, Font.BOLD);
		g2.drawString(speed == 0 ? "||" : (speed + "x"), x + (speed == 0 ? 6 : 2), y + (speed == 0 ? 2 : 3));
		RenderUtil.resetFont(g2);

	}
}