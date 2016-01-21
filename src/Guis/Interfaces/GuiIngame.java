package Guis.Interfaces;

import Guis.Gui;
import Guis.GuiObject;
import Guis.Menu;
import Main.Game;
import Render.Renders.WorldRender;
import Towers.Turret;
import Utils.Registrations;
import Utils.RenderUtil;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.FontUtils;

import java.awt.*;

public class GuiIngame extends Gui {

	public static Turret selectedTurret = null;

	public GuiIngame(  ) {
		super(false);

		//TODO Add scrolling or "pages"
		int i = 0;
		for(Turret t : Registrations.turrets){
			guiObjects.add(new shopButton(Display.getWidth() - 95, 5 + (i * 42), 90, 42, this, t));
			i += i;
		}
	}

	@Override
	public void render( Graphics g2 ) {
		guiObjects.removeIf(e -> (!(e instanceof shopButton)));

		if(WorldRender.turretSelected != null && WorldRender.turretSelected.canUpgrade())
		guiObjects.add(new upgradeButton(250, Display.getHeight() - 25, 200, 20, this));

		if(WorldRender.turretSelected != null)
		guiObjects.add(new sellButton(250, Display.getHeight() - 50, 200, 20, this));

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
		g2.drawString("Round: " + Game.player.round + "\nWave: " + Game.player.wave + "/" + Game.player.waveMax, 115, Display.getHeight() - 100);
		RenderUtil.resetFont(g2);

		Rectangle tangle2 = new Rectangle(240, Display.getHeight() - 100, 259, 100);

		g2.setColor(Color.darkGray);
		g2.fill(tangle2);

		g2.setColor(Color.darkGray.darker());
		g2.draw(tangle2);

		//TODO Add tooltip text to towers so that you dont have to select a tower
		if(WorldRender.turretSelected != null){
			Turret tower = WorldRender.turretSelected;

			g2.setColor(Color.black);
			RenderUtil.resizeFont(g2, 16);
			RenderUtil.changeFontStyle(g2, Font.BOLD);
			g2.drawString("Selected: " + tower.getTurretName(), 250, Display.getHeight() - 100);
			RenderUtil.resetFont(g2);

			//TODO Add upgrade button
			g2.setColor(Color.black);
			RenderUtil.resizeFont(g2, 11);
			RenderUtil.changeFontStyle(g2, Font.BOLD);
			g2.drawString("Damage: " + Game.player.getTowerDamage(tower) + "\n" +
					"Range: " + Game.player.getTowerRange(tower), 250, Display.getHeight() - 85);

			g2.drawString("Level: " + tower.getTurretLevel() + "/" + tower.getTurretMaxLevel() + "\n", 350, Display.getHeight() - 85);
			RenderUtil.resetFont(g2);
		}

		if(Game.world.getNode(WorldRender.mX, WorldRender.mY) instanceof Turret){
			Turret turret = (Turret)Game.world.getNode(WorldRender.mX, WorldRender.mY);
			renderTooltip(WorldRender.mouseX + 5, WorldRender.mouseY + 5, 0,0, new String[]{turret.getTurretName(),"Level: " + turret.getTurretLevel() + "/" + turret.getTurretMaxLevel(), "Damage: " + Game.player.getTowerDamage(turret), "Range: " + Game.player.getTowerRange(turret), "", "Enemies killed: " + turret.getEnemiesKilledByTurret(), "Enemies in sight: " + turret.getEnemiesInSight()});
		}

	}

	@Override
	public boolean canRender() {
		return Game.ingame;
	}
}

class shopButton extends GuiObject{

	Turret turret;
	boolean selected = false;

	public shopButton( int x, int y, int width, int height, Menu menu, Turret turret) {
		super(x, y, width, height, menu);

		this.turret = turret;
	}

	@Override
	public void onClicked( int button, int x, int y, Menu menu ) {
		selected ^= true;

		if(selected) {
			GuiIngame.selectedTurret = turret;
		}else{
			GuiIngame.selectedTurret = null;
		}
	}

