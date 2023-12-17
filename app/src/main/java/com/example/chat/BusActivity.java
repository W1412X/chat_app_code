package com.example.chat;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.Console;
import java.io.IOException;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.icu.util.BuddhistCalendar;
import android.location.Location;
import android.location.LocationManager;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import com.amap.api.location.APSService;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.bumptech.glide.Glide;
import  com.amap.api.maps2d.model.Marker;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import android.Manifest;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
public class BusActivity extends Activity implements LocationSource,AMapLocationListener {
    //
    private boolean doubleBackToExitPressedOnce;
    private Button return_to_chat_when_main_button;
    //高的地图
    private  double last_position_longitude=0;
    private double last_position_latitude=0;
    private MapView mMapView = null;
    private AMapLocationClient mapLocationClient;
    private AMapLocationClientOption mapLocationClientOption;
    private Marker marker;
    private  AMap aMap=null;
    private OnLocationChangedListener mListener;
    private RadioGroup mGPSModeGroup;
    private TextView mLocationErrText;
    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);



    //位置
    boolean if_first_do_position=true;
    private Button set_as_main_button;
    private LocationManager locationManager;
    private ArrayList<String>now_nearest_time_list;
    //天气数据
    private LinearLayout dialog_station_list;
     private Button dialog_line_name;
    private String high=null,low=null;
    private String whether_url="https://www.msn.cn/zh-cn/weather/forecast/in-%E5%B1%B1%E4%B8%9C%E7%9C%81,%E5%8D%B3%E5%A2%A8%E5%8C%BA";
    private String whether_type=null;
    private String time=null;
    private String wind=null,humi=null;
    private WebView whether_web;
    private Button position_data_button;
    private LocationUtils locationUtils;
    private ArrayList<String>lines;
    //
    private Button whether_button;

    public BusActivity() throws Exception {
    }

    public class PositionButton extends androidx.appcompat.widget.AppCompatButton {
        private String position_name;
        public PositionButton(Context context,  String position) {
            super(BusActivity.this);
            this.position_name = position;
            setBackground(getResources().getDrawable(R.drawable.position_button));
            setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            setTextSize(25);
            setTextColor(Color.parseColor("#ffffff"));
            setText(position_name);
            //设置对应的margin参数
            int margin = 5; // 16dp
            float density = context.getResources().getDisplayMetrics().density;
            int marginPx = (int) (margin * density + 0.5f); // 将dp转换为px
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getLayoutParams();
            layoutParams.setMargins(marginPx, marginPx, marginPx, marginPx);
            //设置监听
            setOnClickListener(v -> {
                lines=BusSchedule.getLinesByStation(position_name);
                show_dialog_lines();
            });
        }

        public String getPosition() {
            return position_name;
        }
        public void setPosition(String position) {
            this.position_name = position;
        }
    }
    private LinearLayout bus_position_button_list;
    private WebView map_web;
    private CardView whether_card;
    private Location location;
    public String add_enter(String input){
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            builder.append(input.charAt(i));
            builder.append("\n");
        }

        return builder.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus);
        //设置为主键的监听
        set_as_main_button=findViewById(R.id.set_as_main_in_bus);
        SharedPreferences sharedPreferences=getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String main_id=sharedPreferences.getString("main_ui_id","chat");
        if(main_id.equals("bus")){
            set_as_main_button.setText("返回原版");
        }
        set_as_main_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(main_id.equals("chat")){
                    SharedPreferences.Editor editor= sharedPreferences.edit();
                    editor.putString("main_ui_id","bus");
                    editor.apply();
                    Toast.makeText(BusActivity.this,"已设置此界面为主界面",Toast.LENGTH_SHORT).show();
                }
                else{
                    SharedPreferences.Editor editor= sharedPreferences.edit();
                    editor.putString("main_ui_id","chat");
                    editor.apply();
                    Toast.makeText(BusActivity.this,"已设置Chat为主界面，左划以进入bus界面",Toast.LENGTH_SHORT).show();
                    try{
                        Handler handler=new Handler();
                        handler.postDelayed(new Runnable(){
                            @Override
                            public void run(){
                                RestartAppTool.restartAPP(BusActivity.this);
                            }
                        },2000);
                    }catch (Exception e){
                        AlertDialog.Builder builder =new AlertDialog.Builder(BusActivity.this);
                        builder.setMessage(e.getMessage());
                        builder.show();
                    }
                }
            }
        });
        //
        locationUtils=new LocationUtils(BusActivity.this);
        locationUtils.getLocation();
        bus_position_button_list = findViewById(R.id.bus_position_button_list);
        bus_position_button_list.addView(new PositionButton(BusActivity.this, "阅海居"));
        bus_position_button_list.addView(new PositionButton(BusActivity.this, "曦园餐厅"));
        bus_position_button_list.addView(new PositionButton(BusActivity.this, "博物馆(振声苑)"));
        bus_position_button_list.addView(new PositionButton(BusActivity.this, "第周苑"));
        bus_position_button_list.addView(new PositionButton(BusActivity.this, "樱海湖(校区北门)"));
        bus_position_button_list.addView(new PositionButton(BusActivity.this, "华岗苑"));
        bus_position_button_list.addView(new PositionButton(BusActivity.this, "会文广场(校区东门)"));
        bus_position_button_list.addView(new PositionButton(BusActivity.this, "图书馆"));
        bus_position_button_list.addView(new PositionButton(BusActivity.this, "淦昌苑"));
        bus_position_button_list.addView(new PositionButton(BusActivity.this, "地铁山东大学站(乐水居南区)"));
        //获取天气数据
        whether_web=findViewById(R.id.whether_web);
        whether_web.getSettings().setUserAgentString("Mozilla/5.0");
        whether_web.getSettings().setDomStorageEnabled(true);
        whether_web.getSettings().setJavaScriptEnabled(true);
        whether_web.setWebViewClient(new WhetherWebClient());
        whether_web.loadUrl(whether_url);
        whether_button=findViewById(R.id.bus_whether_button);
        whether_web.getSettings().setDomStorageEnabled(true);
        whether_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whether_button.setTextColor(Color.parseColor("#ffffff"));
            }
        });



        try{
            //地图添加
            mMapView=(MapView) findViewById(R.id.map_2d);
            mMapView.onCreate(savedInstanceState);
            //定义了一个地图view
//初始化地图控制器对象
            if (aMap == null) {
                aMap = mMapView.getMap();
            }
//add signs station
            aMap.setLocationSource(this);//设置定位监听
            aMap.getUiSettings().setMyLocationButtonEnabled(true);//显示重新定位按钮
            aMap.setMyLocationEnabled(true);//设置显示定位器并可以定位
            setupLocationStyle();//
        }catch(Exception e){
            Log.e("TAG", "Exception occurred: " + Log.getStackTraceString(e));
        }
        try{
            requestLocationPermission();
        }catch (Exception e){
            Log.e("BusActivity",  e.getMessage());
        }
        /*int permissionCheck=ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionCheck==PackageManager.PERMISSION_GRANTED){
            try{
                mapLocationClient.setLocationListener(new AMapLocationListener() {
                    @Override
                    public void onLocationChanged(AMapLocation aMapLocation) {
                        if (aMapLocation != null) {
                            // 获取经纬度信息
                            double latitude = aMapLocation.getLatitude();
                            double longitude = aMapLocation.getLongitude();
                            // 在这里处理经纬度信息
                            Toast.makeText(BusActivity.this, "Latitude: " + latitude + ", Longitude: " + longitude, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                mapLocationClientOption=new AMapLocationClientOption();
                mapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
                mapLocationClientOption.setInterval(2000);
                mapLocationClient.setLocationListener((AMapLocationListener) this);
                mapLocationClient.startLocation();
            }catch (Exception e){
                Toast.makeText(BusActivity.this,"exception", Toast.LENGTH_SHORT).show();
            }
        }*/





        //

        for(int i=0;i<MarkerSet.maker_list.size();i++){
            aMap.addMarker(MarkerSet.maker_list.get(i));
        }



        //设置进入chat的butto

        //
        return_to_chat_when_main_button=findViewById(R.id.to_chat_button_when_main);
        if(main_id.equals("chat")){
            return_to_chat_when_main_button.setVisibility(View.GONE);
        }else{
            return_to_chat_when_main_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(BusActivity.this,ChatPlusActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
    class MapWebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return false;
        }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.d("web_view","on_page_started...");
        }
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            // 隐藏WebView控件
            whether_web.setVisibility(View.GONE);
            Toast.makeText(BusActivity.this,"请检查你的网络连接",Toast.LENGTH_SHORT).show();
            map_web.loadDataWithBaseURL("android.resource://com.example.app/drawable/send_icon","<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "    <meta charset='UTF-8'>\n" +
                    "    <title>啦啦啦啦啦啦</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<h1>啦啦啦啦啦</h1>\n" +
                    "<img src='test.png' alt='eeeeeeeeeee'>\n" +
                    "</body>" +
                    "</html>","text/html",null,null);
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.d("web_view","on_page_finished...");
        }
    }
    class WhetherWebClient extends WebViewClient{
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            // 正常处理请求
            String url=request.getUrl().toString();
            if(!url.contains("weather")){
                return new WebResourceResponse("text/html", "UTF-8", null);
            }
            return super.shouldInterceptRequest(view, request);
        }
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url=whether_web.getUrl().toString();
            if(url.contains("www.msn.cn")){
                return true;
            }
            else{
                return super.shouldOverrideUrlLoading(view,request);
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
            whether_web.setVisibility(View.GONE);
            Toast.makeText(BusActivity.this,"请检查你的网络连接",Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            /*String delete_element_code =
                    "var zx1=document.querySelector('.sb_count'); " +
                    "zx1.style.display='none'; " +
                    "var zx2=document.querySelector('#ev_talkbox_wrapper'); " +
                    "zx2.style.display='none'; " +
                    "var element_list= document.getElementsByTagName('div'); " +
                    "var whether_part=document.getElementById('wtr_cardContainer'); " +
                    "var footer=document.getElementById('b_footer'); " +
                    "footer.style.display='none'; " +
                    "var results=(document.getElementById('b_results')).children; " +
                    "for(var i=1;i<results.length;i++){ " +
                    "  results[i].style.display='none'; " +
                    "} "+
                    "var search_bar=document.getElementById('b_header'); " +
                    "search_bar.style.display='none'; ";
            String listen="var search_bar = document.getElementById('b_header');" +
                    "search_bar.style.display = 'none';" +
                    "var observer = new MutationObserver(function(mutations) {" +
                    "  mutations.forEach(function(mutation) {" +
                    "    if (mutation.target.style.display !== 'none') {" +
                    "      mutation.target.style.display = 'none';" +
                    "    }" +
                    "  });" +
                    "});" +
                    "observer.observe(search_bar, { attributes: true });";
            whether_web.evaluateJavascript(delete_element_code+listen,null);*/
// 创建一个 Handler 对象

            Handler handler = new Handler();

// 创建一个 Runnable 对象，用于定义需要延时执行的代码
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    // 在这里编写需要延时执行的代码
                    String jsCode = "var e=document.getElementsByClassName('summaryCaptionCompact-DS-EntryPoint1-1');" +
                            "var whether_type=e[0].textContent;" +
                            "var e=document.getElementsByClassName('summaryTemperatureCompact-DS-EntryPoint1-1 summaryTemperatureHover-DS-EntryPoint1-1');" +
                            "var t_now=e[0].textContent;" +
                            "e=document.getElementsByClassName('summaryFeelLikeContainerCompact-DS-EntryPoint1-1 detailItemGroupHover-DS-EntryPoint1-1');" +
                            "var t_tg=e[0].textContent;" +
                            "e=document.getElementsByClassName('detailItemGroup-DS-EntryPoint1-1 detailItemGroupHover-DS-EntryPoint1-1');" +
                            "var wind=e[0].getElementsByTagName('div');" +
                            "wind=wind[3].textContent;" +
                            "'天气-'+whether_type+' 当前温度'+t_now+' '+t_tg+' '+wind;";
                    whether_web.evaluateJavascript(jsCode, new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            whether_button.setText(value);
                        }
                    });
                }
            };
