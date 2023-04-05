package com.shrubyway.game;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

public class KeyboardAdapter extends InputAdapter{
    private boolean LeftPressed;
    private boolean RightPressed;
    private boolean UpPressed;
    private boolean DownPressed;

    public final Vector2 MovementDirection = new Vector2();
    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.A) LeftPressed = true;
        if(keycode == Input.Keys.D) RightPressed = true;
        if(keycode == Input.Keys.W) UpPressed = true;
        if(keycode == Input.Keys.S) DownPressed = true;
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.A) LeftPressed = false;
        if(keycode == Input.Keys.D) RightPressed = false;
        if(keycode == Input.Keys.W) UpPressed = false;
        if(keycode == Input.Keys.S) DownPressed = false;
        return false;
    }

    public Vector2 getMovementDirection() {
        MovementDirection.set(0, 0);
        if(LeftPressed) MovementDirection.add(-1, 0);
        if(RightPressed) MovementDirection.add(1, 0);
        if(UpPressed) MovementDirection.add(0, 1);
        if(DownPressed) MovementDirection.add(0, -1);
        if((LeftPressed || RightPressed) && (UpPressed || DownPressed))
            MovementDirection.scl((float) Math.sqrt(2));
        return MovementDirection;

    }
}
