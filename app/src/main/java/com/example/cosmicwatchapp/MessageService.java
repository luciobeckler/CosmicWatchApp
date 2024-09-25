package com.example.cosmicwatchapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

public class MessageService extends Service {
    private RabbitMQClient rabbitMQClient;

    @Override
    public void onCreate() {
        super.onCreate();
        rabbitMQClient = new RabbitMQClient("YOUR_RABBITMQ_HOST", "exchangeName");
        try {
            rabbitMQClient.connect();
            subscribeToChannel("channelName", ((consumerTag, delivery) -> onMessageReceived(new String(delivery.getBody(), StandardCharsets.UTF_8))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void subscribeToChannel(String channel, DeliverCallback callback) {
        try {
            rabbitMQClient.subscribe(channel, callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onMessageReceived(String message) {
        // Lógica para tratar a mensagem recebida
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
