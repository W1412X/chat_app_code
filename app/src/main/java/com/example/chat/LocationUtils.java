package com.example.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;
import android.Manifest;
import androidx.core.app.ActivityCompat;

import java.util.List;
import java.util.Locale;

/**
 * 获取经纬度、位置工具类
 */
public class LocationUtils {

    @SuppressLint("StaticFieldLeak")
    private volatile static LocationUtils uniqueInstance;
    private LocationManager locationManager;
    private String locationProvider;
    private Location location;
    private final Context mContext;

    public LocationUtils(Context context) {
        mContext = context;
        getLocation();
    }

    //采用Double CheckLock(DCL)实现单例
    public static LocationUtils getInstance(Context context) {
        if (uniqueInstance == null) {
            synchronized (LocationUtils.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new LocationUtils( context );
                }
            }
        }
        return uniqueInstance;
    }

    //获取经纬度location
    public void getLocation() {
        //1.获取位置管理器
        locationManager = (LocationManager) mContext.getSystemService( Context.LOCATION_SERVICE );
        //2.获取位置提供器，GPS或是NetWork
        List<String> providers = locationManager.getProviders( true );
        if (providers.contains( LocationManager.NETWORK_PROVIDER )) {
            //如果是网络定位
            Log.d( "TAG", "如果是网络定位" );
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else if (providers.contains( LocationManager.GPS_PROVIDER )) {
            //如果是GPS定位
            Log.d( "TAG", "如果是GPS定位" );
            locationProvider = LocationManager.GPS_PROVIDER;
        } else {
            Log.d( "TAG", "没有可用的位置提供器" );
            return;
        }
        // 需要检查权限,否则编译报错,想抽取成方法都不行,还是会报错。只能这样重复 code 了。
        if (Build.VERSION.SDK_INT >= 23 &&
                ActivityCompat.checkSelfPermission( mContext, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission( mContext, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (ActivityCompat.checkSelfPermission( mContext, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( mContext, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //3.获取上次的位置，一般第一次运行，此值为null
        Location location = locationManager.getLastKnownLocation( locationProvider );
        if (location != null) {
            setLocation( location );
        }
        // 监视地理位置变化，第二个和第三个参数分别为更新的最短时间minTime和最短距离minDistace
    }

    public void setLocation(Location location) {
        this.location = location;
        String address = "纬度：" + location.getLatitude() + "经度：" + location.getLongitude();
        Log.d( "TAG", address );
    }

    //获取经纬度
    public Location showLocation() {
        return location;
    }

    //获取地址信息:城市、街道等信息
    public String getAddress(Context context,Location location) {
        List<Address> result = null;
        String address = null;
        try {
            if (location != null) {
                Geocoder gc = new Geocoder(context, Locale.getDefault());
                result = gc.getFromLocation(location.getLatitude(),
                        location.getLongitude(), 1);
//                result = gc.getFromLocation(37.833832912379386,
//                        112.4759295159054, 1);
                Toast.makeText(context, "获取地址信息：" + result.toString(), Toast.LENGTH_LONG).show();
                Log.v("TAG", "获取地址信息：" + result.toString());
                for (int i = 0; i < result.size(); i++) {
                    Log.d("TAG", result.get(i).getSubAdminArea());//开发区
                    Log.d("TAG", result.get(i).getAdminArea());//山西省
                    Log.d("TAG", result.get(i).getLocality());//晋城市
                    Log.d("TAG", result.get(i).getAddressLine(0));
                    address = result.get(i).getAddressLine(0);

                }
                //admin=山西省,sub-admin=长风西街街道,locality=太原市
                //admin=山西省,sub-admin=开发区,locality=晋城市

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return address;
    }

}