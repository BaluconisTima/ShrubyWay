package com.shrubyway.game.myinterface;

import com.badlogic.gdx.graphics.Texture;
import com.shrubyway.game.GlobalBatch;
import com.shrubyway.game.ShrubyWay;

public class LineRenderer {

    Texture texture;
    float alpha = 0;
    Boolean played = false;
    String speaker;
    String renderingText[];
    public void init() {
        texture = ShrubyWay.assetManager.get("interface/subtitles.png", Texture.class);
        alpha = 0;
        renderingText = new String[1];
        renderingText[0] = null;
    }



    String[] splitDisplayText(String text) {
        String[] words = text.split(" ");
        String[] lines = new String[words.length];
        int line = 0;
        lines[line] = "";
        for(int i = 0; i < words.length; i++) {
            if(lines[line].length() + words[i].length() < 50) {
                lines[line] += words[i] + " ";
            } else {
                line++;
                lines[line] = words[i] + " ";
            }
        }
        return lines;

    }

    public void setText(String text, String speaker) {
        played = true;
        this.speaker = speaker.toUpperCase();
        renderingText = splitDisplayText(text);
    }
    public void clearText() {
        played = false;
    }



    public void update(float delta) {
        if(played) {
            if(alpha < 0.01f) alpha = 0.02f;
            alpha *= Math.pow(1.2, delta * 60);
            if(alpha > 1) {
                alpha = 1;
            }
        } else {
            alpha /= Math.pow(1.2, delta * 60);
            if(alpha < 0) {
                alpha = 0;
            }
        }
    }

    public void render() {
          if(alpha < 0.01f || alpha == 0 || speaker == null) {
              return;
          }

        GlobalBatch.batch.setColor(1,1,1, alpha);
        GlobalBatch.render(texture, GlobalBatch.centerX() - texture.getWidth()/2, 75);
        TextDrawer.drawCenterWhite(speaker, 1920/2, 300, 0.7f, alpha);
        int base = -1;
        for(int i = 0; i < renderingText.length; i++) {
            if(renderingText[i] != null) {
                base++;
            } else {
                break;
            }
        }
        for(int i = 0; i < renderingText.length; i++) {
            if(renderingText[i] != null) {
                TextDrawer.drawCenterWhite(renderingText[i], 1920/2,
                185 - 30 * i  + 15 * base, 0.5f, alpha);
            } else {
                break;
            }
        }
        GlobalBatch.batch.setColor(1,1,1, alpha);
    }
}
