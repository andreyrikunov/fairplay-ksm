package ru.devinside.drm.fairplay.ksm.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import ru.devinside.drm.fairplay.ksm.TestUtils;

public class FairPlayServer {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        context.setContextPath("/");
        context.setResourceBase(TestUtils.getResourcePath("/server"));
        context.addServlet(CertServlet.class, "/cert");
        context.addServlet(KeyServlet.class, "/ksm");
        context.addServlet(DefaultServlet.class, "/");

        server.setHandler(context);

        server.start();
    }
}