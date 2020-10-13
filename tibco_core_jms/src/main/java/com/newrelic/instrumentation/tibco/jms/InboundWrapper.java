package com.newrelic.instrumentation.tibco.jms;

import java.util.logging.Level;

import javax.jms.JMSException;
import javax.jms.Message;

import com.newrelic.api.agent.HeaderType;
import com.newrelic.api.agent.InboundHeaders;
import com.newrelic.api.agent.NewRelic;

public class InboundWrapper implements InboundHeaders {

	private final Message delegate;

	public InboundWrapper(Message message) {
		delegate = message;
	}

	@Override
	public HeaderType getHeaderType() {
		return HeaderType.MESSAGE;
	}

	@Override
	public String getHeader(String name) {
		try {
			return delegate.getStringProperty(name);
		} catch (JMSException e) {
			NewRelic.getAgent().getLogger().log(Level.FINE, e, "Error getting property ({0}) from JMS message.", new Object[] { name });
		}
		return null;
	}

}
