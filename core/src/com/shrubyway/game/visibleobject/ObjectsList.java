package com.shrubyway.game.visibleobject;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ObjectsList {
    private CopyOnWriteArrayList<VisibleObject> list = new CopyOnWriteArrayList<>();

    public void add(VisibleObject object) {
        if(object != null) list.add(object);
    }

    public void del(VisibleObject object) {
        list.remove(object);
    }

    public boolean contains(VisibleObject object) {
        return list.contains(object);
    }

    public List<VisibleObject> getList() {
        return list;
    }

    public void sort() {
        Collections.sort(list);
    }

    public void dispose() {
        for(VisibleObject object : list) {
            object.dispose();
        }
        list.clear();
        list = null;
    }



}
