package com.pace.cs639spring.hw3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;

public class Main3Activity extends AppCompatActivity {

    SophisticatedLineGraphView mGraphView;
    SeekBar mSeekBar;
    Button b1;

    Intent intent;
    Date date , date2;
    String date1;
    int studentCount;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        b1 = (Button)findViewById(R.id.button);
        mGraphView = findViewById(R.id.line_graph2);
        mSeekBar = findViewById(R.id.seek_bar);

        db=openOrCreateDatabase("Attendance", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS attendance(date VARCHAR(5),count INTEGER);");


        intent = getIntent();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd");
        try {
            date = format.parse(intent.getStringExtra("date"));
            date1 = intent.getStringExtra("date");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            //try to parse student count from the text field
            studentCount =  Integer.parseInt(intent.getStringExtra("count"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        db.execSQL("INSERT INTO attendance VALUES('"+date1+"','"+studentCount+"');");


        Cursor c=db.rawQuery("SELECT * FROM attendance", null);

        while(c.moveToNext())
        {

            SimpleDateFormat format1 = new SimpleDateFormat("MM/dd");
            try {
               date2 = format.parse(c.getString(0));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            mGraphView.addData(date2.getTime(), c.getInt(1));

        }




        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float radius = 5 + ((((float) progress) / 100f) * 5);
                mGraphView.setCircleRadius(radius);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Main3Activity.this ,MainActivity.class);
                startActivity(intent);
            }
        });

    }

    public void onClearDataClicked(View v) {

        db.execSQL("DELETE FROM attendance;");

        mGraphView.clearData();
    }

    public void onHighlightIntegralClicked(View view) {
        mGraphView.highlightIntegral(((CheckBox)view).isChecked());
    }

    public void onShowLinesClicked(View view) {
        mGraphView.showLines(((CheckBox)view).isChecked());
    }
}
