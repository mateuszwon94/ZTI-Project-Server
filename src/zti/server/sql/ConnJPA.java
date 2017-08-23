package zti.server.sql;

import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import zti.server.data.Stop;
import zti.server.data.Constants;
import zti.server.data.Line;

public class ConnJPA {
	private EntityManagerFactory managerFactory = Persistence.createEntityManagerFactory("Elephant DB");
	private EntityManager entityManager = managerFactory.createEntityManager();
	private EntityTransaction entityTransaction = entityManager.getTransaction();

	public Map<Integer, Stop> getStopMap() {
		Map<Integer, Stop> stopMap = new LinkedHashMap<Integer, Stop>();
		try {
			for (Stop stop : (List<Stop>) entityManager.createNamedQuery("findAllStops").getResultList())
				stopMap.put(stop.getId(), stop);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stopMap;
	}

	public Map<Integer, Line> getLineMap() {
		Map<Integer, Line> lineMap = new LinkedHashMap<Integer, Line>();
		try {
			for (Line line : (List<Line>) entityManager.createNamedQuery("findAllLines").getResultList())
				lineMap.put(line.getNumber(), line);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lineMap;
	}

	public Stop getStop(Integer stopId) {
		try {
			return (Stop) entityManager.createQuery("SELECT s FROM Stop s WHERE s.id = " + stopId.toString()).getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public Line getLine(Integer lineNumber) {
		try {
			return (Line) entityManager.createQuery("SELECT l FROM Line l WHERE l.number = " + lineNumber.toString()).getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}