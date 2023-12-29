package com.shrubyway.game.event;

import java.util.HashMap;

public class Event {
    static public HashMap eventHashMap = new HashMap<String, Integer>();
    public static Integer getEvent(String eventName) {
        if(eventHashMap.containsKey(eventName)) {
            return (Integer) eventHashMap.get(eventName);
        }
        else {
            Integer x = eventHashMap.size();
            eventHashMap.put(eventName, x);
            return x;
        }
    }

    static public void clear() {
        eventsHappened.clear();
        eventHashMap.clear();
    }
    static public HashMap eventsHappened = new HashMap<Integer, Boolean>();
    public static void cast(Integer event) {
        eventsHappened.put(event, true);
    }

    public static void cast(String eventName) {
        cast(getEvent(eventName));
    }

    public static boolean happened(Integer event) {
        if(eventsHappened.containsKey(event)) {
            return (boolean) eventsHappened.get(event);
        }
        else {
            return false;
        }
    }

    public static boolean happened(String eventName) {
        return happened(getEvent(eventName));
    }


}
