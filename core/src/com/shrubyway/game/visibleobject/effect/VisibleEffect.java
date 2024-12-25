package com.shrubyway.game.visibleobject.effect;

import com.shrubyway.game.visibleobject.VisibleObject;

public abstract class VisibleEffect extends VisibleObject {

    public boolean applied = false;

    public boolean interactive = false;

    public abstract void apply(VisibleObject visibleObject);

    public void update(float delta){}
}
