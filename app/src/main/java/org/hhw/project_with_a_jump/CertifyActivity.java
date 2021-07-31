package org.hhw.project_with_a_jump;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

public class CertifyActivity extends AppCompatActivity {
    EditText editText;
    EditText editText2;
    public static WebView web;
    public static WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certify);

        web = findViewById(R.id.pass);

        editText=(EditText) findViewById(R.id.editText);
        editText2=(EditText) findViewById(R.id.editText2);

    }

    public void Button1Clicked(View V){
        String inputName=this.editText.getText().toString().trim();
        String inputNum=this.editText2.getText().toString().trim();

        if(inputName.length()>0 && inputNum.length()>0){
            Intent intent= new Intent(getApplicationContext(), MainActivity.class);

            intent.putExtra("name", inputName);
            intent.putExtra("num", Integer.parseInt(inputNum));
            startActivity(intent);
        }
    }
    public void Button2Clicked(View V){
        webV = findViewById(R.id.pass);

        webView.setWebViewClient(new WebViewClient()); //클릭시 새창 뜨지않게
        webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.loadUrl("file:///android_asset/info.html");
    }
}
