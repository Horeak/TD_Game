package Render.Renders;

import Guis.Interfaces.GuiIngame;
import Main.Files.BaseNode;
import Main.Files.Tower;
import Main.Game;
import PathFinding.Utils.Node;
import Rendering.AbstractWindowRender;
import Utilities.FontHandler;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

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
					((Tower)node).renderTowerPostEffects(g2, (int)(x * renderX), (int)(y * renderY), (int)renderX, (int)renderY);
				}
			}
		}
		
		float towerCircleScale = 1.75F;

		//TODO Need to change render of circles to match actual range
		if(Game.ingame){
			boolean tt = towerSelected == null || towerSelected.x != mX && Game.world.getTower(mX, mY) != null || towerSelected.y != mY && Game.world.getTower(mX, mY) != null || (Game.world.getTower(mX, mY) == null);
			if(towerSelected != null && (tt)){
				for(int xX = -Game.player.getTowerRange(towerSelected); xX <= Game.player.getTowerRange(towerSelected); xX++){
					for(int yY = -Game.player.getTowerRange(towerSelected); yY <= Game.player.getTowerRange(towerSelected); yY++){
						if(new Vector2f(xX + towerSelected.x, yY + towerSelected.y).distance(new Vector2f(towerSelected.x, towerSelected.y)) <= Game.player.getTowerRange(towerSelected)){
							Rectangle rectangle = new Rectangle((xX + towerSelected.x) * renderX, (yY + towerSelected.y) * renderY, renderX, renderY);
							
							g2.setColor(FontHandler.getColorToSlick(new java.awt.Color(255, 145, 135, 100)));
							g2.fill(rectangle);
							
							g2.setColor(Color.black);
							g2.draw(rectangle);
						}
					}
				}
			}

			if(mouseX > 0 && mouseY > 0 && mouseX < Game.gameWindowX && mouseY < Game.gameWindowY) {
				g2.setColor(Color.black);
				g2.draw(new Rectangle(mX * renderX, mY * renderY, renderX, renderY));

				if(Game.world.getTower(mX, mY) != null){
					Tower tower = Game.world.getTower(mX, mY);
					
					for(int xX = -Game.player.getTowerRange(tower); xX <= Game.player.getTowerRange(tower); xX++){
						for(int yY = -Game.player.getTowerRange(tower); yY <= Game.player.getTowerRange(tower); yY++){
							if(new Vector2f(xX + tower.x, yY + tower.y).distance(new Vector2f(tower.x, tower.y)) <= Game.player.getTowerRange(tower)){
								Rectangle rectangle = new Rectangle((xX + tower.x) * renderX, (yY + tower.y) * renderY, renderX, renderY);
								
								g2.setColor(FontHandler.getColorToSlick(new java.awt.Color(255, 255, 255, 100)));
								g2.fill(rectangle);
								
								g2.setColor(Color.black);
								g2.draw(rectangle);
							}
						}
					}
				}
				
			if(GuiIngame.selectedTower != null){
				GuiIngame.selectedTower.renderTower(g2, mouseX - (int) (renderX / 2), mouseY - (int) (renderY / 2), (int) renderX, (int) renderY);
				int Range = 3;
				
				if(Game.player.getTowerRange(GuiIngame.selectedTower) >= Range){
					Range = Game.player.getTowerRange(GuiIngame.selectedTower) + 3;
				}
			
				for(int xX = -Range; xX <= Range; xX++){
					for(int yY = -Range; yY <= Range; yY++){
						Rectangle rectangle = new Rectangle((xX + mX) * renderX, (yY + mY) * renderY, renderX, renderY);
						Color g = Game.world.validNode(null, xX + mX, yY + mY) ? Color.green : Color.red;
						
						if(new Vector2f(xX + mX, yY + mY).distance(new Vector2f(mX, mY)) <= Game.player.getTowerRange(GuiIngame.selectedTower)){
							g = Color.blue;
						}
						
						g2.setColor(FontHandler.getColorToSlick(new java.awt.Color(g.getRed(), g.getGreen(), g.getBlue(), 100)));
						g2.fill(rectangle);
						
						g2.setColor(Color.black);
						g2.draw(rectangle);
					}
				}
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
					towerSelected = Game.world.getTower(mX, mY);
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
