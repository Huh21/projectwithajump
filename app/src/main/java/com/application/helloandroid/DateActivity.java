package com.application.helloandroid;


import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateActivity extends AppCompatActivity {
    EditText Date;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH:mm:ss");
    String time=sdf.format(new Date(System.currentTimeMillis()));

    Calendar cal = new GregorianCalendar();
    String timeGre = String.format("%d/%d/%d/%d:%d:%d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1,
            cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE),
            cal.get(Calendar.SECOND));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Date = (EditText) findViewById(R.id.Date);

        Date.setText(time);

    }
}
