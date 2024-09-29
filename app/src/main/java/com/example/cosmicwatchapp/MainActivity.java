package com.example.cosmicwatchapp;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    private TextView titleTextView;
    private ImageView pictureImageView;
    private Button buttonAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titleTextView = findViewById(R.id.titleTextView);
        pictureImageView = findViewById(R.id.pictureImageView);
        buttonAlert = findViewById(R.id.button_alert);

        buttonAlert.setOnClickListener((View v)-> {
            this.startNotificationWorker();
        });

        fetchPictureOfDay();
    }

    public void startNotificationWorker(){
        OneTimeWorkRequest messageWorkRequest = new OneTimeWorkRequest.Builder(MessageWorker.class).build();
        WorkManager.getInstance(this).enqueue(messageWorkRequest);
        Toast.makeText(this, "Pronto! Você está inscrito para receber alertas de meteoro.", Toast.LENGTH_SHORT).show();
    }

    private void fetchPictureOfDay() {
        PictureOfDayApplication app = (PictureOfDayApplication) getApplication();
        app.fetchPictureOfDay(new Callback<Picture>() {
            @Override
            public void onResponse(Call<Picture> call, Response<Picture> response) {
                if (response.isSuccessful() && response.body() != null) {
                    displayPictureOfDay(response.body());
                }
            }

            @Override
            public void onFailure(Call<Picture> call, Throwable t) {
                // Lógica de erro
            }
        });
    }

    private void displayPictureOfDay(Picture picture) {
        titleTextView.setText(picture.getTitle());
        Picasso.get().load(picture.getUrl()).into(pictureImageView);
    }
}