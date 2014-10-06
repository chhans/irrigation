package common;

import humidity.HumiditySensore;

public class ServiceUserThread extends Thread { 
	private HumiditySensore humiditySensore = null; 
	private boolean      running = true; 

	public ServiceUserThread(HumiditySensore humiditySensore, String threadName) { 
		super(threadName);
		this.humiditySensore = humiditySensore; 
	}

	public void run() { 
		int humidity = 0; 
		while (running) { 
			try { 
				humidity = humiditySensore.getHumidity(); 
			} catch (RuntimeException e) { 
				System.out.println("RuntimeException occured during service usage: " 
						+ e); 
			} 
			System.out.println(getName() + ": current humidity has value: " 
					+ humidity); 
			try { 
				Thread.sleep(1000); 
			} catch (InterruptedException e) { 
				System.out.println("ServiceUserThread ERROR: " + e); 
			} 
		} 
	}

	public void stopThread() { 
		System.out.println("stopping " + this);
		this.running = false; 
	}

}
