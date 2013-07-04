/*
* 
* This file is part of Episteme UI.
* 
* Episteme UI has been developed by members of the research Group on 
* Intelligent Systems [GSI] (Grupo de Sistemas Inteligentes), 
* acknowledged group by the  Technical University of Madrid [UPM] 
* (Universidad Politécnica de Madrid) 
* 
* Contact: 
* http://www.gsi.dit.upm.es/;
* 
* 
* Copyright 2012 UPM-GSI: Group of Intelligent Systems - Universidad Politécnica de Madrid (UPM)
* Licensed under the Apache License, Version 2.0 (the "License"); 
* You may not use this file except in compliance with the License. 
* You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by
* applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific 
* language governing permissions and limitations under the License.
* 
*/

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
