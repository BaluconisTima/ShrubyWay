package com.shrubyway.game.adapters;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;

public class MyInputAdapter extends InputAdapter {
    private boolean leftPressed, rightPressed, upPressed, downPressed,
            spacePressed, ePressed, qPressed, escPressed, lPressed, cPressed, minusPressed, plusPressed, zPressed;
    private boolean numberPressed[] = new boolean[10];
    private boolean mouseLeft, mouseRight, mouseLeft2;
    private boolean runing;
    private final Vector2 mousePosition = new Vector2();
    public final Vector2 movementDirection = new Vector2();
    private int scroll;


    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.A) leftPressed = true;
        if(keycode == Input.Keys.D) rightPressed = true;
        if(keycode == Input.Keys.W) upPressed = true;
        if(keycode == Input.Keys.S) downPressed = true;
        if(keycode == Input.Keys.E) ePressed = true;
        if(keycode == Input.Keys.CONTROL_LEFT) runing = true;
        if(keycode == Input.Keys.SPACE) spacePressed = true;
        if(keycode == Input.Keys.Q) qPressed = true;
        if(keycode == Input.Keys.ESCAPE) escPressed = true;
        if(keycode == Input.Keys.L) lPressed = true;
        if(keycode == Input.Keys.C) cPressed = true;
        if(keycode == Input.Keys.MINUS) minusPressed = true;
        if(keycode == Input.Keys.PLUS) plusPressed = true;
        if(keycode == Input.Keys.Z) zPressed = true;

        for(int i = 0; i < 10; i++) {
            if(keycode == Input.Keys.NUM_0 + i) numberPressed[i] = true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.A) leftPressed = false;
        if(keycode == Input.Keys.D) rightPressed = false;
        if(keycode == Input.Keys.W) upPressed = false;
        if(keycode == Input.Keys.S) downPressed = false;
        if(keycode == Input.Keys.E) ePressed = false;
        if(keycode == Input.Keys.CONTROL_LEFT) runing = false;
        if(keycode == Input.Keys.SPACE) spacePressed = false;
        if(keycode == Input.Keys.Q) qPressed = false;
        if(keycode == Input.Keys.ESCAPE) escPressed = false;
        if(keycode == Input.Keys.L) lPressed = false;
        if(keycode == Input.Keys.C) cPressed = false;
        if(keycode == Input.Keys.MINUS) minusPressed = false;
        if(keycode == Input.Keys.PLUS) plusPressed = false;
        if(keycode == Input.Keys.Z) zPressed = false;
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
            mouseLeft2 = true;
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
            mouseLeft2 = false;
        } else if (button == Input.Buttons.RIGHT) {
            mouseRight = false;
        }
        return false;
    }
    @Override
    public boolean scrolled(float amountX, float amountY) {
        scroll = Math.round(amountY);
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
        if((leftPressed ^ rightPressed) && (upPressed ^ downPressed)) movementDirection.nor();
        return movementDirection;
    }
    public boolean isSpacePressed() {
        return spacePressed;
    }
    public void setSpacePressed(boolean spacePressed) {
        this.spacePressed = spacePressed;
    }
    public boolean isMouseLeft() {
        if(mouseLeft) {mouseLeft = false; return true;}
        return false;
    }

    public boolean isMouseLeftPressed() {
        return mouseLeft2;
    }
    public boolean isMouseRight() {
        return mouseRight;
    }
    public void setMouseRight(boolean mouseRight) {
        this.mouseRight = mouseRight;
    }
    public boolean isEPressed() {
        if(ePressed) {ePressed = false; return true;}
        return ePressed;
    }

    public boolean isMinusPressed() {
        return minusPressed;
    }

    public boolean isPlusPressed() {
        return plusPressed;
    }



    public boolean isEscPressed() {
        if(escPressed) {escPressed = false; return true;}
        return escPressed;
    }

    public boolean isZPressed() {
        if(zPressed) {zPressed = false; return true;}
        return zPressed;
    }

    public boolean isLPressed() {
        if(lPressed) {lPressed = false; return true;}
        return lPressed;
    }

    public boolean isCPressed() {
        if(cPressed) {cPressed = false; return true;}
        return cPressed;
    }

    public int numberPressed() {
        for(int i = 0; i < 10; i++) {
            if(numberPressed[i]) {
                numberPressed[i] = false;
                return i;
            }
        }
        return -1;
    }
    public int getScroll() {
        int x = scroll;
        scroll = 0;
        return x;
    }



    public boolean isRuning() {
        return runing;
    }

    public boolean isQPressed() {
        return qPressed;
    }

    public void dispose () {

    }


}
