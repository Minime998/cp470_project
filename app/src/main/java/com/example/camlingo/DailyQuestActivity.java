package com.example.camlingo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Objects;

import model.MultipleChoiceQuestion;
import repository.QuestionRepository;

public class DailyQuestActivity extends AppCompatActivity {

    private Button ClaimButton, StarQuestButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_daily_quest);

        //find button by id
        ClaimButton = findViewById(R.id.claim_button_DailyQuest);
        StarQuestButton = findViewById(R.id.startQuest_button_DailyQuest);


        //"Claim Now" button click
        ClaimButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //no activity to open yet, just a placeholder here
                //Intent intent = new Intent(DailyQuestActivity.this, DailyQuestActivity.class);
                //startActivity(intent);
                Toast.makeText(DailyQuestActivity.this, "ClaimButton Clicked", Toast.LENGTH_SHORT).show();

            }
        });

        //Start today's quest button click
        StarQuestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DailyQuestActivity.this, DailyQuestsTasks.class);
                startActivity(intent);
                Toast.makeText(DailyQuestActivity.this, "StarQuestButton Clicked", Toast.LENGTH_SHORT).show();

            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.daily_quests), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}