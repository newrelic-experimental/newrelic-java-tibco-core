package com.newrelic.instrumentation.tibco.jms8;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;

import javax.jms.JMSException;
import javax.jms.Message;

import com.newrelic.api.agent.HeaderType;
import com.newrelic.api.agent.Headers;
import com.newrelic.api.agent.NewRelic;

public class TibJMSHeaders implements Headers{
	
	private Message message = null;
	
	public TibJMSHeaders(Message msg) {
		message = msg;
	}

	@Override
	public HeaderType getHeaderType() {
		return HeaderType.MESSAGE;
	}

	@Override
	public String getHeader(String name) {
		
		try {
			return message.getStringProperty(name);
		} catch (JMSException e) {
			NewRelic.getAgent().getLogger().log(Level.FINEST, e, "Error getting property ({0}) from JMS message.", new Object[] { name });
		}
		return null;
	}

	@Override
	public Collection<String> getHeaders(String name) {
		List<String> list = new ArrayList<String>();
		String value = getHeader(name);
		if(value != null) {
			list.add(value);
		}
		return list;
	}

	@Override
	public void setHeader(String name, String value) {
		try {
			message.setStringProperty(name, value);
		} catch (JMSException e) {
			NewRelic.getAgent().getLogger().log(Level.FINEST, e, "Failed to set header {0} to {1}", name, value);
		}
	}

	@Override
	public void addHeader(String name, String value) {
		setHeader(name, value);
	}

	@Override
	public Collection<String> getHeaderNames() {
		List<String> list = new ArrayList<String>();
		try {
			Enumeration<?> headerNames = message.getPropertyNames();
			while(headerNames.hasMoreElements()) {
				Object name = headerNames.nextElement();
				list.add(name.toString());
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
		return list;
	}

	@Override
	public boolean containsHeader(String name) {
		return getHeaderNames().contains(name);
	}

}
