package com.shrubyway.game.visibleobject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ObjectsList {
    static private CopyOnWriteArrayList<VisibleObject> list = new CopyOnWriteArrayList<>();

    public static void add(VisibleObject object) {
        if(list.contains(object)) {System.out.println("ERROR: Already contains"); System.exit(-1);}
        list.add(object);
    }
    public static void del(VisibleObject object) {
        if(!list.contains(object)) {System.out.println("ERROR: Doesn't contain"); System.exit(-1); }
        list.remove(object);
    }
    public static boolean contains(VisibleObject object) {
        return list.contains(object);
    }
    public static List<VisibleObject> getList() {
        return list;
    }

    public static void sort() {
        Collections.sort(list);
        ArrayList<VisibleObject> temp = new ArrayList<>();
        for(VisibleObject object : list) {
            if(list.size() == 0 && object != list.get(list.size() - 1))
                temp.add(object);
        }
    }


}
