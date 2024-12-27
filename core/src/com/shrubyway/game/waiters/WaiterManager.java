package com.shrubyway.game.waiters;

import java.util.ArrayList;

public class WaiterManager {
    public static ArrayList<Waiter> waiters = new ArrayList<Waiter>();

    public static void addWaiter(Waiter waiter) {
        waiters.add(waiter);
    }

    public static void update(float delta) {
        ArrayList<Waiter> tempWaiters = new ArrayList<Waiter>(waiters);
        for(Waiter waiter : tempWaiters) {
            waiter.update(delta);
        }
    }

    public static void removeWaiter(Waiter waiter) {
        waiters.remove(waiter);
    }

    public static void removeWaiterIfExist(Waiter waiter) {
        if(waiters.contains(waiter)) {
            waiters.remove(waiter);
        }
    }

}
