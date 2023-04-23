package com.shrubyway.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.shrubyway.game.adapters.MyInputAdapter;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.item.ItemManager;
import com.shrubyway.game.map.Map;
import com.shrubyway.game.visibleobject.ObjectsList;
import com.shrubyway.game.visibleobject.VisibleObject;
import com.shrubyway.game.visibleobject.bullet.Bullet;
import com.shrubyway.game.visibleobject.decoration.Decoration;
import com.shrubyway.game.visibleobject.entity.Entity;
import com.shrubyway.game.visibleobject.entity.Shraby;
import com.shrubyway.game.visibleobject.visibleitem.VisibleItem;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;


public class ShrubyWay extends ApplicationAdapter {
    Shraby player;
    Map map;
    SpriteBatch batch;
    OrthographicCamera camera;
    Vector2 cameraPosition;
    Vector2 mousePosition;

    MyInputAdapter inputProcessor = new MyInputAdapter();

    @Override
    public void create() {
        Gdx.graphics.setVSync(true);
        Gdx.gl.glTexParameterf(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_MIN_FILTER, GL20.GL_LINEAR);
        batch = new SpriteBatch();
        batch.enableBlending();
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        map = new Map(1);
        player = new Shraby(50, 50);
        Gdx.input.setInputProcessor(inputProcessor);
        ObjectsList.add(player);
        ItemManager.init();
        cameraPosition = new Vector2(player.positionCenter());
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(cameraPosition.x, cameraPosition.y, 0);
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        AnimationGlobalTime.x = 0f;
    }

    public void correctPosition() {
        Vector2 temp = new Vector2(player.position());
        if (temp.x < 0) {
            temp.add(new Vector2(38400, 0));
            cameraPosition.add(new Vector2(38400, 0));
        }
        if (temp.y < 0) {
            temp.add(new Vector2(0, 38400));
            cameraPosition.add(new Vector2(0, 38400));
        }
        if (temp.x >= 38400) {
            temp.add(new Vector2(-38400, 0));
            cameraPosition.add(new Vector2(-38400, 0));
        }
        if (temp.y >= 38400) {
            temp.add(new Vector2(0, -38400));
            cameraPosition.add(new Vector2(0, -38400));
        }
        player.changePosition(temp);
    }

    boolean leftClick = false, rightClick = false;
    public void playerInputWorking() {
        player.running(inputProcessor.isRuning());
        Vector2 movingVector = inputProcessor.getMovementDirection();
        mousePosition = new Vector2(inputProcessor.mousePosition().x + cameraPosition.x - Gdx.graphics.getWidth() / 2,
                inputProcessor.mousePosition().y + cameraPosition.y - Gdx.graphics.getHeight() / 2);
        leftClick = inputProcessor.isMouseLeft();
        rightClick = inputProcessor.isMouseRight();
        player.tryMoveTo(movingVector);
        correctPosition();
        player.liquidStatus(map.checkLiquid(player.positionLegs()));
        if (inputProcessor.isSpacePressed()) {
            player.attack();
        }
        if((leftClick || rightClick) &&
                !Inventory.checkClick(inputProcessor.mousePosition())) {
            leftClick = false;
            rightClick = false;
        }
    }
    public float lastDrop = -1000000000f;
    public void interfaceInputWorking() {
        if(inputProcessor.isEPressed()){
            Inventory.changeOpenned();
        }
        int x = inputProcessor.numberPressed();
        if(x > 0) {
            Inventory.changeSelected(x);
        }
        x = inputProcessor.getScroll();
        Inventory.addSelected(x);

        if(inputProcessor.isQPressed() && (AnimationGlobalTime.x -
        lastDrop) > 0.1f) {
            lastDrop = AnimationGlobalTime.x;
            Inventory.dropItem(player.faceDirection, player.positionLegs());
        }
        if(leftClick){
            leftClick = false;
            Inventory.leftClick(inputProcessor.mousePosition());
        }
        if(rightClick){
            rightClick = false;
            Inventory.rightClick(inputProcessor.mousePosition());
        }
        if(!Inventory.opened) {
            Inventory.dropItemHand(player.faceDirection, player.positionLegs());
        }

    }


    public void globalProcessing() {
        CopyOnWriteArrayList<VisibleObject> temp = new CopyOnWriteArrayList<VisibleObject>(ObjectsList.getList());

        for (VisibleObject obj : temp) {
            if(!ObjectsList.getList().contains(obj)) continue;
            if (obj instanceof Bullet) {
                ((Bullet) obj).tryMoveTo();
            } else
            if (obj instanceof Entity) {
                if (((Entity) obj).makingStep(map.checkTile(((Entity) obj).positionLegs()))) {
                    map.makeStep(((Entity) obj).positionLegs(), player.positionLegs());
                }
            } else
            if(obj instanceof VisibleItem){
                ((VisibleItem) obj).moveToPlayer(player.positionLegs());
            }
        }


        map.updateRenderingObjects(player.positionCenter());
        temp = new CopyOnWriteArrayList<VisibleObject>(ObjectsList.getList());

        for(VisibleObject obj : temp){
            if(!ObjectsList.getList().contains(obj)) continue;
            if(obj.attackBox() != null && obj.attackBox().topLeftCorner.x < obj.attackBox().bottomRightCorner.x) {
                for(VisibleObject obj2 : temp){
                    if(!ObjectsList.getList().contains(obj2)) continue;
                    if(obj2.hitBox() != null) {
                        if (obj.attackBox().overlaps(obj2.hitBox())) {
                            if(obj2 instanceof Decoration) {
                                ((Decoration) obj2).interact();
                            }
                        }
                    }
                }
            }
        }
        ObjectsList.sort();
    }

    public void cameraUpdate() {
        cameraPosition.lerp(player.positionCenter(),
                0.1f);
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(Math.round(cameraPosition.x), Math.round(cameraPosition.y), 0);
        camera.update();
    }
    public void gameTick() {
        AnimationGlobalTime.x += Gdx.graphics.getDeltaTime();
        playerInputWorking();
        interfaceInputWorking();
        globalProcessing();
        cameraUpdate();
    }

    public void renderObjects() {
        batch.setProjectionMatrix(camera.combined);
        ScreenUtils.clear(1, 1, 1, 1);
        batch.begin();
        map.render(batch, player.position());
        for (VisibleObject obj : ObjectsList.getList()) {
            obj.render(batch);
        }
        batch.end();
    }

    public void renderInterface() {
        batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        batch.begin();
        Inventory.render(batch, inputProcessor.mousePosition());
        TextDrawer.drawWhite(batch, "" + inputProcessor.mousePosition() + " " + VisibleObject.ids, 100, 100, 0.5f);
        batch.end();
    }

    public void renderFrame() {
        renderObjects();
        renderInterface();
    }

    @Override
    public void render() {
        gameTick();
        renderFrame();
    }

    @Override
    public void dispose() {
        // player.dispose();
    }
}
