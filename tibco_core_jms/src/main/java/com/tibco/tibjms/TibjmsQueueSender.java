package com.tibco.tibjms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.TracedMethod;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.tibco.jms.TibcoUtils;

@Weave
abstract class TibjmsQueueSender extends TibjmsMessageProducer {

	public abstract Queue getQueue() throws JMSException;

	@Trace(dispatcher=true)
	public void send(Message message) throws JMSException {
		TracedMethod tracedMethod = NewRelic.getAgent().getTracedMethod();
		
		String metricName = TibcoUtils.nameProducerMetric(getQueue());
		tracedMethod.setMetricName(metricName);
		TibcoUtils.processQueueSendMessage(message, getQueue().getQueueName(), tracedMethod);
		TibcoUtils.saveMessageParameters(message);
		Weaver.callOriginal();
	}

	@Trace(dispatcher=true)
	public void send(Message message, int paramInt1, int paramInt2, long paramLong) throws JMSException {
		TracedMethod tracedMethod = NewRelic.getAgent().getTracedMethod();
		String metricName = TibcoUtils.nameProducerMetric(getQueue());
		tracedMethod.setMetricName(metricName);
		TibcoUtils.processQueueSendMessage(message, getQueue().getQueueName(), tracedMethod);
		TibcoUtils.saveMessageParameters(message);
		Weaver.callOriginal();
	}

	@Trace(dispatcher=true)
	public void send(Queue paramQueue, Message message) throws JMSException {
		TracedMethod tracedMethod = NewRelic.getAgent().getTracedMethod();
		String metricName = TibcoUtils.nameProducerMetric(getQueue());
		tracedMethod.setMetricName(metricName);
		TibcoUtils.processQueueSendMessage(message,paramQueue.getQueueName(), tracedMethod);
		TibcoUtils.saveMessageParameters(message);
		Weaver.callOriginal();
	}

	@Trace(dispatcher=true)
	public void send(Queue paramQueue, Message message, int paramInt1, int paramInt2, long paramLong) throws JMSException {
		TracedMethod tracedMethod = NewRelic.getAgent().getTracedMethod();
		String metricName = TibcoUtils.nameProducerMetric(getQueue());
		tracedMethod.setMetricName(metricName);
		TibcoUtils.processQueueSendMessage(message, paramQueue.getQueueName(), tracedMethod);
		TibcoUtils.saveMessageParameters(message);
		Weaver.callOriginal();
	}

}
