package humidity.impl;

import java.util.Random;

public class HumiditySensoreThread extends Thread {
	final int max = 100;
	final int min = 1;
	Random r = new Random();
	protected int humidity = r.nextInt(100) + 1; 
	boolean running = true;
	
	private void updateHumidity(){
		int rand = r.nextInt(100) + 1;
		if (rand < 50) {
			humidity++;
		}
		else {
			humidity--;
		}
	}

	
	public void stopThread(){
		running = false;
	}
	
	public void run(){
		while(running){
			updateHumidity();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
