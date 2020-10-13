package com.newrelic.instrumentation.tibco.jms2;

import javax.jms.CompletionListener;
import javax.jms.Message;

import com.newrelic.api.agent.MessageProduceParameters;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Segment;

public class NRCompletionListener implements CompletionListener {
	
	private Segment segment = null;
	
	private MessageProduceParameters params = null;
	
	private CompletionListener delegate = null;
	
	public NRCompletionListener(CompletionListener d, Segment s, MessageProduceParameters p) {
		delegate = d;
		segment = s;
		params = p;
	}

	@Override
	public void onCompletion(Message message) {
		if(segment != null) {
			if(params != null) {
				segment.reportAsExternal(params);
			}
			segment.end();
			segment = null;
		}
		if(delegate != null) {
			delegate.onCompletion(message);
		}
	}

	@Override
	public void onException(Message message, Exception exception) {
		NewRelic.noticeError(exception);
		if(segment != null) {
			if(params != null) {
				segment.reportAsExternal(params);
			}
			segment.end();
			segment = null;
		}
		if(delegate != null) {
			delegate.onException(message, exception);
		}
	}

}
