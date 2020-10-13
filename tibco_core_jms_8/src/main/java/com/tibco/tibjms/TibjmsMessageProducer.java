package com.tibco.tibjms;

import javax.jms.CompletionListener;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;

import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.tibco.jms2.NRCompletionListener;
import com.newrelic.instrumentation.tibco.jms2.TibcoUtils;

@Weave(type=MatchType.BaseClass)
public abstract class TibjmsMessageProducer implements MessageProducer, TibjmsxConst {

	public abstract Destination getDestination() throws JMSException;

	
	@Trace
	public void send(Message message, int var2, int var3, long var4, CompletionListener var6) {
		Destination useDest = null;
		try {
			useDest = getDestination();
			if(useDest == null) {
				useDest = message.getJMSDestination();
			}
		} catch (JMSException e) {
		}
		if (useDest != null && !TibcoUtils.ignore(useDest)) {
			NRCompletionListener wrapper = TibcoUtils.processSendMessage(message, var6, useDest);
			if(wrapper != null) {
				var6 = wrapper;
			}
			TibcoUtils.saveMessageParameters(message);
		}
		Weaver.callOriginal();
	}
	
	@Trace
	public void send(Message message, CompletionListener var2) {
		Destination useDest = null;
		try {
			useDest = getDestination();
			if(useDest == null) {
				useDest = message.getJMSDestination();
			}
		} catch (JMSException e) {
		}
		if (useDest != null && !TibcoUtils.ignore(useDest)) {
			NRCompletionListener wrapper = TibcoUtils.processSendMessage(message, var2, useDest);
			if(wrapper != null) {
				var2 = wrapper;
			}
			TibcoUtils.saveMessageParameters(message);
		}
		Weaver.callOriginal();
	}
}
