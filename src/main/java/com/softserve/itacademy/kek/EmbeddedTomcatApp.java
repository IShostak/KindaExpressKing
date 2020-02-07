package com.softserve.itacademy.kek;

import com.softserve.itacademy.kek.security.SecurityWebApplicationInitializer;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.SpringServletContainerInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Properties;

// TODO: Add logger

public class EmbeddedTomcatApp {
    final Logger logger = LoggerFactory.getLogger(EmbeddedTomcatApp.class);
    private final Tomcat tomcat;

    /**
     * Creates the object to control the embedded Tomcat server.
     * Reads server parameters from the server.properties.
     * If the properties are not defined in the file, uses the following default values:
     * - port = 8080
     * - contextPath = \
     * - appBase = .
     * @throws IOException in case when the properties file is not found
     */
    public EmbeddedTomcatApp() throws IOException {
        logger.info("Reading the server properties file");
        InputStream resourceStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("server.properties");
        Properties properties = new Properties();
        properties.load(resourceStream);
        int port = Integer.parseInt(properties.getProperty("server.port", "8080"));

        logger.info("Configuring embedded tomcat");
        final File base = new File("");
        tomcat = new Tomcat();
        tomcat.setPort(port);
        final Context rootCtx = tomcat.addContext("", base.getAbsolutePath());
        rootCtx.setDocBase(properties.getProperty("doc.base", base.getAbsolutePath()));

        rootCtx.addServletContainerInitializer(new SpringServletContainerInitializer(),
                Collections.singleton(SecurityWebApplicationInitializer.class));

        final AnnotationConfigWebApplicationContext actx = new AnnotationConfigWebApplicationContext();

        actx.scan("com.softserve.itacademy.kek");
        final DispatcherServlet dispatcher = new DispatcherServlet(actx);
        Tomcat.initWebappDefaults(rootCtx);
        Tomcat.addServlet(rootCtx, "SpringMVC", dispatcher);
        rootCtx.addServletMapping("/api/v1/*", "SpringMVC");
    }

    /**
     * Starts the Tomcat server with defined parameters
     */
    public void start() {
        try {
            tomcat.start();
            tomcat.getServer().await();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stops the Tomcat server
     */
    public void stop() {
        try {
            tomcat.stop();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
    }
}
