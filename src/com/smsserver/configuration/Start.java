package com.smsserver.configuration;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.smsserver.dao.ScheduledBackupLogs;
import com.smsserver.services.Services;

@WebListener
public class Start implements ServletContextListener {

	private ScheduledExecutorService scheduler;
	
	public void contextInitialized(ServletContextEvent event) {
		Services.loadMobileOriginated();
		Services.loadMobileTerminated();
		GetPropertyValues.loadConfig();




		scheduler = Executors.newScheduledThreadPool(1); 
		scheduler.scheduleAtFixedRate(new ScheduledBackupLogs(), 0, 
				Integer.parseInt(GetPropertyValues.getProperties().getProperty("backupInterval")), TimeUnit.MINUTES);
//		scheduler.scheduleAtFixedRate(new ScheduledSendNewGrades(), calculateDelay(),
//                24*60*60, TimeUnit.SECONDS);
		System.out.println("server started");

	}

	public void contextDestroyed(ServletContextEvent event) {
		scheduler.shutdownNow();
	    ScheduledBackupLogs.backup();
	}
	
	private long calculateDelay(){
        LocalDateTime localNow = LocalDateTime.now();
        ZoneId currentZone = ZoneId.of("Greece/Athens");
        ZonedDateTime zonedNow = ZonedDateTime.of(localNow, currentZone);
        ZonedDateTime zonedNext5 ;
        zonedNext5 = zonedNow.withHour(16).withMinute(0).withSecond(0);//stis 16:00
        if(zonedNow.compareTo(zonedNext5) > 0)
            zonedNext5 = zonedNext5.plusDays(1);

        Duration duration = Duration.between(zonedNow, zonedNext5);
        return  duration.getSeconds();

	}
}