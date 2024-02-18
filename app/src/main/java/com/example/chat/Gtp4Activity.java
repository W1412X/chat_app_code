package com.example.chat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.core.view.GestureDetectorCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Map;
import java.util.Random;

public class Gtp4Activity extends AppCompatActivity {
    private WebView web;
    public String versionname;
    public ProgressBar loading_view;
    private boolean doubleBackToExitPressedOnce = false;
    private Button tool_bar_button;
    private Button ad_web_page_button;
    private Button update_button;
    public String latest_version_url="https://raw.githubusercontent.com/W1412X/chat_app_update/main/version.json";
    public String download_url="https://raw.githubusercontent.com/W1412X/chat_app_update/main/app-debug.apk";
    private CardView tool_list;
    private Bundle extras;
    private Button fuck_phone_button;
    private RelativeLayout total_layout;
    private Button source_web_button;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        extras=getIntent().getExtras();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gtp4);
        web=findViewById(R.id.gpt4_web_view_page);
        loading_view=findViewById(R.id.gpt4_web_load_progressbar);
        try{
            web.setWebViewClient(new My_Web_ViewClient());
        }catch (Exception e){
            String s=e.getMessage().toString();
        }
        try{
            new Record(Gtp4Activity.this,"About");
        }catch(Exception e){

        }
        /*source_web_button=findViewById(R.id.gpt4_source_web);
        source_web_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url=web.getUrl();
                Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse(url));
                startActivity(intent);
            }
        });*/

        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setDomStorageEnabled(true);
        versionname=BuildConfig.VERSION_NAME;
        web.loadUrl("https://chat.zonas.wang");
        AlertDialog.Builder builder=new AlertDialog.Builder(Gtp4Activity.this);
        builder.setTitle("提示");
        builder.setMessage("这个界面就是相当于直接使用浏览器访问了网站https://chat.zonas.wang(虽然加上通用的弹窗和跳转拦截，但是网站本来就没有貌似)\n点击'进入源站'按钮直接在浏览器访问");
        builder.setNegativeButton("进入源站", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String url=web.getUrl();
                Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse(url));
                startActivity(intent);
                Toast.makeText(Gtp4Activity.this,"即将打开默认浏览器",Toast.LENGTH_SHORT).show();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }
    @Override
    protected void onStart(){
        super.onStart();
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK&&web.canGoBack()){
            web.goBack();
            return true;
        }
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
            return false;
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
            if(!view.getUrl().toString().contains("zonas")){
                return true;
            }
            return super.shouldOverrideUrlLoading(view,request);
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
            onBackPressed();
            Toast.makeText(Gtp4Activity.this,"请检查你的网络连接",Toast.LENGTH_SHORT).show();
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