package com.shrubyway.game.visibleobject;

import java.util.TreeSet;

public class RenderingList {
    static public TreeSet<VisibleObject> list = new TreeSet<>();
    static public TreeSet<VisibleObject> tempAddList = new TreeSet<>();
    static public TreeSet<VisibleObject> tempDelList = new TreeSet<>();

    public static void add(VisibleObject object) {
        list.add(object);
    }

    public static void addTemp(VisibleObject object) {
        tempAddList.add(object);
    }
    public static void delTemp(VisibleObject object) {
        tempDelList.add(object);
    }
    public static void allTemp() {
        list.addAll(tempAddList); tempAddList.clear();
        list.removeAll(tempDelList); tempDelList.clear();
    }


}
