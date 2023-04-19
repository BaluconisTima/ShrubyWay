package com.shrubyway.game.adapters;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;

public class MyInputAdapter extends InputAdapter {
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean upPressed;
    private boolean downPressed;
    private boolean spacePressed;

    private boolean mouseLeft;
    private boolean mouseRight;


    private boolean runing;
    private final Vector2 mousePosition = new Vector2();

    public final Vector2 movementDirection = new Vector2();
    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.A) leftPressed = true;
        if(keycode == Input.Keys.D) rightPressed = true;
        if(keycode == Input.Keys.W) upPressed = true;
        if(keycode == Input.Keys.S) downPressed = true;
        if(keycode == Input.Keys.CONTROL_LEFT) runing = true;
        if(keycode == Input.Keys.SPACE) spacePressed = true;
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.A) leftPressed = false;
        if(keycode == Input.Keys.D) rightPressed = false;
        if(keycode == Input.Keys.W) upPressed = false;
        if(keycode == Input.Keys.S) downPressed = false;
        if(keycode == Input.Keys.CONTROL_LEFT) runing = false;
        if(keycode == Input.Keys.SPACE) spacePressed = false;
        return false;
    }
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        mousePosition.set(screenX, Gdx.graphics.getHeight() - screenY);
        return false;
    }
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        mousePosition.set(screenX, Gdx.graphics.getHeight() - screenY);
        if (button == Input.Buttons.LEFT) {
            mouseLeft = true;
        } else if (button == Input.Buttons.RIGHT) {
            mouseRight = true;
        }
        return false;
    }
    @Override public boolean touchDragged (int screenX, int screenY, int pointer) {
        mousePosition.set(screenX, Gdx.graphics.getHeight() - screenY);
        return true;
    }
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        mousePosition.set(screenX, Gdx.graphics.getHeight() - screenY);
        if (button == Input.Buttons.LEFT) {
            mouseLeft = false;
        } else if (button == Input.Buttons.RIGHT) {
            mouseRight = false;
        }
        return false;
    }

    public Vector2 mousePosition() {
        return mousePosition;
    }

    public Vector2 getMovementDirection() {
        movementDirection.set(0, 0);
        if(leftPressed) movementDirection.add(-1, 0);
        if(rightPressed) movementDirection.add(1, 0);
        if(upPressed) movementDirection.add(0, 1);
        if(downPressed) movementDirection.add(0, -1);
        if((leftPressed ^ rightPressed) && (upPressed ^ downPressed))
            movementDirection.scl(1/(float) Math.sqrt(2));
        return movementDirection;

    }
    public boolean isSpacePressed() {
        if(spacePressed) {spacePressed = false; return true;}
        return false;
    }
    public boolean isMouseLeft() {
        return mouseLeft;
    }
    public boolean isMouseRight() {
        return mouseRight;
    }

    public boolean isRuning() {
        return runing;
    }
}
