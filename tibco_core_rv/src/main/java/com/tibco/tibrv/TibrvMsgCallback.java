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
import com.newrelic.expertservices.tibco.rv.NRUtils;

@Weave(type=MatchType.Interface)
public abstract class TibrvMsgCallback {

	@Trace(dispatcher=true)
	public void onMsg(TibrvListener tibrvListener, TibrvMsg tibrvMsg) {
		
		InboundWrapper wrapper = new InboundWrapper(tibrvMsg);
		
		TracedMethod traced = NewRelic.getAgent().getTracedMethod();
		String queueName = tibrvListener.getQueue().getName();
		
		if(!NRUtils.ignore(queueName)) {
			if(queueName.contains(NRUtils.ADMINSTART)) {
				queueName = NRUtils.ADMINSUB;
			}
			String sendSubject = tibrvMsg.getSendSubject();
			if(sendSubject != null && !NRUtils.ignoreSubject(sendSubject)) {
				NewRelic.addCustomParameter("Send Subject", sendSubject);
			}
			String replySubject = tibrvMsg.getReplySubject();
			if(replySubject != null && !NRUtils.ignoreSubject(replySubject)) {
				NewRelic.addCustomParameter("Reply Subject", replySubject);
			}
			
			String subject = tibrvListener.getSubject();
			if(subject != null && !subject.isEmpty() && !NRUtils.ignoreSubject(subject)) {
				NewRelic.getAgent().getTransaction().setTransactionName(TransactionNamePriority.FRAMEWORK_HIGH, false, "RV", new String[] {"Tibco/RV/MsgCallback",queueName,subject});
				traced.addRollupMetricName("Custom/Tibco/MsgCallback/"+queueName);
				traced.setMetricName("Custom/Tibco/MsgCallback/"+queueName+"/"+subject);
			} else {
				NewRelic.getAgent().getTransaction().setTransactionName(TransactionNamePriority.FRAMEWORK_HIGH, false, "RV", new String[] {"Tibco/RV/MsgCallback",queueName});
				traced.setMetricName("Custom/Tibco/MsgCallback/"+queueName);
			}
			MessageConsumeParameters params = MessageConsumeParameters.library("TibcoRV").destinationType(DestinationType.NAMED_QUEUE).destinationName(queueName).inboundHeaders(wrapper).build();
			traced.reportAsExternal(params);
		} else {
			NewRelic.getAgent().getTransaction().ignore();
		}
		Weaver.callOriginal();
	}
	
}