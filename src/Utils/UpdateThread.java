package Utils;

import Main.Game;

public class UpdateThread extends Thread {

	public void run(){
		super.run();

		while(this.isAlive()){
			try {
				if (Game.ingame && Game.world != null) {
					if(Game.gameSpeed != 0) {
						Game.world.updateGame();
					}
				}
				sleep(10);
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}
}
