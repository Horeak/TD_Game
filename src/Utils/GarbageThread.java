package Utils;

import Main.Game;

public class GarbageThread extends Thread {

	public void run(){
		super.run();

		while(this.isAlive()){
			try {
				sleep(60000 * 5); //Clear every 5 min
				System.gc();
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}
}
