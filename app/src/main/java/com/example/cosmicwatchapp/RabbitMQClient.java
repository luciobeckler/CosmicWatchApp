package com.example.cosmicwatchapp;

import com.rabbitmq.client.*;

public class RabbitMQClient {
    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;
    private String exchangeName;

    public RabbitMQClient(String host, String exchangeName) {
        this.factory = new ConnectionFactory();
        factory.setHost(host);
        this.exchangeName = exchangeName;
    }

    public void connect() throws Exception {
        connection = factory.newConnection();
        channel = connection.createChannel();
    }

    public void publishMessage(String routingKey, String message) throws Exception {
        channel.basicPublish(exchangeName, routingKey, null, message.getBytes());
    }

    public void subscribe(String routingKey, DeliverCallback deliverCallback) throws Exception {
        channel.queueDeclare(routingKey, false, false, false, null);
        channel.queueBind(routingKey, exchangeName, routingKey);
        channel.basicConsume(routingKey, true, deliverCallback, consumerTag -> {});
    }

    public void disconnect() throws Exception {
        channel.close();
        connection.close();
    }
}