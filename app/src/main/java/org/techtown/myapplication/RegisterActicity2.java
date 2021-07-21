package org.techtown.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

public class RegisterActicity2 extends AppCompatActivity {
    Button nextbutton2;
    private WebView daum_webView;
    private TextView daum_result;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_acticity2);

        nextbutton2=findViewById(R.id.nextbutton2);
        nextbutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RegisterActicity2.this, RegisterActivity3.class);
                startActivity(intent);
            }
        });

        daum_result=(TextView) findViewById(R.id.daum_result);

        init_webView();

        handler=new Handler();
    }

    private void init_webView() {
        daum_webView=(WebView) findViewById(R.id.daum_webview);
        daum_webView.getSettings().setJavaScriptEnabled(true);
        daum_webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        daum_webView.addJavascriptInterface(new AndroidBridge(), "TestApp");
        daum_webView.setWebChromeClient(new WebChromeClient());
        daum_webView.loadUrl("php파일 url 입력하기");
    }

    private class AndroidBridge {
        @JavascriptInterface
        public void setAddress(final String arg1, final String arg2, final String arg3) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    daum_result.setText(String.format("(%s) %s %s", arg1, arg2, arg3));
                    init_webView();
                }
            });
        }
    }

}