package org.techtown.myandriodproject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    TextView clock1, clock2;
    private static Handler handler;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //툴바
        toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //현재시각
        handler= new Handler(){
            @Override
            public void handleMessage(Message msg){
                Calendar cal= Calendar.getInstance();

                SimpleDateFormat stf1= new SimpleDateFormat("yyyy년 MM월 dd일");
                SimpleDateFormat stf2= new SimpleDateFormat("hh시 mm분 ss초");
                String strTime1= stf1.format(cal.getTime());
                String strTime2= stf2.format(cal.getTime());

                clock1= findViewById(R.id.nowTime1);
                clock1.setText(strTime1);
                clock2= findViewById(R.id.nowTime2);
                clock2.setText(strTime2);
            }
        };

        class NewRunnable implements Runnable{
            @Override
            public void run(){
                while(true){
                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    handler.sendEmptyMessage(0);
                }
            }
        }

        NewRunnable nr= new NewRunnable();
        Thread t=new Thread(nr);
        t.start();
    }

    //옵션 메뉴 관련 메소드
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_button,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setup:
                Intent setupIntent = new Intent(getApplicationContext(), SetUp.class);
                startActivity(setupIntent);
                return true;
            case R.id.history:
                Intent historyIntent = new Intent(getApplicationContext(), History.class);
                startActivity(historyIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}