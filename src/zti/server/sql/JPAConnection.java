package zti.server.sql;

import java.util.List;
import java.util.Map;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import zti.server.data.*;

@ManagedBean(name = "servJPAPostgresqlBean")
@SessionScoped
public class JPAConnection {
	public JPAConnection(String persistenceUnitName) {
		managerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
		entityManager = managerFactory.createEntityManager();
		entityTransaction = entityManager.getTransaction();
	}

	@SuppressWarnings("unchecked")
	public List<Stop> getStopList() {
		List<Stop> stops = null;
		try {
			stops = (List<Stop>) entityManager.createNamedQuery("findAll").getResultList();
		} catch (Exception e) {
			System.out.println("Failed !!! " + e.getMessage());
		}

		return stops;
	}

	private EntityManagerFactory managerFactory; // = Persistence.createEntityManagerFactory(persistenceUnitName);
	private EntityManager entityManager; // = managerFactory.createEntityManager();
	private EntityTransaction entityTransaction;
}
