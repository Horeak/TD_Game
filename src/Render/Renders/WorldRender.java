package Render.Renders;

import Guis.Interfaces.GuiIngame;
import Main.Game;
import PathFinding.Utils.Node;
import Rendering.AbstractWindowRender;
import Towers.BaseNode;
import Towers.Tower;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;

import java.util.ArrayList;

public class WorldRender extends AbstractWindowRender {

	public static float renderX = 0;
	public static float renderY = 0;

	public static int mouseX = 0;
	public static int mouseY = 0;

	public static int mX = 0;
	public static int mY = 0;

	public static Tower towerSelected = null;

	//TODO: Make blocks generate randomised textures instead of color

	@Override
	public void render( Graphics g2 ) {
		g2.setClip(0,0,Game.gameWindowX,Game.gameWindowY);

		renderX = Game.gameWindowX / Game.world.xSize;
		renderY = Game.gameWindowY / Game.world.ySize;

		mouseX = Game.game.gameContainer.getInput().getMouseX();
		mouseY = Game.game.gameContainer.getInput().getMouseY();

		mX = (int) (mouseX / renderX);
		mY = (int) (mouseY / renderY);


		for(int x = 0; x < Game.world.xSize; x++){
			for(int y = 0; y < Game.world.ySize; y++){
				Node node = Game.world.getNode(x, y);

				if(node != null && node instanceof BaseNode){
					((BaseNode)node).renderNode(g2, (int)(x * renderX), (int)(y * renderY), (int)renderX, (int)renderY);

				}
			}
		}

		for(int x = 0; x < Game.world.xSize; x++){
			for(int y = 0; y < Game.world.ySize; y++){
				Tower node = Game.world.getTower(x, y);

				if(node != null && node instanceof Tower){
					((Tower)node).renderTower(g2, (int)(x * renderX), (int)(y * renderY), (int)renderX, (int)renderY);

				}
			}
		}

		if(Game.ingame){
			boolean tt = towerSelected == null || towerSelected.x != mX && Game.world.getTower(mX, mY) != null || towerSelected.y != mY && Game.world.getTower(mX, mY) != null || (Game.world.getTower(mX, mY) == null);
			if(towerSelected != null && (tt)){
				Color g = Color.orange;
				g = new Color(g.getRed(), g.getGreen(), g.getBlue(), 100);

				g2.setColor(g);
				g2.fill(new Circle(((towerSelected.x) * (int)renderX)  + (renderX / 2), ((towerSelected.y) * (int)renderY)  + (renderY / 2), (Game.player.getTowerRange(towerSelected) * ((renderX + renderY) / 2))));

				g2.setColor(g.darker());
				g2.draw(new Circle(((towerSelected.x) * (int)renderX)  + (renderX / 2), ((towerSelected.y) * (int)renderY)  + (renderY / 2), (Game.player.getTowerRange(towerSelected) * ((renderX + renderY) / 2))));
			}

			if(mouseX > 0 && mouseY > 0 && mouseX < Game.gameWindowX && mouseY < Game.gameWindowY) {
				g2.setColor(Color.black);
				g2.draw(new Rectangle(mX * renderX, mY * renderY, renderX, renderY));

				if(Game.world.getTower(mX, mY) != null){
					Tower tower = Game.world.getTower(mX, mY);

					Color g = Color.white;
					g = new Color(g.getRed(), g.getGreen(), g.getBlue(), 100);
					g2.setColor(g);
					g2.fill(new Circle(((tower.x) * renderX)  + (renderX / 2), ((tower.y) * (int)renderY)  + (renderY / 2), (Game.player.getTowerRange(tower) * ((renderX + renderY) / 2))));

					g2.setColor(g.darker());
					g2.draw(new Circle(((tower.x) * renderX)  + (renderX / 2), ((tower.y) * (int)renderY)  + (renderY / 2), (Game.player.getTowerRange(tower) * ((renderX + renderY) / 2))));
				}

				boolean t = Game.world.validNode(null, mX, mY) || Game.world.getNode(mX, mY) != null && ((BaseNode)Game.world.getNode(mX, mY)).getValue() > 0 && !((BaseNode) Game.world.getNode(mX, mY)).isPath && ((BaseNode) Game.world.getNode(mX, mY)).getValue() != 100;

			if(GuiIngame.selectedTower != null){
				GuiIngame.selectedTower.renderTower(g2, mouseX - (int) (renderX / 2), mouseY - (int) (renderY / 2), (int) renderX, (int) renderY);
					Color g = Game.player.canAffordTower(GuiIngame.selectedTower) && (Game.world.getTower(mX, mY) == null) && t ? Color.green.darker() : Color.red.darker();
					g = new Color(g.getRed(), g.getGreen(), g.getBlue(), 100);

					g2.setColor(g);
					g2.fill(new Circle(mouseX, mouseY, (Game.player.getTowerRange(GuiIngame.selectedTower) * ((renderX + renderY) / 2))));

					g2.setColor(g.darker());
					g2.draw(new Circle(mouseX, mouseY, (Game.player.getTowerRange(GuiIngame.selectedTower) * ((renderX + renderY) / 2))));
				}
			}
		}

		g2.setClip(null);
	}

	@Override
	public void mouseClick( int button, int x, int y ) {
		super.mouseClick(button, x, y);

		if(Game.world == null) return;

		if(mouseX > 0 && mouseY > 0 && mouseX < Game.gameWindowX && mouseY < Game.gameWindowY) {
			towerSelected = null;
		}

		boolean t = Game.world.validNode(null, mX, mY) || Game.world.getNode(mX, mY) != null && Game.world.getNode(mX,mY) instanceof BaseNode && ((BaseNode)Game.world.getNode(mX,mY)).getValue() > 0 && !((BaseNode) Game.world.getNode(mX, mY)).isPath && ((BaseNode) Game.world.getNode(mX, mY)).getValue() != 100;

		if(Game.world.getTower(mX,mY) != null){
			if(towerSelected != null && towerSelected.x == mX && towerSelected.y == mY || towerSelected == null && (Game.world.getTower(mX, mY) == null)){
				towerSelected = null;
			}else{
				towerSelected = (Tower)Game.world.getTower(mX, mY);
			}
		}

		if(GuiIngame.selectedTower != null) {
			if (Game.player.canAffordTower(GuiIngame.selectedTower) && (Game.world.getTower(mX, mY) == null) && t) {
				if (mouseX > 0 && mouseY > 0 && mouseX < Game.gameWindowX && mouseY < Game.gameWindowY) {
					Game.player.money -= Game.player.getCostFromTower(GuiIngame.selectedTower);
					Game.world.setTower((Tower) GuiIngame.selectedTower.clone(), mX, mY);
					towerSelected = null;
					GuiIngame.selectedTower = null;
				}
			}else{
				GuiIngame.selectedTower = null;
			}
		}

	}

	@Override
	public boolean canRender() {
		return Game.ingame && Game.world != null;
	}

	@Override
	public boolean canRenderWithWindow() {
		return true;
	}
}
