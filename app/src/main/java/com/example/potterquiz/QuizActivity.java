package com.example.potterquiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
    private TextView questionTextView;
    private RadioGroup radioGroup;

    private List<Question> questionList = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int[] userAnswers;
    private String gameType;
    private int gryffindorScore = 0;
    private int ravenclawScore = 0;
    private int hufflepuffScore = 0;
    private int slytherinScore = 0;

    private DatabaseReference databaseReference; // อ้างอิง Firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questionTextView = findViewById(R.id.questionTextView);
        radioGroup = findViewById(R.id.radioGroup);
        Button nextButton = findViewById(R.id.nextButton);

        // รับประเภทเกมจาก Intent
        Intent intent = getIntent();
        gameType = intent.getStringExtra("GAME_TYPE");

        // อ้างอิง Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("questions").child(gameType);

        // ดึงข้อมูลคำถามจาก Firebase
        fetchQuestionsFromFirebase();

        nextButton.setOnClickListener(v -> {
            saveUserAnswer();
            int selectedOption = userAnswers[currentQuestionIndex];

            if ("HARRY_POTTER".equals(gameType)) {
                switch (selectedOption) {
                    case 0:
                        gryffindorScore++;
                        break;
                    case 1:
                        ravenclawScore++;
                        break;
                    case 2:
                        hufflepuffScore++;
                        break;
                    case 3:
                        slytherinScore++;
                        break;
                }
            }

            currentQuestionIndex++;

            if (currentQuestionIndex < questionList.size()) {
                displayQuestion();
            } else {
                showResult();
            }
        });
    }

    private void fetchQuestionsFromFirebase() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Question question = snapshot.getValue(Question.class);
                    questionList.add(question);
                }
                userAnswers = new int[questionList.size()]; // เตรียมอาร์เรย์เก็บคำตอบ
                displayQuestion(); // แสดงคำถามแรก
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(QuizActivity.this, "Failed to load questions.", Toast.LENGTH_SHORT).show();
                Log.e("QuizActivity", "Error fetching questions", databaseError.toException());
            }
        });
    }

    private void displayQuestion() {
        Question question = questionList.get(currentQuestionIndex);
        questionTextView.setText(question.getQuestionText());
        String[] options = question.getOptions();

        radioGroup.removeAllViews();
        for (int i = 0; i < options.length; i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(options[i]);
            radioButton.setId(i);
            radioGroup.addView(radioButton);
        }
        radioGroup.clearCheck();
    }

    private void saveUserAnswer() {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        userAnswers[currentQuestionIndex] = selectedId;
    }

    private void showResult() {
        if ("HARRY_POTTER".equals(gameType)) {
            Intent resultIntent = new Intent(QuizActivity.this, ResultActivity.class);
            resultIntent.putExtra("gryffindorScore", gryffindorScore);
            resultIntent.putExtra("ravenclawScore", ravenclawScore);
            resultIntent.putExtra("hufflepuffScore", hufflepuffScore);
            resultIntent.putExtra("slytherinScore", slytherinScore);
            startActivity(resultIntent);
            finish();
        } else {
            // เพิ่มเงื่อนไขสำหรับเกมอื่นๆ
        }
    }
}
