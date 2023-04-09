package mn.edu.num.stud.enkhjin.memorizeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ResetWord extends AppCompatActivity {
    EditText word , translated;
    Button resetButton , cancelButton;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_word);

        word = findViewById(R.id.wordAdd);
        translated = findViewById(R.id.wordTranslated);
        resetButton = findViewById(R.id.resetWordButton);
        cancelButton = findViewById(R.id.cancelWordButton);
        dbHelper = new DatabaseHelper(ResetWord.this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String originalWord = bundle.getString("wordValue");

        Word resetWord = new Word(bundle.getString("wordValue") , bundle.getString("translatedValue") );
        word.setText(resetWord.getWord());
        translated.setText(resetWord.getTranslatedWord());

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent result = getIntent();
                resetWord.setWord(word.getText().toString());
                resetWord.setTranslatedWord(translated.getText().toString());
                dbHelper.updateWord(originalWord , resetWord);
                setResult(100 , result);
                finish();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}