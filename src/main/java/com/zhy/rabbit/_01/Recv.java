package com.zhy.rabbit._01;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.*;

public class Recv
{
	//队列名称
	private final static String QUEUE_NAME = "hellox";

	public static void main(String[] argv) throws java.io.IOException,
			java.lang.InterruptedException
	{
		//打开连接和创建频道，与发送端一样
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		//声明队列，主要为了防止消息接收者先运行此程序，队列还不存在时创建队列。
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		//创建队列消费者
		QueueingConsumer consumer = new QueueingConsumer(channel);
		//指定消费队列
		channel.basicConsume(QUEUE_NAME, true, consumer);
		long starttime = System.currentTimeMillis();
		while (true)
		{
			//nextDelivery是一个阻塞方法（内部实现其实是阻塞队列的take方法）
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            getFileFromBytes(delivery.getBody(),"d:\\onesite111.war");
			//String message = new String(delivery.getBody());
			//System.out.println(" [x] Received '" + message + "'");
			long endTime = System.currentTimeMillis();
			System.out.println(endTime-starttime);
		}
	}


	/**
	 * 把流生成图片
	 * TODO
	 * @history
	 * @knownBugs
	 * @param
	 * @return
	 * @exception
	 */
	public static File getFileFromBytes(byte[] b, String outputFile){
		 File ret=null;
		BufferedOutputStream stream=null;
		try{
			 ret=new File(outputFile);
			FileOutputStream fstream=new FileOutputStream(ret);
			stream=new BufferedOutputStream(fstream);
			stream.write(b);
			}catch (Exception e){
			 // log.error("helper:get file from byte process error!");　　
			e.printStackTrace();
			}finally{
			if (stream!=null){
				try{
					stream.close();
					}catch (IOException e){
					// log.error("helper:get file from byte process error!");
					e.printStackTrace();
					}
				}
			}
		return ret;
		}

}
