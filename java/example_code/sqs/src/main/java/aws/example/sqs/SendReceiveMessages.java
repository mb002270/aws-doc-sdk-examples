//snippet-sourcedescription:[<<FILENAME>> demonstrates how to send, receive and delete messages from a queue.]
//snippet-keyword:[Java]
//snippet-keyword:[Code Sample]
//snippet-keyword:[Amazon Simple Queue Service]
//snippet-service:[sqs]
//snippet-sourcetype:[full-example]
//snippet-sourcedate:[]
//snippet-sourceauthor:[soo-aws]
/*
 * Copyright 2011-2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *	http://aws.amazon.com/apache2.0
 *
 * This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and
 * limitations under the License.
 */
package aws.example.sqs;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.SendMessageBatchRequest;
import com.amazonaws.services.sqs.model.SendMessageBatchRequestEntry;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import java.util.Date;
import java.util.List;

public class SendReceiveMessages
{
	private static final String QUEUE_NAME = "mwaters-test-queue";

	public static void main(String[] args)
	{
		final AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();

		try {
			CreateQueueResult create_result = sqs.createQueue(QUEUE_NAME);
		} catch (AmazonSQSException e) {
			if (!e.getErrorCode().equals("QueueAlreadyExists")) {
				throw e;
			}
		}

		String queueUrl = sqs.getQueueUrl(QUEUE_NAME).getQueueUrl();

		int i=0;
		long start,end;
		start = System.currentTimeMillis();
		for(i=0;i<100;i++)
		{
			sendDummyMessage(sqs,queueUrl,"dummy");
		}
		end = System.currentTimeMillis();

		System.out.print("Took " + (end-start) + " milliseconds to send " + i + " messages\n");

		start = System.currentTimeMillis();
		long total = receiveAllMessages(sqs, queueUrl);
		end = System.currentTimeMillis();
		System.out.print("Took " + (end-start) + " milliseconds to receive " + total + " messages\n");

//		for(int i=0;i<10;i++)
//		{
//			String time = Long.toString(System.currentTimeMillis());
//			SendMessageRequest send_msg_request = new SendMessageRequest()
//					.withQueueUrl(queueUrl)
//					.withMessageBody(time);
//			sqs.sendMessage(send_msg_request);
//
//
//			// Send multiple messages to the queue
//			SendMessageBatchRequest send_batch_request = new SendMessageBatchRequest()
//					.withQueueUrl(queueUrl)
//					.withEntries(
//							new SendMessageBatchRequestEntry(
//									"msg_1", "Hello from message 1"),
//							new SendMessageBatchRequestEntry(
//									"msg_2", "Hello from message 2")
//									.withDelaySeconds(10));
//			sqs.sendMessageBatch(send_batch_request);
//	
//			// receive messages from the queue
//			List<Message> messages = sqs.receiveMessage(queueUrl).getMessages();
//	
//			// delete messages from the queue
//			for (Message m : messages) {
//				long rec_time = System.currentTimeMillis();
//				long send_time = Long.parseLong(m.getBody());
//					long total_time = rec_time - send_time;
//					long per_second = 1000/total_time;
//				System.out.println("Took: " + (rec_time-send_time) + " milliseconds ("+ per_second + " per second)");
//				sqs.deleteMessage(queueUrl, m.getReceiptHandle());
//			}
//		}	
	}

	private static void sendDummyMessage(final AmazonSQS sqs, String queueUrl,String message)
	{
		SendMessageRequest send_msg_request = new SendMessageRequest()
			.withQueueUrl(queueUrl)
			.withMessageBody(message);
		sqs.sendMessage(send_msg_request);
	}

	private static long receiveAllMessages(final AmazonSQS sqs, String queueUrl)
	{
		boolean keep_reading=true;
		long total = 0;
		while(keep_reading)
		{
			keep_reading=false;
			List<Message> messages = sqs.receiveMessage(queueUrl).getMessages();
	
			for(Message m: messages)
			{
				keep_reading=true;
				total++;
				String body = m.getBody();
				if (null == body) 
				{
					System.out.println("null body");
				}
				sqs.deleteMessage(queueUrl, m.getReceiptHandle());
			}
		}
		return total;
	}
}
