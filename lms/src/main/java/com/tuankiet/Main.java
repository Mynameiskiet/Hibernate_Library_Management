package com.tuankiet;

import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tuankiet.config.AppConfig;

public class Main {
    public static void main(String[] args) {
        // Test the application context with Java configuration
        testApplicationWithJavaConfig();

        // Test the application context with XML configuration
        // testApplicationXML();
    }

    private static void testApplicationWithJavaConfig() {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        SessionFactory sessionFactory = (SessionFactory) context.getBean("sessionFactory");
        if (sessionFactory != null) {
            System.out.println("SessionFactory bean is successfully created.");
        } else {
            System.out.println("Failed to create SessionFactory bean.");
        }

        SessionFactory sessionFactory2 = context.getBean(SessionFactory.class);
        if (sessionFactory2 != null) {
            System.out.println("SessionFactory bean retrieved successfully.");
        } else {
            System.out.println("Failed to retrieve SessionFactory bean.");
        }
        ((AnnotationConfigApplicationContext) context).close();
        sessionFactory.close();
    }

    private static void testApplicationXML() {
        ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");
        SessionFactory sessionFactory = (SessionFactory) context.getBean("sessionFactory");
        if (sessionFactory != null) {
            System.out.println("SessionFactory bean is successfully created.");
        } else {
            System.out.println("Failed to create SessionFactory bean.");
        }
        ((ClassPathXmlApplicationContext) context).close();
        sessionFactory.close();
    }
}


