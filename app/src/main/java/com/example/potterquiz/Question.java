package com.example.potterquiz;

import java.util.List;

public class Question {
    private String questionText;
    private List<String> options;

    public Question() {
        // Default constructor สำหรับ Firebase
    }

    public Question(String questionText, List<String> options) {
        this.questionText = questionText;
        this.options = options;
    }

    public String getQuestionText() {
        return questionText;
    }

    public List<String> getOptions() {
        return options;
    }
}
