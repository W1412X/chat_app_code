package com.example.chat;
import com.example.chat.R;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import io.noties.markwon.Markwon;

public class ChatPlusActivity extends AppCompatActivity {
    //设置不可点击的linearlayout
    private LinearLayout set_unclickable_linear_out;

    //历史记录列表
    private ScrollView history_scrollview;
    private LinearLayout history_linear_layout;
    private boolean if_first_open=true;//帮助添加历史列表
    //
    private boolean doubleBackToExitPressedOnce;
    public String latest_version_url="https://raw.githubusercontent.com/W1412X/chat_app_update/main/version.json";
    public String download_url="https://raw.githubusercontent.com/W1412X/chat_app_update/main/chat.apk";
    private ProgressBar loading_view;
    private String[] emojis={"(●'◡'●)","(๑￫ܫ￩)","@(｡･o･)@","@(o･ｪ･)@","ヽ( o･ｪ･)ﾉ","@(o･ｪ･o)@","٩(͡๏̯͡๏)۶","<゜)))彡","ζ°)))彡","くコ:彡","(:◎)≡",">°))))彡","<+ ))><<","<*)) >>=<","(°)#))<<","<・ )))><<","( ´_ゝ`)✎","✎～～～✐","ﾍ(･_|","｜−・;）","|_・)","|ω・）","|･ω･｀)","$_$","^_~","(´･_･`)","•﹏•","(O_o)(o_O)","(╯ε╰)","( ╯▽╰)","ヾ(^▽^*)))","（´v｀）","(≧∇≦)ﾉ","o(^▽^)o","(￣︶￣)↗","o(*￣▽￣*)o","(p≧w≦q)","ㄟ(≧◇≦)ㄏ","(/≧▽≦)/","(　ﾟ∀ﾟ) ﾉ♡","o(*￣︶￣*)o","(๑¯∀¯๑)","(≧∀≦)ゞ","φ(≧ω≦*)♪","(๑´ㅂ`๑)","╰(*°▽°*)╯","^O^","(´▽`ʃ♡ƪ)","ヽ(✿ﾟ▽ﾟ)ノ","(*^▽^*)","(u‿ฺu✿ฺ)","o(￣▽￣)ｄ","φ(゜▽゜*)♪","<(￣︶￣)>","(๑•̀ㅂ•́)و✧","テ_デ","□_□","┭┮﹏┭┮","╥﹏╥...","o(TヘTo)","〒▽〒","ε(┬┬﹏┬┬)3","(;´༎ຶД༎ຶ`)","(ノへ`、)","(￣ ‘i ￣;)","（-_-。）","(ノへ￣、)","（ ￣ー￣）","<(*￣▽￣*)/","(^▽^ )","o(*≧▽≦)ツ","ヾ(≧▽≦*)o","|ω・）","|･ω･｀)","◕ฺ‿◕ฺ✿ฺ)","つ﹏⊂","(* /ω＼*)","(*/ω＼*)","(′▽`〃)","(✿◡‿◡)","(。﹏。)","(/▽＼)","(๑´ㅂ`๑)","（＞人＜；）","<(－︿－)>","(ー`´ー)","（｀へ´）","(///￣皿￣)○～","（＝。＝）","→)╥﹏╥)","<)。(>","(* ￣︿￣)","(*￣︿￣)","（＃￣～￣＃）","￣へ￣","(⊙x⊙;)","( -'`-)","(lll￢ω￢)","o(一︿一+)o","(* ￣︿￣)","(┬＿┬)↘","凸(艹皿艹 )","o(≧口≦)o","(*Φ皿Φ*)","（≧0≦）","X﹏X","(* /ω＼*)","(/▽＼)","ヽ（≧□≦）ノ","～(　TロT)σ","(。﹏。)","(。﹏。*)","┗( T﹏T )┛","（＞人＜；）","（。・＿・。）ﾉ","o(￣┰￣*)ゞ","┏ (^ω^)=☞","[]~(￣▽￣)~*","(*ﾟｰﾟ)","ヾ(＠⌒ー⌒＠)ノ","(oﾟvﾟ)ノ","o(*^▽^*)┛","┗|*｀0′*|┛","<(￣ˇ￣)/","<(ˉ^ˉ)>","ʅ（´◔౪◔）ʃ","ㄟ(≧◇≦)ㄏ","︿(￣︶￣)～","(～￣▽￣)～","o(*￣︶￣*)o","♪(^∇^*)","φ(゜▽゜*)♪","︿(￣︶￣)︿","<(￣︶￣)>","(☄⊙ω⊙)☄","┌(。Д。)┐","o((⊙﹏⊙))o.","(*Φ皿Φ*)","ヽ(*。>Д<)o゜","X﹏X","(′д｀ )…彡…彡","(#｀-_ゝ-)","(┬＿┬)↘","(＞﹏＜)","（；´д｀）ゞ","╥﹏╥...","ε(┬┬﹏┬┬)3","(;´༎ຶД༎ຶ`)","(ノへ`、)","o(一︿一+)o","(。_。)","○|￣|_","((*・∀・）ゞ→→","╮(╯▽╰)╭","┑(￣Д ￣)┍","╮（╯＿╰）╭","(⊙﹏⊙)","(o_ _)ﾉ","￣△￣","(๑￫ܫ￩)","٩(͡๏̯͡๏)۶","ﾍ(･_|","(´･_･`)","テ_デ","(=^x^=)","◑▂◐","(눈~눈)","(o≖◡≖)","つ﹏⊂","(✿◡‿◡)","(。﹏。*)","（。・＿・。）ﾉ","┑(￣Д ￣)┍","(ー`´ー)","(；′⌒`)","￣へ￣","(。_。)","（*゜ー゜*）","(～o￣3￣)～","=￣ω￣=","（*＾-＾*）","o(*^＠^*)o","ヾ(´∀`o)+","(っ*´Д`)っ","(～￣(OO)￣)ブ","_(:驴」∠)_","ㄟ( ▔, ▔ )ㄏ","（o´・ェ・｀o）","’(°ー°〃)","(ーー゛)","(○´･д･)ﾉ","(*Φ皿Φ*)","wow~ ⊙o⊙","ヽ(*。>Д<)o゜","~(￣0￣)/","╰(*°‿°*)╯","Σ(｀д′*ノ)ノ","(⊙ˍ⊙)","（*゜ー゜*）","Ｏ(≧口≦)Ｏ","w(ﾟДﾟ)w","ｍ(o・ω・o)ｍ","(＾Ｕ＾)ノ~ＹＯ","♪(´∇`*)","⊙▽⊙","•﹏•","o((⊙﹏⊙))o.","o(￣┰￣*)ゞ","(⊙﹏⊙)","ㄟ( ▔, ▔ )ㄏ","￣△￣","(。_。)","（o´・ェ・｀o）","ﾍ(･_|","｜−・;）","^_~","(*/ω＼*)","o(￣ヘ￣o＃)","(￣┰￣*)","o(*^＠^*)o","(o゜▽゜)o☆","(～￣▽￣)～","<(*￣▽￣*)/","(눈▂눈)","(oﾟvﾟ)ノ","(＠￣ー￣＠)","<(ˉ^ˉ)>","(◡ᴗ◡✿)","<(￣ ﹌ ￣)@m","( ﹁ ﹁ ) ~→","(☆-ｖ-)","(´ｰ∀ｰ`)","┌( ´_ゝ` )┐","（ ￣ー￣）","(‾◡◝)","ʅ（´◔౪◔）ʃ","(o≖◡≖)","(￣_,￣ )","(ˉ▽￣～) 切~~","(；′⌒`)","( ￣ー￣)","( ‵▽′)ψ","ԅ(¯﹃¯ԅ)","( >ρ < ”)","┬┴┤_•)","( -'`-)","(=′ー`)","( -'`-; )","(・-・*)","┌|*´∀｀|┘","(σ｀д′)σ","( ｀д′)","⊙▾⊙","(￣m￣）","( ╯▽╰)","⊙▽⊙","( σ'ω')σ","（＞人＜；）","o(′益`)o","(ー`´ー)","（＝。＝）","(〃＞目＜)","o(≧口≦)o","Ｏ(≧口≦)Ｏ","（≧0≦）","(´･_･`)","•﹏•","╯_╰","(-@y@)","━┳━　━┳━","┌|*´∀｀|┘","╯_╰","○|￣|_","_〆(´Д｀ )","(lll￢ω￢)","|(*′口`)","(*￣rǒ￣)","━┳━　━┳━","○|￣|_ =3","(°ー°〃)","(￣△￣；)","(-@y@)","(*￣rǒ￣)","(☆´益`)c","◐▽◑","(＠_＠;)","( *⊙~⊙)","0＾）吞！","Ψ(￣∀￣)Ψ","(￣～～￣) 嚼！","(╯▽╰ )好香~~","ψ(｀∇´)ψ","┏ (^ω^)=☞","→_←","◑▂◐","( ‵▽′)ψ","ԅ(¯﹃¯ԅ)","=└(┐卍^o^)卍","ᕕ( ᐛ )ᕗ","（〃｀ 3′〃）","(￫ܫ￩)","_( ﾟДﾟ)ﾉ","(。・∀・)ノ","(ﾟｰﾟ)","ヽ(✿ﾟ▽ﾟ)ノ","（゜▽＾*））","(u‿ฺu✿ฺ)","φ(-ω-*)","(ﾟДﾟ*)ﾉ","(❤´艸｀❤)","|(*′口`)","(。・・)ノ","ヾ(´∀`o)+","(っ*´Д`)っ","(～￣(OO)￣)ブ","o(^▽^)o","ㄟ(≧◇≦)ㄏ","<(￣3￣)> 表！","♪(´∇`*)","(☆▽☆)","(ρ_・).。","(◡ᴗ◡✿)","(－∀＝)","(ーー゛)","ｍ(o・ω・o)ｍ","＼（＾▽＾）／","(◆゜∀゜）b","( ﾟдﾟ)つBye","┏(＾0＾)┛","(。・_・)/~~~","（＾∀＾●）ﾉｼ","(つω｀)～","ヾ(^▽^*)))","~(～￣▽￣)～","╰(￣▽￣)╭","=└(┐卍^o^)卍","( ﾟдﾟ)つBye","┏(＾0＾)┛","(*￣;(￣ *)","✧(≖ ◡ ≖✿)","(．． )…","￣O￣)ノ","(ToT)/~~~","(#`O′)","ヾ(´･ω･｀)ﾉ","ヾ(≧∇≦*)ゝ","(｡･∀･)ﾉﾞ","(ง •_•)ง","_〆(´Д｀ )","( >ρ < ”)","▄︻┻┳═一……","○|￣|_ =3","→)╥﹏╥)","(ﾉ*･ω･)ﾉ","^_~","…（⊙＿⊙；）…","┗( T﹏T )┛","～（□`）～","ヽ（≧□≦）ノ","￣△￣","(。・・)ノ","(っ*´Д`)っ","””(￣ー￣)/””","ヾ(≧O≦)〃嗷~","φ(≧ω≦*)♪","♪(^∇^*)","ﾍ(･_|","｜−・;）","|_・)","|ω・）","|･ω･｀)","( ´_ゝ`)✎","✎～～～✐","（。＾▽＾）","ヾ(●゜ⅴ゜)ﾉ","＜（＾－＾）＞","ヾ(＠⌒ー⌒＠)ノ","(≧∇≦)ﾉ","o(^▽^)o","( *︾▽︾)","╰(*°‿°*)╯","(≧∇≦)ﾉ","(＾－＾)V","^O^","(´▽`ʃ♡ƪ)","╰(￣▽￣)╭","︿(￣︶￣)︿","ヾ(≧▽≦*)o","(๑•̀ㅂ•́)و✧","(❤´艸｀❤)","┗|*｀0′*|┛","o(￣ε￣*)","＼（＾▽＾）／","(╯ε╰)","(*￣;(￣ *)","(っ´Ι`)っ","o(*￣3￣)o","（づ￣3￣）づ╭❤～","(*╯3╰)","!(*￣(￣　*)","(*￣3￣)╭","（○｀ 3′○）","(O_o)(o_O)","＼（＾▽＾）／","（づ￣3￣）づ╭❤～","(*╯3╰)","o(*￣▽￣*)o","[]~(￣▽￣)~*","(๑￫ܫ￩)","(=^x^=)","≡ω≡","o(=•ェ•=)m","U•ェ•*U","(*￣(エ)￣)","(+(工)+╬)","<゜)))彡","ζ°)))彡","くコ:彡","(:◎)≡",">°))))彡","<+ ))><<","<*)) >>=<","(°)#))<<","<・ )))><<","@(｡･o･)@","@(o･ｪ･)@","ヽ( o･ｪ･)ﾉ","@(o･ｪ･o)@","٩(͡๏̯͡๏)۶","_(:驴」∠)_","( ఠൠఠ )ﾉ","（￣。。￣）","٩(͡๏̯͡๏)۶","ﾍ(･_|","(´･_･`)","•﹏•","テ_デ","눈_눈","□_□","ԅ(¯﹃¯ԅ)","○|￣|_","(*ﾟｰﾟ)","(つω｀)～","◕ฺ‿◕ฺ✿ฺ)","(*/ω＼*)","(✿◡‿◡)","(/▽＼)","(・-・*)","╮(╯▽╰)╭","→)╥﹏╥)","<)。(>","￣へ￣","(⊙x⊙;)","ヽ(*。>Д<)o゜","(⊙ˍ⊙)","(。_。)","（*゜ー゜*）","o(〃'▽'〃)o","(ﾉ*･ω･)ﾉ","눈_눈","(눈益눈)","(눈160눈)","(눈▽눈)","(눈◇눈)","(屮눈皿눈)","(눈~눈)","(눈0눈)","(눈_눈)","$_$","(O_o)(o_O)","→_←","→_→","←_←"};
    private Button top_button;
    private Button tool_bar_button;
    private int loading_time=0;
    private Button update_button;
    private Button fuck_phone_button;
    private Button with_out_ad_button;
    private CardView other_tool_list;
    private ScrollView total_layout;
    private int screen_width;
    private String source_url;
    private String ans;
    private String pre_ans;
    private LinearLayout message_container;
    public ImageButton send_button;
    public ScrollView scrollView;
    public EditText msg_input;
    private WebView help_web;
    private Button send_suggestions_button;
    private ImageButton chat_history_list_button;
    private ImageButton download_chat_content_button;
    public class MessageAndUser extends LinearLayout {
        private boolean who;
        public class WhoButton extends androidx.appcompat.widget.AppCompatTextView {

