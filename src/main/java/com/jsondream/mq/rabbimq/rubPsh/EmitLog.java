package com.jsondream.mq.rabbimq.rubPsh;

import java.io.IOException;
import java.util.Date;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class EmitLog {

	private static final String CHANGE_NAME = "ex_log";
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException {
		// 创建连接和频道  
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		// 声明转发器和类型  
		channel.exchangeDeclare(CHANGE_NAME,"fanout");

//		String queueName = channel.queueDeclare().getQueue();
//        channel.queueDeclare(queueName, true, false, false, null);  
//        channel.queueBind(queueName, CHANGE_NAME, "");
        String message = new Date().toLocaleString()+" : log something";  
     // 往转发器上发送消息  
        channel.basicPublish(CHANGE_NAME, "",null, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");  
        channel.close();  
        connection.close();  
        
	}
}
