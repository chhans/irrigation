package sprinkler.impl; 
import org.osgi.framework.BundleActivator; 
import org.osgi.framework.BundleContext; 
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import controllerservice.ControllerService;

public class Activator implements BundleActivator { 
	public static BundleContext bc = null; 

	ControllerService controllerService;
	//ServiceUserThread thread;

	public void start(BundleContext bc) throws Exception  { 
		Activator.bc = bc; 
		log("Registering sprinkler...");

		String filter = "(objectclass=" + ControllerService.class.getName() + ")"; 
		bc.addServiceListener(listener, filter); 

		ServiceReference references[] = bc.getServiceReferences((String)null, filter); 
		for (int i = 0; references != null && i < references.length; i++) 
		{ 
			listener.serviceChanged(new ServiceEvent(ServiceEvent.REGISTERED, 
					references[i])); 
		} 
	} 

	public void stop(BundleContext bc) throws Exception { 
		log("Unregistering sprinkler...");
		//thread.stopThread();
		Activator.bc = null; 
	}

	private void log(String message) { 
		System.out.println(Activator.bc.getBundle().getHeaders()
				.get(Constants.BUNDLE_NAME) 
				+ ": " + message); 
	} 

	ServiceListener listener = new ServiceListener() {      
		public void serviceChanged(ServiceEvent event) { 
			switch (event.getType()) { 
			case ServiceEvent.REGISTERED: 
				log("ServiceEvent.REGISTERED"); 
				controllerService = (ControllerService) Activator.bc.getService(event 
						.getServiceReference()); 
				startUsingService(); 
				break; 
			case ServiceEvent.MODIFIED: 
				log("ServiceEvent.MODIFIED received"); 
				stopUsingService(); 
				controllerService = (ControllerService) Activator.bc.getService(event 
						.getServiceReference()); 
				startUsingService(); 
				break; 
			case ServiceEvent.UNREGISTERING: 
				log("ServiceEvent.UNREGISTERING"); 
				stopUsingService(); 
				break; 
			} 
		}
		
		private void stopUsingService() { 
			/*thread.stopThread(); 
			try { 
				thread.join(); 
			} catch (InterruptedException e) { 
				e.printStackTrace(); 
			} */
			controllerService = null; 
		} 

		private void startUsingService() { 
			//thread = new ServiceUserThread(controllerService, "listener example"); 
			//thread.start(); 
		} 

	};


} 
