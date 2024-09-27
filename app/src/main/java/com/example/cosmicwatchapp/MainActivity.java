package com.example.cosmicwatchapp;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    private TextView titleTextView;
    private ImageView pictureImageView;
    private TextView descriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titleTextView = findViewById(R.id.titleTextView);
        pictureImageView = findViewById(R.id.pictureImageView);
        descriptionTextView = findViewById(R.id.descriptionTextView);

        fetchPictureOfDay();
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