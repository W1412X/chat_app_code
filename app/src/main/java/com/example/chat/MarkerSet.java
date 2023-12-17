package com.example.chat;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import  com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;

public class MarkerSet {
    static public ArrayList<MarkerOptions>maker_list=new ArrayList<>();
    static public ArrayList<LatLng>position_list=new ArrayList<>();
    static{
        position_list.add(new LatLng(36.375219,120.686772));
        position_list.add(new LatLng(36.375758,120.687731));
        position_list.add(new LatLng(36.370845,120.69126));
        position_list.add(new LatLng(36.366719,120.692903));
        position_list.add(new LatLng(36.365326,120.690709));
        position_list.add(new LatLng(36.365378,120.687675));
        position_list.add(new LatLng(36.364172,120.684787));
        position_list.add(new LatLng(36.362448,120.685209));
        position_list.add(new LatLng(36.361925,120.686085));
        position_list.add(new LatLng(36.361899,120.687074));
        position_list.add(new LatLng(36.361886,120.690579));
        position_list.add(new LatLng(36.358613,120.693388));

        MarkerOptions markerOptions1 = new MarkerOptions();
        markerOptions1.position(position_list.get(0));
        markerOptions1.title("阅海居\n1号线 3号线");
        markerOptions1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        markerOptions1.draggable(false);
        BitmapDescriptor bitmapDescriptor1=null;
        try{
            bitmapDescriptor1=BitmapDescriptorFactory.fromResource(R.drawable.yhj);
        }catch (Exception e){
            Log.e("tag",e.getMessage());
        }
        markerOptions1.icon(bitmapDescriptor1);
        maker_list.add(markerOptions1);
        //
        MarkerOptions markerOptions2 = new MarkerOptions();
        markerOptions2.position(position_list.get(1));
        markerOptions2.title("樱海湖\n2号线");
        markerOptions2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        markerOptions2.draggable(false);
        BitmapDescriptor bitmapDescriptor2=null;
        try{
            bitmapDescriptor2=BitmapDescriptorFactory.fromResource(R.drawable.yhh);
        }catch (Exception e){
            Log.e("tag",e.getMessage());
        }
        markerOptions2.icon(bitmapDescriptor2);
        maker_list.add(markerOptions2);
        //
        MarkerOptions markerOptions3 = new MarkerOptions();
        markerOptions3.position(position_list.get(2));
        markerOptions3.title("敬贤大道东口\n1号线 2号线");
        markerOptions3.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        markerOptions3.draggable(false);
        BitmapDescriptor bitmapDescriptor3=null;
        try{
            bitmapDescriptor3=BitmapDescriptorFactory.fromResource(R.drawable.jxdd);
        }catch (Exception e){
            Log.e("tag",e.getMessage());
        }
        markerOptions3.icon(bitmapDescriptor3);
        maker_list.add(markerOptions3);


        MarkerOptions markerOptions4 = new MarkerOptions();
        markerOptions4.position(position_list.get(3));
        markerOptions4.title("会文广场(校区东门)\n1号线 2号线 ");
        markerOptions4.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        markerOptions4.draggable(false);
        BitmapDescriptor bitmapDescriptor4=null;
        try{
            bitmapDescriptor4=BitmapDescriptorFactory.fromResource(R.drawable.hwgc);
        }catch (Exception e){
            Log.e("tag",e.getMessage());
        }
        markerOptions4.icon(bitmapDescriptor4);
        maker_list.add(markerOptions4);


        MarkerOptions markerOptions5 = new MarkerOptions();
        markerOptions5.position(position_list.get(4));
        markerOptions5.title("淦昌苑\n1号线 2号线");
        markerOptions5.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        markerOptions5.draggable(false);
        BitmapDescriptor bitmapDescriptor5=null;
        try{
            bitmapDescriptor5=BitmapDescriptorFactory.fromResource(R.drawable.gcy);
        }catch (Exception e){
            Log.e("tag",e.getMessage());
        }
        markerOptions5.icon(bitmapDescriptor5);
        maker_list.add(markerOptions5);



        MarkerOptions markerOptions6 = new MarkerOptions();
        markerOptions6.position(position_list.get(5));
        markerOptions6.title("图书馆\n1号线 2号线");
        markerOptions6.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        markerOptions6.draggable(false);
        BitmapDescriptor bitmapDescriptor6=null;
        try{
            bitmapDescriptor6=BitmapDescriptorFactory.fromResource(R.drawable.tsg);
        }catch (Exception e){
            Log.e("tag",e.getMessage());
        }
        markerOptions6.icon(bitmapDescriptor6);
        maker_list.add(markerOptions6);

        MarkerOptions markerOptions7 = new MarkerOptions();
        markerOptions7.position(position_list.get(6));
        markerOptions7.title("华岗苑\n1号线 2号线");
        markerOptions7.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        markerOptions7.draggable(false);
        BitmapDescriptor bitmapDescriptor7=null;
        try{
            bitmapDescriptor7=BitmapDescriptorFactory.fromResource(R.drawable.hgy);
        }catch (Exception e){
            Log.e("tag",e.getMessage());
        }
        markerOptions7.icon(bitmapDescriptor7);
        maker_list.add(markerOptions7);


        MarkerOptions markerOptions8 = new MarkerOptions();
        markerOptions8.position(position_list.get(7));
        markerOptions8.title("振声苑\n 3号线");
        markerOptions8.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        markerOptions8.draggable(false);
        BitmapDescriptor bitmapDescriptor8=null;
        try{
            bitmapDescriptor8=BitmapDescriptorFactory.fromResource(R.drawable.zsy3);
        }catch (Exception e){
            Log.e("tag",e.getMessage());
        }
        markerOptions8.icon(bitmapDescriptor8);
        maker_list.add(markerOptions8);

        MarkerOptions markerOptions9 = new MarkerOptions();
        markerOptions9.position(position_list.get(8));
        markerOptions9.title("振声苑\n 4号线");
        markerOptions9.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        markerOptions9.draggable(false);
        BitmapDescriptor bitmapDescriptor9=null;
        try{
            bitmapDescriptor9=BitmapDescriptorFactory.fromResource(R.drawable.zsy4);
        }catch (Exception e){
            Log.e("tag",e.getMessage());
        }
        markerOptions9.icon(bitmapDescriptor9);
        maker_list.add(markerOptions9);

        MarkerOptions markerOptions10 = new MarkerOptions();
        markerOptions10.position(position_list.get(9));
        markerOptions10.title("博物馆\n1号线 2号线");
        markerOptions10.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        markerOptions10.draggable(false);
        BitmapDescriptor bitmapDescriptor10=null;
        try{
            bitmapDescriptor10=BitmapDescriptorFactory.fromResource(R.drawable.bwg);
        }catch (Exception e){
            Log.e("tag",e.getMessage());
        }
        markerOptions10.icon(bitmapDescriptor10);
        maker_list.add(markerOptions10);

        MarkerOptions markerOptions11 = new MarkerOptions();
        markerOptions11.position(position_list.get(10));
        markerOptions11.title("第周苑\n1号线 2号线");
        markerOptions11.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        markerOptions11.draggable(false);
        BitmapDescriptor bitmapDescriptor11=null;
        try{
            bitmapDescriptor11=BitmapDescriptorFactory.fromResource(R.drawable.dzy);
        }catch (Exception e){
            Log.e("tag",e.getMessage());
        }
        markerOptions11.icon(bitmapDescriptor11);
        maker_list.add(markerOptions11);



        MarkerOptions markerOptions12 = new MarkerOptions();
        markerOptions12.position(position_list.get(11));
        markerOptions12.title("曦园餐厅\n1号线 2号线 4号线");
        markerOptions12.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        markerOptions12.draggable(false);
        BitmapDescriptor bitmapDescriptor12=null;
        try{
            bitmapDescriptor12=BitmapDescriptorFactory.fromResource(R.drawable.xyct);
        }catch (Exception e){
            Log.e("tag",e.getMessage());
        }
        markerOptions12.icon(bitmapDescriptor12);
        maker_list.add(markerOptions12);

    }
}
