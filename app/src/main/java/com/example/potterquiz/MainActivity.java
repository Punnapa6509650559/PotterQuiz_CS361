package com.example.potterquiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
<<<<<<< HEAD
=======

>>>>>>> UI
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
<<<<<<< HEAD
=======

>>>>>>> UI
        Button startButton = findViewById(R.id.startButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // เปิดหน้าเลือกหัวข้อ
                Intent intent = new Intent(MainActivity.this, QuizSelectionActivity.class);
                startActivity(intent);
            }
        });
    }
}

