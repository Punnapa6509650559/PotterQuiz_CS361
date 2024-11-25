package com.example.potterquiz;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Locale;

public class ResultActivity extends AppCompatActivity {

    private Button retryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Get the game type from Intent
        String gameType = getIntent().getStringExtra("GAME_TYPE");

        // TextView for displaying the result
        TextView resultTextView = findViewById(R.id.resultText);

        // Get the current language of the device
        String language = Locale.getDefault().getLanguage();

        // Determine the result based on the game type
        if ("HARRY_POTTER".equals(gameType)) {
            int gryffindorScore = getIntent().getIntExtra("gryffindorScore", 0);
            int ravenclawScore = getIntent().getIntExtra("ravenclawScore", 0);
            int hufflepuffScore = getIntent().getIntExtra("hufflepuffScore", 0);
            int slytherinScore = getIntent().getIntExtra("slytherinScore", 0);

            String result = calculateMaxScore(new HashMap<String, Integer>() {{
                put("Gryffindor", gryffindorScore);
                put("Ravenclaw", ravenclawScore);
                put("Hufflepuff", hufflepuffScore);
                put("Slytherin", slytherinScore);
            }});

            // Display house result based on the language
            if (language.equals("th")) {
                resultTextView.setText(getString(R.string.house_result, getHouseInThai(result)));
            } else {
                resultTextView.setText(getString(R.string.house_result, result));
            }

        } else if ("CHARACTER".equals(gameType)) {
            Map<String, Integer> characterScores = new HashMap<>();
            characterScores.put("Harry Potter", getIntent().getIntExtra("harryScore", 0));
            characterScores.put("Hermione Granger", getIntent().getIntExtra("hermioneScore", 0));
            characterScores.put("Ron Weasley", getIntent().getIntExtra("ronScore", 0));
            characterScores.put("Draco Malfoy", getIntent().getIntExtra("dracoScore", 0));
            characterScores.put("Lord Voldemort", getIntent().getIntExtra("voldemortScore", 0));
            characterScores.put("Severus Snape", getIntent().getIntExtra("snapeScore", 0));
            characterScores.put("Albus Dumbledore", getIntent().getIntExtra("dumbledoreScore", 0));
            characterScores.put("Ginny Weasley", getIntent().getIntExtra("ginnyScore", 0));
            characterScores.put("Dobby", getIntent().getIntExtra("dobbyScore", 0));

            String result = calculateMaxScore(characterScores);

            if (language.equals("th")) {
                resultTextView.setText(getString(R.string.character_result, getCharacterInThai(result)));
            } else {
                resultTextView.setText(getString(R.string.character_result, result));
            }

        } else if ("PATRONUS".equals(gameType)) {
            Map<String, Integer> patronusScores = new HashMap<>();
            patronusScores.put("Stag", getIntent().getIntExtra("stagScore", 0));
            patronusScores.put("Phoenix", getIntent().getIntExtra("phoenixScore", 0));
            patronusScores.put("Tabby Cat", getIntent().getIntExtra("tabbyCatScore", 0));
            patronusScores.put("Doe", getIntent().getIntExtra("doeScore", 0));
            patronusScores.put("Otter", getIntent().getIntExtra("otterScore", 0));

            String result = calculateMaxScore(patronusScores);

            if (language.equals("th")) {
                resultTextView.setText(getString(R.string.patronus_result, getPatronusInThai(result)));
            } else {
                resultTextView.setText(getString(R.string.patronus_result, result));
            }
        }

        // Retry button functionality
        retryButton = findViewById(R.id.retryButton);
        retryButton.setOnClickListener(v -> {
            Intent retryIntent = new Intent(ResultActivity.this, QuizSelectionActivity.class);
            retryIntent.putExtra("GAME_TYPE", gameType);
            startActivity(retryIntent);
            finish(); // Close ResultActivity
        });
    }

    private String calculateMaxScore(Map<String, Integer> scores) {
        String maxKey = null;
        int maxValue = Integer.MIN_VALUE;

        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            if (entry.getValue() > maxValue) {
                maxValue = entry.getValue();
                maxKey = entry.getKey();
            }
        }

        return maxKey != null ? maxKey : getString(R.string.select_answer);
    }

    private String getHouseInThai(String house) {
        switch (house) {
            case "Gryffindor": return getString(R.string.gryffindor);
            case "Ravenclaw": return getString(R.string.ravenclaw);
            case "Hufflepuff": return getString(R.string.hufflepuff);
            case "Slytherin": return getString(R.string.slytherin);
            default: return house;
        }
    }

    private String getCharacterInThai(String character) {
        switch (character) {
            case "Harry Potter": return getString(R.string.harry_potter);
            case "Hermione Granger": return getString(R.string.hermione_granger);
            case "Ron Weasley": return getString(R.string.ron_weasley);
            case "Draco Malfoy": return getString(R.string.draco_malfoy);
            case "Lord Voldemort": return getString(R.string.lord_voldemort);
            case "Severus Snape": return getString(R.string.severus_snape);
            case "Albus Dumbledore": return getString(R.string.albus_dumbledore);
            case "Ginny Weasley": return getString(R.string.ginny_weasley);
            case "Dobby": return getString(R.string.dobby);
            default: return character;
        }
    }

    private String getPatronusInThai(String patronus) {
        switch (patronus) {
            case "Stag": return getString(R.string.stag);
            case "Phoenix": return getString(R.string.phoenix);
            case "Tabby Cat": return getString(R.string.tabby_cat);
            case "Doe": return getString(R.string.doe);
            case "Otter": return getString(R.string.otter);
            default: return patronus;
        }
    }
}