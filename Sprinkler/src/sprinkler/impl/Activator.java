package sprinkler.impl; 

import java.util.Hashtable;
import sprinkler.Sprinkler;
import org.osgi.framework.BundleActivator; 
import org.osgi.framework.BundleContext; 
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator { 
	
	public static BundleContext bc = null; 

	public void start(BundleContext bc) throws Exception { 
	    Activator.bc = bc;

	    Sprinkler sprinkler = new SprinklerImpl();
	    ServiceRegistration registration = bc.registerService(Sprinkler.class.getName(), sprinkler, new Hashtable());
	}

	public void stop(BundleContext bc) throws Exception { 
	    Activator.bc = null; 
	}
}