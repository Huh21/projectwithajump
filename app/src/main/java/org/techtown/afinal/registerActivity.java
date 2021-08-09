package org.techtown.afinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import org.jetbrains.annotations.NotNull;
import org.techtown.myandriodproject.R;

public class registerActivity extends AppCompatActivity {
    Button nextbutton;
    private EditText email_join;
    private EditText pwd_join;
    private EditText name_join, birth_join, daum_resultDetail_join, companyNmae_join;
    private TextView daum_result_join;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email_join = (EditText) findViewById(R.id.email_join);
        pwd_join = (EditText) findViewById(R.id.pwd_join);
        name_join = (EditText) findViewById(R.id.name_join);
        birth_join = (EditText) findViewById(R.id.birth_join2);
        daum_result_join=(TextView) findViewById(R.id.daum_result_join);
        daum_resultDetail_join=(EditText) findViewById(R.id.daum_resultDetail_join);
        companyNmae_join=(EditText) findViewById(R.id.companyName_join);
        nextbutton = findViewById(R.id.nextbutton);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("final");

        nextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //회원가입 처리 시작
                String email = email_join.getText().toString();
                String pwd = pwd_join.getText().toString();
                String name = name_join.getText().toString();
                String birth = birth_join.getText().toString();
                String daum1=daum_result_join.getText().toString();
                String daum2=daum_resultDetail_join.getText().toString();
                String companyName=companyNmae_join.getText().toString();

                //Firebase Auth 진행
                mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(registerActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                            UserAccount account = new UserAccount();
                            account.setIdToken(firebaseUser.getUid());
                            account.setEmailId(firebaseUser.getEmail());
                            account.setPassword(pwd);
                            account.setName(name);
                            account.setBirth(birth);
                            account.setDaum1(daum1);
                            account.setDaum2(daum2);
                            account.setCompanyName(companyName);

                            //setvalue(): database에 insert(삽입)
                            mDatabaseRef.child("UserAccount:").child(firebaseUser.getUid()).setValue(account);

                            Toast.makeText(registerActivity.this, "회원가입에 성공하셨습니다", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(registerActivity.this, "회원가입에 실패하셨습니", Toast.LENGTH_SHORT).show();
                        }
                    }


                });
            }
        });


    }
}