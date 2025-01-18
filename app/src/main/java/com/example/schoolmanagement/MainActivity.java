package com.example.schoolmanagement;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            // Set up the ImageView
            ImageView eduConnectImage = findViewById(R.id.eduConnectImage);
            eduConnectImage.setImageResource(R.drawable.edu_logo); // Ensure the image is in the `res/drawable` folder

            // Set up the Button
            Button getStartedButton = findViewById(R.id.getStartedButton);
            getStartedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Navigate to Screen2
                    Intent intent = new Intent(MainActivity.this, Login.class);
                    startActivity(intent);
                }
            });
        }
    }
