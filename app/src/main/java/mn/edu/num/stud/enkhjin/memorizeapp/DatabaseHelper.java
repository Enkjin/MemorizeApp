package mn.edu.num.stud.enkhjin.memorizeapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "name.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "dictionary";

    // below variable is for our id column.
    private static final String ID_COL = "id";

    // below variable is for our course name column
    private static final String WORD_COL = "word";

    // below variable id for our course duration column.
    private static final String TRANSLATED_COL = "translated";
    private final ArrayList<Word>  wordArrayList = new ArrayList<>();
    public DatabaseHelper(Context context){

        super(context , DATABASE_NAME , null ,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_NAME + "("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + WORD_COL + " TEXT,"
                + TRANSLATED_COL + " TEXT" + ");");

    }
    public Boolean addNewWord(String word, String translated){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(WORD_COL,word);
        values.put(TRANSLATED_COL , translated);

        long result = db.insert(TABLE_NAME , null , values);
        db.close();
        if(result==-1){
            return false;
        }
        else return true;
    }
    public Word readWord(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME , null);
        Word word ;
        if(cursor.getCount() == 0){
            addNewWord("first" , "word");
        }
        cursor.moveToLast();
        cursor.moveToPrevious();
        word = new Word(cursor.getString(1) , cursor.getString(2));
        cursor.close();
        return word;
    }
    // end boolean bolgoh word bish
    public  Boolean deleteWord(Word word){
        SQLiteDatabase db = getWritableDatabase();
        Integer check =  db.delete(TABLE_NAME , "word=?" , new String[]{word.getWord()});
        db.close();
        if(check == 0) return false;
        return true;
    }
    public void updateWord(String originalWord , Word word){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(WORD_COL , word.getWord());
        values.put(TRANSLATED_COL , word.getTranslatedWord());

        db.update(TABLE_NAME , values , "word=?" , new String[]{originalWord});
        db.close();
    }
public ArrayList<Word> getWords(){
        wordArrayList.clear();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME , null);
        if (cursor.getCount()==0){
            Log.i("wordGet" , "getwords empty error ");
            return wordArrayList;
        }
        else {
            while (cursor.moveToNext()){
                Word word = new Word(cursor.getString(1), cursor.getString(2) );
                word.setId(cursor.getInt(0));
                wordArrayList.add(word);
            }
        }
        db.close();
        return wordArrayList;
}
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
