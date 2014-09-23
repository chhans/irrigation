package controllerservice.impl;

import java.util.Hashtable;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;

import controllerservice.ControllerService;

public class Activator implements BundleActivator {


	public static BundleContext bc = null; 
	public void start(BundleContext bc) throws Exception { 
		System.out.println(bc.getBundle().getHeaders().get 
				(Constants.BUNDLE_NAME) + " starting..."); 
		Activator.bc = bc; 
		ControllerService service = new ControllerServiceImpl(); 
		ServiceRegistration registration = 
				bc.registerService(ControllerService.class.getName(), 
						service, 
						new Hashtable());
		System.out.println("Service registered: DateServicew"); 
	} 
	public void stop(BundleContext bc) throws Exception { 
		System.out.println(bc.getBundle().getHeaders().get 
				(Constants.BUNDLE_NAME) + " stopping..."); 
		Activator.bc = null; 
	} 
}