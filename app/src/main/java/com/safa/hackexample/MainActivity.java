package com.safa.hackexample;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    private int step;

    private TextView firstTimeTV;
    private TextView secondTimeTV;
    private TextView passingTimeTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        firstTimeTV = findViewById(R.id.press_one_textView);
        secondTimeTV = findViewById(R.id.press_two_textView);
        passingTimeTV = findViewById(R.id.passing_time_textView);

        editor.putInt(Const.STEP, Const.STEP_1);
        editor.commit();

    }

    public void onShowTime(View view) {
        long time = System.currentTimeMillis();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat s = new SimpleDateFormat("hhmmss");
        String format = s.format(time);

        step = sharedPref.getInt(Const.STEP,0);


        switch (step){
            case 0:
            case 1:
                editor.putInt(Const.STEP, Const.STEP_2);
                editor.putLong(Const.FIRST_PRESS, time);
                editor.commit();

                firstTimeTV.setText(convertTime(String.valueOf(format)));
                secondTimeTV.setVisibility(View.VISIBLE);
                break;
            case 2:
                editor.putInt(Const.STEP, Const.STEP_3);
                editor.putLong(Const.SECOND_PRESS, time);
                editor.commit();

                secondTimeTV.setText(convertTime(String.valueOf(format)));

                onPassingTime();

                break;
            case 3:
                break;
        }

    }

    public void onPassingTime(){

        long timeOne =  sharedPref.getLong(Const.FIRST_PRESS, 0);
        long timeTwo =  sharedPref.getLong(Const.SECOND_PRESS, 0);

        final long passingTime = timeTwo - timeOne;


        passingTimeTV.setVisibility(View.VISIBLE);
        passingTimeTV.setText(getResources().getString(R.string.passing_time) + " : " + passingTime + " ms");

        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle(getResources().getString(R.string.passing_time) + " : " + passingTime + " ms");
        alertDialog.setMessage("Play again");

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        findViewById(R.id.show_time_button).setVisibility(View.INVISIBLE);
                    }
                });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        firstTimeTV.setText(getResources().getString(R.string.press_one));
                        secondTimeTV.setText(getResources().getString(R.string.press_two));
                        passingTimeTV.setText(getResources().getString(R.string.passing_time));

                        secondTimeTV.setVisibility(View.INVISIBLE);
                        passingTimeTV.setVisibility(View.INVISIBLE);

                        editor.putInt(Const.STEP, Const.STEP_1);
                        editor.commit();

                        dialog.dismiss();
                    }
                });


        alertDialog.show();

    }

    public String convertTime(String time){

        String hh = time.substring(0,2);
        String mm = time.substring(2,4);
        String ss = time.substring(4,6);

        Log.d(TAG, "convertTime: "+ hh+ ":" + mm + ":"+ ss );

        return hh+ ":" + mm + ":"+ ss;
    }
}
