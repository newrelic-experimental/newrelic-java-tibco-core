package com.newrelic.instrumentation.tibco.jms8;


import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.TracedMethod;
import com.newrelic.api.agent.Transaction;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

@Weave(originalName = "javax.jms.MessageListener",type = MatchType.Interface)
public abstract class MessageListener_instrumentation {

	@Trace(dispatcher = true)
	public void onMessage(Message message) {
		try {
			Destination dest = message.getJMSDestination();
			TracedMethod traced = NewRelic.getAgent().getTracedMethod();
			Transaction txn = NewRelic.getAgent().getTransaction();
			TibcoUtils.processInbound(message, dest, traced, txn);
			if (!NewRelic.getAgent().getTransaction().isTransactionNameSet()) {
				// Do not override transaction name unless we started the transaction.
				TibcoUtils.nameTransaction(dest,txn);
			}
			TibcoUtils.saveMessageParameters(message);
		} catch (JMSException e) {
		}
		Weaver.callOriginal();
	}
}
