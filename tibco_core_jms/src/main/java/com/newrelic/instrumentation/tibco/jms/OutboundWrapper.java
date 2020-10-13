package com.newrelic.instrumentation.tibco.jms;

import java.util.logging.Level;

import javax.jms.JMSException;
import javax.jms.Message;

import com.newrelic.api.agent.HeaderType;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.OutboundHeaders;

public class OutboundWrapper implements OutboundHeaders {
	
	private final Message delegate;
	
	public OutboundWrapper(Message message) {
		delegate = message;
	}

	@Override
	public HeaderType getHeaderType() {
		return HeaderType.MESSAGE;
	}

	@Override
	public void setHeader(String name, String value) {
		try {
			delegate.setStringProperty(name, value);
		} catch (JMSException e) {
		      NewRelic.getAgent().getLogger().log(Level.FINE, e, "Error setting property ({0}) on JMS message.", new Object[] { name });
		}
	}

}
