package com.tibco.tibjms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Topic;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.TracedMethod;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.tibco.jms.TibcoUtils;

@Weave
abstract class TibjmsTopicPublisher {
	public abstract Topic getTopic() throws JMSException;

	@Trace(leaf=true)
	public void publish(Message message) throws JMSException {
		TracedMethod tracedMethod = NewRelic.getAgent().getTracedMethod();
		String metricName = TibcoUtils.nameProducerMetric(getTopic());
		tracedMethod.setMetricName(metricName);
		TibcoUtils.processSendMessage(message, getTopic(), tracedMethod);
		TibcoUtils.saveMessageParameters(message);
		Weaver.callOriginal();
	}

	@Trace(leaf=true)
	public void publish(Message message, int paramInt1, int paramInt2, long paramLong) throws JMSException {
		TracedMethod tracedMethod = NewRelic.getAgent().getTracedMethod();
		String metricName = TibcoUtils.nameProducerMetric(getTopic());
		tracedMethod.setMetricName(metricName);
		TibcoUtils.processSendMessage(message, getTopic(), tracedMethod);
		TibcoUtils.saveMessageParameters(message);
		Weaver.callOriginal();
	}

	@Trace(leaf=true)
	public void publish(Topic topic, Message message) throws JMSException {
		TracedMethod tracedMethod = NewRelic.getAgent().getTracedMethod();
		String metricName = TibcoUtils.nameProducerMetric(getTopic());
		tracedMethod.setMetricName(metricName);
		TibcoUtils.processSendMessage(message, topic, tracedMethod);
		TibcoUtils.saveMessageParameters(message);
		Weaver.callOriginal();
	}

	@Trace(leaf=true)
	public void publish(Topic topic, Message message, int paramInt1, int paramInt2, long paramLong) throws JMSException {
		TracedMethod tracedMethod = NewRelic.getAgent().getTracedMethod();
		String metricName = TibcoUtils.nameProducerMetric(getTopic());
		tracedMethod.setMetricName(metricName);
		TibcoUtils.processSendMessage(message, topic, tracedMethod);
		TibcoUtils.saveMessageParameters(message);
		Weaver.callOriginal();
	}

}
