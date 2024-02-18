package com.example.chat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BusSchedule {
    private static Map<String, ArrayList<String>> schedule = new HashMap<>(); // 存储公交线路和发车时间表
    private static Map<String, ArrayList<String>> stationLines = new HashMap<>(); // 存储站点和所属公交线路
    private static Map<String, ArrayList<String>> line_stations = new HashMap<>();
    static private ArrayList<String> null_time=new ArrayList<>();
    static {
        // 添加1号线发车时间表
        ArrayList<String> line1 = new ArrayList<>();
        line1.add("7:30");
        line1.add("7:45");
        line1.add("8:00");
        line1.add("8:15");
        line1.add("8:30");
        line1.add("9:00");
        line1.add("9:30");
        line1.add("9:55");
        line1.add("10:30");
        line1.add("11:00");
        line1.add("11:30");
        line1.add("11:45");
        line1.add("12:00");
        line1.add("12:15");
        line1.add("12:30");
        line1.add("13:00");
        line1.add("13:30");
        line1.add("13:45");
        line1.add("14:00");
        line1.add("14:15");
        line1.add("14:30");
        line1.add("15:00");
        line1.add("15:30");
        line1.add("15:55");
        line1.add("16:30");
        line1.add("17:00");
        line1.add("17:30");
        line1.add("17:45");
        line1.add("18:00");
        line1.add("18:15");
        line1.add("18:30");
        line1.add("18:45");
        line1.add("19:00");
        line1.add("19:30");
        line1.add("20:00");
        line1.add("20:30");
        line1.add("21:00");
        line1.add("21:30");
        line1.add("22:00");
        line1.add("22:30");
        schedule.put("1号线(正) - 校内循环专线", line1);
        schedule.put("1号线(反) - 校内循环专线", line1);
        // 添加2号线发车时间表
        ArrayList<String> line2 = new ArrayList<>();
        // 工作日首发站发车时间
        line2.add("7:00");
        line2.add("7:20");
        line2.add("12:10");
        line2.add("13:20");
        line2.add("17:30");
        line2.add("18:10");
        line2.add("21:20");
        schedule.put("2号线(正) - 校外循环专线g", line2);
        // 双休日及节假日发车时间
        ArrayList<String> line2_weekend = new ArrayList<>();
        line2_weekend.add("7:20");
        line2_weekend.add("8:20");
        line2_weekend.add("9:20");
        line2_weekend.add("10:20");
        line2_weekend.add("11:20");
        line2_weekend.add("12:20");
        line2_weekend.add("13:20");
        line2_weekend.add("14:20");
        line2_weekend.add("15:20");
        line2_weekend.add("16:20");
        line2_weekend.add("17:20");
        line2_weekend.add("18:20");
        line2_weekend.add("19:20");
        line2_weekend.add("20:20");
        line2_weekend.add("21:20");
        line2_weekend.add("22:20");
        schedule.put("2号线(正) - 校外循环专线", line2_weekend);
        // 添加2号线反线发车时间表
        ArrayList<String> line2_reverse = new ArrayList<>();
        // 工作日首发站发车时间
        line2_reverse.add("7:20");
        line2_reverse.add("7:40");
        line2_reverse.add("12:30");
        line2_reverse.add("13:40");
        line2_reverse.add("17:50");
        line2_reverse.add("18:30");
        line2_reverse.add("21:40");
        schedule.put("2号线(反) - 校外循环专线g", line2_reverse);
        // 双休日及节假日发车时间
        ArrayList<String> line2_reverse_weekend = new ArrayList<>();
        line2_reverse_weekend.add("7:40");
        line2_reverse_weekend.add("8:40");
        line2_reverse_weekend.add("9:40");
        line2_reverse_weekend.add("10:40");
        line2_reverse_weekend.add("11:40");
        line2_reverse_weekend.add("12:40");
        line2_reverse_weekend.add("13:40");
        line2_reverse_weekend.add("14:40");
        line2_reverse_weekend.add("15:40");
        line2_reverse_weekend.add("16:40");
        line2_reverse_weekend.add("17:40");
        line2_reverse_weekend.add("18:40");
        line2_reverse_weekend.add("19:40");
        line2_reverse_weekend.add("20:40");
        line2_reverse_weekend.add("21:40");
        line2_reverse_weekend.add("22:40");
        schedule.put("2号线(反) - 校外循环专线", line2_reverse_weekend);

        // 添加3号线上课专线发车时间表
        ArrayList<String> line3_class_up = new ArrayList<>();
        line3_class_up.add("7:30");
        line3_class_up.add("7:40");
        line3_class_up.add("7:50");
        line3_class_up.add("13:30");
        line3_class_up.add("13:40");
        line3_class_up.add("13:50");
        schedule.put("3号线(上课) - 北区上课专线", line3_class_up);

        // 添加3号线下课专线发车时间表
        ArrayList<String> line3_class_down = new ArrayList<>();
        line3_class_down.add("12:05");
        line3_class_down.add("12:10");
        line3_class_down.add("18:05");
        line3_class_down.add("18:10");
        schedule.put("3号线(下课) - 北区下课专线", line3_class_down);

        // 添加4号线南区上课专线发车时间表
        ArrayList<String> line4_class_up = new ArrayList<>();
        line4_class_up.add("7:40");
        line4_class_up.add("7:50");
        schedule.put("4号线 - 南区上课专线", line4_class_up);

        // 添加站点所属公交线路
        stationLines.put("曦园餐厅", new ArrayList<>(Arrays.asList("1号线(正) - 校内循环专线","1号线(反) - 校内循环专线", "2号线(正) - 校外循环专线", "2号线(反) - 校外循环专线","4号线 - 南区上课专线")));
        stationLines.put("第周苑", new ArrayList<>(Arrays.asList("1号线(正) - 校内循环专线","1号线(反) - 校内循环专线", "2号线(正) - 校外循环专线", "2号线(反) - 校外循环专线")));
        stationLines.put("博物馆(振声苑)", new ArrayList<>(Arrays.asList("1号线(正) - 校内循环专线","1号线(反) - 校内循环专线", "2号线(正) - 校外循环专线", "2号线(反) - 校外循环专线","3号线(上课) - 北区上课专线","3号线(下课) - 北区下课专线","4号线 - 南区上课专线")));
        stationLines.put("华岗苑", new ArrayList<>(Arrays.asList("1号线(正) - 校内循环专线","1号线(反) - 校内循环专线", "2号线(正) - 校外循环专线", "2号线(反) - 校外循环专线")));
        stationLines.put("图书馆", new ArrayList<>(Arrays.asList("1号线(正) - 校内循环专线","1号线(反) - 校内循环专线", "2号线(正) - 校外循环专线", "2号线(反) - 校外循环专线")));
        stationLines.put("淦昌苑", new ArrayList<>(Arrays.asList("1号线(正) - 校内循环专线","1号线(反) - 校内循环专线", "2号线(正) - 校外循环专线", "2号线(反) - 校外循环专线")));
        stationLines.put("会文广场(校区东门)", new ArrayList<>(Arrays.asList("1号线(正) - 校内循环专线","1号线(反) - 校内循环专线", "2号线(正) - 校外循环专线", "2号线(反) - 校外循环专线")));
        stationLines.put("敬贤大道东口", new ArrayList<>(Arrays.asList("1号线(正) - 校内循环专线","1号线(反) - 校内循环专线", "2号线(正) - 校外循环专线", "2号线(反) - 校外循环专线")));
        stationLines.put("阅海居", new ArrayList<>(Arrays.asList("1号线(正) - 校内循环专线","1号线(反) - 校内循环专线", "3号线(下课) - 北区下课专线","3号线(上课) - 北区上课专线")));
        stationLines.put("樱海湖(校区北门)", new ArrayList<>(Arrays.asList("2号线(正) - 校外循环专线", "2号线(反) - 校外循环专线")));
        stationLines.put("乐水居步行街东口", new ArrayList<>(Arrays.asList("2号线(正) - 校外循环专线")));
        stationLines.put("地铁山东大学站(乐水居南区)", new ArrayList<>(Arrays.asList("2号线(反) - 校外循环专线")));
        stationLines.put("乐水居步行街西口", new ArrayList<>(Arrays.asList("2号线(反) - 校外循环专线")));






        ArrayList<String> line1Stations = new ArrayList<>();
        line1Stations.add("曦园餐厅");
        line1Stations.add("第周苑");
        line1Stations.add("博物馆(振声苑)");
        line1Stations.add("华岗苑");
        line1Stations.add("图书馆");
        line1Stations.add("淦昌苑");
        line1Stations.add("会文广场(校区东门)");
        line1Stations.add("敬贤大道东口");
        line1Stations.add("阅海居");
        line_stations.put("1号线(正) - 校内循环专线", line1Stations);
        ArrayList<String> line9Stations = new ArrayList<>();
        line9Stations.add("阅海居");
        line9Stations.add("敬贤大道东口");
        line9Stations.add("会文广场(校区东门)");
        line9Stations.add("淦昌苑");
        line9Stations.add("图书馆");
        line9Stations.add("华岗苑");
        line9Stations.add("博物馆(振声苑)");
        line9Stations.add("第周苑");
        line9Stations.add("曦园餐厅");
        line_stations.put("1号线(反) - 校内循环专线", line9Stations);
        // 2号线:校外循环专线
        ArrayList<String> line2StationsForward = new ArrayList<>();
        line2StationsForward.add("曦园餐厅");
        line2StationsForward.add("第周苑");
        line2StationsForward.add("博物馆(振声苑)");
        line2StationsForward.add("华岗苑");
        line2StationsForward.add("图书馆");
        line2StationsForward.add("淦昌苑");
        line2StationsForward.add("会文广场(校区东门)");
        line2StationsForward.add("敬贤大道东口");
        line2StationsForward.add("樱海湖(校区北门)");
        line2StationsForward.add("乐水居步行街东口");
        line2StationsForward.add("地铁山东大学站(乐水居南区)");
        line_stations.put("2号线(正) - 校外循环专线", line2StationsForward);

        ArrayList<String> line2StationsBackward = new ArrayList<>();
        line2StationsBackward.add("地铁山东大学站(乐水居南区)");
        line2StationsBackward.add("乐水居步行街西口");
        line2StationsBackward.add("乐水居北区西门");
        line2StationsBackward.add("樱海湖(校区北门)");
        line2StationsBackward.add("敬贤大道东口");
        line2StationsBackward.add("会文广场(校区东门)");
        line2StationsBackward.add("淦昌苑");
        line2StationsBackward.add("图书馆");
        line2StationsBackward.add("华岗苑");
        line2StationsBackward.add("博物馆(振声苑)");
        line2StationsBackward.add("第周苑");
        line2StationsBackward.add("曦园餐厅");
        line_stations.put("2号线(反) - 校外循环专线", line2StationsBackward);

        // 3号线:北区上下课专线(上课)(仅限工作日运行)
        ArrayList<String> line3StationsUp = new ArrayList<>();
        line3StationsUp.add("阅海居");
        line3StationsUp.add("振声苑");
        line_stations.put("3号线(上课) - 北区上课专线", line3StationsUp);

        // 3号线:北区上下课专线(下课)(仅限工作日运行)
        ArrayList<String> line3StationsDown = new ArrayList<>();
        line3StationsDown.add("振声苑");
        line3StationsDown.add("阅海居");
        line_stations.put("3号线(下课) - 北区下课专线", line3StationsDown);

        // 4号线:南区上课专线(仅限工作日运行)
        ArrayList<String> line4Stations = new ArrayList<>();
        line4Stations.add("曦园餐厅");
        line4Stations.add("振声苑");
        line_stations.put("4号线 - 南区上课专线", line4Stations);
        null_time.add("不再运行时间内");
    }
    public static ArrayList<String> get_null_Schedule(String line){
        return null_time;
    }
    // 查询某线路的发车时间表
    public static ArrayList<String> getScheduleByLine(String line) {
        return schedule.getOrDefault(line, null);
    }
    // 查询某站点所属的公交线路
    public static ArrayList<String> getLinesByStation(String station) {
        return stationLines.getOrDefault(station, null);
    }
    public static  ArrayList<String>getStationsInLine(String line){
        return line_stations.getOrDefault(line,null);
    }
}