package humidity.impl; 
public class HumidityThread extends Thread { 
	private boolean running = true; 
	public HumidityThread() { 
		super("Hello knoplerfish");
	} 
	public void run() { 
		while (running) { 
			System.out.println("Hello knoplerfish"); 
			try { 
				Thread.sleep(2000); 
			} catch (InterruptedException e) { 
				System.out.println("HelloWorldThread ERROR: " + e); 
			} 
		} 
		System.out.println("thread stopped"); 
	}
	public void stopThread() { 
		System.out.println("stopping thread"); 
		this.running = false; 
	} 
} 