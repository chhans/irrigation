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
				Thread.sleep(Log.time.realMinutesToSystemMillis(60*24));
				if (!running) break;
				periodicIrrigation();
			} catch (InterruptedException e) {
				Log.log("Sprinkler Interrupted " + e);
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
			Log.log("Sprinkler Interrupted " + e);
			this.stopSprinkler("Interrupted");
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