            public WhoButton(boolean who) {
                super(ChatPlusActivity.this);
                setPadding(28,10,28,10);
                if(who){
                    setBackground(getResources().getDrawable(R.drawable.user_button));
                    setText("U");
                    setTextSize(19);
                    setGravity(Gravity.CENTER);
                    setTextColor(Color.parseColor("#aaffffff"));
                }
                else{
                    setBackground(getResources().getDrawable(R.drawable.robot_button));
                    setText("R");
                    setTextSize(19);
                    setGravity(Gravity.CENTER);
                    setTextColor(Color.parseColor("#aaffffff"));
                }
            }

            @Override
            public void destroyDrawingCache() {
                super.destroyDrawingCache();
            }
        }
        public class MsgTextView extends androidx.appcompat.widget.AppCompatTextView{

            public MsgTextView(boolean who,String msg){
                super(ChatPlusActivity.this);
                setTextIsSelectable(true);
                setSingleLine(false);
                setPadding(20,15,20,15);
                setMaxWidth((int)(screen_width*0.7));
                msg=msg.replaceAll("Copy Code","");
                msg=msg.replace("\\n","<br>");
                // 将 HTML 内容转换为 Spanned 对象
                Spanned spannedHtml = Html.fromHtml(msg, Html.FROM_HTML_MODE_COMPACT);
                setText(spannedHtml);
                //setText(msg);
                setTextColor(Color.parseColor("#ffffff"));
                //Markwon markwon=Markwon.create(ChatPlusActivity.this);
                //markwon.setMarkdown(MsgTextView.this,msg);
                if(who){
                    setGravity(Gravity.LEFT);
                    setBackground(getResources().getDrawable(R.drawable.user_msg_text));
                }
                else{
                    setGravity(Gravity.LEFT);
                    setBackground(getResources().getDrawable(R.drawable.robot_reply_text));
                }
            }

