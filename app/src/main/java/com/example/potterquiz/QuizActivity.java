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
import java.util.Collections;
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

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questionTextView = findViewById(R.id.questionTextView);
        radioGroup = findViewById(R.id.radioGroup);
        Button nextButton = findViewById(R.id.nextButton);

        Intent intent = getIntent();
        gameType = intent.getStringExtra("GAME_TYPE");

        // ตั้งค่า Firebase Database
        databaseReference = FirebaseDatabase.getInstance("https://harryquiz-c1143-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("quizzes")
                .child(gameType)
                .child("questions");

        fetchQuestionsFromFirebase();

        nextButton.setOnClickListener(v -> {
            if (!saveUserAnswer()) {
                return; // หยุดถ้าผู้ใช้ยังไม่ได้เลือกคำตอบ
            }

            if ("HARRY_POTTER".equals(gameType)) {
                calculateHouseScore(userAnswers[currentQuestionIndex]);
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
                questionList.clear();

                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String questionText = snapshot.child("questionText").getValue(String.class);
                        List<String> options = new ArrayList<>();
                        for (DataSnapshot optionSnapshot : snapshot.child("options").getChildren()) {
                            String option = optionSnapshot.getValue(String.class);
                            if (option != null) {
                                options.add(option);
                            }
                        }

                        if (questionText != null && !options.isEmpty()) {
                            questionList.add(new Question(questionText, options));
                        }
                    }

                    if (questionList.isEmpty()) {
                        Toast.makeText(QuizActivity.this, "No questions available.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        // สุ่มคำถาม
                        Collections.shuffle(questionList);

                        // จำกัดจำนวนคำถามที่จะใช้ในเกม (ตัวอย่าง 5 คำถาม)
                        int numberOfQuestions = Math.min(5, questionList.size());
                        questionList = questionList.subList(0, numberOfQuestions);

                        userAnswers = new int[questionList.size()];
                        displayQuestion();
                        Log.d("QuizActivity", "Questions loaded: " + questionList.size());
                    }
                } else {
                    Toast.makeText(QuizActivity.this, "No data found in Firebase.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(QuizActivity.this, "Failed to load questions.", Toast.LENGTH_SHORT).show();
                Log.e("QuizActivity", "Error fetching questions", databaseError.toException());
                finish();
            }
        });
    }

    private void displayQuestion() {
        Question question = questionList.get(currentQuestionIndex);
        questionTextView.setText(question.getQuestionText());

        radioGroup.removeAllViews(); // ล้างตัวเลือกก่อนแสดงคำถามใหม่
        for (int i = 0; i < question.getOptions().size(); i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(question.getOptions().get(i));
            radioButton.setId(i);
            radioGroup.addView(radioButton);
        }
        radioGroup.clearCheck(); // รีเซ็ตการเลือกตัวเลือก
    }

    private boolean saveUserAnswer() {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Please select an answer.", Toast.LENGTH_SHORT).show();
            return false;
        }
        userAnswers[currentQuestionIndex] = selectedId;
        return true;
    }

    private void calculateHouseScore(int selectedOption) {
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
            Toast.makeText(this, "Game type not supported.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
