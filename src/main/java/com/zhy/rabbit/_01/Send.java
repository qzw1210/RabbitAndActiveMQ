package com.zhy.rabbit._01;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Send
{
	//队列名称
	private final static String QUEUE_NAME = "hellox";

	public static void main(String[] args) throws java.io.IOException
	{
		/**
		 * 创建连接连接到MabbitMQ
		 */
		ConnectionFactory factory = new ConnectionFactory();
		//设置MabbitMQ所在主机ip或者主机名
		factory.setHost("localhost");
		
		//创建一个连接
		Connection connection = factory.newConnection();
		//创建一个频道
		Channel channel = connection.createChannel();
		//指定一个队列
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		//发送的消息
		String message = "hello rabbitMQ";
		//往队列中发出一条消息
		long startTime = System.currentTimeMillis();
		File file = new File("D:\\develop\\temp\\2016\\6月\\onesite.war");
		channel.basicPublish("", QUEUE_NAME, null, getBytesFromFile(file));
		long endTime = System.currentTimeMillis();

		System.out.println( endTime-startTime + "秒");
		//关闭频道和连接
		channel.close();
		connection.close();
	 }
	/**
	 * 生成Byte流
	 * TODO
	 * @history
	 * @knownBugs
	 * @param
	 * @return
	 * @exception
	 */
	public static byte[] getBytesFromFile(File file){
		byte[] ret=null;
		try{
			if (file==null){
				// log.error("helper:the file is null!");
				return null;
			}
			FileInputStream in=new FileInputStream(file);
			ByteArrayOutputStream out=new ByteArrayOutputStream(4096);
			byte[] b=new byte[4096];
			int n;
			while ((n= in.read(b))!=-1){
				out.write(b,0, n);
			}
			in.close();
			out.close();
			ret= out.toByteArray();
		}catch (IOException e){
			// log.error("helper:get bytes from file process error!");
			e.printStackTrace();
		}
		return ret;
	}
}
