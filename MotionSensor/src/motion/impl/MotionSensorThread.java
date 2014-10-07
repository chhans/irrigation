package motion.impl;

import java.util.Random;

/**
 * 1% chance of movement every 200ms
 */

/* TODO: Fix something probable e.g.
 * Stop thread for a small amount of time, freezing the motion-boolean to true, then start normal again
 */

public class MotionSensorThread extends Thread {
	
	protected boolean motion = false;
	private Random r = new Random();
	private boolean running = true;
	
	public void stopThread(){
		running = false;
	}
	
	public void run(){
		while(running){
			if (r.nextInt() == 1) {
				motion = true;
			} else {
				motion = false;
			}
			
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
