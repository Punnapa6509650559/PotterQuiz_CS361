package com.example.potterquiz;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    private Button retryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // ดึง GAME_TYPE จาก Intent
        String gameType = getIntent().getStringExtra("GAME_TYPE");

        // TextView สำหรับแสดงผลลัพธ์
        TextView resultTextView = findViewById(R.id.resultText);

        // ตรวจสอบโหมดเกม
        if ("HARRY_POTTER".equals(gameType)) {
            // รับคะแนนบ้านจาก Intent
            int gryffindorScore = getIntent().getIntExtra("gryffindorScore", 0);
            int ravenclawScore = getIntent().getIntExtra("ravenclawScore", 0);
            int hufflepuffScore = getIntent().getIntExtra("hufflepuffScore", 0);
            int slytherinScore = getIntent().getIntExtra("slytherinScore", 0);

            // คำนวณผลลัพธ์สำหรับบ้าน
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

            // แสดงผลบ้าน
            resultTextView.setText("บ้านของคุณคือ: " + result);

        } else if ("CHARACTER".equals(gameType)) {
            // รับคะแนนตัวละครจาก Intent
            int harryScore = getIntent().getIntExtra("harryScore", 0);
            int hermioneScore = getIntent().getIntExtra("hermioneScore", 0);
            int ronScore = getIntent().getIntExtra("ronScore", 0);
            int dracoScore = getIntent().getIntExtra("dracoScore", 0);
            int voldemortScore = getIntent().getIntExtra("voldemortScore", 0);
            int snapeScore = getIntent().getIntExtra("snapeScore", 0);
            int dumbledoreScore = getIntent().getIntExtra("dumbledoreScore", 0);
            int ginnyScore = getIntent().getIntExtra("ginnyScore", 0);
            int dobbyScore = getIntent().getIntExtra("dobbyScore", 0);

            // คำนวณผลลัพธ์สำหรับตัวละคร
            int maxScore = Math.max(
                    Math.max(harryScore, hermioneScore),
                    Math.max(
                            Math.max(ronScore, dracoScore),
                            Math.max(
                                    Math.max(voldemortScore, snapeScore),
                                    Math.max(dumbledoreScore, Math.max(ginnyScore, dobbyScore))
                            )
                    )
            );

            String result;
            if (maxScore == harryScore) result = "Harry Potter";
            else if (maxScore == hermioneScore) result = "Hermione Granger";
            else if (maxScore == ronScore) result = "Ron Weasley";
            else if (maxScore == dracoScore) result = "Draco Malfoy";
            else if (maxScore == voldemortScore) result = "Lord Voldemort";
            else if (maxScore == snapeScore) result = "Severus Snape";
            else if (maxScore == dumbledoreScore) result = "Albus Dumbledore";
            else if (maxScore == ginnyScore) result = "Ginny Weasley";
            else result = "Dobby";

            // แสดงผลตัวละคร
            resultTextView.setText("คุณคือ: " + result);
        } else if ("PATRONUS".equals(gameType)) {
            int stagScore = getIntent().getIntExtra("stagScore", 0);
            int phoenixScore = getIntent().getIntExtra("phoenixScore", 0);
            int tabbyCatScore = getIntent().getIntExtra("tabbyCatScore", 0);
            int doeScore = getIntent().getIntExtra("doeScore", 0);
            int otterScore = getIntent().getIntExtra("otterScore", 0);
            String patronusResult = getIntent().getStringExtra("patronusResult");

            // แสดงผลลัพธ์
            resultTextView.setText("สัตว์วิเศษของคุณคือ: " + patronusResult);

        }

        // ปุ่มเล่นใหม่
        retryButton = findViewById(R.id.retryButton);
        retryButton.setOnClickListener(v -> {
            // สร้าง Intent กลับไปที่ QuizActivity และเริ่มเล่นใหม่
            Intent retryIntent = new Intent(ResultActivity.this, QuizSelectionActivity.class);
            retryIntent.putExtra("GAME_TYPE", gameType);
            startActivity(retryIntent);
            finish(); // ปิด ResultActivity
        });
    }
}
