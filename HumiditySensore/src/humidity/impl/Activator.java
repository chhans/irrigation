package humidity.impl; 
import org.osgi.framework.BundleActivator; 
import org.osgi.framework.BundleContext; 
public class Activator implements BundleActivator { 
	public static BundleContext bc = null; 

	private HumidityThread thread = null; 
	public void start(BundleContext bc) throws Exception { 
		System.out.println("HumiditySensore starting..."); 
		Activator.bc = bc; 
		thread = new HumidityThread(); 
		thread.start(); 
	} 

	public void stop(BundleContext bc) throws Exception { 
		System.out.println("Humidity Sensire stopping..."); 
		thread.stopThread(); 
		thread.join(); 
	} 
}
