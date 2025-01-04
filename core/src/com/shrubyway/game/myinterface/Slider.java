package com.shrubyway.game.myinterface;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.GlobalBatch;

public class Slider {
    static private Texture base, slider;
    static TextDrawer textDrawer;
    Vector2 position;
    private float value = 0;
    Rectangle rectangle;
    String text = "";
    float scale = -1;

    public Slider(float x_center, float y_center, float value, String text) {
        if(base == null) base = new Texture("interface/slider_base.png");
        if(slider == null) slider = new Texture("interface/slider.png");
        if(textDrawer == null) textDrawer = new TextDrawer();
        this.text = text;
        this.value = value;

        position = new Vector2(x_center - base.getWidth()/2, y_center - base.getHeight()/2);
        rectangle = new Rectangle(position.x - 20, position.y,
                base.getWidth() + 40, base.getHeight());
    }

    public void set(float x_center, float y_center) {
        position = new Vector2(x_center - base.getWidth()/2, y_center - base.getHeight()/2);
        rectangle = new Rectangle(position.x - 20, position.y,
                base.getWidth() + 40, base.getHeight());
        scale = -1;
    }

    public void update() {
        if(scale != GlobalBatch.getScale()) {
            scale = GlobalBatch.getScale();
            rectangle = new Rectangle((position.x - 20) * scale, (position.y - 20) * scale,
                    (base.getWidth() + 40) * scale, (base.getHeight()+40) * scale);
        }
    }


    public void render() {
        update();

        TextDrawer.drawCenterWhite(text, position.x + base.getWidth()/2, position.y + base.getHeight()/2 + 150, 0.7f);
        GlobalBatch.render(base, position.x, position.y + 15);
        GlobalBatch.render(slider, position.x + (12 + base.getWidth()) * value - slider.getWidth()/2 - 6, position.y);
       // textDrawer.drawCenterWhite((int)(value * 100) + "%", position.x + base.getWidth()/2, position.y + base.getHeight()/2 + 20, 0.5f);
    }

    public void setSliderValue(float value) {
        this.value = value;
    }

    public void click(float x, float y) {
        if(rectangle.contains(x, y)) {
            value = (x - position.x * scale) / ((base.getWidth() + 20) * scale);
            if(value < 0) value = 0;
            if(value > 1) value = 1;
        }
    }

    public float getValue() {
        return value;
    }


}
