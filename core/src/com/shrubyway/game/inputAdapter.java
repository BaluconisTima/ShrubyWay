package com.shrubyway.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;

public class inputAdapter extends InputAdapter {
    private boolean LeftPressed;
    private boolean RightPressed;
    private boolean UpPressed;
    private boolean DownPressed;

    private boolean MouseLeft;
    private boolean MouseRight;

    private boolean Runing;
    private final Vector2 MousePosition = new Vector2();

    public final Vector2 MovementDirection = new Vector2();
    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.A) LeftPressed = true;
        if(keycode == Input.Keys.D) RightPressed = true;
        if(keycode == Input.Keys.W) UpPressed = true;
        if(keycode == Input.Keys.S) DownPressed = true;
        if(keycode == Input.Keys.CONTROL_LEFT) Runing = true;
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.A) LeftPressed = false;
        if(keycode == Input.Keys.D) RightPressed = false;
        if(keycode == Input.Keys.W) UpPressed = false;
        if(keycode == Input.Keys.S) DownPressed = false;
        if(keycode == Input.Keys.CONTROL_LEFT) Runing = false;
        return false;
    }
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        MousePosition.set(screenX, Gdx.graphics.getHeight() - screenY);
        return false;
    }
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        MousePosition.set(screenX, Gdx.graphics.getHeight() - screenY);
        if (button == Input.Buttons.LEFT) {
            MouseLeft = true;
        } else if (button == Input.Buttons.RIGHT) {
            MouseRight = true;
        }
        return false;
    }
    @Override public boolean touchDragged (int screenX, int screenY, int pointer) {
        MousePosition.set(screenX, Gdx.graphics.getHeight() - screenY);
        return true;
    }
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        MousePosition.set(screenX, Gdx.graphics.getHeight() - screenY);
        if (button == Input.Buttons.LEFT) {
            MouseLeft = false;
        } else if (button == Input.Buttons.RIGHT) {
            MouseRight = false;
        }
        return false;
    }

    public Vector2 MousePosition() {
        return MousePosition;
    }

    public Vector2 getMovementDirection() {
        MovementDirection.set(0, 0);
        if(LeftPressed) MovementDirection.add(-1, 0);
        if(RightPressed) MovementDirection.add(1, 0);
        if(UpPressed) MovementDirection.add(0, 1);
        if(DownPressed) MovementDirection.add(0, -1);
        if((LeftPressed ^ RightPressed) && (UpPressed ^ DownPressed))
            MovementDirection.scl(1/(float) Math.sqrt(2));
        return MovementDirection;

    }
    public boolean isMouseLeft() {
        return MouseLeft;
    }
    public boolean isMouseRight() {
        return MouseRight;
    }

    public boolean isRuning() {
        return Runing;
    }
}
