package com.titanserver;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.cfg.AnnotationConfiguration;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

public class HibernateUtil {
	private static final SessionFactory sessionFactory = buildSessionFactory();

	private static SessionFactory buildSessionFactory() {
		try {
			File configFile = new File("hibernate.cfg.xml");
			if (!configFile.exists()) {
				System.err.println(configFile.getName() + " not error, error");
			}
			System.out.println(IOUtils.toString(new FileInputStream(configFile)));
			AnnotationConfiguration config = new AnnotationConfiguration().configure(configFile);
			List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
			classLoadersList.add(ClasspathHelper.contextClassLoader());
			classLoadersList.add(ClasspathHelper.staticClassLoader());

			Reflections reflections = new Reflections(new ConfigurationBuilder().setScanners(new SubTypesScanner(false /* don't exclude Object.class */), new ResourcesScanner())
					.setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
					.filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix("com.titanserver.table"))));
			Set<Class<?>> classes = reflections.getSubTypesOf(Object.class);
			Iterator<Class<?>> iterator = classes.iterator();
			while (iterator.hasNext()) {
				config.addAnnotatedClass(iterator.next());
			}

			return config.buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static Session openSession() {
		return getSessionFactory().openSession();
	}

	public static StatelessSession openStatelessSession() {
		return getSessionFactory().openStatelessSession();
	}

	public static void shutdown() {
		getSessionFactory().close();
	}

}