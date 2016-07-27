package com.zhy.rabbit._01;

/**
 * Created by quzhiwen on 2016/7/20.
 */
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.StreamMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Receiver {

    /**
     * @param args
     */
    public static void main(String[] args) throws JMSException, IOException {
        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.9.63:61616");

        Connection connection = factory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Destination destination = session.createTopic("EXCHANGE.FILE");

        MessageConsumer consumer = session.createConsumer(destination);

        boolean appended = false;
        try {
            while (true) {
                Message message = consumer.receive(5000);
                if (message == null) {
                    continue;
                }

                if (message instanceof StreamMessage) {
                    StreamMessage streamMessage = (StreamMessage) message;
                    String command = streamMessage.getStringProperty("COMMAND");

                    if ("start".equals(command)) {
                        appended = false;
                        continue;
                    }

                    if ("sending".equals(command)) {
                        byte[] content = new byte[4096];
                        String file_name = message.getStringProperty("FILE_NAME");
                        BufferedOutputStream bos = null;
                        bos = new BufferedOutputStream(new FileOutputStream("E:/" + file_name, appended));
                        if (!appended) {
                            appended = true;
                        }
                        while (streamMessage.readBytes(content) > 0) {
                            bos.write(content);
                        }
                        bos.close();
                        continue;
                    }

                    if ("end".equals(command)) {
                        appended = false;
                        continue;
                    }
                }
            }
        } catch (JMSException e) {
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

    }

}

