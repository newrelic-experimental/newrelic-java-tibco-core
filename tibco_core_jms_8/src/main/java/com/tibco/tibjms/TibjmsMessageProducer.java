package com.tibco.tibjms;

import javax.jms.CompletionListener;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.TracedMethod;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.tibco.jms8.NRCompletionListener;
import com.newrelic.instrumentation.tibco.jms8.TibcoUtils;

@Weave
public abstract class TibjmsMessageProducer {

	public abstract Destination getDestination() throws JMSException;

	@Trace(leaf = true)
	TibjmsMessage _publish(Destination destination, Message message, boolean var3, int var4, int var5, long var6, boolean var8,CompletionListener listener) {

		try {
			Destination destToUse;

			if(destination != null) {
				destToUse = destination;
			} else {
				destToUse = getDestination();
			}
			if (destToUse != null && !TibcoUtils.ignore(destToUse)) {
				TracedMethod tracedMethod = NewRelic.getAgent().getTracedMethod();
				String metricName = TibcoUtils.nameProducerMetric(destination);
				tracedMethod.setMetricName(metricName);
				if(listener == null) {
					TibcoUtils.processSendMessage(message, destToUse, tracedMethod);
				} else {
					NRCompletionListener wrapper = TibcoUtils.processSendMessage(message, listener, destToUse);
					if(wrapper != null) {
						listener = wrapper;
					}
				}
				TibcoUtils.saveMessageParameters(message);
			}
		} catch (JMSException e) {
		}
		return Weaver.callOriginal();
	}

}