// 使用 postDelayed() 方法将 Runnable 对象提交到消息队列，3 秒后执行
            handler.postDelayed(runnable, 4000);
            super.onPageFinished(view, url);
        }
    }

    private class NetworkTask extends AsyncTask<Void, Void, Document> {
        private String url;

        public NetworkTask(String url) {
            this.url = url;
        }

        @Override
        protected Document doInBackground(Void... params) {
            try {
                Document d=Jsoup.connect(url).userAgent("Mozilla").get();
                return d;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Document document) {
            if (document != null) {
                Element element=document.getElementsByClass("wtr_forecastDay wtr_noselect wtr_tab_sel").get(0);
                String dateAndtype=element.attr("data-label");
                high=element.attr("data-hi").toString();
                low=element.attr("data-lo").toString();
                String[] divided=dateAndtype.split(",");
                time=divided[0];
                whether_type=divided[1];
                String whether_sign;
                if(whether_type.contains("cloudy")){
                    whether_sign="☀";
                } else if (whether_type.contains("sunny")) {
                    whether_sign="☁";
                } else if (whether_type.contains("Rain")) {
                    whether_sign="☂";
                } else if (whether_type.contains("snow")) {
                    whether_sign="☃";
                }else{
                    whether_sign="☢";
                }
                whether_button.setText(time+"   "+whether_sign+"  "+low+"℃"+" ~ "+high+"℃"+" ");
            } else {
                AlertDialog.Builder alert=new AlertDialog.Builder(BusActivity.this);
                alert.setTitle("网络错误");
                alert.setMessage("检查您的网络连接");
                alert.setPositiveButton("晓得",null);
                alert.show();
            }
            super.onPostExecute(document);
        }
    }
    public class TimeButton extends androidx.appcompat.widget.AppCompatButton{
        public TimeButton(String text){
            super(BusActivity.this);
            setBackground(getResources().getDrawable(R.drawable.time_button));
            setText(text);
            setTextSize(20);
            setTextColor(Color.parseColor("#55000000"));
        }

        @Override
        public void destroyDrawingCache() {
            super.destroyDrawingCache();
        }
    }
    public class ScheduleOfLineView extends ScrollView{
        public ScheduleOfLineView(Context context,ArrayList<String> line_schedule){
            super(context);
            setBackgroundColor(Color.parseColor("#00000000"));
            LinearLayout tmp=new LinearLayout(BusActivity.this);
            tmp.setOrientation(LinearLayout.VERTICAL);
            TimeButton tmp3=new TimeButton("首战发车时间");
            boolean if_first=true;
            for(int i=0;i<line_schedule.size();i++){
                TimeButton tmp1=new TimeButton(line_schedule.get(i));
                if(!new BusTime().isTimeBefore(tmp1.getText().toString())&&if_first){
                    tmp1.setTextColor(Color.parseColor("#ffffff"));
                    tmp1.setText(("最近班车  ")+tmp1.getText().toString());
                    tmp.addView(tmp1,0);
                    if_first=false;
                    TimeButton tmp11=new TimeButton(line_schedule.get(i));
                    tmp11.setTextColor(Color.parseColor("#55111111"));
                    tmp.addView(tmp11);
                }
                else{
                    tmp1.setTextColor(Color.parseColor("#55111111"));
                    tmp.addView(tmp1);
                }
            }
            this.addView(tmp);
        }

        @Override
        public void destroyDrawingCache() {
            super.destroyDrawingCache();
        }
    }
    public  class MyDialog extends Dialog{
        private ViewPager viewPager;
        private MyPagerAdapter pagerAdapter;
        private Button closeButton;
        public MyDialog(Context context){
            super(context);
        }
        private Button dialog_line_name;
        public MyDialog(Context context,ArrayList<ScrollView>view_list){
            super(context);
            dialog_station_list=findViewById(R.id.station_list_001);
            this.dialog_line_name=findViewById(R.id.line_name_button_001);
            setContentView(R.layout.my_dialog_layout);
            viewPager=findViewById(R.id.viewPager);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    // 当页面正在滑动时调用此方法
                    // position：当前页面的索引
                    // positionOffset：当前页面偏移的百分比
                    // positionOffsetPixels：当前页面偏移的像素值
                }

                @Override
                public void onPageSelected(int position) {
                    dialog_station_list.removeAllViews();
                    update_line_name_station_list(position);
                }
                @Override
                public void onPageScrollStateChanged(int state) {
                    // 当页面滚动状态发生变化时调用此方法
                    // state：页面滚动状态，有三种状态：SCROLL_STATE_IDLE（空闲）、SCROLL_STATE_DRAGGING（拖动）、SCROLL_STATE_SETTLING（自动滑动）
                }
            });

            pagerAdapter=new MyPagerAdapter(view_list);
            viewPager.setAdapter(pagerAdapter);
        }
        public Button get_line_name_button(){
            return this.findViewById(R.id.line_name_button_001);
        }
        public LinearLayout get_station_list(){
            return this.findViewById(R.id.station_list_001);
        }
    }
    public class MyPagerAdapter extends PagerAdapter {
        private ArrayList<ScrollView> lists;
        public  MyPagerAdapter(){
            super();
        }
        public MyPagerAdapter(ArrayList<ScrollView>list){
            super();
            dialog_station_list=findViewById(R.id.station_list_001);
            dialog_line_name=findViewById(R.id.line_name_button_001);
            this.lists=list;
        }
        @Override
        public int getCount(){
                return lists.size();
        }
        @Override
        public boolean isViewFromObject(View view,Object object){
            return view==object;
        }
        @Override
        public Object instantiateItem(ViewGroup container,int position){
            container.addView(lists.get(position));
            return lists.get(position);
        }
        @Override
        public void destroyItem(ViewGroup container,int position,Object object){
            container.removeView(lists.get(position));
        }

        @Override
        public void destroyItem(@NonNull View container, int position, @NonNull Object object) {
            super.destroyItem(container, position, object);
        }
    }
    public void show_dialog_lines(){
        ArrayList<ScrollView> view_list=new ArrayList<>();
        BusTime bustime=new BusTime();
        for(int i=0;i<lines.size();i++){
            if(!bustime.if_holiday()){
                if(lines.get(i).equals("2号线(正) - 校外循环专线")||lines.get(i).equals("2号线(反) - 校外循环专线")){
                    view_list.add(new ScheduleOfLineView(BusActivity.this,BusSchedule.getScheduleByLine(lines.get(i)+"g")));
                }else{
                    view_list.add(new ScheduleOfLineView(BusActivity.this,BusSchedule.getScheduleByLine(lines.get(i))));
                }
            }
            else{
                if(lines.get(i).equals("2号线(正) - 校外循环专线")||lines.get(i).equals("2号线(反) - 校外循环专线")){
                    view_list.add(new ScheduleOfLineView(BusActivity.this,BusSchedule.getScheduleByLine(lines.get(i))));
                }
                else{
                    view_list.add(new ScheduleOfLineView(BusActivity.this,BusSchedule.get_null_Schedule(lines.get(i))));
                }
            }
        }
        MyDialog myDialog=new MyDialog(BusActivity.this,view_list);
        myDialog.show();
        dialog_line_name=myDialog.get_line_name_button();
        dialog_station_list=myDialog.get_station_list();
        update_line_name_station_list(0);
    }
    public void update_line_name_station_list(int id0){
        dialog_line_name.setText(lines.get(id0));
        ArrayList<String> station_list=BusSchedule.getStationsInLine(lines.get(id0));
        Button tmp1=new TimeButton("发车方向");
        tmp1.setGravity(Gravity.LEFT);
        tmp1.setTextColor(Color.parseColor("#ffffff"));
        tmp1.setPadding(10,30,10,10);
        dialog_station_list.addView(tmp1);
        for(int i=0;i<station_list.size();i++){
            Button tmp=new TimeButton(station_list.get(i));
            tmp.setGravity(Gravity.LEFT);
            tmp.setPadding(10,30,10,10);
            dialog_station_list.addView(tmp);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }



    //重写定位





    private void setupLocationStyle(){
        // 自定义系统定位蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
                fromResource(R.drawable.gps_point));
        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(STROKE_COLOR);
        //自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(5);
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(FILL_COLOR);
        // 将自定义的 myLocationStyle 对象添加到地图上
        aMap.setMyLocationStyle(myLocationStyle);
    }

    // 请求定位权限
    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            // 启动定位
            try{
                if(mapLocationClient==null){
                    Toast.makeText(BusActivity.this,"mapLocationclient is null",Toast.LENGTH_SHORT).show();
                }
                mapLocationClient.startLocation();
            }catch (Exception e){
                Toast.makeText(BusActivity.this,"in start location error",Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 处理权限请求结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 启动定位
                mapLocationClient.startLocation();
            } else {
                Toast.makeText(this, "定位权限被拒绝", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 实现 AMapLocationListener 接口的回调方法
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            //获取当前位置信息
            LatLng latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            String address = aMapLocation.getAddress();
            try{
                double latitude = aMapLocation.getLatitude();
                double longitude = aMapLocation.getLongitude();
                if(Math.abs(latitude-last_position_latitude)>0.00005||Math.abs(longitude-last_position_longitude)>0.00005){
                    if(if_first_do_position){
                        aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                        if_first_do_position=false;
                    }
                    aMap.animateCamera(CameraUpdateFactory.changeLatLng(latLng));
                    last_position_longitude=longitude;
                    last_position_latitude=latitude;
                }
            }catch(Exception e){
                Log.e("tag",e.getMessage());
            }
            //在地图上添加定位点
            if (marker == null) {
                //创建一个MarkerOptions对象，并设置其属性
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(address);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                markerOptions.draggable(false);
                //在地图上添加Marker，并获取其对象
                marker = aMap.addMarker(markerOptions);
            } else {
                //更新Marker的位置和信息
                marker.setPosition(latLng);
                marker.setTitle(address);
            }
        }
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        try{
            mListener = listener; // 设置监听器
        }catch (Exception e){
            Toast.makeText(BusActivity.this,"in activate",Toast.LENGTH_SHORT).show();
        }
        mListener = listener;
        if (mapLocationClient == null) {
            try{
                AMapLocationClient.updatePrivacyAgree(BusActivity.this,true);
                AMapLocationClient.updatePrivacyShow(BusActivity.this,true,true);

                AMapLocationClient.setApiKey("a807cab0c56a1cb6fefe14f4960755ff");
                mapLocationClient  =new AMapLocationClient(BusActivity.this);
                mapLocationClientOption = new AMapLocationClientOption();
                //设置定位监听
                mapLocationClient.setLocationListener(this);
                //设置为高精度定位模式
                mapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
                //设置定位参数
                mapLocationClient.setLocationOption(mapLocationClientOption);
                // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
                // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
                // 在定位结束后，在合适的生命周期调用onDestroy()方法
                // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
                mapLocationClient.startLocation();
            }catch (Exception e){

                Toast.makeText(BusActivity.this,"LocationClient init failed",Toast.LENGTH_SHORT);
                Log.e("BusActivity", "LocationClient init failed: " + e.getMessage());
            }
        }
    }

    @Override
    public void deactivate() {
        try{
            mListener = null; // 取消监听器
        }catch (Exception e){
            Toast.makeText(BusActivity.this,"in activate",Toast.LENGTH_SHORT).show();
        }

    }

    // 在 Activity 销毁时停止定位
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 停止定位
        mapLocationClient.stopLocation();
        // 销毁定位客户端
        mapLocationClient.onDestroy();
    }

    //返回键逻辑

    //设施连续两次返回退出应用
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        SharedPreferences sharedPreferences=getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String main_id=sharedPreferences.getString("main_ui_id","chat");
        if(main_id.equals("bus")){
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

}