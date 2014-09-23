package humidity.impl; 

import java.util.Date;
import controllerservice.ControllerService; 

public class ServiceUserThread extends Thread { 
	private ControllerService  controllerService = null; 
	private boolean running = true; 

	public ServiceUserThread(ControllerService service, String threadName) { 
		super(threadName);
		this.controllerService = service; 
	}

	public void run() { 
		String formattedDate = null; 

		while (running) { 
			Date date = new Date(); 
			try { 
				formattedDate = controllerService.getFormattedDate(date); 
			} catch (RuntimeException e) { 
				System.out.println("RuntimeException occured during service usage: " 
						+ e); 
			} 
			System.out.println(getName() + ": converted date has value: " 
					+ formattedDate); 
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