	@Override
	public void renderObject( Graphics g2, Menu menu ) {
		Color c = isMouseOver() ? selected ? Color.blue : Game.player.canAffordTower(turret) ? Color.gray : Color.gray.darker() : selected ? Color.blue.darker().darker() : Game.player.canAffordTower(turret) ? Color.darkGray : Color.darkGray.darker();
		int mouseX = Game.gameContainer.getInput().getMouseX();
		int mouseY = Game.gameContainer.getInput().getMouseY();

		if(isMouseOver()){
			String[] tt = new String[]{turret.getTurretName(), "Range: " + Game.player.getTowerRange(turret), "Damage: " + Game.player.getTowerDamage(turret), "Cost: " + Game.player.getCostFromTower(turret)};
			((Gui)menu).renderTooltip(x, y + height, width, height, tt);
		}

		g2.setColor(c);
		g2.fill(new Rectangle(x, y, width, height));

		g2.setColor(c.darker());
		g2.draw(new Rectangle(x, y, width, height));

		g2.setClip(x + 1, y + 1, width - 2, height - 2);

		turret.renderTower(g2, x + 5, y + 5, 32, 32);

		g2.setColor(Color.yellow);
		RenderUtil.resizeFont(g2, 11);
		RenderUtil.changeFontStyle(g2, Font.BOLD);
		g2.drawString("$" + Game.player.getCostFromTower(turret), x + 40, y + 4);
		RenderUtil.resetFont(g2);

		g2.setColor(Color.orange);
		RenderUtil.resizeFont(g2, 10);
		RenderUtil.changeFontStyle(g2, Font.BOLD);
		g2.drawString("RN: " + turret.getTurretRange(), x + 40, y + 18);
		g2.drawString("DMG: " + turret.getTurretDamage(), x + 40, y + 26);
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
		if(WorldRender.turretSelected != null)
		if(WorldRender.turretSelected.canUpgrade()){
			if(Game.player.money >= WorldRender.turretSelected.getUpgradeCost()){
				Game.player.money -= WorldRender.turretSelected.getUpgradeCost();
				WorldRender.turretSelected.upgradeTurret();
			}
		}
	}

	@Override
	public void renderObject( Graphics g2, Menu menu ) {
		Rectangle tangle = new Rectangle(x, y, width, height);

		int mouseX = Game.gameContainer.getInput().getMouseX();
		int mouseY = Game.gameContainer.getInput().getMouseY();

		boolean hasUpgrade = WorldRender.turretSelected.canUpgrade();
		boolean canAfford = hasUpgrade && Game.player.money >= WorldRender.turretSelected.getUpgradeCost();

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

		RenderUtil.resizeFont(g2, 14);
		RenderUtil.changeFontStyle(g2, Font.BOLD);
		g2.drawString(hasUpgrade ? "Upgrade cost: $" + WorldRender.turretSelected.getUpgradeCost() : "", x + 5, y + 3);
		RenderUtil.resetFont(g2);

		g2.setClip(null);
	}
}

class sellButton extends GuiObject{

	public sellButton( int x, int y, int width, int height, Menu menu ) {
		super(x, y, width, height, menu);
	}

	@Override
	public void onClicked( int button, int x, int y, Menu menu ) {
		if(WorldRender.turretSelected != null) {
			Game.player.money += WorldRender.turretSelected.getTurretSellAmount();
			Game.world.setTower(null, WorldRender.turretSelected.x, WorldRender.turretSelected.y);
			WorldRender.turretSelected = null;
			GuiIngame.selectedTurret = null;
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

		if(isMouseOver()){
			((Gui)menu).renderTooltip(mouseX, mouseY - 20,0,0, new String[]{"Sell turret"});
		}

		g2.setClip(tangle);

		RenderUtil.resizeFont(g2, 14);
		RenderUtil.changeFontStyle(g2, Font.BOLD);
		g2.drawString("Sell turret for: $" + WorldRender.turretSelected.getTurretSellAmount(), x + 5, y + 3);
		RenderUtil.resetFont(g2);

		g2.setClip(null);
	}
}