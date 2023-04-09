package mn.edu.num.stud.enkhjin.memorizeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import mn.edu.num.stud.enkhjin.memorizeapp.MainActivity;
import mn.edu.num.stud.enkhjin.memorizeapp.R;

public class settings extends AppCompatActivity  implements View.OnClickListener {
    Button save,cancel;
    RadioGroup settingsRadio;
    RadioButton word,translated,both;
    String LogTag = "Lab4";
    SharedPreferences myShared;
    public static String prefName  = "settings";
    public static String intentSet = "settings_key";
    int mode ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        save = this.findViewById(R.id.save);
        cancel = this.findViewById(R.id.cancel);
        save.setOnClickListener(this);
        cancel.setOnClickListener(this);



        settingsRadio = this.findViewById(R.id.group);
        settingsRadio.setOnClickListener(this);

        word = this.findViewById(R.id.word);
        translated = this.findViewById(R.id.translated);

        both = this.findViewById(R.id.Both);

        myShared = getSharedPreferences( prefName,MODE_PRIVATE);
        mode =  myShared.getInt(intentSet,0);
        Log.i(LogTag,Integer.toString(mode));
        if (mode == 0){
            both.setChecked(true);
        } else if( mode == 1 ){
            word.setChecked(true);
        } else if( mode == 2){
            translated.setChecked(true);
        }



    }


    @Override
    public void onClick(View view) {
        if( view.getId() == save.getId()){

            SharedPreferences.Editor editor = myShared.edit();

            if( settingsRadio.getCheckedRadioButtonId() == word.getId()){

                editor.putInt(intentSet,1);

            } else if( settingsRadio.getCheckedRadioButtonId() == translated.getId()){
                editor.putInt(intentSet,2);
            } else {
                editor.putInt(intentSet,0);
            }
            editor.apply();
            finish();

        } else if(view.getId() == cancel.getId()){
            Intent intent   = new Intent();
            intent.setClass(getApplicationContext(), MainActivity.class);
            finish();
        }


    }
}