package com.redmancometh.proxytester.gui;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.vaadin.jetty.VaadinJettyServer;

import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

public class BotMonitor extends UI
{

    private static final long serialVersionUID = -8633210585410721487L;
    ScheduledExecutorService heartbeat = Executors.newSingleThreadScheduledExecutor();

    public void start()
    {
        try
        {
            ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
            contextHandler.setContextPath("/");
            ServletHolder sh = new ServletHolder(new VaadinServlet());
            contextHandler.addServlet(sh, "/*");
            contextHandler.setInitParameter("ui", HelloWorldUI.class.getCanonicalName());
            VaadinJettyServer server = new VaadinJettyServer(5050);
            server.setHandler(contextHandler);
            server.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void init(VaadinRequest request)
    {

    }

}
