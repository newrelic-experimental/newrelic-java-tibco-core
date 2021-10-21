package com.tibco.tibrv;

import com.newrelic.api.agent.DestinationType;
import com.newrelic.api.agent.MessageConsumeParameters;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.TracedMethod;
import com.newrelic.api.agent.TransactionNamePriority;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.expertservices.tibco.rv.InboundWrapper;

@Weave(type=MatchType.Interface)
public abstract class TibrvMsgCallback {

	@Trace(dispatcher=true)
	public void onMsg(TibrvListener tibrvListener, TibrvMsg tibrvMsg) {
		String queueName = tibrvListener.getQueue().getName();
		if(queueName.toLowerCase().contains("_inbox")) {
			queueName = "INBOX";
		}
		InboundWrapper wrapper = new InboundWrapper(tibrvMsg);
		TracedMethod traced = NewRelic.getAgent().getTracedMethod();
		MessageConsumeParameters params = MessageConsumeParameters.library("TibcoRV").destinationType(DestinationType.NAMED_QUEUE).destinationName(queueName).inboundHeaders(wrapper).build();
		traced.reportAsExternal(params);
		String sendSubject = tibrvMsg.getSendSubject();
		
		if(sendSubject != null) {
			NewRelic.addCustomParameter("Send Subject", sendSubject);
		}
		String replySubject = tibrvMsg.getReplySubject();
		if(replySubject != null) {
			NewRelic.addCustomParameter("Reply Subject", replySubject);
		}
		
		String subject = tibrvListener.getSubject();
		NewRelic.getAgent().getTransaction().setTransactionName(TransactionNamePriority.FRAMEWORK_HIGH, false, "RV", new String[] {"Tibco/RV/MsgCallback",queueName,subject});
		traced.addRollupMetricName("Custom/Tibco/MsgCallback/"+queueName);
		traced.setMetricName("Custom/Tibco/MsgCallback/"+queueName+"/"+subject);
		
		Weaver.callOriginal();
	}
}
