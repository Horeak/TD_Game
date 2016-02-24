package Render.Renders;

import Main.Game;
import PathFinding.Utils.Path;
import Rendering.AbstractWindowRender;
import Towers.BaseNode;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;

public class DirectionRender extends AbstractWindowRender {

	public float x, y;

	@Override
	public void render(Graphics g2) {
		if(Game.world == null || Game.world.enemyPath == null){
			return;
		}

		if(!((BaseNode)Game.world.getNode(Math.round(x), Math.round(y))).isPath){
			x = 0;
			y = 0;
		}

		if(Game.world != null && x == 0 && y == 0){
			x = Game.world.getStartNode().x;
			y = Game.world.getStartNode().y;
		}


		boolean found = false;

		for(Path.Step step : Game.world.enemyPath.steps) {
			if (!found) {
				if (Math.round(x) == step.getX()) {
					if (Math.round(y) == step.getY()) {
						found = true;
						continue;
					}
				}
			} else {
				float xx = step.getX() - x;
				float yy = step.getY() - y;

				x += (xx * 0.08F);
				y += (yy * 0.08F);
				break;
			}
		}

		if(Math.round(x) == Game.world.getEndNode().x && Math.round(y) == Game.world.getEndNode().y){
			x = Game.world.getStartNode().x;
			y = Game.world.getStartNode().y;
		}

		g2.setColor(Color.black);
		g2.draw(new Circle(x * (WorldRender.renderX) + (WorldRender.renderX * 0.5F),y * (WorldRender.renderY) + (WorldRender.renderY * 0.5F), (WorldRender.renderX + WorldRender.renderY) / 8));
	}

	//TODO Only render between rounds
	@Override
	public boolean canRender() {
		return Game.ingame && Game.world != null && (Game.game.player.wave == 0 || Game.gameSpeed == 0);
	}

	@Override
	public boolean canRenderWithWindow() {
		return true;
	}
}
