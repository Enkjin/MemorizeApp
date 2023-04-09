package mn.edu.num.stud.enkhjin.memorizeapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.*;

public class MainActivity extends AppCompatActivity {
    public static String prefName = "settings";
    public static String intentSet = "settings_key";
    ArrayList<Word> words;
    SharedPreferences myShared;
    int mode;
    String tag = getClass().getName();
    private Button addButton, resetButton, deleteButton, backButton, nextButton;
    private TextView word, translated;
    private DatabaseHelper dbHandler;
    private Word currentWord;
    private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addButton = findViewById(R.id.add);
        resetButton = findViewById(R.id.reset);
        deleteButton = findViewById(R.id.delete);
        word = findViewById(R.id.word);
        translated = findViewById(R.id.translated);
        backButton = findViewById(R.id.backButton);
        nextButton = findViewById(R.id.nextButton);
        dbHandler = new DatabaseHelper(MainActivity.this);
        dbHandler.addNewWord("word", "tr");

        words = dbHandler.getWords();

        currentWord = words.get(words.size() - 1);
        currentIndex = words.size() - 1;
        word.setText(currentWord.getWord());
        translated.setText(currentWord.getTranslatedWord());

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("com.example.myapplication.AddWord");
                startActivityForResult(intent, 200);
            }
        });
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("wordValue", currentWord.getWord());
                bundle.putString("translatedValue", currentWord.getTranslatedWord());
                intent.putExtras(bundle);
                intent.setAction("com.example.myapplication.ResetWord");
                startActivityForResult(intent, 100);

            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Do you want to delete => " + currentWord.getWord() + " " + currentWord.getTranslatedWord());
                builder.setTitle("Delete");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                    words = dbHandler.getWords();
                    Log.i(tag, Arrays.toString(dbHandler.getWords().toArray()));
                    Log.i(tag, "index:" + String.valueOf(currentIndex));
                    Log.i(tag, "size:" + words.size());
                    Log.i(tag, "item:" + words.get(currentIndex));
                    if (words.size() == 0) return;
                    else if (words.size() == 1) {
                        dbHandler.deleteWord(currentWord);
                        Toast.makeText(getApplicationContext(), "There is no word", Toast.LENGTH_SHORT).show();
                        word.setText("");
                        translated.setText("");
                        word.setLongClickable(false);
                        translated.setLongClickable(false);
                        backButton.setClickable(false);
                        nextButton.setClickable(false);
                        resetButton.setClickable(false);
                        deleteButton.setClickable(false);

                        return;
                    } else {
                        Boolean check = dbHandler.deleteWord(currentWord);
                        if (check) {
                            if (words.size() - 1 > currentIndex) currentIndex++;
                            else if (currentIndex > 0) currentIndex--;
                            words = dbHandler.getWords();
                            currentWord = words.get(currentIndex);
                            word.setText(currentWord.getWord());
                            translated.setText(currentWord.getTranslatedWord());
                        } else Log.i(tag, "can't delete word from db");

                    }
                });
                builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.cancel();
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        word.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("wordValue", currentWord.getWord());
                bundle.putString("translatedValue", currentWord.getTranslatedWord());
                intent.putExtras(bundle);
                intent.setAction("com.example.myapplication.ResetWord");
                startActivityForResult(intent, 100);
                return false;
            }
        });
        translated.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("wordValue", currentWord.getWord());
                bundle.putString("translatedValue", currentWord.getTranslatedWord());
                intent.putExtras(bundle);
                intent.setAction("com.example.myapplication.ResetWord");
                startActivityForResult(intent, 100);
                return false;
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentIndex > 0) {
                    words = dbHandler.getWords();
                    currentIndex--;
                    Log.i("wordGet", String.valueOf(currentIndex));
                    currentWord = words.get(currentIndex);
                    word.setText(currentWord.getWord());
                    translated.setText(currentWord.getTranslatedWord());
                } else {
                    Log.i(tag, "Index is bound of");
                    Toast.makeText(getApplicationContext(), "No data exists", Toast.LENGTH_SHORT).show();
                }
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (words.size() - 1 > currentIndex) {
                    words = dbHandler.getWords();
                    currentIndex++;
                    Log.i("wordGet", String.valueOf(currentIndex));
                    currentWord = words.get(currentIndex);
                    word.setText(currentWord.getWord());
                    translated.setText(currentWord.getTranslatedWord());
                    Toast.makeText(getApplicationContext(), "No data exists", Toast.LENGTH_SHORT).show();
                } else
                    Log.i(tag, "Index is bound of");
            }
        });
        myShared = getSharedPreferences(prefName, MODE_PRIVATE);
        SharedPreferences.Editor editor = myShared.edit();
//        editor.putInt(intentSet,0);
        editor.apply();

        mode = myShared.getInt(intentSet, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 100:
                words = dbHandler.getWords();
                currentWord = words.get(currentIndex);
                word.setText(currentWord.getWord());
                translated.setText(currentWord.getTranslatedWord());
                Toast.makeText(getApplicationContext(), "Resetted", Toast.LENGTH_SHORT).show();

                return;
            case 200:
                words = dbHandler.getWords();
                if (words.size() == 1) {
                    currentIndex = 0;
                    currentWord = words.get(currentIndex);
                    word.setText(currentWord.getWord());
                    translated.setText(currentWord.getTranslatedWord());
                }
                Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_SHORT).show();
                backButton.setClickable(true);
                nextButton.setClickable(true);
                resetButton.setClickable(true);
                deleteButton.setClickable(true);
                return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mode = myShared.getInt(intentSet, 0);
        if (mode == 1) {
            word.setTextColor(Color.rgb(230, 230, 230));
            translated.setTextColor(Color.rgb(0, 0, 0));


        } else if (mode == 2) {

            translated.setTextColor(Color.rgb(230, 230, 230));
            word.setTextColor(Color.rgb(0, 0, 0));
        } else {
            word.setTextColor(Color.rgb(0, 0, 0));
            translated.setTextColor(Color.rgb(0, 0, 0));

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    public void write() {
        try {
            // Create a file output stream and write some data to it
            FileOutputStream outputStream = new FileOutputStream("words.txt");
            outputStream.write("Hello, world!".getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void Read() {
        String fileContents;
        // write();
        ArrayList<Word> wordsDb = new ArrayList<>();
        Word current;
        int id = 0;
        try {
            InputStream inputStream = new BufferedInputStream(
                    openFileInput("words.txt"));
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {

                String[] parts = line.split(",");
                // Get the original word and its translation
                String originalWord = parts[0];
                String translation = parts[1];
                current = new Word(originalWord, translation);
                current.setId(id++);
                wordsDb.add(current);
            }
            inputStream.close();
            words.clear();
            words = wordsDb;
            Log.i(tag, String.valueOf(wordsDb.size()));
            Log.i(tag, String.valueOf(words.size()));
            currentWord = words.get(words.size() - 1);
            currentIndex = words.size() - 1;
            word.setText(currentWord.getWord());
            translated.setText(currentWord.getTranslatedWord());
        } catch (IOException e) {
            e.printStackTrace();
        }
//        byte[] data = new byte[inputStream.available()];
//        inputStream.read(data, 0, inputStream.available());
//        inputStream.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Option:
                Intent OptionIntent = new Intent();
                OptionIntent.setClass(getApplicationContext(), settings.class);
                startActivity(OptionIntent);
                break;
            case R.id.read:
                Read();
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

}