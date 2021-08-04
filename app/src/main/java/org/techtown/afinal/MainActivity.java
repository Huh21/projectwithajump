package org.techtown.afinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.techtown.myandriodproject.HomeScreen;
import org.techtown.myandriodproject.R;

public class MainActivity extends AppCompatActivity {
    Button registerButton;
    Button loginButton;
    private Button login;
    private EditText email_login;
    private EditText pwd_login;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login=(Button)findViewById(R.id.login);
        email_login=(EditText) findViewById(R.id.email_login);
        pwd_login=(EditText)findViewById(R.id.pwd_login);
        firebaseAuth=firebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=email_login.getText().toString().trim();
                String pwd=pwd_login.getText().toString().trim();

                firebaseAuth.signInWithEmailAndPassword(email,pwd)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(MainActivity.this, HomeScreen.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(MainActivity.this, "로그인 오류", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });


        registerButton=findViewById(R.id.사업자register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, registerActivity.class);
                startActivity(intent);
            }
        });



    }
}