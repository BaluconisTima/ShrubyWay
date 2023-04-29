package com.shrubyway.game.visibleobject.decoration;

public class decorationsManager {
    public static int decorationNumber = 3;
    public static Class<? extends Decoration> decorations[] = new Class[decorationNumber];
    static {
        decorations[0] = Bush.class;
        decorations[1] = Pine.class;
        decorations[2] = Rock.class;
    }
    public static Decoration newOf(int i){
        try {
            return decorations[i].newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
