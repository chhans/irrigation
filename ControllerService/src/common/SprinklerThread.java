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
			//If no sensors are available, run every 24h for 1/2 hour.
			try {
				Thread.sleep(Log.time.realMinutesToSystemMillis((int) (60*23.5)));
				if (!running) break;
				periodicIrrigation();
			} catch (InterruptedException e) {
				//Knopflerfish will fix it
			}
		} 
	}

	public void stopThread() {
		this.running = false; 
	}
	
	private void periodicIrrigation() {
		this.startSprinkler("Periodic irrigation");
		try {
			Thread.sleep(Log.time.realMinutesToSystemMillis(30));
			this.stopSprinkler("Periodic irrigation");
		} catch (Exception e) {
			this.stopSprinkler("Sprinkler was unregistered");
		}
	}
	
	private void startSprinkler(String startReason) {
		boolean success = false;
		try {
			success = sprinkler.startSprinkler();
		} catch (Exception e) {
			Log.log("Exception occured during service usage: " + e);
		}
		
		if (success) {
			Log.log(startReason + ": sprinkler started");
		} else {
			Log.log("Sprinkler could not be started (it may already be on)");
		}
	}
	
	private void stopSprinkler(String stopReason) {
		boolean success = false;
		try {
			success = sprinkler.stopSprinkler();
		} catch (Exception e) {
			Log.log("Exception occured during service usage: " + e);
		}
		
		if (success) {
			Log.log(stopReason + ": sprinkler stopped");
		} else {
			Log.log("Sprinkler could not be stopped (it may already be off)");
		}
	}
}
