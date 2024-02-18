package com.example.chat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.materialswitch.MaterialSwitch;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.spec.ECField;
import java.util.Timer;
import java.util.TimerTask;
import java.util.WeakHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RoomToWatchActivity extends AppCompatActivity {
    private String port;
    private String room_id;
    private String video_url;
    private String nick_name;
    private boolean if_first=false;
    private String domain="https://www.runoob.com/runcode";
    private String total_time="00:00";//second
    private WebView webView;
    private TextView loading_background;
    private ProgressBar loading_view;
    private WatchChatWebSocket watchChatWebSocket;
    private ImageButton send_button;
    private EditText msg_input;
    private LinearLayout msg_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_to_watch);


        //record
        try{
            new Record(RoomToWatchActivity.this,"About");
        }catch(Exception e){

        }

        port=getIntent().getStringExtra("port");
        room_id=getIntent().getStringExtra("room_id");
        video_url=getIntent().getStringExtra("video_url");
        webView=findViewById(R.id.room_video_web);
        webView.getSettings().setUserAgentString("Mozilla/5.0");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new My_Web_ViewClient());
        watchChatWebSocket=new WatchChatWebSocket("ws://39.101.160.55:"+port,nick_name);
        update_time();
        //
        /*// 支持缩放
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        // 设置插件状态
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        // 设置全屏播放模式
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);
                // 进入全屏模式
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                setContentView(view);
            }

            @Override
            public void onHideCustomView() {
                super.onHideCustomView();
                // 退出全屏模式
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                setContentView(webView);
            }
        });*/
        webView.loadUrl("http://39.101.160.55/watch_html/"+room_id+".html");
        loading_view=findViewById(R.id.room_video_progress_bar);

        //设置用户名
        SharedPreferences sharedPreferences=getSharedPreferences("MyPrefs", MODE_PRIVATE);
        nick_name=sharedPreferences.getString("nick_name","visitor");

        connectWebSocket();
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sendPeriodicMessage();
            }
        },1000);
        if(isOnlyContainSpace(nick_name)){
            nick_name="visitor";
        }
        //消息列表的初始化
        msg_container=findViewById(R.id.watch_together_msg_list);

        //发送按钮与输入框的设置
        send_button=findViewById(R.id.watch_together_send_button);
        msg_input=findViewById(R.id.watch_together_edit_text);
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(msg_input.getVisibility()==View.GONE){
                    msg_input.setVisibility(View.VISIBLE);
                    return;
                }else if(msg_input.getVisibility()==View.VISIBLE&&msg_input.getText().toString().replaceAll(" ","").equals("")){
                    msg_input.setVisibility(View.GONE);
                    return;
                }else{
                    String msg=msg_input.getText().toString();
                    try{
                        synchronized (this) {
                            msg = current_time_normal+ "\n" + "@" + nick_name + ": " + msg+"\n";
                            watchChatWebSocket.send(msg);
                            Toast.makeText(RoomToWatchActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                            msg_input.setText("");
                            msg_input.setVisibility(View.GONE);
                        }
                    }catch (Exception e){
                        Toast.makeText(RoomToWatchActivity.this,"发送失败",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
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
            return false;
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
            webView.setVisibility(View.GONE);
            webView.setVisibility(View.GONE);
            Toast.makeText(RoomToWatchActivity.this,"请检查你的网络连接",Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            if(loading_view!=null&&loading_view.getVisibility()==View.VISIBLE){
                loading_view.setVisibility(View.GONE);
            }
            Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    play();
                    Handler handler1=new Handler();
                    handler1.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            get_total_time();
                        }
                    },500);
                }
            },1000);
            super.onPageFinished(view, url);
        }
    }


    public boolean isOnlyContainSpace(String str) {
        String trimmedStr = str.trim();
        return trimmedStr.isEmpty();
    }



    private String current_time="0";
    private String current_time_normal="00:00";
    public String get_time_now_normal(){
        webView.evaluateJavascript("document.getElementsByClassName('current-time')[0].textContent", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                RoomToWatchActivity.this.current_time_normal=value.replaceAll("\"","").toString();
            }
        });
        return current_time_normal;
    }
    public void get_time_now_sec(){//sec
        webView.evaluateJavascript("document.getElementsByClassName('current-time')[0].textContent", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                RoomToWatchActivity.this.current_time=timeToSeconds(value.replaceAll("\"","").toString());
            }
        });
    }
    public void adjust_play_time(String time){//sec
        String adjust_time_code="var video = document.getElementById('player-con');\n" +
                "var all_time_str="+total_time+";\n" +
                "var all_time=parseInt(all_time_str);\n" +
                "var mouseOverEvent = new MouseEvent('mouseover', {\n" +
                "    bubbles: true,\n" +
                "    cancelable: true\n" +
                "});\n" +
                "var time_str="+time+";\n" +
                "var time=parseInt(time_str);\n" +
                "video.dispatchEvent(mouseOverEvent);\n" +
                "var progressBar = document.getElementsByClassName('prism-progress')[0];\n" +
                "var rect = progressBar.getBoundingClientRect();\n" +
                "var x = rect.left + rect.width*(time/all_time);\n" +
                "var y = rect.top + rect.height * 0.5;\n" +
                "var mouseClickEvent = new MouseEvent('click', {\n" +
                "    clientX: x,\n" +
                "    clientY: y,\n" +
                "    bubbles: true,\n" +
                "    cancelable: true\n" +
                "});\n" +
                "document.elementFromPoint(x, y).dispatchEvent(mouseClickEvent);";
        webView.evaluateJavascript(adjust_time_code, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                Toast.makeText(RoomToWatchActivity.this,"同步成功",Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void connectWebSocket() {
            watchChatWebSocket=new WatchChatWebSocket("ws://39.101.160.55:"+port,nick_name);
            watchChatWebSocket.setMessageListener(new WatchChatWebSocket.OnMessageReceivedListener() {
                @Override
                public void onMessageReceived(String message) {
                    // 处理接收到的消息
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 在 UI 线程更新 UI 组件或显示消息
                            if(message.startsWith("ad")){
                                String a="123";
                                return;
                            }
                            Toast.makeText(RoomToWatchActivity.this,"get",Toast.LENGTH_SHORT).show();
                            msg_container.addView(new MsgView(RoomToWatchActivity.this,message));
                        }
                    });
                }
            });
            watchChatWebSocket.connect();
    }




    public void play(){
        // 在页面加载完成后执行点击操作
        String script1 = "document.getElementsByClassName('prism-big-play-btn')[0].click();";
        String script2 = "document.getElementsByClassName('prism-big-play-btn pause')[0].click();";
        String script3 = "document.getElementsByClassName('prism-big-play-btn playing')[0].click();";

        // 执行点击操作
        webView.loadUrl("javascript:" + script1);
        webView.loadUrl("javascript:" + script2);
        webView.loadUrl("javascript:" + script3);
    }
    public static boolean hasOtherNumbers(String str) {
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c) && c != '0') {
                return true;
            }
        }
        return false;
    }
    public void get_total_time(){
        String code="var total_time=document.getElementsByClassName('duration')[0];\n" +
                "total_time.textContent;";
        webView.evaluateJavascript(code, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                value=value.replaceAll("\"","");
                total_time=timeToSeconds(value);
                if(hasOtherNumbers(total_time)){
                    Toast.makeText(RoomToWatchActivity.this,total_time,Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    Handler handler=new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            get_total_time();
                        }
                    },500);
                }
            }
        });
    }
    public String timeToSeconds(String time) {
        // 分割时间字符串
        String[] parts = time.split(":");

        int hours = 0;
        int minutes = 0;
        int seconds = 0;

        if (parts.length == 3) {
            hours = Integer.parseInt(parts[0]);
            minutes = Integer.parseInt(parts[1]);
            seconds = Integer.parseInt(parts[2]);
        } else if (parts.length == 2) {
            minutes = Integer.parseInt(parts[0]);
            seconds = Integer.parseInt(parts[1]);
        } else if (parts.length == 1) {
            seconds = Integer.parseInt(parts[0]);
        }

        // 计算总秒数
        int totalSeconds = hours * 3600 + minutes * 60 + seconds;

        return String.valueOf(totalSeconds);
    }

    class MsgView extends androidx.appcompat.widget.AppCompatTextView {
        private String msg;
        public MsgView(Context context,String text) {
            super(context);
            msg=text;
            setText(msg);
            setTextColor(Color.parseColor("#ffffff"));
            setTextSize(15);
            setMaxWidth(140);
            Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setVisibility(View.GONE);
                }
            },60000);
        }
    }

    private void update_time() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // 每隔1秒发送一条时间信息
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        get_time_now_normal();
                    }
                });
            }
        }, 0, 1000); // 0表示立即开始，1000表示每隔1秒执行一次
    }

     void sendPeriodicMessage() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // 每隔5秒发送一条消息
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String message = "time "+timeToSeconds(current_time_normal);
                        watchChatWebSocket.send(message);
                    }
                });
            }
        }, 0, 1000); // 0表示立即开始，5000表示每隔5秒执行一次
    }

}