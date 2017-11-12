package com.ef.services;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DataBaseConnectionProvider {

	private String persistence_unit = "logAccessDBUnit";

	/**
	 * That method provides entity managers according to persistence.xml
	 * configuration
	 */
	public EntityManager getEntityManager() {
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistence_unit);
		EntityManager entityManager = null;

		entityManager = entityManagerFactory.createEntityManager();

		return entityManager;
	}

	/**
	 * close entity manager
	 */
	public void closeEntityManager(EntityManager entityManager) {
		if (entityManager != null) {
			entityManager.close();
		}
	}
}
