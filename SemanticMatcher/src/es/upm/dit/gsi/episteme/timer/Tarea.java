package es.upm.dit.gsi.episteme.timer;

import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import es.upm.dit.gsi.episteme.server.CompanyMatcher;

public class Tarea extends TimerTask implements ServletContextListener {
    private Timer timer;
    public static String pathFileOffer;
    public static String pathFileEnt;
 
    /* (non-Javadoc)
     * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent evt) {
        // Iniciamos el timer
        timer = new Timer();
        timer.schedule(this, 0, 15*1000);  // Ejemplo: Cada 10 minutos
    }
 
    /* (non-Javadoc)
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent evt) {
        timer.cancel();
    }
    
    /**
     * @return
     */
    public static String getPathFileOffer (){
    	return pathFileOffer;
    }
    
    /**
     * @return
     */
    public static String getPathFileEnt (){
    	return pathFileEnt;
    }
    
    
    /* (non-Javadoc)
     * @see java.util.TimerTask#run()
     */
    public void run() {
    	CompanyMatcher cm = new CompanyMatcher();
    	try {
			cm.refresh();
		} catch (Exception e) {
		}
    }    
}
