package com.zhy.rabbit._01;

import java.io.*;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.StreamMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Sender {

    public static String FILE_NAME = "D:\\develop\\temp\\2016\\6月\\onesite.sql";

    public static void main(String[] args) throws JMSException, IOException {
        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.9.63:61616");
        Connection connection = factory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createTopic("EXCHANGE.FILE");
        MessageProducer producer = session.createProducer(destination);
        long time = System.currentTimeMillis();

        //通知客户端开始接受文件
        StreamMessage message = session.createStreamMessage();
        message.setStringProperty("COMMAND", "start");
        producer.send(message);

        //开始发送文件
        File file = new File(FILE_NAME);
        byte[] content = new byte[1024 * 1024];
        InputStream ins = new FileInputStream(file);
        BufferedInputStream bins = new BufferedInputStream(ins);
        while (bins.read(content) > 0) {
            //
            message = session.createStreamMessage();
            message.setStringProperty("FILE_NAME", "onesite.sql");
            message.setStringProperty("COMMAND", "sending");
            message.clearBody();
            message.writeBytes(content);
            producer.send(message);
        }
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - time + "*****************");
        bins.close();
        ins.close();

        //通知客户端发送完毕
        message = session.createStreamMessage();
        message.setStringProperty("COMMAND", "end");
        producer.send(message);

        connection.close();

        System.out.println("Total Time costed : " + (System.currentTimeMillis() - time) + " mili seconds");
    }
}