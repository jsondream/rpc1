package com.jsondream.mq.rabbimq.workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class Work {
	//队列名称  
    private final static String QUEUE_NAME = "workqueue";  
  
    public static void main(String[] argv) throws java.io.IOException,  
            java.lang.InterruptedException  
    {  
        //区分不同工作进程的输出  
        int hashCode = Work.class.hashCode();  
        //创建连接和频道  
        ConnectionFactory factory = new ConnectionFactory();  
        factory.setHost("localhost");  
        Connection connection = factory.newConnection();  
        Channel channel = connection.createChannel();  
        // 消息持久化
        boolean durable = true;
        // 非消息持久化
        durable = false;
        // 声明队列 
        channel.queueDeclare("task_queue", durable, false, false, null);
        System.out.println(hashCode  
                + " [*] Waiting for messages. To exit press CTRL+C");  
        //设置最大服务转发消息数量  
        int prefetchCount = 1;  
        channel.basicQos(prefetchCount);  
        QueueingConsumer consumer = new QueueingConsumer(channel);  
        // 关闭了应答机制
        boolean ack = true ;
        // 指定消费队列  
        channel.basicConsume(QUEUE_NAME, ack, consumer);  
        
        while (true)  
        {  
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();  
            String message = new String(delivery.getBody());  
  
            System.out.println(hashCode + " [x] Received '" + message + "'");  
            doWork(message);  
            System.out.println(hashCode + " [x] Done");  
  
        }  
  
    }  
  
    /** 
     * 每个点耗时1s 
     * @param task 
     * @throws InterruptedException 
     */  
    private static void doWork(String task) throws InterruptedException  
    {  
        for (char ch : task.toCharArray())  
        {  
            if (ch == '.')  
                Thread.sleep(1000);  
        }  
    }  
}
