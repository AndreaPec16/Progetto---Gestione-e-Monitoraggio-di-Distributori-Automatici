package com.monitoraggio.service;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
public class SchedulerActivator implements ServletContextListener {

    private ScheduledExecutorService scheduler;
    private MonitorService monitorService;

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        monitorService = new MonitorService();
        
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(monitorService::checkDistributorStatus, 10, 60, TimeUnit.SECONDS);
        
        System.out.println(">>> SCHEDULER ATTIVATO: Il controllo dei guasti è automatico.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (scheduler != null) {
            scheduler.shutdown();
        }
        System.out.println(">>> SCHEDULER DISATTIVATO.");
    }
}
