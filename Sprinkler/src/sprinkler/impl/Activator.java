package sprinkler.impl; 

import java.util.Hashtable;
import sprinkler.Sprinkler;
import org.osgi.framework.BundleActivator; 
import org.osgi.framework.BundleContext; 
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator { 
	public static BundleContext bc = null; 

	public void start(BundleContext bc) throws Exception { 
		System.out.println(bc.getBundle().getHeaders().get(Constants.BUNDLE_NAME) + " starting..."); 
	    Activator.bc = bc;

	    Sprinkler sprinkler = new SprinklerImpl();
	    ServiceRegistration registration = bc.registerService(Sprinkler.class.getName(), sprinkler, new Hashtable());
	    System.out.println("Service registered: Sprinkler");
	}

	public void stop(BundleContext bc) throws Exception { 
		System.out.println(bc.getBundle().getHeaders().get(Constants.BUNDLE_NAME) + " stopping..."); 
	    Activator.bc = null; 
	} 
}