package com.tuankiet;

import com.tuankiet.cli.LibraryManagementCLI;
import com.tuankiet.config.ApplicationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
* Main application entry point for Library Management System
* 
* @author congdinh2008
* @version 1.0.0
* @since 1.0.0
*/
public class Application {
  
  private static final Logger logger = LoggerFactory.getLogger(Application.class);
  
  public static void main(String[] args) {
      logger.info("Starting Library Management System...");
      
      AnnotationConfigApplicationContext context = null;
      try {
          // Set system properties for faster startup
          System.setProperty("spring.backgroundpreinitializer.ignore", "true");
          System.setProperty("hibernate.temp.use_jdbc_metadata_defaults", "false");
          
          logger.info("Initializing Spring context...");
          // Initialize Spring context
          context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
          logger.info("Spring context initialized successfully");
          
          // Get CLI bean and start the application
          LibraryManagementCLI cli = context.getBean(LibraryManagementCLI.class);
          logger.info("Starting CLI application...");
          cli.start();
          
          logger.info("Library Management System started successfully");
          
      } catch (Exception e) {
          logger.error("Failed to start Library Management System", e);
          logger.error("Error details: {}", e.getMessage());
          if (e.getCause() != null) {
              logger.error("Root cause: {}", e.getCause().getMessage());
          }
          System.err.println("Application failed to start. Check logs for details.");
          System.exit(1);
      } finally {
          // Close context if it was created
          if (context != null) {
              context.close();
          }
      }
  }
}
