package org.techtown.afinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ThrowOnExtraProperties;

import java.util.HashMap;
import java.util.Map;

import org.techtown.myandriodproject.R;

public class registerActivity extends AppCompatActivity {
    Button nextbutton;
    private EditText email_join;
    private EditText pwd_join;
    private EditText name_join, birth_join;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference= firebaseDatabase.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email_join = (EditText) findViewById(R.id.email_join);
        pwd_join = (EditText) findViewById(R.id.pwd_join);
        name_join = (EditText) findViewById(R.id.name_join);
        birth_join = (EditText) findViewById(R.id.birth_join2);
        nextbutton = findViewById(R.id.nextbutton);

        firebaseAuth = FirebaseAuth.getInstance();

        findViewById(R.id.nextbutton).setOnClickListener(onClickListener);
    }
    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.nextbutton:
                    signUp();
                    break;
            }
        }
    };


        private void signUp() {
            final String email = email_join.getText().toString().trim();
            final String pwd = pwd_join.getText().toString().trim();
            final String name = name_join.getText().toString().trim();
            final String birth = birth_join.getText().toString().trim();

            firebaseAuth.createUserWithEmailAndPassword(email, pwd)
                    .addOnCompleteListener(registerActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(registerActivity.this, "회원가입에 성공했습니다.",Toast.LENGTH_SHORT).show();
                            } else {
                                if(task.getException().toString() !=null) {
                                    Toast.makeText(registerActivity.this,"회원가입에 실패했습니다.",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }



                    });
        }



}
