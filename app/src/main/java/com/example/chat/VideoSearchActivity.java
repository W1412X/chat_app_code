package com.example.chat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VideoSearchActivity extends AppCompatActivity {
    private WebView page_web;
    private ProgressBar loading_view;
    private Integer op=1;
    private String domain="ccyy6";
    private Button find_source_button;
    private String target_url="https://www.ccw5.cc/";
    private String video_url;
    private  Button enter_room_button;
    private String input_room_id;//get fro the enter room button' dialog
    private WatchTogetherWebSocket webSocketClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            setContentView(R.layout.activity_video_search);
        }catch (Exception e){
            String s=e.getMessage().toString();
        }
        //record
        try{
            new Record(VideoSearchActivity.this,"About");
        }catch(Exception e){

        }


        find_source_button=findViewById(R.id.video_search_page_find_source_button);
        page_web=findViewById(R.id.video_get_web);
        loading_view=findViewById(R.id.video_get_page_progressbar);
        //set the web's attr
        page_web.getSettings().setJavaScriptEnabled(true);
        page_web.getSettings().setUserAgentString("Mozilla/5.0");
        page_web.getSettings().setDomStorageEnabled(true);
        page_web.loadUrl("https://www.ccyy6.cc/");
        try{
            page_web.setWebViewClient(new My_Web_ViewClient());
        }catch (Exception e){
            String s=e.getMessage().toString();
        }
        //设置使用房间号进入房间的按钮逻辑
        enter_room_button=findViewById(R.id.video_search_page_enter_room_by_passwd_button);
        enter_room_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                op=2;


                AlertDialog.Builder builder=new AlertDialog.Builder(VideoSearchActivity.this);
                builder.setTitle("输入房间码");
                EditText input=new EditText(VideoSearchActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        input_room_id=input.getText().toString();
                        try{
                            //获取服务器响应
                            try{
                                new GetRoomId().execute();
                            }catch (Exception e){
                                Toast.makeText(VideoSearchActivity.this,"无法链接到服务器",Toast.LENGTH_SHORT).show();
                                Log.e("TAG", "Error message: " + e.getMessage(), e);
                            }
                        }catch (Exception e){
                            Toast.makeText(VideoSearchActivity.this,"无法链接到服务器",Toast.LENGTH_SHORT).show();
                            Log.e("TAG", "Error message: " + e.getMessage(), e);
                        }
                    }
                });
                builder.show();
            }
        });
        //设置这个资源嗅探按钮的监听
        //需要向下一个界面传递参数信息url
        find_source_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                op=1;
                //设置加载界面展示
                if(video_url.contains("null")){
                    Toast.makeText(VideoSearchActivity.this,"未嗅探到资源，请稍后再试",Toast.LENGTH_SHORT).show();
                    return;
                }
                try{
                    loading_view.setVisibility(View.VISIBLE);
                }catch (Exception e){
                    Toast.makeText(VideoSearchActivity.this,"无法链接到服务器",Toast.LENGTH_SHORT).show();
                    Log.e("TAG", "Error message: " + e.getMessage(), e);
                }

                //获取服务器响应
                try{
                    new GetRoomId().execute();
                }catch (Exception e){
                    Toast.makeText(VideoSearchActivity.this,"无法链接到服务器",Toast.LENGTH_SHORT).show();
                    Log.e("TAG", "Error message: " + e.getMessage(), e);
                }
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK&&page_web.canGoBack()){
            page_web.goBack();
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

    //filter
    class My_Web_ViewClient extends WebViewClient {
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            String url=request.getUrl().toString();
            // 判断请求头中是否包含特定字段
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
            Handler handler=new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    loading_view.setVisibility(View.VISIBLE);
                    find_source_button.setVisibility(View.GONE);
                }
            });
            super.onPageStarted(view, url, favicon);
        }
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            // 隐藏WebView控件
            page_web.setVisibility(View.GONE);
            page_web.setVisibility(View.GONE);
            Toast.makeText(VideoSearchActivity.this,"请检查你的网络连接",Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            try{
                if(isInPlayPage(page_web.getUrl().toString())){//如果是在视频播放界面，则获取链接
                    get_video_url_circle();
                }
                else{
                    if(loading_view!=null&&loading_view.getVisibility()==View.VISIBLE){
                        loading_view.setVisibility(View.GONE);
                    }
                }
            }catch (Exception e){
                Log.e("tag",e.getMessage());
            }
            super.onPageFinished(view, url);
        }
    }


    public String extractUrl(String input) {//used to extract url from the father url
        String pattern = "url=(.*)";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(input);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
    public boolean isInPlayPage(String url) {
        String pattern = "https://([^/]+/){3}.*[^/]";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(url);
        return matcher.matches();
    }

//字线程
    private class GetRoomId extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                reconnectToServer();//重新连接服务器
            } catch (Exception e) {
                //
                Toast.makeText(VideoSearchActivity.this,"获取失败",Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    //建立房间的逻辑
    public class WatchTogetherWebSocket extends WebSocketClient {//一个类用于获取房间码，接收消息，发送消息
        public boolean isOnlyContainSpace(String str) {
            String trimmedStr = str.trim();
            return trimmedStr.isEmpty();
        }
        private Integer op=1;
        public String extractNumbers(String str) {//获取数字房间号
            Pattern pattern = Pattern.compile("\\d+");  // 匹配一个或多个数字
            Matcher matcher = pattern.matcher(str);

            StringBuilder sb = new StringBuilder();
            while (matcher.find()) {
                sb.append(matcher.group());
            }

            return sb.toString();
        }
        private String nick_name;
        public WatchTogetherWebSocket(String serverUri,Integer op){//1->create 2->enter
            super(URI.create(serverUri));
            //get the nick_name
            WatchTogetherWebSocket.this.op=op;
            SharedPreferences sharedPreferences=getSharedPreferences("MyPrefs", MODE_PRIVATE);
            nick_name=sharedPreferences.getString("nick_name","visitor");
            if(isOnlyContainSpace(nick_name)){
                nick_name="visitor";
            }
        }
        @Override
        public void onOpen(ServerHandshake handshakedata){
            // 连接建立后的操作，可在此处进行握手等逻辑
            //申请建立房间
            if(op==1){
                send("cr "+video_url);
            } else if (op==2) {
                send("id "+input_room_id);//send msg to certificate
            }
        }
        @Override
        public void onMessage(String message) {
            try {
                Handler handler = new Handler(Looper.getMainLooper());
// 在非 UI 线程中执行后台任务
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 执行耗时操作
                        // 将结果传递给 UI 线程
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(message.toString().startsWith("room_id")){//在这里执行成功获取房间号后的操作
                                    String[] msg=message.split(" ");
                                    AlertDialog.Builder builder=new AlertDialog.Builder(VideoSearchActivity.this);
                                    if(op==1){
                                        builder.setTitle("房间号");
                                    }else{
                                        builder.setTitle("查询成功");
                                    }
                                    builder.setMessage(msg[1]);
                                    Toast.makeText(VideoSearchActivity.this,"请尽快进入房间，否则房间号将失效", Toast.LENGTH_SHORT).show();
                                    builder.setPositiveButton("进入房间", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent=new Intent(VideoSearchActivity.this,RoomToWatchActivity.class);
                                            intent.putExtra("port",msg[3]);
                                            intent.putExtra("room_id",msg[1]);
                                            startActivity(intent);
                                        }
                                    });
                                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel(); // 取消对话框
                                        }
                                    });
                                    AlertDialog dialog = builder.create();
                                    loading_view.setVisibility(View.GONE);
                                    dialog.show();
                                    onClose(1000,"normal",true);//close the connection
                                }else if(message.toString().startsWith("error")){
                                    if(op==1){
                                        Toast.makeText(VideoSearchActivity.this,"获取房间码失败", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(VideoSearchActivity.this,"房间码不存在", Toast.LENGTH_SHORT).show();
                                    }

                                }
                                // 在 UI 线程中更新视图
                            }
                        });
                    }
                }).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onClose(int code, String reason, boolean remote) {
            // 连接关闭后的操作
        }

        @Override
        public void onError(Exception ex) {
            // 发生错误时的处理逻辑
            Toast.makeText(VideoSearchActivity.this,"unknown error",Toast.LENGTH_SHORT).show();
        }
    }


    private void reconnectToServer() {
        try {
            webSocketClient=new VideoSearchActivity.WatchTogetherWebSocket("ws://39.101.160.55:8627",op);
            try {
                webSocketClient.connect();
            }catch (Exception e){
                Log.e("TAG", "Error message: " + e.getMessage(), e);
            }
            //webSocketClient.send(model); // 发送握手字符串,握手字符串是model name
        } catch (Exception e) {
            Toast.makeText(VideoSearchActivity.this, "连接到服务器失败", Toast.LENGTH_SHORT).show();
        }
    }


    //由于网络不稳定，所以需要循环获取资源
    public void get_video_url_circle(){
        loading_view.setVisibility(View.VISIBLE);
        String get_url_code="var box=document.getElementById('playerbox');" +
                "var url=box.children[2]['src'];" +
                "url;";
        Handler handler=new Handler();
        try {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(loading_view!=null&&loading_view.getVisibility()==View.VISIBLE){
                        loading_view.setVisibility(View.GONE);
                    }
                    Toast.makeText(VideoSearchActivity.this, "尝试获取资源链接", Toast.LENGTH_SHORT).show();
                    page_web.evaluateJavascript(get_url_code, new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            try{
                                if(value.contains("null")){//如果返回的是null，说明网路资源仍为家在完成
                                    loading_view.setVisibility(View.VISIBLE);
                                    get_video_url_circle();
                                    return;
                                }
                                find_source_button.setVisibility(View.VISIBLE);
                                video_url = extractUrl(value).replaceAll("\"", "");
                            }catch (Exception e){
                                Toast.makeText(VideoSearchActivity.this,"加载失败",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }, 500);
        }catch (Exception e){
            Log.e("tag",e.getMessage());
        }
    }
}