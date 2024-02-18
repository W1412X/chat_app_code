package com.example.chat;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class Record {
    static private String PhoneBrand= Build.BRAND;
    static private String PhoneId=Build.MODEL;
    static private String AppVersion=BuildConfig.VERSION_NAME;
    private String NickName;
    private String Time;
    private String Page;
    public Record(Context context, String page){
        Record.this.Page=page;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Time= sdf.format(new Date());
        SharedPreferences sharedPreferences=context.getSharedPreferences("MyPrefs", MODE_PRIVATE);
        NickName=sharedPreferences.getString("nick_name","visitor");
        record_msg();
    }
    public void record_msg(){
        new WebSocketClient().connect("ws://39.101.160.55:8626");
    }
    public class WebSocketClient {
        private OkHttpClient client;
        private WebSocket webSocket;

        public void connect(String url) {
            Request request = new Request.Builder().url(url).build();
            client = new OkHttpClient();
            webSocket = client.newWebSocket(request, new WebSocketListener() {
                @Override
                public void onOpen(WebSocket webSocket, Response response) {
                    // 连接成功后发送消息
                    String message =getSendMessage();
                    webSocket.send(message);
                    System.out.println("Sent message: " + message);
                }

                @Override
                public void onMessage(WebSocket webSocket, String text) {
                    // 接收到服务器的消息
                    System.out.println("Received message: " + text);
                    disconnect();
                }

                @Override
                public void onClosed(WebSocket webSocket, int code, String reason) {
                    // 连接关闭
                    System.out.println("Connection closed");
                }

                @Override
                public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                    // 连接失败
                    System.out.println("Connection failed");
                }
            });
        }

        public void disconnect() {
            if (webSocket != null) {
                webSocket.close(1000, null);
            }
        }
    }
    public void record_login_msg(){
        new WebSocketClient().connect("ws://39.101.160.55:8626");
    }
    //获取手机机型以及时间等信息
    private String getSendMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.NickName).append("\n");
        sb.append(this.Page).append("\n");
        sb.append(this.Time).append("\n");
        sb.append(AppVersion).append("\n");
        sb.append(PhoneBrand).append("\n");
        sb.append(PhoneId).append("\n");
        return sb.toString();
    }
}
