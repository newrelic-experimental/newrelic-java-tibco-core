package com.newrelic.expertservices.tibco.rv;

import java.util.logging.Level;

import com.newrelic.api.agent.HeaderType;
import com.newrelic.api.agent.InboundHeaders;
import com.newrelic.api.agent.NewRelic;
import com.tibco.tibrv.TibrvException;
import com.tibco.tibrv.TibrvMsg;

public class InboundWrapper implements InboundHeaders {

	private TibrvMsg msg;
	
	public InboundWrapper(TibrvMsg m) {
		msg = m;
	}
	
	@Override
	public String getHeader(String key) {
		String header = null;
		try {
			header = (String) msg.get(key);
		} catch(TibrvException e) {
			NewRelic.getAgent().getLogger().log(Level.FINER, e, "Exception trying to retrieve header");
		}
		return header;
	}

	@Override
	public HeaderType getHeaderType() {
		return HeaderType.MESSAGE;
	}

}
