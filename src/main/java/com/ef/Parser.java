package com.ef;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.ef.dao.AccessDAO;
import com.ef.dao.AccessDAOImpl;
import com.ef.dao.BlockedIPDAO;
import com.ef.dao.BlockedIPDAOImpl;
import com.ef.domain.Access;
import com.ef.domain.BlockedIP;
import com.ef.services.DataBaseConnectionProvider;
import com.ef.utils.CommandLineInputParser;
import com.ef.utils.DateBuilder;

public class Parser {

	private static CommandLineInputParser commandLineInputParser;

	public static void main(String[] args) {

		DataBaseConnectionProvider dataBaseConnectionProvider = null;
		EntityManager entityManager = null;
		commandLineInputParser = new CommandLineInputParser(args);

		/* Assign arguments */
		String inputFile = commandLineInputParser.getAccesslog();
		String startDate = commandLineInputParser.getStartDate();
		String duration = commandLineInputParser.getDuration();
		int threshold = commandLineInputParser.getThreshold();

		/* create entity manager */
		dataBaseConnectionProvider = new DataBaseConnectionProvider();
		entityManager = dataBaseConnectionProvider.getEntityManager();

		/* loading data into database */

		loadDataToDataBase(inputFile, entityManager);
		
		

		// Executy the command line query
		insertIntoTableIPsBlockedHourlyAccess(entityManager, startDate, duration, threshold);

		dataBaseConnectionProvider.closeEntityManager(entityManager);
	}

	private static void loadDataToDataBase(String fileName, EntityManager entityManager) {

		/**
		 * Open and read file, line by line, and insert data into database
		 * Access table
		 */
		File inputFile = new File(fileName);
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		DateBuilder dateBuilder = new DateBuilder();

		try {
			fileReader = new FileReader(inputFile);
			bufferedReader = new BufferedReader(fileReader);

			int persisted = 0;
			String lineOfLog;
			Access accessObject;
			AccessDAO accessDAO;

			entityManager.getTransaction().begin();

			while (((lineOfLog = bufferedReader.readLine()) != null)) {
				if ((persisted > 0) && ((persisted % 30) == 0)) {
					entityManager.flush();
					entityManager.clear();

					entityManager.getTransaction().commit();
					entityManager.getTransaction().begin();
				}

				accessObject = parseInputLineBuildDataObject(lineOfLog, dateBuilder);
				accessDAO = new AccessDAOImpl(entityManager);
				accessDAO.insertAccess(accessObject);
				persisted++;

			}
			entityManager.getTransaction().commit();

		} catch (RuntimeException e) {
			if (entityManager.getTransaction() != null && entityManager.getTransaction().isActive()) {
				entityManager.getTransaction().rollback();
			}
			throw e;
		} catch (FileNotFoundException e) {
			System.out.println("Couldn't find especified file. Please, check out error message:\n" + e.getMessage());
		} catch (IOException e) {
			System.out.println("Couldn't read file. Please, check out error message:\n" + e.getMessage());
		} finally {

			if (entityManager != null)
				entityManager.close();
			try {

				if (bufferedReader != null) {
					bufferedReader.close();
					bufferedReader = null;
				}
				if (fileReader != null) {
					fileReader.close();
					fileReader = null;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	/**
	 * This method parse a line from input file and build the object that will
	 * be registered into users_accesses table
	 * 
	 * @param line
	 *            - a line from input file
	 * @param dateBuilder
	 *            - formats the date
	 * @return an Access object that will be persisted
	 */
	private static Access parseInputLineBuildDataObject(String line, DateBuilder dateBuilder) {

		String[] logContent = line.trim().split(Pattern.quote("|"));
		final String doubleQuotePattern = "^\"|\"$";

		Access access = new Access();
		// yyyy-MM-dd HH:mm:ss.SSS
		access.setAccessTime(dateBuilder.getStringBuildDate(logContent[0]));
		// IP
		access.setIp(logContent[1]);
		access.setRequest(logContent[2].replaceAll(doubleQuotePattern, ""));
		access.setStatus(logContent[3]);
		access.setUserAgent(logContent[4].replaceAll(doubleQuotePattern, ""));

		return access;

	}

	/**
	 * This method insert into DB
	 * 
	 * @param entityManager
	 *            provides query mechanism
	 */
	private static void insertIntoTableIPsBlockedHourlyAccess(EntityManager entityManager, String startDateString,
			String duration, int threshold) {

		DateBuilder dateBuilder = new DateBuilder();

		Query query = entityManager.createQuery(
				"select access from Access as access where access.access_time between :startDate and :endDate");

		Date startDate = dateBuilder.getStringBuildDate(startDateString);
		query.setParameter("startDate", startDate);

		if (duration.equalsIgnoreCase("hourly")) {
			Date endDate = getEndDate(startDateString, dateBuilder, duration);
			query.setParameter("endDate", endDate);
		} else if (duration.equalsIgnoreCase("daily")) {
			Date endDate = getEndDate(startDateString, dateBuilder, duration);
			query.setParameter("endDate", endDate);
		}

		@SuppressWarnings("unchecked")
		List<Access> matchedAccesses = query.getResultList();
		Map<String, Integer> blockedIPs = new HashMap<String, Integer>();

		for (Access access : matchedAccesses) {
			if (!isIPAlreadyMapped(blockedIPs, access.getIp())) {
				blockedIPs.put(access.getIp(), 0);
			} else {
				int value = blockedIPs.get(access.getIp()) + 1;
				blockedIPs.put(access.getIp(), value);
			}
		}

		/**
		 * Create and persist blocked IPs
		 */

		BlockedIP blockedIP;
		BlockedIPDAO blockedIPDAO;

		entityManager.getTransaction().begin();
		int persisted = 0;
		for (Map.Entry<String, Integer> entry : blockedIPs.entrySet()) {
			
			if ((persisted > 0) && ((persisted % 30) == 0)) {
				entityManager.flush();
				entityManager.clear();

				entityManager.getTransaction().commit();
				entityManager.getTransaction().begin();
			}

			blockedIP = new BlockedIP();
			blockedIP.setIp(entry.getKey());
			if (duration.equalsIgnoreCase("hourly")) {
				blockedIP.setComment("Tried hourly!\n User Agent unknown");
			} else if (duration.equalsIgnoreCase("daily")) {
				blockedIP.setComment("Tried hourly!\n User Agent unknown");
			}
			blockedIPDAO = new BlockedIPDAOImpl(entityManager);
			blockedIPDAO.insertBlockedIP(blockedIP);

		}
		entityManager.getTransaction().commit();

	}

	private static boolean isIPAlreadyMapped(Map<String, Integer> blockedIPs, String IP) {

		boolean flag = false;
		for (Map.Entry<String, Integer> entry : blockedIPs.entrySet()) {
			if (entry.getKey().equals(IP)) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	private static Date getEndDate(String startDateString, DateBuilder dateBuilder, String duration) {
		Calendar calendar = null;
		Date startDate = dateBuilder.getStringBuildDate(startDateString);
		if (startDate != null) {
			calendar = Calendar.getInstance();
			calendar.setTime(startDate);
			if (duration.equalsIgnoreCase("hourly"))
				calendar.add(Calendar.HOUR, 1);
			else if (duration.equalsIgnoreCase("daily"))
				calendar.add(Calendar.HOUR, 24);
		}
		return calendar.getTime();
	}
}
