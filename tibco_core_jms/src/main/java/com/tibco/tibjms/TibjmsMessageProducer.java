package com.tibco.tibjms;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.TracedMethod;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.tibco.jms.TibcoUtils;

@Weave(type=MatchType.BaseClass)
public abstract class TibjmsMessageProducer implements MessageProducer, TibjmsxConst {

	public abstract Destination getDestination() throws JMSException;

	@Trace(leaf=true)
	public void send(Message message) throws JMSException {
		TracedMethod tracedMethod = NewRelic.getAgent().getTracedMethod();
		String metricName = TibcoUtils.nameProducerMetric(getDestination());
		tracedMethod.setMetricName(metricName);
		TibcoUtils.processSendMessage(message, getDestination(), tracedMethod);
		TibcoUtils.saveMessageParameters(message);
		Weaver.callOriginal();
	}

	@Trace(leaf=true)
	public void send(Message message, int paramInt1, int paramInt2, long paramLong) throws JMSException {
		TracedMethod tracedMethod = NewRelic.getAgent().getTracedMethod();
		String metricName = TibcoUtils.nameProducerMetric(getDestination());
		tracedMethod.setMetricName(metricName);
		TibcoUtils.processSendMessage(message, getDestination(), tracedMethod);
		TibcoUtils.saveMessageParameters(message);
		Weaver.callOriginal();
	}

	@Trace(leaf=true)
	public void send(Destination destination, Message message) throws JMSException {
		Destination destToUse;

		if(destination != null) {
			destToUse = destination;
		} else {
			destToUse = getDestination();
		}
		if (destToUse != null) {
			TracedMethod tracedMethod = NewRelic.getAgent().getTracedMethod();
			String metricName = TibcoUtils.nameProducerMetric(destination);
			tracedMethod.setMetricName(metricName);
			TibcoUtils.processSendMessage(message, destToUse, tracedMethod);
			TibcoUtils.saveMessageParameters(message);
		}
		Weaver.callOriginal();
	}

	@Trace(leaf=true)
	public void send(Destination destination, Message message, int paramInt1, int paramInt2, long paramLong) throws JMSException {
		Destination destToUse;

		if(destination != null) {
			destToUse = destination;
		} else {
			destToUse = getDestination();
		}
		if (destToUse != null) {
			TracedMethod tracedMethod = NewRelic.getAgent().getTracedMethod();
			String metricName = TibcoUtils.nameProducerMetric(destination);
			tracedMethod.setMetricName(metricName);
			TibcoUtils.processSendMessage(message, destToUse, tracedMethod);
			TibcoUtils.saveMessageParameters(message);
		}
		Weaver.callOriginal();
	}

}
