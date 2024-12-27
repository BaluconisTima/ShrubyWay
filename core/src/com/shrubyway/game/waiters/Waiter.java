package com.shrubyway.game.waiters;

public class Waiter implements java.io.Serializable {
    public void update(float delta) {
        if(checkCondition()) {
            action();
            WaiterManager.removeWaiter(this);
        }
    }

    public boolean checkCondition() {return false;}

    public void action() {}

}
