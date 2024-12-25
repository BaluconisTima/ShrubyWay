package com.shrubyway.game.visibleobject.effect;

import com.badlogic.gdx.graphics.Color;
import com.shrubyway.game.myinterface.TextDrawer;
import com.shrubyway.game.screen.Game;
import com.shrubyway.game.visibleobject.VisibleObject;

public class DamageDisplay extends VisibleEffect {

    static Color color[];
    static int range[];
    static {
        color = new Color[6];
        color[0] = new Color(1, 1, 1, 1);
        color[1] = new Color(1, 1, 0, 1);
        color[2] = new Color(1, 0.5f, 0, 1);
        color[3] = new Color(1, 0, 0, 1);
        color[4] = new Color(0.5f, 0, 0, 1);
        color[5] = new Color(0, 0, 0, 1);
        range = new int[6];
        range[0] = 0;
        range[1] = 7;
        range[2] = 20;
        range[3] = 50;
        range[4] = 100;
        range[5] = 1000000;
    }

    @Override
    public void apply(VisibleObject visibleObject) {}

    static Color getColor(float damage) {
        for(int i = 0; i < 5; i++) {
            if(damage >= range[i] && damage < range[i + 1]) {
               Color color1 = new Color(color[i].r + (color[i + 1].r - color[i].r) * (damage - range[i]) / (range[i + 1] - range[i]),
                       color[i].g + (color[i + 1].g - color[i].g) * (damage - range[i]) / (range[i + 1] - range[i]),
                       color[i].b + (color[i + 1].b - color[i].b) * (damage - range[i]) / (range[i + 1] - range[i]),
                       1);
                return color1;
            }
        }
        return color[5];
    }

    public DamageDisplay(float x, float y, float damage) {
        position.set(x, y);
        this.damage = damage;
        size = 0.1f;
        timeLeft = 0.5f;
        jumpedOut = false;
    }


    float damage;
    float size = 0.1f;
    float timeLeft = 1;
    boolean jumpedOut = false;


    @Override public void update(float delta) {
        if(timeLeft > 0) {
            timeLeft -= delta;
            if(size > 1.5) {
                jumpedOut = true;
            }
            if(!jumpedOut) {
                size *= Math.pow(1.5, delta * 60);
            } else {
                size *= Math.pow(0.7, delta * 60);
                if(size < 1) {
                    size = 1;
                }
            }
        } else {
           size *= Math.pow(0.7, delta * 60);
           if(size < 0.01f) {
               Game.objectsList.del(this);
           }
        }
    }

    @Override
    public void render() {
        String damageText = String.format("%.1f", this.damage);
        if(damageText.endsWith(".0")) {
            damageText = damageText.substring(0, damageText.length() - 2);
        }
        TextDrawer.drawWithShadowColor(damageText, position.x, position.y, size * 0.5f, getColor(damage));
    }
}
