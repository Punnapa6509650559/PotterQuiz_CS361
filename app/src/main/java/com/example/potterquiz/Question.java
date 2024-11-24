package com.example.potterquiz;

<<<<<<< HEAD
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
=======
public class Question {
    private String questionText;
    private String[] options;
    private int correctAnswerIndex;

    public Question(String questionText, String[] options, int correctAnswerIndex) {
        this.questionText = questionText;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
>>>>>>> UI
    }

    public String getQuestionText() {
        return questionText;
    }

<<<<<<< HEAD
    public List<String> getOptions() {
        return options;
    }
=======
    public String[] getOptions() {
        return options;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

>>>>>>> UI
}
