package common;

import sprinkler.Sprinkler;

public class SprinklerThread extends Thread {
	
	private Sprinkler sprinkler = null;
	private boolean running = true;
	
	public SprinklerThread(Sprinkler sprinkler, String threadName) {
		super(threadName);
		this.sprinkler = sprinkler;
	}
	
	public void run() {
		while (running) {
			//Running sprinkler every 10 seconds for 1 second
			try {
				Thread.sleep(10000);
				if (!running) break;
				periodicIrrigation();
			} catch (InterruptedException e) {
				System.out.println("SprinklerThread ERROR: " + e);
			}
		} 
	}

	public void stopThread() { 
		System.out.println("Stopping automatic irrigation for "+this);
		this.running = false; 
	}
	
	private void periodicIrrigation() {
		this.startSprinkler("Periodic irrigation");
		try {
			Thread.sleep(1000);
			this.stopSprinkler("Periodic irrigation");
		} catch (Exception e) {
			System.out.println("SprinklerThread ERROR: " + e);
		}
	}
	
	private void startSprinkler(String startReason) {
		boolean success = false;
		try {
			success = sprinkler.startSprinkler();
		} catch (Exception e) {
			System.out.println("Exception occured during service usage: " + e);
		}
		
		if (success) {
			System.out.println(startReason + ": sprinkler started");
		} else {
			System.out.println("Sprinkler could not be started");
		}
	}
	
	private void stopSprinkler(String stopReason) {
		boolean success = false;
		try {
			success = sprinkler.stopSprinkler();
		} catch (Exception e) {
			System.out.println("Exception occured during service usage: " + e);
		}
		
		if (success) {
			System.out.println(stopReason + ": sprinkler stopped");
		} else {
			System.out.println("Sprinkler could not be stopped");
		}
	}
}
