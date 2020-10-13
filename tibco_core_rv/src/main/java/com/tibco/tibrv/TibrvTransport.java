package com.tibco.tibrv;

import com.newrelic.api.agent.DestinationType;
import com.newrelic.api.agent.MessageConsumeParameters;
import com.newrelic.api.agent.MessageProduceParameters;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.TracedMethod;
import com.newrelic.api.agent.TransactionNamePriority;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.expertservices.tibco.rv.InboundWrapper;
import com.newrelic.expertservices.tibco.rv.NRUtils;
import com.newrelic.expertservices.tibco.rv.OutboundWrapper;

@Weave(type=MatchType.BaseClass)
public abstract class TibrvTransport {
	
	@Trace
	public void send(TibrvMsg tibrvMsg) throws TibrvException {
		String name = getName(this);
		if (!NRUtils.ignore(name)) {
			if(name.matches(NRUtils.ADMINREGEX)) {
				name = "INBOX";
			}
			OutboundWrapper wrapper = new OutboundWrapper(tibrvMsg);
			MessageProduceParameters params = MessageProduceParameters.library("TibcoRV").destinationType(DestinationType.NAMED_QUEUE).destinationName(tibrvMsg.getSendSubject()).outboundHeaders(wrapper).build();
			NewRelic.getAgent().getTracedMethod().reportAsExternal(params);
			NewRelic.getAgent().getTracedMethod().setMetricName(new String[] { "TibrvTransport", name, "send" });
			NewRelic.getAgent().getTransaction().setTransactionName(TransactionNamePriority.FRAMEWORK_HIGH, true, "TibrvTransport",new String[] { "TibrvTransport", name, "send" });
		} else {
			NewRelic.getAgent().getTransaction().ignore();
		}
		Weaver.callOriginal();
	}
	
	@Trace
	public void sendReply(TibrvMsg tibrvMsg1, TibrvMsg tibrvMsg2) throws TibrvException {
		String name = getName(this);
		if (!NRUtils.ignore(name)) {
			if(name.matches(NRUtils.ADMINREGEX)) {
				name = "INBOX";
			}
			NewRelic.getAgent().getTracedMethod().setMetricName(new String[] { "TibrvTransport", name, "sendReply" });
			NewRelic.getAgent().getTransaction().setTransactionName(TransactionNamePriority.FRAMEWORK_HIGH, true, "TibrvTransport", new String[] { "TibrvTransport", name, "sendReply" });
			InboundWrapper wrapper = new InboundWrapper(tibrvMsg1);
			TracedMethod traced = NewRelic.getAgent().getTracedMethod();
			MessageConsumeParameters params = MessageConsumeParameters.library("TibcoRV").destinationType(DestinationType.NAMED_QUEUE).destinationName(name).inboundHeaders(wrapper).build();
			traced.reportAsExternal(params);
			OutboundWrapper wrapper2 = new OutboundWrapper(tibrvMsg2);
			traced.addOutboundRequestHeaders(wrapper2);
		} else {
			NewRelic.getAgent().getTransaction().ignore();
		}
		Weaver.callOriginal();
	}
	
	@Trace
	public TibrvMsg sendRequest(TibrvMsg tibrvMsg, double paramDouble) throws TibrvException {
		String name = getName(this);
		if (!NRUtils.ignore(name)) {
			if(name.matches(NRUtils.ADMINREGEX)) {
				name = "INBOX";
			}
			NewRelic.getAgent().getTracedMethod().setMetricName(new String[] {"TibrvTransport",name,"sendRequest"});
			NewRelic.getAgent().getTransaction().setTransactionName(TransactionNamePriority.FRAMEWORK_HIGH,true,"TibrvTransport",new String[] {"TibrvTransport",name,"sendRequest"});
			OutboundWrapper wrapper = new OutboundWrapper(tibrvMsg);
			NewRelic.getAgent().getTracedMethod().addOutboundRequestHeaders(wrapper);
		} else {
			NewRelic.getAgent().getTransaction().ignore();
		}
		return Weaver.callOriginal();		
	}
	
	private String getName(Object transport) {
		String name = transport.toString();
		return name;
	}


}
