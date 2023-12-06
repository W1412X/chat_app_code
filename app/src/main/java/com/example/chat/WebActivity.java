package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
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
public class WebActivity extends AppCompatActivity {
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
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        web=findViewById(R.id.web_view_page);
        loading_view=findViewById(R.id.web_load_progressbar);
        web.setWebViewClient(new My_Web_ViewClient());
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setDomStorageEnabled(true);
        tool_bar_button=findViewById(R.id.draggableButton);
        tool_list=findViewById(R.id.other_tool_list);
        versionname=BuildConfig.VERSION_NAME;
        web.loadUrl("https://chat18.aichatos.xyz/");
        //添加可拖拽按钮的逻辑
        tool_bar_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tool_list.getVisibility()==View.GONE){
                    tool_list.setVisibility(View.VISIBLE);
                }
                else{
                    tool_list.setVisibility(View.GONE);
                }
            }
        });
        //网站拦截页面的按钮
        ad_web_page_button=findViewById(R.id.prevent_ad_button);
        ad_web_page_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(WebActivity.this,WithOutAdActivity.class);
                startActivity(intent);
            }
        });
        //更新按钮
        update_button=findViewById(R.id.update_version_button);
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckUpdate checkUpdate=new CheckUpdate();
                CheckUpdate.CheckVersionTask checkVersionTask=checkUpdate.new CheckVersionTask();
                checkVersionTask.execute(latest_version_url);
            }
        });
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
        if(keyCode==KeyEvent.KEYCODE_BACK&&!web.canGoBack()){
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "再按一次返回键退出应用", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> doubleBackToExitPressedOnce=false, 2000);
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
            Toast.makeText(WebActivity.this,"请检查你的网络连接",Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            if(loading_view!=null&&loading_view.getVisibility()==View.VISIBLE){
                loading_view.setVisibility(View.GONE);
            }
            super.onPageFinished(view, url);
        }
    }
    public class CheckUpdate{
        public class DownloadApkAsyncTask extends AsyncTask<String, Integer, Boolean> {

            private Context context;

            public DownloadApkAsyncTask(Context context) {
                this.context = context;
            }

            @Override
            protected Boolean doInBackground(String... strings) {
                try {
                    String apkUrl = strings[0];
                    String apkFileName = "chat.apk";
                    apkUrl=download_url;
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(apkUrl));
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, apkFileName);
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                    long downloadId = downloadManager.enqueue(request);

                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (result) {
                    // 下载成功，触发安装
                    String apkFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/app.apk";
                    Uri apkUri = Uri.fromFile(new File(apkFilePath));
                    Intent installIntent = new Intent(Intent.ACTION_VIEW);
                    installIntent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                    installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(installIntent);
                } else {
                }
            }
        }
        public int versioncode;
        public String new_version;
        public class CheckVersionTask extends AsyncTask<String,Integer,String>{
            @Override
            protected  void onPreExecute(){
                loading_view.setVisibility(View.VISIBLE);

            }
            @Override
            protected String doInBackground(String... strings) {
                URL versionUrl= null;
                try {
                    versionUrl = new URL(latest_version_url);
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
                HttpURLConnection connection= null;
                try {
                    connection = (HttpURLConnection) versionUrl.openConnection();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                } catch (ProtocolException e) {
                    throw new RuntimeException(e);
                }
                try {
                    connection.setReadTimeout(3000);
                    if(connection.getResponseCode()==HttpURLConnection.HTTP_OK){
                        InputStream inputStream=connection.getInputStream();
                        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader((inputStream)));
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line);
                        }
                        bufferedReader.close();
                        inputStream.close();
                        JSONObject jsonObject=new JSONObject(stringBuilder.toString());
                        new_version=jsonObject.getString("version");
                        versioncode = BuildConfig.VERSION_CODE;
                        versionname=BuildConfig.VERSION_NAME;
                        if(versionname.equals(new_version)){
                            return "no new version";
                        }
                        else{
                            return "new version";
                        }
                    }
                } catch (IOException e) {
                    return "time out";
                } catch (JSONException e) {
                    return "version check error";
                }
                return "nothing";
            }
            public void showConnectionErrorDialog(){
                AlertDialog.Builder builder =new AlertDialog.Builder(WebActivity.this);
                builder.setTitle("连接超时，检查你的网络连接");
                builder.setPositiveButton("确认", null);
                builder.show();
            }
            public void showConfirmDialog(){
                Toast.makeText(WebActivity.this,"发现新版本",Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder =new AlertDialog.Builder(WebActivity.this);
                builder.setTitle("有新的版本可以更新");
                builder.setMessage("当前版本为"+ versionname +",最新版本为"+ new_version);
                builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CheckUpdate checkUpdate=new CheckUpdate();
                        CheckUpdate.DownloadApkAsyncTask downloadApkAsyncTask=checkUpdate.new DownloadApkAsyncTask(WebActivity.this);
                        downloadApkAsyncTask.execute(latest_version_url);
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.show();
            }
            public void showDialogNewest(){
                AlertDialog.Builder builder =new AlertDialog.Builder(WebActivity.this);
                builder.setTitle("当前版本已为最新版本"+ versionname);
                builder.setPositiveButton("确认", null);
                builder.show();
            }
            public void showDialogSourceError(){
                AlertDialog.Builder builder =new AlertDialog.Builder(WebActivity.this);
                builder.setTitle("检查版本错误，请联系开发者邮箱tor108@outlook.com");
                builder.setPositiveButton("确认", null);
                builder.show();
            }
            @Override
            protected void onPostExecute(String result) {
                loading_view.setVisibility(View.GONE);
                if(result.equals("no new version")){
                    showDialogNewest();
                }
                else if(result.equals("time out")){
                    showConnectionErrorDialog();
                }
                else if(result.equals("version check error")){
                    showDialogSourceError();
                }
                else if(result.equals("nothing")){
                    showDialogSourceError();
                }
                else if(result.equals("new version")){
                    showConfirmDialog();
                }
            }
        }
    }
}