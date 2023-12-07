package com.example.chat;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedList;
public class BusMap {
    private boolean[] if_visit=new boolean[100];
    private ArrayList<ArrayList<LinkStop>>ways;
    public class LinkStop {
        private String line_name;
        private String target_stop;
        private int target_id;
        private int start_id;
        private String start_stop;
        private float length;
        public LinkStop(int start_id,int target_id,String line_name,float l){
            this.start_id=start_id;this.target_id=target_id;
            this.start_stop= bus_stops.get(start_id).name;
            this.target_stop=bus_stops.get(target_id).name;
            this.line_name=line_name;
            length=l;
        }
        public LinkStop(String l_n,String t_s,String s_s){
            line_name=l_n;
            target_stop=t_s;
            start_stop=s_s;
            length=0;
        }
    }
    public class BusStop {
        private int id;//存储对应的在图中的站点列表的id，便于查询
        private String name;//
        private String which_line;//一个站点可能对应多个线路
        private ArrayList<LinkStop> can_go;
        private boolean if_first_stop;
        private ArrayList<BusTime>bus_times;
        public BusStop(int id,String name,String which_line,boolean if_first_stop){
            this.id=id;
            this.name=name;
            this.which_line=(which_line);
            this.if_first_stop=if_first_stop;
        }
        public void add_link_stop(LinkStop ls){
            can_go.add(ls);
        }
    }
    public class BusLine {
        private String routeName;
        private ArrayList<BusTime> departureTimes;
        private ArrayList<BusStop> way;//
        // 构造函数
        public BusLine(String routeName, ArrayList<BusTime> departureTimes) {
            this.routeName = routeName;
            this.departureTimes = departureTimes;
        }
        public BusLine(String routeName){
            this.routeName=routeName;
        }
    }
    public BusMap(){
        for(int i=0;i<30;i++){
            can_directly_go_pairs.add(new ArrayList<>());
        }
    }
    public ArrayList<BusStop>bus_stops;
    public Map<String,java.lang.Integer>name_id_map;
    public ArrayList<ArrayList<Integer>> can_directly_go_pairs;
    public void add_stop(String line_name,String stop_name,boolean if_first_stop){
        name_id_map.put(stop_name,bus_stops.size());
        bus_stops.add(new BusStop(bus_stops.size(),stop_name,line_name,if_first_stop));
    }
    public void add_way(int id1,int id2,String line_name){
        bus_stops.get(id1).can_go.add(new LinkStop(id1,id2,line_name,1));
        bus_stops.get(id2).can_go.add(new LinkStop(id2,id1,line_name,1));
    }
    public void add_can_directly_go_pairs(int id1,int id2){
        can_directly_go_pairs.get(id1).add(id2);
        can_directly_go_pairs.get(id2).add(id1);
    }
    public void find_way_dfs(int start_id,int target_id,ArrayList<LinkStop>way){
        LinkedList<Integer>queue1=new LinkedList<Integer>();
        int pre=0;
        queue1.add(start_id);
        for(int i=0;i<can_directly_go_pairs.get(start_id).size();i++){
            queue1.add(bus_stops.get(can_directly_go_pairs.get(start_id).get(i)).id);
        }
        for(int i=0;i<queue1.size();i++){
            if_visit[queue1.get(queue1.size()-1)]=true;
            for(int u=0;u<bus_stops.get(queue1.get(i)).can_go.size();u++){
                way.add(bus_stops.get(i).can_go.get(u));
                if(bus_stops.get(i).can_go.get(u).target_id==target_id){
                    ways.add(way);
                    continue;
                }
                if(!if_visit[bus_stops.get(i).can_go.get(u).target_id]){
                    //还没有被访问过
                    if_visit[bus_stops.get(i).can_go.get(u).target_id]=true;
                    find_way_dfs(bus_stops.get(i).can_go.get(u).target_id,target_id,way);
                }
                way.remove(way.size()-1);
            }
        }
    }
    public void find_way(int start_id,int target_id){
        for(int i=0;i<100;i++){
            if_visit[i]=false;
        }
        ways=new ArrayList<ArrayList<LinkStop>>();
        ArrayList<LinkStop>way=new ArrayList<>();
        find_way_dfs(start_id,target_id,way);
    }
}
