package com.example.potterquiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {
    private TextView questionTextView;
    private RadioGroup radioGroup;

    private List<Question> questionList = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int[] userAnswers;
    private String gameType;

    // House Scores
    private int gryffindorScore = 0;
    private int ravenclawScore = 0;
    private int hufflepuffScore = 0;
    private int slytherinScore = 0;

    // Character Scores
    private int harry = 0;
    private int hermione = 0;
    private int ron = 0;
    private int draco = 0;
    private int voldemort = 0;
    private int snape = 0;
    private int dumbledore = 0;
    private int ginny = 0;
    private int dobby = 0;

    // Patronus Scores
    private int phoenixScore = 0;
    private int tabbyCatScore = 0;
    private int doeScore = 0;
    private int stagScore = 0;
    private int otterScore = 0;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questionTextView = findViewById(R.id.questionTextView);
        radioGroup = findViewById(R.id.radioGroup);
        Button nextButton = findViewById(R.id.nextButton);
        Button backButton = findViewById(R.id.backButton);

        Intent intent = getIntent();
        gameType = intent.getStringExtra("GAME_TYPE");

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
            } else if ("CHARACTER".equals(gameType)) {
                calculateCharacterScore(userAnswers[currentQuestionIndex]);
            } else if ("PATRONUS".equals(gameType)) {
                calculatePatronusScore(userAnswers[currentQuestionIndex]);
            }

            currentQuestionIndex++;
            if (currentQuestionIndex < questionList.size()) {
                displayQuestion();
            } else {
                showResult();
            }
        });

        backButton.setOnClickListener(v -> {
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--;
                displayQuestion();
            } else {
                Toast.makeText(this, getString(R.string.cannot_go_back), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("QuizActivity", "Quiz activity has started.");
    }

    private void fetchQuestionsFromFirebase() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                questionList.clear();

                String languageCode = Locale.getDefault().getLanguage();
                if (!languageCode.equals("th")) {
                    languageCode = "en"; // Default to English
                }

                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String questionText = snapshot.child("questionText").child(languageCode).getValue(String.class);
                        List<String> options = new ArrayList<>();
                        for (DataSnapshot optionSnapshot : snapshot.child("options").child(languageCode).getChildren()) {
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
                        Collections.shuffle(questionList);

                        int numberOfQuestions = Math.min(5, questionList.size());
                        questionList = questionList.subList(0, numberOfQuestions);

                        userAnswers = new int[questionList.size()];
                        // ตั้งค่าเริ่มต้นว่าไม่มีคำตอบ
                        Arrays.fill(userAnswers, -1);
                        displayQuestion();
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
        // แสดงข้อความคำถาม
        Question question = questionList.get(currentQuestionIndex);
        questionTextView.setText(question.getQuestionText());

        // ลบตัวเลือกเก่าใน RadioGroup และเพิ่มตัวเลือกใหม่
        radioGroup.removeAllViews();
        for (int i = 0; i < question.getOptions().size(); i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(question.getOptions().get(i));
            radioButton.setId(View.generateViewId()); // ใช้ generateViewId เพื่อหลีกเลี่ยง ID ซ้ำ
            radioGroup.addView(radioButton);

            // Map ระหว่าง index ของตัวเลือกกับ ID
            radioButton.setTag(i); // ใช้ tag เก็บ index เพื่อความสะดวกในการบันทึกคำตอบ
        }

        // เคลียร์ตัวเลือกปัจจุบัน
        radioGroup.clearCheck();

        // หากเคยเลือกคำตอบสำหรับคำถามนี้แล้ว ให้แสดงคำตอบเดิม
        int previousAnswer = userAnswers[currentQuestionIndex];
        if (previousAnswer != -1) { // ตรวจสอบว่ามีคำตอบก่อนหน้า
            for (int i = 0; i < radioGroup.getChildCount(); i++) {
                RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                if ((int) radioButton.getTag() == previousAnswer) {
                    radioButton.setChecked(true); // กำหนดคำตอบที่เลือกไว้
                    break;
                }
            }
        }

        // เพิ่มการบันทึกคำตอบทันทีเมื่อเลือก
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            if (selectedId != -1) {
                RadioButton selectedRadioButton = findViewById(selectedId);
                if (selectedRadioButton != null) {
                    int selectedIndex = (int) selectedRadioButton.getTag();
                    userAnswers[currentQuestionIndex] = selectedIndex; // บันทึกคำตอบในทันที
                }
            }
        });
    }

    private boolean saveUserAnswer() {
        // บันทึกคำตอบที่เลือกโดยผู้ใช้ (ตอนนี้ทำงานใน onCheckedChangeListener)
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId == -1) { // ถ้ายังไม่ได้เลือกคำตอบ
            Toast.makeText(this, getString(R.string.select_answer), Toast.LENGTH_SHORT).show();
            return false;
        }

        // บันทึกคำตอบใน userAnswers ถ้าไม่ได้ทำใน listener
        RadioButton selectedRadioButton = findViewById(selectedId);
        if (selectedRadioButton != null) {
            int selectedIndex = (int) selectedRadioButton.getTag();
            userAnswers[currentQuestionIndex] = selectedIndex;
        }
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

    private void calculateCharacterScore(int selectedOption) {
        switch (selectedOption) {
            case 0:
                harry += 3;
                dumbledore += 3;
                ginny += 2;
                dobby += 1;
                ron += 2;
                draco += 1;
                voldemort += 1;
                hermione += 2;
                break;

            case 1:
                hermione += 3;
                snape += 2;
                voldemort += 2;
                dumbledore += 2;
                draco += 1;
                dobby += 1;
                ginny += 1;
                break;

            case 2:
                ron += 3;
                harry += 1;
                dobby += 3;
                ginny += 3;
                draco += 1;
                hermione += 1;
                snape += 1;
                break;

            case 3:
                draco += 3;
                voldemort += 3;
                snape += 3;
                dobby += 1;
                break;
        }
    }

    private void calculatePatronusScore(int selectedOption) {
        switch (selectedOption) {
            case 0:
                stagScore += 3;
                phoenixScore += 2;
                doeScore += 1;
                break;
            case 1:
                otterScore += 3;
                tabbyCatScore += 2;
                phoenixScore += 1;
                break;
            case 2:
                phoenixScore += 3;
                doeScore += 2;
                stagScore += 1;
                break;
            case 3:
                doeScore += 3;
                stagScore += 2;
                otterScore += 1;
                break;
        }
    }

    private void showResult() {
        Intent resultIntent = new Intent(QuizActivity.this, ResultActivity.class);
        resultIntent.putExtra("GAME_TYPE", gameType);

        if ("HARRY_POTTER".equals(gameType)) {
            resultIntent.putExtra("gryffindorScore", gryffindorScore);
            resultIntent.putExtra("ravenclawScore", ravenclawScore);
            resultIntent.putExtra("hufflepuffScore", hufflepuffScore);
            resultIntent.putExtra("slytherinScore", slytherinScore);
        } else if ("CHARACTER".equals(gameType)) {
            resultIntent.putExtra("harryScore", harry);
            resultIntent.putExtra("hermioneScore", hermione);
            resultIntent.putExtra("ronScore", ron);
            resultIntent.putExtra("dracoScore", draco);
            resultIntent.putExtra("voldemortScore", voldemort);
            resultIntent.putExtra("snapeScore", snape);
            resultIntent.putExtra("dumbledoreScore", dumbledore);
            resultIntent.putExtra("ginnyScore", ginny);
            resultIntent.putExtra("dobbyScore", dobby);
        } else if ("PATRONUS".equals(gameType)) {
            resultIntent.putExtra("stagScore", stagScore);
            resultIntent.putExtra("phoenixScore", phoenixScore);
            resultIntent.putExtra("tabbyCatScore", tabbyCatScore);
            resultIntent.putExtra("doeScore", doeScore);
            resultIntent.putExtra("otterScore", otterScore);
        }

        startActivity(resultIntent);
        finish();
    }
}