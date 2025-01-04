package com.shrubyway.game.effect.effecttypes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.shrubyway.game.GlobalBatch;
import com.shrubyway.game.sound.SoundSettings;
import com.shrubyway.game.visibleobject.entity.Entity;
import com.shrubyway.game.visibleobject.entity.Shruby;

public class AgaricEffect extends SpeedEffect {

    private static ShaderProgram shader;
    public AgaricEffect(float time, Entity owner) {
        super(time, owner, new Color(0, 218f/256, 175f/256, 1), -0.7f);
        if(shader == null) {
            String vertexShader = Gdx.files.internal("shaders/rainbow.vert").readString();
            String fragmentShader = Gdx.files.internal("shaders/rainbow.frag").readString();
            shader = new ShaderProgram(vertexShader, fragmentShader);
        }
        if(owner instanceof Shruby) {
            GlobalBatch.setShader(shader);
        }
        SoundSettings.setReverse(true);
    }

    @Override public void update(float delta) {
        super.update(delta);
        if(time_left <= 0) {
            owner.removeEffect(this);
            SoundSettings.setReverse(false);
            if(owner instanceof Shruby) {
                GlobalBatch.unsetShader();
            }
        }
    }
}
