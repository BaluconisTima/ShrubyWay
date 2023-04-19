package com.shrubyway.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
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
import com.shrubyway.game.visibleobject.RenderingList;
import com.shrubyway.game.visibleobject.VisibleObject;
import com.shrubyway.game.visibleobject.bullet.Bullet;
import com.shrubyway.game.visibleobject.decoration.Decoration;
import com.shrubyway.game.visibleobject.entity.Entity;
import com.shrubyway.game.visibleobject.entity.Shraby;
import com.shrubyway.game.visibleobject.visibleitem.VisibleItem;
import java.util.TreeSet;


public class ShrubyWay extends ApplicationAdapter {
    Shraby player;
    Map map;
    SpriteBatch batch;
    OrthographicCamera camera;
    Vector2 cameraPosition;
    Vector2 mousePosition;

    BitmapFont font;
    MyInputAdapter inputProcessor = new MyInputAdapter();

    @Override
    public void create() {
        Gdx.graphics.setVSync(true);
        batch = new SpriteBatch();
        batch.enableBlending();
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        font = new BitmapFont(Gdx.files.internal("fonts/font1.fnt"));
        batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        map = new Map(1);
        player = new Shraby(50, 50);
        Gdx.input.setInputProcessor(inputProcessor);
        RenderingList.add(player);
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

    public void playerInputWorking() {
        player.running(inputProcessor.isRuning());
        Vector2 movingVector = inputProcessor.getMovementDirection();
        mousePosition = new Vector2(inputProcessor.mousePosition().x + cameraPosition.x - Gdx.graphics.getWidth() / 2,
                inputProcessor.mousePosition().y + cameraPosition.y - Gdx.graphics.getHeight() / 2);
        if (inputProcessor.isMouseLeft()) {
            Bullet temp = player.shoot(mousePosition);
            if (temp != null) map.addVisibleObject(temp);
        }
        player.tryMoveTo(movingVector);
        correctPosition();
        player.liquidStatus(map.checkLiquid(player.positionLegs()));
        if (inputProcessor.isSpacePressed()) {
            player.attack();
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
        lastDrop) / 100000000000f > 0.1) {
            lastDrop = AnimationGlobalTime.x;
            Inventory.dropItem(player.faceDirection, player.positionLegs());
        }


    }

    public void sortList() {
        TreeSet<VisibleObject> temp = new TreeSet<>();
        for(VisibleObject obj : RenderingList.list){
            temp.add(obj);
        }
        RenderingList.list = temp;
    }

    public void globalProcessing() {
        for (VisibleObject obj : RenderingList.list) {
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
        sortList();
        RenderingList.allTemp();

        map.updateRenderingObjects(player.positionCenter());
        for(VisibleObject obj : RenderingList.list){
            if(obj.attackBox() != null && obj.attackBox().topLeftCorner.x < obj.attackBox().bottomRightCorner.x) {
                for(VisibleObject obj2 : RenderingList.list){
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
        sortList();
        RenderingList.allTemp();
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
        for (VisibleObject obj : RenderingList.list) {
            obj.render(batch);
        }
        batch.end();
    }

    public void renderInterface() {

        batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        batch.begin();
        Inventory.render(batch);
        font.draw(batch, "" + RenderingList.list.size() + " " + Gdx.graphics.getFramesPerSecond(), 100, 100);
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
