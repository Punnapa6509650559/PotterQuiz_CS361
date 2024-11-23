package com.example.potterquiz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class QuizSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_selection);

        Button housesButton = findViewById(R.id.housesButton);
        Button patronusButton = findViewById(R.id.patronusButton);
        Button characterButton = findViewById(R.id.characterButton);

        // เมื่อกดปุ่ม "Hogwart Houses"
        housesButton.setOnClickListener(v -> {
            // แสดงข้อความ Toast
            Toast.makeText(QuizSelectionActivity.this, "Hogwart Houses Selected", Toast.LENGTH_SHORT).show();
            // สร้าง Intent ไปยัง QuizActivity และส่งข้อมูลประเภทเกม
            Intent intent = new Intent(QuizSelectionActivity.this, QuizActivity.class);
            intent.putExtra("GAME_TYPE", "HARRY_POTTER");
            startActivity(intent);
        });

        // เมื่อกดปุ่ม "Patronus Charm"
        patronusButton.setOnClickListener(v -> {
            // แสดงข้อความ Toast
            Toast.makeText(QuizSelectionActivity.this, "Patronus Charm Selected", Toast.LENGTH_SHORT).show();
            // สร้าง Intent ไปยัง QuizActivity และส่งข้อมูลประเภทเกม
            Intent intent = new Intent(QuizSelectionActivity.this, QuizActivity.class);
            intent.putExtra("GAME_TYPE", "PATRONUS");
            startActivity(intent);
        });

        // เมื่อกดปุ่ม "Character"
        characterButton.setOnClickListener(v -> {
            // แสดงข้อความ Toast
            Toast.makeText(QuizSelectionActivity.this, "Character Selected", Toast.LENGTH_SHORT).show();
            // สร้าง Intent ไปยัง QuizActivity และส่งข้อมูลประเภทเกม
            Intent intent = new Intent(QuizSelectionActivity.this, QuizActivity.class);
            intent.putExtra("GAME_TYPE", "CHARACTER");
            startActivity(intent);
        });
    }
}
