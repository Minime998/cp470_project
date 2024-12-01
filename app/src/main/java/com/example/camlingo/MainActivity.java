package com.example.camlingo;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import android.widget.PopupMenu;

import java.util.Objects;

import database.FireBaseQuestionLoader;

public class MainActivity extends AppCompatActivity {

    private CardView continueLearningCard, dailyQuestsCard, leaderboardCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView animated_camera = findViewById(R.id.camera_gif);
        Glide.with(this)
                .load(R.drawable.camera)
                .into(animated_camera);

        ImageView animated_bag = findViewById(R.id.bag_gif);
        Glide.with(this)
                .load(R.drawable.backpack)
                .into(animated_bag);

        ImageView animated_leaderboard = findViewById(R.id.leaderboard_gif);
        Glide.with(this)
                .load(R.drawable.leaderboard)
                .into(animated_leaderboard);

        // Find CardViews by ID
        continueLearningCard = findViewById(R.id.continue_learning_card);
        dailyQuestsCard = findViewById(R.id.daily_quests_card);
        leaderboardCard = findViewById(R.id.leaderboard_card);

        // daily question questions
        FireBaseQuestionLoader qloader = new FireBaseQuestionLoader(this);
        qloader.loadQuestions();

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Clear default title
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);



        dailyQuestsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "DailyQuest Clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, DailyQuestActivity.class));
            }
        });

        //ContinueLearning button click
        continueLearningCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //no activity to open yet, just a placeholder here
                Intent intent = new Intent(MainActivity.this, image_upload.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "continueLearningCard Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        //leader board card button click
        leaderboardCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //no activity to open yet, just a placeholder here
                //Intent intent = new Intent(MainActivity.this, MainActivity.class);
                //startActivity(intent);
                Toast.makeText(MainActivity.this, "leaderboardCard Clicked", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.settings) {
            // Handle setting action
            return true;
        }

        if (id == R.id.logout) {
            // Handle logout action
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}