package com.example.chat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class WithOutAdActivity extends AppCompatActivity {
    private WebView web;
    private EditText editText;
    private ProgressBar loading_view;
    private Button enter_button;
    private String target_url;
    private String domain;
    private Button tips_button;
    private String get_domain(){
        String regex="^(https?://)?([a-zA-Z0-9.-]+)\\b";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(target_url);
        if(matcher.find()){
            return matcher.group(2).toString();
        }
        return null;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_out_ad);
        web=findViewById(R.id.ad_prevent_webview);
        editText=findViewById(R.id.ad_edit_text);
        enter_button=findViewById(R.id.ad_prevent_enter_button);
        enter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s=editText.getText().toString();
                target_url=s;
                web.setVisibility(View.VISIBLE);
                web.setWebViewClient(new WithOutAdActivity.My_Web_ViewClient());
                web.getSettings().setJavaScriptEnabled(true);
                findViewById(R.id.ad_text_notice).setVisibility(View.GONE);
                web.getSettings().setDomStorageEnabled(true);
                editText.setVisibility(View.GONE);
                enter_button.setVisibility(View.GONE);
                web.loadUrl(s);
                domain=get_domain();
            }
        });
        //tips_button
        tips_button=findViewById(R.id.web_ad_tip_button);
        tips_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(WithOutAdActivity.this);
                builder.setTitle("密码验证");
                EditText input=new EditText(WithOutAdActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String correct_passwd=getString((R.string.ad_page_tip_passwd));
                        String password = input.getText().toString();
                        if(password.equals(correct_passwd)){
                            AlertDialog.Builder builder1=new AlertDialog.Builder(WithOutAdActivity.this);
                            builder1.setTitle("TIPS");
                            builder1.setMessage(R.string.ad_page_tips);
                            builder1.show();
                            builder1.setPositiveButton("OK",null);
                        }
                        else{
                            Toast.makeText(WithOutAdActivity.this,"密码错误",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel(); // 取消对话框
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK&&web.canGoBack()){
            web.goBack();
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }
    class My_Web_ViewClient extends WebViewClient {
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            String url=request.getUrl().toString();
            // 判断请求头中是否包含特定字段
            if (url.contains("notice.txt")) {
                return new WebResourceResponse("text/html", "UTF-8", null);
            }
            // 正常处理请求
            return super.shouldInterceptRequest(view, request);
        }
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url=request.getUrl().toString();
            if(url.contains(domain)){
                return super.shouldOverrideUrlLoading(view,request);
            }
            else{
                return true;
            }
        }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            // 隐藏WebView控件
            web.setVisibility(View.GONE);
            web.setVisibility(View.GONE);
            Toast.makeText(WithOutAdActivity.this,"请检查你的网络连接",Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            if(loading_view!=null&&loading_view.getVisibility()==View.VISIBLE){
                loading_view.setVisibility(View.GONE);
            }
            super.onPageFinished(view, url);
        }
    }
}
