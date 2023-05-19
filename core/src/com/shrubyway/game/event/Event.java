package com.shrubyway.game.event;

import java.util.HashMap;

public class Event {
    static private HashMap eventHashMap = new HashMap<String, Event>();
    private int eventID = 0;
    static private int eventCounter = 0;

    public Event() {
        eventID = eventCounter++;
    }
    public static Event getEvent(String eventName) {
        if(eventHashMap.containsKey(eventName)) {
            return (Event) eventHashMap.get(eventName);
        }
        else {
            Event newEvent = new Event();
            eventHashMap.put(eventName, newEvent);
            return newEvent;
        }
    }

    static private HashMap eventsHappened = new HashMap<Event, Boolean>();

    public static void cast(Event event) {
        eventsHappened.put(event, true);
    }

    public static boolean happened(Event event) {
        if(eventsHappened.containsKey(event)) {
            return (boolean) eventsHappened.get(event);
        }
        else {
            return false;
        }
    }

}
