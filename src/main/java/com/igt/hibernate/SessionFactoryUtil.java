package com.igt.hibernate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class SessionFactoryUtil {

	protected static final Logger log = LogManager.getLogger(SessionFactoryUtil.class);

	private static final SessionFactory sessionFactory = buildSessionFactory();

	private static SessionFactory buildSessionFactory() {
		try {
			// Create the SessionFactory from hibernate.cfg.xml
			Configuration configuration = new Configuration();
			configuration.configure();
			StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().
					applySettings(configuration.getProperties());
			return configuration.buildSessionFactory(builder.build());
		} catch (Throwable ex) {
			// Make sure you log the exception, as it might be swallowed
			log.fatal("Initial SessionFactory creation failed.", ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

}