package humidity.impl; 
import java.util.Hashtable;

import humidity.HumiditySensore;

import org.osgi.framework.BundleActivator; 
import org.osgi.framework.BundleContext; 
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;


public class Activator implements BundleActivator { 

	  public static BundleContext bc = null; 

	  public void start(BundleContext bc) throws Exception { 
	    System.out.println(bc.getBundle().getHeaders().get 
	                       (Constants.BUNDLE_NAME) + " starting..."); 
	    Activator.bc = bc; 

	    HumiditySensore sensore = new HumiditySensoreImpl(); 
	    ServiceRegistration registration = 
	      bc.registerService(HumiditySensore.class.getName(), 
	                         sensore, 
	                         new Hashtable());  
	    System.out.println("Service registered: HumiditySensore"); 
	    
	  } 
	  public void stop(BundleContext bc) throws Exception { 
	    System.out.println(bc.getBundle().getHeaders().get 
	                       (Constants.BUNDLE_NAME) + " stopping..."); 
	    Activator.bc = null; 
	  } 


} 
