package common;

import java.util.Timer;
import java.util.TimerTask;

import sprinkler.Sprinkler;

public class SprinklerThread extends Thread {
	private static final String deviceName = "SPRINKLER";
	private Timer autoIrrigationTimer;
	private Timer motionTimer;
	private Sprinkler sprinkler = null;
	private boolean running = true;
	
	public SprinklerThread(Sprinkler sprinkler, String threadName) {
		super(threadName);
		this.sprinkler = sprinkler;
	}
	
	public void run() {
		while (running) {
			//Periodic irrigation every 24h
			if (sprinkler != null && autoIrrigationTimer == null) {
				autoIrrigationTimer = new Timer();
				autoIrrigationTimer.scheduleAtFixedRate(new PeriodicIrrigation(), Log.time.realMinutesToSystemMillis((int) (60*23.5)), Log.time.realMinutesToSystemMillis(60*24));
			}
			
			//Motion sensor irrigation
			if (DeviceStatus.motionStatus == DeviceStatus.MotionStatus.MOTION && (Log.time.getCurrentTime().getTime()/(60*60*1000) % 24) <= 6) {
				motionTimer = new Timer();
				motionTimer.schedule(new MotionIrrigation(), 0);
			}
			
			//Humidity sensor irrigation
			//if ()
			
			try {
				Thread.sleep(200);
				if (!running) break;
			} catch (InterruptedException e) {
	
			}
		} 
	}

	public void stopThread() {
		autoIrrigationTimer.cancel();
		autoIrrigationTimer = null;
		this.running = false;
		sprinkler = null;
	}
	
	private void startSprinkler(String startReason) {
		if (DeviceStatus.sprinklerStatus == DeviceStatus.SprinklerStatus.OFF) {
			DeviceStatus.sprinklerStatus = DeviceStatus.SprinklerStatus.ON;
			
			try {
				//TODO: Fix no such method error?
				//sprinkler.startSprinkler();
			} catch (Exception e) {
				Log.log("Exception occured during service usage: " + e, deviceName);
			}
			
			Log.log(startReason + ": sprinkler started", deviceName);
		} else if (DeviceStatus.sprinklerStatus == DeviceStatus.SprinklerStatus.ON) {
			Log.log(startReason + ": failed to start sprinkler: already on", deviceName);
		} else {
			Log.log(startReason + ": failed to start sprinkler: no sprinkler registered", deviceName);
		}
	}
	
	private void stopSprinkler(String stopReason, int sprinklerTime) {
		if (DeviceStatus.sprinklerStatus == DeviceStatus.SprinklerStatus.ON) {
			DeviceStatus.sprinklerStatus = DeviceStatus.SprinklerStatus.OFF;
			//sprinkler.stopSprinkler();
			Log.log(stopReason + ": sprinkler stopped", deviceName);
			updateHumidity(sprinklerTime);
		} else if (DeviceStatus.sprinklerStatus == DeviceStatus.SprinklerStatus.OFF) {
			Log.log(stopReason + ": failed to stop sprinkler: already off", deviceName);
		} else {
			Log.log(stopReason + ": failed to stop sprinkler: no sprinkler registered", deviceName);
		}
	}
	
	private void updateHumidity(int sprinklerTime) {
		int curHum = DeviceStatus.humidityStatus;
		if (curHum > -1) {
			int randInc = sprinklerTime/3 * (new java.util.Random().nextInt(5) + 1);
			if (curHum + randInc <= 100) {
				DeviceStatus.humidityStatus = curHum + randInc;
			} else {
				DeviceStatus.humidityStatus = 100;
			}
			
			if (curHum < 90 && curHum + randInc > 90) {
				Log.log("Humidity very high", HumidityThread.deviceName);
			} else if (curHum < 80 && curHum + randInc > 80) {
				Log.log("Humidity high", HumidityThread.deviceName);
			}
		}
	}
	
	class PeriodicIrrigation extends TimerTask {
		@Override
		public void run() {
			startSprinkler("Periodic irrigation");
			try {
				Thread.sleep(Log.time.realMinutesToSystemMillis(30));
				stopSprinkler("Periodic irrigation", 30);
			} catch (Exception e) {
				stopSprinkler("Sprinkler was unregistered", 0);
			}
		}
	}
	
	class MotionIrrigation extends TimerTask {
		@Override
		public void run() {
			startSprinkler("Punish the trespassers");
			try {
				Thread.sleep(Log.time.realMinutesToSystemMillis(5));
				stopSprinkler("Punish the trespassers", 5);
			} catch (Exception e) {
				stopSprinkler("Sprinkler was unregistered", 0);
			}
		}
	}
	
	class HumidityIrrigation extends TimerTask {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
	}
}
