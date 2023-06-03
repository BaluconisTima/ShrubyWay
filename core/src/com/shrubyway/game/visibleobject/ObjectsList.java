package com.shrubyway.game.visibleobject;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ObjectsList {
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
    /*@Override
    public void write(Json json) {
        json.writeArrayStart("list");
        for (VisibleObject item : list) {
            json.writeValue(ClassReflection.getSimpleName(VisibleObject.class), item);
        }
        json.writeArrayEnd();
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        list.clear();
        for (JsonValue item : jsonData.get("list")) {
            VisibleObject value = json.readValue(VisibleObject.class, item);
            list.add(value);
        }
    } */


}
