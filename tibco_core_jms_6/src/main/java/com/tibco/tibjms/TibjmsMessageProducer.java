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
import com.newrelic.instrumentation.tibco.jms6.TibcoUtils;

@Weave(type=MatchType.BaseClass)
public abstract class TibjmsMessageProducer implements MessageProducer, TibjmsxConst {

	public abstract Destination getDestination() throws JMSException;

	@Trace(leaf=true)
	TibjmsMessage _publish(Destination destination, Message message, int var3, int var4, long var5, boolean var7) {
		try {
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
		} catch (JMSException e) {
		}
		
		return Weaver.callOriginal();
	}

}
