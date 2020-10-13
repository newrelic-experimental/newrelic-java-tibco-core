package com.tibco.tibrv;

import com.newrelic.api.agent.DestinationType;
import com.newrelic.api.agent.MessageProduceParameters;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.expertservices.tibco.rv.OutboundWrapper;

@Weave(type=MatchType.ExactClass)
public abstract class TibrvTransport {

	public abstract String getDescription();

	@Trace(dispatcher=true)
	public void send(TibrvMsg tibrvMsg) throws TibrvException {
		OutboundWrapper wrapper = new OutboundWrapper(tibrvMsg);
		MessageProduceParameters params = MessageProduceParameters.library("TibcoRV").destinationType(DestinationType.EXCHANGE).destinationName(tibrvMsg.getSendSubject()).outboundHeaders(wrapper).build();
		
		NewRelic.getAgent().getTracedMethod().reportAsExternal(params);
		Weaver.callOriginal();
		NewRelic.getAgent().getTracedMethod().setMetricName(new String[] {"Custom","TibrvRvTransport","TIBRV "+tibrvMsg.getSendSubject(),"send"});
	}

	@Trace(dispatcher=true)
	public void sendReply(TibrvMsg replyMsg, TibrvMsg requestMsg) throws TibrvException {
		OutboundWrapper wrapper2 = new OutboundWrapper(replyMsg);
		MessageProduceParameters params = MessageProduceParameters.library("TibcoRV").destinationType(DestinationType.EXCHANGE).destinationName(requestMsg.getReplySubject()).outboundHeaders(wrapper2).build();
		
		NewRelic.getAgent().getTracedMethod().reportAsExternal(params);
		NewRelic.getAgent().getTracedMethod().setMetricName(new String[] {"Custom","TibrvRvTransport",requestMsg.getReplySubject(),"sendReply"});
		Weaver.callOriginal();
	}

	@Trace(dispatcher=true)
	public TibrvMsg sendRequest(TibrvMsg tibrvMsg, double paramDouble) throws TibrvException {
		OutboundWrapper wrapper = new OutboundWrapper(tibrvMsg);
		MessageProduceParameters params = MessageProduceParameters.library("TibcoRV").destinationType(DestinationType.EXCHANGE).destinationName(tibrvMsg.getSendSubject()).outboundHeaders(wrapper).build();
		
		NewRelic.getAgent().getTracedMethod().reportAsExternal(params);

		NewRelic.getAgent().getTracedMethod().setMetricName(new String[] {"Custom","TibrvRvTransport","TIBRV "+tibrvMsg.getSendSubject(),"sendRequest"});
		return Weaver.callOriginal();		
	}
}
