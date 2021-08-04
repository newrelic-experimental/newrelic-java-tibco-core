package com.newrelic.expertservices.tibco.rv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;

import com.newrelic.agent.config.AgentConfig;
import com.newrelic.agent.config.AgentConfigListener;
import com.newrelic.agent.service.ServiceFactory;
import com.newrelic.api.agent.Config;
import com.newrelic.api.agent.Logger;
import com.newrelic.api.agent.NewRelic;

public class NRUtils implements AgentConfigListener {

	private static final String IGNORESKEY = "TIBCO.RV.ignores";
	private static final String SUBJECTSKEY = "TIBCO.RV.subjectIgnores";
	private static final String TRACKADMIN = "TIBCO.RV.trackAdminQueues";
	private static final String SAVEFIELDS = "TIBCO.RV.saveFields";
	public static final String ADMINREGEX = "_INBOX..*";
	public static final String ADMINSTART = "_INBOX";
	public static final String ADMINSUB = "INBOX";
	
	public static List<String> transportIgnores;
	public static List<String> subjectIgnores;
	private static boolean trackAdminQueues = false;
	private static boolean initialized = false;
	public static boolean collectFields = false;

	static {
		initialize();
	}

	public static String getName(String queue) {
		if(!initialized) {
			initialize();
		}
		String name = queue;
		if(queue.matches(ADMINREGEX) && !trackAdminQueues) {
			if(trackAdminQueues) 
				return ADMINSUB;
			else
				return null;
		}
		
	
		if(queue.toUpperCase().contains(ADMINSTART)) return null;
		
		if(ignore(name)) return null;
		
		return name;
	}
		
	public static void initialize() {
		Map<String, Object> map = new HashMap<String, Object>();
		Logger logger = NewRelic.getAgent().getLogger();
		logger.log(Level.INFO, "Initializing Rendevous Ignores");
		transportIgnores = new ArrayList<String>();
		subjectIgnores = new ArrayList<String>();
		Config config = NewRelic.getAgent().getConfig();
		String ignoresStr = (String)config.getValue(IGNORESKEY);
		
		Object trackAdminObj = config.getValue(TRACKADMIN);
		if(trackAdminObj != null) {
			if(Boolean.class.isInstance(trackAdminObj)) {
				trackAdminQueues = (Boolean)trackAdminObj;
			} else if(String.class.isInstance(trackAdminObj)) {
				trackAdminQueues = Boolean.parseBoolean((String)trackAdminObj);
			}
		}
		
		map.put("TrackAdminQueues", trackAdminQueues);

		if(ignoresStr != null && !ignoresStr.isEmpty()) {
			map.put("RVIgnoresString", ignoresStr);
			StringTokenizer st = new StringTokenizer(ignoresStr, ",");
			while(st.hasMoreTokens()) {
				String token = st.nextToken();
				logger.log(Level.INFO, "Will ignore Rendevous destinations matching {0}", token);
				transportIgnores.add(token);
			}
			if (!trackAdminQueues) {
				transportIgnores.add(ADMINREGEX);
				logger.log(Level.INFO, "Will ignore Rendevous destinations matching {0}", ADMINREGEX);
			}
		} else if(!trackAdminQueues) {
			map.put("RVIgnoresString", ignoresStr == null ? "null" : "empty");
			transportIgnores.add(ADMINREGEX);
			logger.log(Level.INFO, "Will ignore Rendevous destinations matching {0}", ADMINREGEX);
		}
		map.put("TransportIgnores", transportIgnores.toString());
		String subjectStr = config.getValue(SUBJECTSKEY);
		if(subjectStr != null && !subjectStr.isEmpty()) {
			map.put("SubjectIgnoresString", subjectStr);
			StringTokenizer st = new StringTokenizer(subjectStr, ",");
			while(st.hasMoreTokens()) {
				String token = st.nextToken();
				logger.log(Level.INFO, "Will ignore Rendevous subjects matching {0}", token);
				subjectIgnores.add(token);
			}
		} else {
			map.put("SubjectIgnoresString", subjectStr == null ? "null" : "empty");
		}
		
		Object saveFieldsObject = config.getValue(SAVEFIELDS);
		if(saveFieldsObject != null) {
			if(Boolean.class.isInstance(saveFieldsObject)) {
				collectFields = (Boolean)saveFieldsObject;
			} else if(String.class.isInstance(saveFieldsObject)) {
				collectFields = Boolean.getBoolean((String)saveFieldsObject);
			}
		}
		map.put("SaveFields", collectFields);
		NewRelic.getAgent().getInsights().recordCustomEvent("TibcoRVInit", map);

		NRUtils utils = new NRUtils();
		ServiceFactory.getConfigService().addIAgentConfigListener(utils);
		initialized = true;
	}
	public static boolean ignore(String name) {
		if(!initialized) {
			initialize();
		}
		if(name == null || name.isEmpty()) return false;
		for(int i=0;i<transportIgnores.size();i++) {
			String qName = transportIgnores.get(i);
			if(name.equalsIgnoreCase(qName)) return true;
			if(name.matches(qName)) return true;
		}
		return false;
	}

	public static boolean ignoreSubject(String name) {
		if(!initialized) {
			initialize();
		}
		if(!trackAdminQueues && name.contains(ADMINSTART)) return true;
		for(int i=0;i<subjectIgnores.size();i++) {
			String subject = subjectIgnores.get(i);
			if(name.equalsIgnoreCase(subject)) return true;
			if(name.matches(subject)) return true;
		}
		return false;
	}

	@Override
	public void configChanged(String category, AgentConfig config) {
		String ignoresStr = (String)config.getValue(IGNORESKEY);
		Logger logger = NewRelic.getAgent().getLogger();
		if(ignoresStr != null && !ignoresStr.isEmpty()) {
			StringTokenizer st = new StringTokenizer(ignoresStr, ",");
			while(st.hasMoreTokens()) {
				String token = st.nextToken();
				logger.log(Level.INFO, "Will ignore Rendevous destinations matching {0}", token);
				transportIgnores.add(token);
			}
			if (!trackAdminQueues) {
				transportIgnores.add(ADMINREGEX);
				logger.log(Level.INFO, "Will ignore Rendevous destinations matching {0}", ADMINREGEX);
			}
		} else if(!trackAdminQueues) {
			transportIgnores.add(ADMINREGEX);
			logger.log(Level.INFO, "Will ignore Rendevous destinations matching {0}", ADMINREGEX);
		}

		String subjectStr = config.getValue(SUBJECTSKEY);
		if(subjectStr != null && !subjectStr.isEmpty()) {
			StringTokenizer st = new StringTokenizer(subjectStr, ",");
			while(st.hasMoreTokens()) {
				String token = st.nextToken();
				logger.log(Level.INFO, "Will ignore Rendevous subjects matching {0}", token);
				subjectIgnores.add(token);
			}
		}
	}
	
	public static void main(String[] args) {
		String inbox = "_INBOX.AC1A14E3.5FE2E006312C7.1";
		boolean b = ignore(inbox);
		System.out.println("value of ignore is "+b);
	}
	
}
