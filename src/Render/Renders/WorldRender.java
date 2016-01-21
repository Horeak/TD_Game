package Render.Renders;

import Guis.Interfaces.GuiIngame;
import Main.Game;
import PathFinding.Utils.Node;
import Render.AbstractWindowRender;
import Towers.BaseNode;
import Towers.Turret;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;

public class WorldRender extends AbstractWindowRender {

	public static float renderX = 0;
	public static float renderY = 0;

	public static int mouseX = 0;
	public static int mouseY = 0;

	public static int mX = 0;
	public static int mY = 0;

	public static Turret turretSelected = null;

	@Override
	public void render( Graphics g2 ) {
		g2.setClip(0,0,Game.gameWindowX,Game.gameWindowY);

		//TODO World screen scaling is not working correctly
		renderX = Game.gameWindowX / Game.world.xSize;
		renderY = Game.gameWindowY / Game.world.ySize;

		mouseX = Game.gameContainer.getInput().getMouseX();
		mouseY = Game.gameContainer.getInput().getMouseY();

		mX = (int) (mouseX / renderX);
		mY = (int) (mouseY / renderY);

		for(int x = 0; x < Game.world.xSize; x++){
			for(int y = 0; y < Game.world.ySize; y++){
				Node node = Game.world.getNode(x, y);

				if(node != null && node instanceof BaseNode){
					//selectedTower.renderTower(g2, mouseX - (int) (renderX / 2), mouseY - (int) (renderY / 2), (int) renderX, (int) renderY);
					((BaseNode)node).renderNode(g2, (int)(x * renderX), (int)(y * renderY), (int)renderX, (int)renderY);
				}
			}
		}

		//TODO Range render seems to be offset. (Atleast on mac)
		if(Game.ingame){
			boolean tt = turretSelected == null || turretSelected.x != mX && Game.world.getNode(mX, mY) instanceof Turret || turretSelected.y != mY && Game.world.getNode(mX, mY) instanceof Turret || !(Game.world.getNode(mX, mY) instanceof Turret);
			if(turretSelected != null && (tt)){
				Color g = Color.orange;
				g = new Color(g.getRed(), g.getGreen(), g.getBlue(), 100);

				g2.setColor(g);
				g2.fill(new Circle(((turretSelected.x) * (int)renderX)  + (renderX / 2), ((turretSelected.y) * (int)renderY)  + (renderY / 2), (Game.player.getTowerRange(turretSelected) * ((renderX + renderY) / 2))));

				g2.setColor(g.darker());
				g2.draw(new Circle(((turretSelected.x) * (int)renderX)  + (renderX / 2), ((turretSelected.y) * (int)renderY)  + (renderY / 2), (Game.player.getTowerRange(turretSelected) * ((renderX + renderY) / 2))));
			}

			if(mouseX > 0 && mouseY > 0 && mouseX < Game.gameWindowX && mouseY < Game.gameWindowY) {
				g2.setColor(Color.black);
				g2.draw(new Rectangle(mX * renderX, mY * renderY, renderX, renderY));

				if(Game.world.getNode(mX, mY) instanceof Turret){
					Turret turret = (Turret)Game.world.getNode(mX, mY);

					Color g = Color.white;
					g = new Color(g.getRed(), g.getGreen(), g.getBlue(), 100);

					g2.setColor(g);
					g2.fill(new Circle(((turret.x) * renderX)  + (renderX / 2), ((turret.y) * (int)renderY)  + (renderY / 2), (Game.player.getTowerRange(turret) * ((renderX + renderY) / 2))));

					g2.setColor(g.darker());
					g2.draw(new Circle(((turret.x) * renderX)  + (renderX / 2), ((turret.y) * (int)renderY)  + (renderY / 2), (Game.player.getTowerRange(turret) * ((renderX + renderY) / 2))));
				}

				boolean t = Game.world.validNode(null, mX, mY) || Game.world.getNode(mX, mY) != null && ((BaseNode)Game.world.getNode(mX, mY)).value > 0 && !((BaseNode) Game.world.getNode(mX, mY)).isPath && ((BaseNode) Game.world.getNode(mX, mY)).value != 100;

			if(GuiIngame.selectedTurret != null){
				GuiIngame.selectedTurret.renderTower(g2, mouseX - (int) (renderX / 2), mouseY - (int) (renderY / 2), (int) renderX, (int) renderY);
					Color g = Game.player.canAffordTower(GuiIngame.selectedTurret) && !(Game.world.getNode(mX, mY) instanceof Turret) && t ? Color.green.darker() : Color.red.darker();
					g = new Color(g.getRed(), g.getGreen(), g.getBlue(), 100);

					g2.setColor(g);
					g2.fill(new Circle(mouseX, mouseY, (Game.player.getTowerRange(GuiIngame.selectedTurret) * ((renderX + renderY) / 2))));

					g2.setColor(g.darker());
					g2.draw(new Circle(mouseX, mouseY, (Game.player.getTowerRange(GuiIngame.selectedTurret) * ((renderX + renderY) / 2))));
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
			turretSelected = null;
		}

		boolean t = Game.world.validNode(null, mX, mY) || Game.world.getNode(mX, mY) != null && Game.world.getNode(mX,mY) instanceof BaseNode && ((BaseNode)Game.world.getNode(mX,mY)).value > 0 && !((BaseNode) Game.world.getNode(mX, mY)).isPath && ((BaseNode) Game.world.getNode(mX, mY)).value != 100;

		if(Game.world.getNode(mX,mY) instanceof Turret){
			if(turretSelected != null && turretSelected.x == mX && turretSelected.y == mY || turretSelected == null && !(Game.world.getNode(mX, mY) instanceof Turret)){
				turretSelected = null;
			}else{
				turretSelected = (Turret)Game.world.getNode(mX, mY);
			}
		}

		if(GuiIngame.selectedTurret != null) {
			if (Game.player.canAffordTower(GuiIngame.selectedTurret) && !(Game.world.getNode(mX, mY) instanceof Turret) && t) {
				if (mouseX > 0 && mouseY > 0 && mouseX < Game.gameWindowX && mouseY < Game.gameWindowY) {
					Game.player.money -= Game.player.getCostFromTower(GuiIngame.selectedTurret);
					Game.world.setTower((Turret) GuiIngame.selectedTurret.clone(), mX, mY);
					turretSelected = null;
				}
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
