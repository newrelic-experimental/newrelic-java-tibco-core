package com.newrelic.expertservices.tibco.rv;

import com.tibco.tibrv.TibrvEvent;
import com.tibco.tibrv.TibrvMsg;
import com.tibco.tibrv.TibrvQueue;

public class TibcoRVUtils {

	public static String getQueueName(TibrvMsg tibrvMsg) {
		String queueName = "Unknown Queue";
		
		TibrvEvent event = tibrvMsg.getEvent();
		if(event != null) {
			TibrvQueue queue = event.getQueue();
			if(queue != null) {
				queueName = queue.getName();
			}
		}
		return queueName;
	}
}
