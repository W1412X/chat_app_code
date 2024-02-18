package com.example.chat;

import android.os.Handler;
import android.webkit.ValueCallback;
import android.widget.Toast;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WatchChatWebSocket extends WebSocketClient {//一个类用于获取房间码，接收消息，发送消息
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
    private OnMessageReceivedListener messageListener;
    public void setMessageListener(OnMessageReceivedListener listener){
        this.messageListener=listener;
    }
    public WatchChatWebSocket(String serverUri,String nick_name){//1->create 2->enter
        super(URI.create(serverUri));WatchChatWebSocket.this.nick_name=nick_name;
    }
    @Override
    public void onOpen(ServerHandshake handshakedata){
        // 连接建立后的操作，可在此处进行握手等逻辑
        //连接后发送消息来啦
        try{
            send(nick_name+" 来啦 >_<");
        }catch (Exception e){
        }
    }
    @Override
    public void onMessage(String message) {
        if(message.startsWith("ad")){
            messageListener.onMessageReceived(message);
        }else{
            if(messageListener!=null){
                messageListener.onMessageReceived(message);
            }
        }
    }
    @Override
    public void onClose(int code, String reason, boolean remote) {//keep connect
        // 连接关闭后的操作
    }
    @Override
    public void onError(Exception ex) {
        // 发生错误时的处理逻辑
    }


    public interface OnMessageReceivedListener {
        void onMessageReceived(String message);
    }
}
