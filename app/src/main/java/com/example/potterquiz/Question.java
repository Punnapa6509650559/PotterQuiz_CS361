package com.example.potterquiz;

public class Question {
    private String questionText;  // คำถาม
    private String[] options;     // ช้อยส์คำตอบ

    // Constructor ที่ไม่มี correctAnswerIndex
    public Question(String questionText, String[] options) {
        this.questionText = questionText;
        this.options = options;
    }

    // Default constructor สำหรับ Firebase
    public Question() {}

    public String getQuestionText() {
        return questionText;
    }

    public String[] getOptions() {
        return options;
    }
}
