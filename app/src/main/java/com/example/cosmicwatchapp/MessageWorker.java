package com.example.cosmicwatchapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
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
        rabbitMQClient = new RabbitMQClient("chimpanzee-01.rmq.cloudamqp.com", "amq.direct", "gcnvdgpe", "X8UYYNAF0tLtG3d5qun47TcLxqUZJ5vI", "gcnvdgpe");
        try {
            // Conectar ao RabbitMQ
            rabbitMQClient.connect();
            this.createNotificationChannel();

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
        showNotification("Meteoro se aproximando!", message);
    }

    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Meteor Alerts";
            String description = "Notificações de novos alertas meteorológicos";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("meteor_alerts_channel", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = this.getApplicationContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    // Método para exibir notificação
    public void showNotification(String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.getApplicationContext(), "meteor_alerts_channel")
                .setSmallIcon(R.drawable.meteor) // Coloque o ícone apropriado aqui
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = (NotificationManager) this.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }
}
