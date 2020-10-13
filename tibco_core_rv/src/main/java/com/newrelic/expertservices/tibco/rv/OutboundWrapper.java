package com.newrelic.expertservices.tibco.rv;

import java.util.logging.Level;

import com.newrelic.api.agent.HeaderType;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.OutboundHeaders;
import com.tibco.tibrv.TibrvException;
import com.tibco.tibrv.TibrvMsg;

public class OutboundWrapper implements OutboundHeaders {

	private TibrvMsg msg;
	
	public OutboundWrapper(TibrvMsg m) {
		msg = m;
	}

	@Override
	public HeaderType getHeaderType() {
		return HeaderType.MESSAGE;
	}

	@Override
	public void setHeader(String key, String value) {
		try {
			String s = (String) msg.get(key);
			if(s == null || !s.equals(value)) {
				msg.add(key, value);
			}
		} catch (TibrvException e) {
			NewRelic.getAgent().getLogger().log(Level.FINER,e, "Exception trying to set header");
		}
		

	}

}