            @Override
            public void destroyDrawingCache() {
                super.destroyDrawingCache();
            }
        }
        public MessageAndUser(boolean which,String text) {
            super(ChatPlusActivity.this);
            who=which;
            setBackground(getResources().getDrawable(R.drawable.msg_shape_color));
            if(who){
                WhoButton head=new WhoButton(who);
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT
                );
                layoutParams2.setMargins(20,20,20,20);
                head.setLayoutParams(layoutParams2);
                setGravity(Gravity.RIGHT);
                MsgTextView msgTextView=new MsgTextView(who,text);
                LinearLayout tmp=new LinearLayout(ChatPlusActivity.this);
                tmp.addView(msgTextView);
                LinearLayout.LayoutParams  layoutParams3=new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams3.setMargins(5,30,5,5);
                tmp.setLayoutParams(layoutParams3);
                this.addView(tmp);
                this.addView(head);

            }
            else{
                WhoButton head=new WhoButton(who);
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT
                );
                layoutParams2.setMargins(20,20,20,20);
                head.setLayoutParams(layoutParams2);
                setGravity(Gravity.LEFT);
                this.addView(head);
                MsgTextView msgTextView=new MsgTextView(who,text);
                LinearLayout tmp=new LinearLayout(ChatPlusActivity.this);
                tmp.addView(msgTextView);
                LinearLayout.LayoutParams  layoutParams3=new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams3.setMargins(5,30,5,5);
                tmp.setLayoutParams(layoutParams3);
                this.addView(tmp);
            }
        }
    }
    private Bundle extras;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        total_layout=findViewById(R.id.chat_plus_scrollview);
        //获取屏幕宽度
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screen_width = displayMetrics.widthPixels;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_chat_plus);
        //set the two button above


        //设置不可点击的LinearLayout
        set_unclickable_linear_out=findViewById(R.id.help_unclickable_linear_layout);


        //历史记录
        history_scrollview=findViewById(R.id.chat_history_scrollview);
        history_linear_layout=findViewById(R.id.history_linear_layout);
        //聊天记录按钮和下载按钮的使用
        chat_history_list_button=findViewById(R.id.chat_list_button);
        download_chat_content_button=findViewById(R.id.chat_record_download_button);
        //聊天记录按钮的监听
        chat_history_list_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(history_scrollview.getVisibility()==View.GONE){
                    history_scrollview.setVisibility(View.VISIBLE);
                }else{
                    history_scrollview.setVisibility(View.GONE);
                }

            }
        });
        download_chat_content_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String delete_button_click_code="var e=document.querySelector('.n-button--dashed');" +
                        "e.click();";
                help_web.evaluateJavascript(delete_button_click_code, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        Handler handler=new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                String confirm_button_click_code="var e=document.querySelector('button.n-button:nth-child(2)');" +
                                        "e.click();";
                                help_web.evaluateJavascript(confirm_button_click_code,null);
                            }
                        },1000);
                    }
                });
            }
        });

        //更新说明
        extras=getIntent().getExtras();
        if(extras!=null){
            boolean if_updated=extras.getBoolean("if_updated");
            if(if_updated){
                AlertDialog.Builder builder =new AlertDialog.Builder(ChatPlusActivity.this);
                builder.setTitle("更新说明");
                builder.setMessage(R.string.update_notice);
                builder.setPositiveButton("确认", null);
                builder.show();
            }
        }
        //设置发送建议的逻辑，调用按钮
        send_suggestions_button=findViewById(R.id.chat_plus_submit_suggestions_button);
        send_suggestions_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatPlusActivity.this);
                builder.setTitle("邮件提示");
                builder.setMessage("如果没有自动填充邮件地址，请到剪贴板第一条获取，也可以手动输入\ntor108@outlook.com");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 发送邮件的逻辑
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        String[] address = {"tor108@outlook.com"};
                        String topic = "来自 " + Build.BRAND + " " + Build.MODEL + " 用户的建议";
                        intent.putExtra(Intent.EXTRA_SUBJECT, topic); // 设置主题
                        intent.putExtra(Intent.EXTRA_TEXT, "建议:"); // 设置邮件内容
                        intent.putExtra(Intent.EXTRA_EMAIL, address);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            // 获取剪贴板管理器
                            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            // 创建一个新的ClipData对象，包含要复制的文本内容
                            ClipData clip = ClipData.newPlainText("label", "tor108@outlook.com");
                            // 将ClipData对象添加到剪贴板
                            clipboard.setPrimaryClip(clip);
                            startActivity(Intent.createChooser(intent, "选择邮箱"));
                        } else {
                            Toast.makeText(ChatPlusActivity.this, "无可调用的邮件程序", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.show();
            }
        });
        message_container=findViewById(R.id.message_content);
        //隐藏的webiew的初始化
        help_web=findViewById(R.id.help_web);
        help_web.getSettings().setJavaScriptEnabled(true);
        help_web.getSettings().setDomStorageEnabled(true);
        help_web.setWebViewClient(new My_Web_ViewClient());
        help_web.getSettings().setUserAgentString("Mozilla/5.0 (X11; Linux x86_64; rv:109.0) Gecko/2010");
        help_web.loadUrl("https://chat18.aichatos.xyz/");
        //进度条显示
        send_button=findViewById(R.id.chat_plus_send_button);
        loading_view=findViewById(R.id.chat_plus_loading_progress);
        loading_view=findViewById(R.id.chat_plus_loading_progress);
        loading_view.setVisibility(View.VISIBLE);
        send_button.setEnabled(false);
        source_url=help_web.getUrl().toString();
        //scrollview的初始化
        scrollView=findViewById(R.id.chat_plus_scrollview);
        //输入框的初始化
        msg_input=findViewById(R.id.chat_plus_msg_input);
        //send的绑定监听
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ans="null";
                send_button.setEnabled(false);
                String text=msg_input.getText().toString();
                if(text.equals("")||text.matches("^\\s*$")){
                    send_button.setEnabled(true);
                    Toast.makeText(ChatPlusActivity.this,"请输入文字",Toast.LENGTH_SHORT).show();
                    return;
                }
                message_container.addView(new MessageAndUser(true,text));
                scrollView.fullScroll(View.FOCUS_DOWN);
                //模拟网页的进行
                text=text.replaceAll("\\n","  ");
                help_web.evaluateJavascript(

                        "var inputElement = document.querySelector('.n-input__textarea-el');" +
                                "var inputContent ='" +text+"';"+
                                "inputElement.value = inputContent;" +
                                "var event = new InputEvent('input', {bubbles: true, cancelable: true});" +
                                "inputElement.dispatchEvent(event);" + "var x=document.querySelector('button.n-button:nth-child(4)')",
                        null);
                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        help_web.evaluateJavascript("var button = document.querySelector('.n-button.n-button--primary-type.n-button--medium-type');"+
                                "button.click()",null);
                    }
                },500);
                //清空输入框
                msg_input.setText("");
                //
                loading_time=0;
                Handler handler11 = new Handler();
                handler11.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 在这里执行等待1秒后要做的操作
                        // 例如：启动另一个Activity或者执行特定任务
                        help_web.evaluateJavascript("var content=document.querySelectorAll('div.markdown-body')[document.querySelectorAll('div.markdown-body').length-1].innerText;content;", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                                loading_time+=1;
                                String innerText = value.replaceAll("\"", "");
                                ans = innerText;
                                if (ans.equals("null")||!pre_ans.equals(ans)) {
                                    pre_ans=ans;
                                    repeat(handler);
                                } else {
                                    int childCount = message_container.getChildCount();
                                    if (childCount > 0) {
                                        View lastChild = message_container.getChildAt(childCount - 1); // 获取最后一个子视图
                                        message_container.removeView(lastChild); // 从父视图中移除最后一个子视图
                                    }
                                    message_container.addView(new MessageAndUser(false, ans));
                                    scrollView.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            scrollView.fullScroll(View.FOCUS_DOWN);
                                            Handler handler1=new Handler();
                                            handler1.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    send_button.setEnabled(true);
                                                }
                                            },500);
                                        }
                                    });
                                }
                            }
                        });
                    }
                }, 1500); // 1000毫秒即1秒
                ans="loading";
                message_container.addView(new MessageAndUser(false,ans));
                ans="null";
                pre_ans=ans;
            }
        });


        //tool bar
        // tool bar show button
        tool_bar_button=findViewById(R.id.chat_plus_draggableButton);
        other_tool_list=findViewById(R.id.chat_plus_other_tool_list);
        tool_bar_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(other_tool_list.getVisibility()==View.GONE){
                    other_tool_list.setVisibility(View.VISIBLE);
                }
                else{
                    other_tool_list.setVisibility(View.GONE);
                }
            }
        });

        // with out ad button
        with_out_ad_button=findViewById(R.id.chat_plus_prevent_ad_button);
        with_out_ad_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ChatPlusActivity.this,WithOutAdActivity.class);
                startActivity(intent);
            }
        });

        //fuck button
        fuck_phone_button=findViewById(R.id.chat_plus_fuck_phone_button);
        fuck_phone_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                int if_first_check=sharedPreferences.getInt("if_first_check",0);
                if(if_first_check<100000){
                    Intent intent=new Intent(ChatPlusActivity.this,FuckPhoneActivity.class);
                    startActivity(intent);
                    SharedPreferences.Editor editor= sharedPreferences.edit();
                    if_first_check=if_first_check+1;
                    editor.putInt("if_first_check",if_first_check);
                    editor.apply();
                }
                else{
                    AlertDialog.Builder builder =new AlertDialog.Builder(ChatPlusActivity.this);
                    builder.setTitle("TIP");
                    builder.setMessage("该功能还在开发中✧*｡ (ˊᗜˋ*) ✧*｡");
                    builder.setPositiveButton("确认",null);
                    builder.show();
                }
            }
        });
        //update button
        update_button=findViewById(R.id.chat_plus_update_version_button);
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatPlusActivity.CheckUpdate checkUpdate=new ChatPlusActivity.CheckUpdate();
                ChatPlusActivity.CheckUpdate.CheckVersionTask checkVersionTask=checkUpdate.new CheckVersionTask();
                checkVersionTask.execute(latest_version_url);
            }
        });
        //progress_bar
        //chatplus top button
        top_button=findViewById(R.id.chat_plus_top_position_button);
        top_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                top_button.setText(emojis[random.nextInt(emojis.length)]);
            }
        });

        //set listener
        total_layout=findViewById(R.id.chat_plus_scrollview);
        total_layout.setOnTouchListener(new View.OnTouchListener() {
            private float startX;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        total_layout.performClick();
                        float endX = event.getX();
                        float screenWidth = getResources().getDisplayMetrics().widthPixels;
                        if (startX-endX> screenWidth / 2) {
                            // 向右滑动超过屏幕宽度的1/3，执行跳转页面的逻辑
                            // 在这里添加跳转页面的代码]
                            // 比如：
                            fuck_phone_button.callOnClick();
                        }
                        if (startX-endX< -1*screenWidth / 2) {
                            // 向右滑动超过屏幕宽度的1/3，执行跳转页面的逻辑
                            // 在这里添加跳转页面的代码
                            // 比如：
                            Intent intent = new Intent(ChatPlusActivity.this, BusActivity.class);
                            startActivity(intent);
                        }
                        break;
                }
                return false;
            }
        });
    }



    public class HistoryItemButton extends androidx.appcompat.widget.AppCompatButton {
        public String title;
        public Integer id;

        public HistoryItemButton(Context context, String title, int id) {
            super(context);
            //
            this.title = title;
            this.id = id;
            setSingleLine(true);
            setEllipsize(TextUtils.TruncateAt.END);
            //
            setText(title);
            setTextSize(20);
            setTextColor(Color.parseColor("#ffffff"));
            setBackground(getResources().getDrawable(R.drawable.history_item_button));

            //
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            );
            layoutParams2.setMargins(10, 4, 10, 4);
            setLayoutParams(layoutParams2);
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    set_page_loading();
                    Integer tmp_id=HistoryItemButton.this.id+1;
                    String id_s = (tmp_id).toString();
                    String click_web_history_button_code = "var e=document.querySelector('.gap-2 > div:nth-child(" + id_s + ")').children[0];" +
                            "e=e._vei;" +
                            "e.onClick(e);";
                    help_web.evaluateJavascript(click_web_history_button_code, null);
                    Handler handler=new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            message_container.removeAllViews();
                            String get_msgs_code="var content_list=[];" +
                                    "var question_list=document.querySelectorAll('div.whitespace-pre-wrap');" +
                                    "for(var i=0;i<document.querySelectorAll('div.markdown-body').length;i++){" +
                                    "  content_list[2*i]=question_list[i].innerText;" +
                                    "  content_list[2*i+1]=document.querySelectorAll('div.markdown-body')[i].innerText;" +
                                    "}" +
                                    "content_list;";
                            help_web.evaluateJavascript(get_msgs_code, new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String value) {
                                    // 将返回的字符串转换为 Java 数组
                                    String[] msg_list = value.substring(1, value.length() - 1).split(",");
                                    // 处理返回的字符串数组
                                    for (int i = 0; i < msg_list.length; i++) {
                                        msg_list[i]=msg_list[i].replaceAll("\"","");
                                    }
                                    for(int i=0;i<msg_list.length;i++){
                                        if(i%2==0){
                                            message_container.addView(new MessageAndUser(true,msg_list[i]));
                                        }else{
                                            message_container.addView(new MessageAndUser(false,msg_list[i]));
                                        }
                                    }
                                    finish_loading();
                                }
                            });
                        }
                    },1000);
                }
            });
            setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(ChatPlusActivity.this);
                    builder.setTitle(">_<");
                    builder.setMessage("删除此聊天");
                    builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            set_page_loading();
                            Integer tmp_id=HistoryItemButton.this.id+1;
                            String id_s = (tmp_id).toString();
                            String his_item_button_click_code="var e=document.querySelector('.gap-2 > div:nth-child("+id_s+")').children[0];" +
                                    "                            e=e._vei;" +
                                    "                            e.onClick(e);";
                            String delete_button_click_code="var e=document.querySelector('button.p-1:nth-child(2)');" +
                                    "e.click();";
                            String confirm_button_click_code="var e=document.querySelector('button.n-button:nth-child(2)');" +
                                    "e.click();";
                            help_web.evaluateJavascript(his_item_button_click_code,null);
                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    help_web.evaluateJavascript(delete_button_click_code,null);
                                    Handler handler1=new Handler();
                                    handler1.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                          help_web.evaluateJavascript(confirm_button_click_code,null);
                                            Handler handler4=new Handler();
                                            handler4.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    message_container.removeAllViews();
                                                    String get_msgs_code="var content_list=[];" +
                                                            "var question_list=document.querySelectorAll('div.whitespace-pre-wrap');" +
                                                            "for(var i=0;i<document.querySelectorAll('div.markdown-body').length;i++){" +
                                                            "  content_list[2*i]=question_list[i].innerText;" +
                                                            "  content_list[2*i+1]=document.querySelectorAll('div.markdown-body')[i].innerText;" +
                                                            "}" +
                                                            "content_list;";
                                                    help_web.evaluateJavascript(get_msgs_code, new ValueCallback<String>() {
                                                        @Override
                                                        public void onReceiveValue(String value) {
                                                            // 将返回的字符串转换为 Java 数组
                                                            String[] msg_list = value.substring(1, value.length() - 1).split(",");
                                                            // 处理返回的字符串数组
                                                            for (int i = 0; i < msg_list.length; i++) {
                                                                msg_list[i]=msg_list[i].replaceAll("\"","");
                                                            }
                                                            for(int i=0;i<msg_list.length;i++){
                                                                if(i%2==0){
                                                                    message_container.addView(new MessageAndUser(true,msg_list[i]));
                                                                }else{
                                                                    message_container.addView(new MessageAndUser(false,msg_list[i]));
                                                                }
                                                            }
                                                            finish_loading();
                                                        }
                                                    });
                                                }
                                            },100);
                                        }
                                    },1000);
                                }
                            },1000);
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.show();
                    return false;
                }
            });
        }
    }
    public void repeat(Handler handler) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 在这里执行等待1秒后要做的操作
                // 例如：启动另一个Activity或者执行特定任务
                help_web.evaluateJavascript("var content=document.querySelectorAll('div.markdown-body')[document.querySelectorAll('div.markdown-body').length-1].innerText;content;", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        loading_time+=1;
                        String innerText = value.replaceAll("\"", "");
                        ans = innerText;
                        if(loading_time>=40&&pre_ans.equals(ans)){
                            int childCount = message_container.getChildCount();
                            if (childCount > 0) {
                                View lastChild = message_container.getChildAt(childCount - 1); // 获取最后一个子视图
                                message_container.removeView(lastChild); // 从父视图中移除最后一个子视图
                            }
                            message_container.addView(new MessageAndUser(false, "加载超时了涅  "+top_button.getText().toString()));
                            scrollView.post(new Runnable() {
                                @Override
                                public void run() {
                                    scrollView.fullScroll(View.FOCUS_DOWN);
                                }
                            });
                            top_button.setText("...");
                            send_button.setEnabled(true);
                        }
                        else if (ans.equals("null")||!pre_ans.equals(ans)) {
                            pre_ans=ans;
                            repeat(handler);
                            top_button.callOnClick();
                        } else {
                            if(ans.contains("please try again later")){
                                int childCount = message_container.getChildCount();
                                if (childCount > 0) {
                                    View lastChild = message_container.getChildAt(childCount - 1); // 获取最后一个子视图
                                    message_container.removeView(lastChild); // 从父视图中移除最后一个子视图
                                }
                                message_container.addView(new MessageAndUser(false, "网络错误...  "+top_button.getText().toString()));
                                scrollView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        scrollView.fullScroll(View.FOCUS_DOWN);
                                    }
                                });
                                send_button.setEnabled(true);
                                top_button.callOnClick();
                            }
                            else{
                                int childCount = message_container.getChildCount();
                                if (childCount > 0) {
                                    View lastChild = message_container.getChildAt(childCount - 1); // 获取最后一个子视图
                                    message_container.removeView(lastChild); // 从父视图中移除最后一个子视图
                                }
                                ans.replaceAll("\u003C","<");
                                message_container.addView(new MessageAndUser(false, ans));
                                scrollView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        scrollView.fullScroll(View.FOCUS_DOWN);
                                    }
                                });
                                top_button.callOnClick();
                                send_button.setEnabled(true);
                            }
                        }
                    }
                });
                if(loading_time>=40&&pre_ans.equals(ans)){
                    AlertDialog.Builder builder=new AlertDialog.Builder(ChatPlusActivity.this);
                    builder.setTitle("超时");
                    builder.setMessage("加载超时,未获取到任何信息,请检查你的网络连接");
                    builder.setPositiveButton("好吧",null);
                    int childCount = message_container.getChildCount();
                    if (childCount > 0) {
                        View lastChild = message_container.getChildAt(childCount - 1); // 获取最后一个子视图
                        message_container.removeView(lastChild); // 从父视图中移除最后一个子视图
                    }
                    message_container.addView(new MessageAndUser(false, "加载超时了涅  "+top_button.getText().toString()));
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(View.FOCUS_DOWN);
                        }
                    });
                    builder.show();
                }
            }
        }, 1500); // 1000毫秒即1秒
    }
    class My_Web_ViewClient extends WebViewClient {

        private boolean isLoading = false;
        private Handler handler = new Handler();
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            // 正常处理请求
            return super.shouldInterceptRequest(view, request);
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            //load the history
                Handler handler1=new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loading_view.setVisibility(View.GONE);
                        send_button.setEnabled(true);
                        String get_button_list_code = "var list_e=document.querySelector('.gap-2').children;" +
                                "var title_list=[];" +
                                "for(var i=0;i<list_e.length;i++){" +
                                "  var e=list_e[i].getElementsByTagName('span');" +
                                "  title_list[i]=e[1].innerText;" +
                                "}" +
                                "title_list;";
                        help_web.evaluateJavascript(get_button_list_code, new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                                history_linear_layout.removeAllViews();
                                // 将返回的字符串转换为 Java 数组
                                String[] title_list = value.substring(1, value.length() - 1).split(",");
                                // 处理返回的字符串数组
                                for (int i = 0; i < title_list.length; i++) {
                                    title_list[i]=title_list[i].replaceAll("\"","");
                                }
                                for (int i = 0; i < title_list.length; i++) {
                                    history_linear_layout.addView(new HistoryItemButton(ChatPlusActivity.this,title_list[i],i));
                                }
                            }
                        });
                    }
                },1500);
        }
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl){
            AlertDialog.Builder builder=new AlertDialog.Builder(ChatPlusActivity.this);
            builder.setTitle("TIP");
            builder.setMessage("加载失败，请联系开发者");
            builder.show();
            send_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ChatPlusActivity.this,">_<",Toast.LENGTH_SHORT).show();
                }
            });
        }
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }
    }


    //update button logic
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
                    String apkFileName = "chat"+BuildConfig.VERSION_NAME.toString()+".apk";
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
                if (true) {
                    //获取最新的下载的文件
                    // 下载成功，触发安装
                    String apkFilePath = "storage/Download/chat" + BuildConfig.VERSION_NAME.toString() + ".apk";
                    File apkFile = new File(apkFilePath);
                    Uri apkUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", apkFile);

                    Intent installIntent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                    installIntent.setData(apkUri);
                    installIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    try {
                        context.startActivity(installIntent);
                    } catch (ActivityNotFoundException e) {
                        // 处理未找到可用安装程序的情况
                        Toast.makeText(ChatPlusActivity.this,"找不到安装程序",Toast.LENGTH_SHORT);
                        return;
                    } catch (SecurityException e) {
                        // 处理权限问题
                        Toast.makeText(ChatPlusActivity.this,"没有安装权限，请手动安装",Toast.LENGTH_SHORT);
                        return;
                    } catch (NullPointerException e) {
                        // 处理空指针异常
                        Toast.makeText(ChatPlusActivity.this,"找不到文件",Toast.LENGTH_SHORT);
                        return;
                    } catch (IllegalArgumentException e) {
                        // 处理非法参数异常
                        Toast.makeText(ChatPlusActivity.this,"找不到文件",Toast.LENGTH_SHORT);
                        return;
                    }

                } else {
                    Toast.makeText(ChatPlusActivity.this,"下载失败",Toast.LENGTH_SHORT);
                }
            }
        }
        public int versioncode;
        private String versionname;
        public String new_version;
        public class CheckVersionTask extends AsyncTask<String,Integer,String> {
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
                AlertDialog.Builder builder =new AlertDialog.Builder(ChatPlusActivity.this);
                builder.setTitle("连接超时，检查你的网络连接");
                builder.setPositiveButton("确认", null);
                builder.show();
            }
            public void showConfirmDialog(){
                Toast.makeText(ChatPlusActivity.this,"发现新版本",Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder =new AlertDialog.Builder(ChatPlusActivity.this);
                builder.setTitle("有新的版本可以更新");
                builder.setMessage("当前版本为"+ versionname +",最新版本为"+ new_version);
                builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ChatPlusActivity.CheckUpdate checkUpdate=new ChatPlusActivity.CheckUpdate();
                        ChatPlusActivity.CheckUpdate.DownloadApkAsyncTask downloadApkAsyncTask=checkUpdate.new DownloadApkAsyncTask(ChatPlusActivity.this);
                        downloadApkAsyncTask.execute(latest_version_url);
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.show();
            }
            public void showDialogNewest(){
                AlertDialog.Builder builder =new AlertDialog.Builder(ChatPlusActivity.this);
                builder.setTitle("当前版本已为最新版本"+ versionname);
                builder.setPositiveButton("确认", null);
                builder.show();
            }
            public void showDialogSourceError(){
                AlertDialog.Builder builder =new AlertDialog.Builder(ChatPlusActivity.this);
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
    //set the touch listen
    //设施连续两次返回退出应用
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        SharedPreferences sharedPreferences=getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String main_id=sharedPreferences.getString("main_ui_id","chat");
        if(main_id.equals("chat")){
            if (doubleBackToExitPressedOnce) {
                finishAffinity();
                return false;
            }
            if(keyCode==KeyEvent.KEYCODE_BACK){
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "再按一次返回键退出应用", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(() -> doubleBackToExitPressedOnce=false, 2000);
                return false;
            }
            return super.onKeyDown(keyCode,event);
        }
        else{
            return super.onKeyDown(keyCode,event);
        }
    }



    private void disableAllViews(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                disableAllViews(child);
            }
        } else {
            view.setEnabled(false);
        }
    }
    private void enableAllViews(View view){
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                disableAllViews(child);
            }
        } else {
            view.setEnabled(true);
        }
    }
    void set_page_loading(){
        loading_view.setVisibility(View.VISIBLE);
    }
    void finish_loading(){
        loading_view.setVisibility(View.GONE);
    }
}