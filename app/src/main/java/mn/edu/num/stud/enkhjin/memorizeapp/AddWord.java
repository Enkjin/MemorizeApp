package mn.edu.num.stud.enkhjin.memorizeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddWord extends AppCompatActivity {
    EditText word , translated;
    Button addButton , cancelButton;
    DatabaseHelper dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);
        word = findViewById(R.id.wordAdd);
        translated = findViewById(R.id.wordTranslated);
        addButton = findViewById(R.id.addWordButton);
        cancelButton = findViewById(R.id.cancelWordButton);
        dbHandler = new DatabaseHelper(AddWord.this);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String first = word.getText().toString();
                String second = translated.getText().toString();
                dbHandler.addNewWord(first,second);
                Toast.makeText(AddWord.this , "Success " , Toast.LENGTH_LONG);
                setResult(200);
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