package com.shrubyway.game.visibleobject;

import com.shrubyway.game.animation.AnimationGlobalTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ObjectsList implements Serializable {
    static private CopyOnWriteArrayList<VisibleObject> list = new CopyOnWriteArrayList<>();

    public static void add(VisibleObject object) {
        list.add(object);
    }

    public static void del(VisibleObject object) {
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
    }


}
