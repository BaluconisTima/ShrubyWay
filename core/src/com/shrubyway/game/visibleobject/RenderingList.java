package com.shrubyway.game.visibleobject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

public class RenderingList {
    static private CopyOnWriteArrayList<VisibleObject> list = new CopyOnWriteArrayList<>();

    public static void add(VisibleObject object) {
        list.add(object);
    }
    public static void del(VisibleObject object) {
        list.remove(object);
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
