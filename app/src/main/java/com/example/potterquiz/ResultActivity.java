package com.example.potterquiz;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;


import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    private TextView resultText;
    private Button retryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // รับคะแนนจาก Intent
        int gryffindorScore = getIntent().getIntExtra("gryffindorScore", 0);
        int ravenclawScore = getIntent().getIntExtra("ravenclawScore", 0);
        int hufflepuffScore = getIntent().getIntExtra("hufflepuffScore", 0);
        int slytherinScore = getIntent().getIntExtra("slytherinScore", 0);

        // คำนวณผลลัพธ์
        String result;
        if (gryffindorScore >= ravenclawScore && gryffindorScore >= hufflepuffScore && gryffindorScore >= slytherinScore) {
            result = "Gryffindor";
        } else if (ravenclawScore >= gryffindorScore && ravenclawScore >= hufflepuffScore && ravenclawScore >= slytherinScore) {
            result = "Ravenclaw";
        } else if (hufflepuffScore >= gryffindorScore && hufflepuffScore >= ravenclawScore && hufflepuffScore >= slytherinScore) {
            result = "Hufflepuff";
        } else {
            result = "Slytherin";
        }

        // แสดงผลลัพธ์ใน TextView
        TextView resultTextView = findViewById(R.id.resultText);
        resultTextView.setText("บ้านของคุณคือ: " + result);

        retryButton = findViewById(R.id.retryButton);
        retryButton.setOnClickListener(v -> {
            // สร้าง Intent กลับไปที่ QuizActivity และเริ่มเล่นใหม่
            Intent retryIntent = new Intent(ResultActivity.this, QuizActivity.class);
            retryIntent.putExtra("GAME_TYPE", getIntent().getStringExtra("GAME_TYPE"));
            startActivity(retryIntent);
            finish(); // ปิด ResultActivity
        });
    }

}
