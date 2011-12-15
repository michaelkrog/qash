package dk.apaq.shopsystem.service;

/**
 * Associates a given {@link SystemService} with the current execution thread.
 * <p>
 * This class provides a series of static methods. The purpose of the class is to provide a
 * convenient way to get hold of the SystemService for the calling thread.
 * Everything in this class is <code>static</code> to facilitate ease of use in
 * calling code.
 * <p>
 * 
 * @author Michael Krog
 *
 */
public class SystemServiceHolder {
    
    private static ThreadLocal<SystemService> threadLocal = new ThreadLocal<SystemService>();
    
    public static SystemService getSystemService() {
        return threadLocal.get();
    }
    
    public void setSystemService(SystemService service) {
        threadLocal.set(service);
    }
}
