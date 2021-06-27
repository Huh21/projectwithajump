package com.application.helloandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    Button button1;
    TextView Day;

    //현재 시간
    long now = System.currentTimeMillis();
    Date date = new Date(now);
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd / HH:mm:ss");
    String formatDate = dateFormat.format(date);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //작성하기 버튼
        button1 = findViewById(R.id.ButtonSubmit);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(), "명부가 작성되었습니다",
                        Toast.LENGTH_SHORT).show();
            }
        });

        //시간 출력
        Day=(TextView)findViewById(R.id.Day);
        Day.setText(formatDate);
    }
}

