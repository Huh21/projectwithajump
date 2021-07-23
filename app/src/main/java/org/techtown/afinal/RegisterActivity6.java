package org.techtown.afinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.techtown.myandriodproject.R;

public class RegisterActivity6 extends AppCompatActivity {
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register6);
        button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RegisterActivity6.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"회원가입을 완료했습니다.",Toast.LENGTH_LONG).show();
            }
        });

    }
}