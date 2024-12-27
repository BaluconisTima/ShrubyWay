package com.shrubyway.game.linemaker;

import java.util.ArrayList;

public class ActionLogicManager {

    static public ArrayList<ActionLogic> activeLogics = new ArrayList<ActionLogic>();

    static public void update(float delta) {
        for (int i = 0; i < activeLogics.size(); i++) {
            activeLogics.get(i).update(delta);
        }
    }

    static public void add(ActionLogic logic) {
        activeLogics.add(logic);
    }

    static public void remove(ActionLogic logic) {
        activeLogics.remove(logic);
    }

    static public void removeByClass(Class<?> _class) {
        for (int i = 0; i < activeLogics.size(); i++) {
            if (activeLogics.get(i).getClass() == _class) {
                activeLogics.remove(i);
                i--;
            }
        }
    }
}

