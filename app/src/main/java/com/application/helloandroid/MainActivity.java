package com.application.helloandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    // "인증하기" 버튼
    Button button1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1 = findViewById(R.id.button1);

        button1.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(), "인증되었습니다.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}

