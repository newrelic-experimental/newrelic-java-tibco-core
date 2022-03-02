package com.tibco.tibjms;

import javax.jms.Message;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.tibco.jms8.TibcoUtils;

@Weave
abstract class TibjmsMessageConsumer {

	TibjmsDestination _destination = Weaver.callOriginal();
	
	@Trace(dispatcher = true)
	Message _receive(long var1, Class<?> var3) {
		Message msg = Weaver.callOriginal();
		
		if(msg == null) {
			NewRelic.getAgent().getTransaction().ignore();
		} else {
			TibcoUtils.processInbound(msg, _destination, NewRelic.getAgent().getTracedMethod(),NewRelic.getAgent().getTransaction());
		}
		return msg;
	}
}
