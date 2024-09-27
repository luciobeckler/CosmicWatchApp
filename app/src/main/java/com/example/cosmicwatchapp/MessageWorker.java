package com.example.cosmicwatchapp;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

public class MessageWorker extends Worker {
    private RabbitMQClient rabbitMQClient;

    public MessageWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Conectando ao RabbitMQ
        rabbitMQClient = new RabbitMQClient("gcnvdgpe:X8UYYNAF0tLtG3d5qun47TcLxqUZJ5vI@chimpanzee.rmq.cloudamqp.com/gcnvdgpe", "");
        try {
            // Conectar ao RabbitMQ
            rabbitMQClient.connect();

            // Inscrever-se no canal
            subscribeToChannel("hazardous_meteors", (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                onMessageReceived(message); // Processa a mensagem
            });

        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(); // Retorna falha em caso de erro
        }

        return Result.success(); // Sucesso quando tudo funcionar corretamente
    }

    // Método para se inscrever no canal RabbitMQ
    public void subscribeToChannel(String channel, DeliverCallback callback) {
        try {
            rabbitMQClient.subscribe(channel, callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método chamado quando uma mensagem é recebida
    public void onMessageReceived(String message) {
        Log.d("COSMIC WORKER", "Mensagem recebida: " + message);
        // Implementar lógica para processar a mensagem
    }
}
