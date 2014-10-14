package common;

import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * 1d = 4.8s
 * 1h = 0.2s
 * Thread updates time each half hour (every 0.1s)
 */

public class TimeThread extends Thread {
	private Date currentTime;
	private boolean running = true;
	private static final long systemTimeRatio = 18000;
	
	public TimeThread(String threadName) {
		super(threadName);
		if (currentTime == null) {
			currentTime = new Date();
			currentTime.setTime(currentTime.getTime() - (currentTime.getTime() % (4800 * systemTimeRatio)));
		}
	}
	
	public void run() {
		
		while (running) {
			try {
				Thread.sleep(100);
				if (!running) break;
				currentTime.setTime(currentTime.getTime() + 100*systemTimeRatio);
			} catch (InterruptedException e) {
				System.out.println("TimeThread ERROR: " + e);
			}
		} 
	}

	public void stopThread() {
		this.running = false; 
	}
	
	public Date getCurrentTime() {
		return this.currentTime;
	}
	
	public String getFormattedTime() {
		return new SimpleDateFormat("dd-MM-yy HH:mm").format(this.currentTime);
	}
	
	public long realMinutesToSystemMillis(int min) {
		return (min*60*1000)/systemTimeRatio; 
	}
}
