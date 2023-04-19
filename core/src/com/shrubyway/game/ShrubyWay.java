package com.shrubyway.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.shrubyway.game.animation.AnimationGlobalTime;
import com.shrubyway.game.map.Map;
import com.shrubyway.game.visibleobject.VisibleObject;
import com.shrubyway.game.visibleobject.bullet.Bullet;
import com.shrubyway.game.visibleobject.decoration.Decoration;
import com.shrubyway.game.visibleobject.entity.Entity;
import com.shrubyway.game.visibleobject.entity.Shraby;

import java.util.TreeSet;


public class ShrubyWay extends ApplicationAdapter {
    Shraby player;
    Map map;
    SpriteBatch batch;
    OrthographicCamera camera;
    Vector2 cameraPosition;
    Vector2 mousePosition;
    TreeSet<VisibleObject> renderingObjects;
    private BitmapFont font;

    private MyInputAdapter inputProcessor = new MyInputAdapter();

    @Override
    public void create() {

        Gdx.graphics.setWindowedMode(1920, 1080);
        Graphics.DisplayMode currentDisplayMode = Gdx.graphics.getDisplayMode();
        Gdx.graphics.setFullscreenMode(currentDisplayMode);
        Gdx.graphics.setVSync(true);

        batch = new SpriteBatch();
        batch.enableBlending();
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        font = new BitmapFont();
        font.getData().setScale(3);
        batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        map = new Map(1);
        player = new Shraby(0, 0);
        renderingObjects = new TreeSet<>();
        Gdx.input.setInputProcessor(inputProcessor);
        renderingObjects.add(player);

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
    public void gameTick() {
        AnimationGlobalTime.x += Gdx.graphics.getDeltaTime();
        player.running(inputProcessor.isRuning());
        Vector2 movingVector = inputProcessor.getMovementDirection();
        mousePosition = new Vector2(inputProcessor.mousePosition().x + cameraPosition.x - Gdx.graphics.getWidth() / 2,
                inputProcessor.mousePosition().y + cameraPosition.y - Gdx.graphics.getHeight() / 2);
        if (inputProcessor.isMouseLeft()) {
            Bullet temp = player.shoot(mousePosition);
            if (temp != null) map.addVisibleObject(temp);
        }

        player.tryMoveTo(movingVector, renderingObjects);
        correctPosition();
        player.liquidStatus(map.checkLiquid(player.positionLegs()));
        if (inputProcessor.isSpacePressed()) {
            player.attack();
        }
        TreeSet<VisibleObject> temp = new TreeSet<>();
        for(VisibleObject obj : renderingObjects){
            temp.add(obj);
        }
        renderingObjects = temp;

        for (VisibleObject obj : renderingObjects) {
            if (obj instanceof Bullet) {
                ((Bullet) obj).tryMoveTo();
            }
            if (obj instanceof Entity) {
                if (((Entity) obj).makingStep(map.checkTile(((Entity) obj).positionLegs()))) {
                    map.makeStep(((Entity) obj).positionLegs(), player.positionLegs());
                }
            }
        }

        temp = new TreeSet<>();
        for(VisibleObject obj : renderingObjects){
            temp.add(obj);
        }
        renderingObjects = temp;
        map.updateRenderingObjects(player.positionCenter(), renderingObjects);


        for(VisibleObject obj : renderingObjects){
            if(obj.attackBox() != null && obj.attackBox().topLeftCorner.x < obj.attackBox().bottomRightCorner.x) {
                for(VisibleObject obj2 : renderingObjects){
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
    }

    public void renderFrame() {
        cameraPosition.lerp(player.positionCenter(),
                0.1f);
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(Math.round(cameraPosition.x), Math.round(cameraPosition.y), 0);
        camera.update();

        batch.setProjectionMatrix(camera.combined);

        ScreenUtils.clear(1, 1, 1, 1);
        batch.begin();
        map.render(batch, player.position());
        for (VisibleObject obj : renderingObjects) {
            obj.render(batch);
        }
        batch.end();


        batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        batch.begin();
        font.draw(batch, "" + renderingObjects.size() + " " + Gdx.graphics.getFramesPerSecond(), 100, 100);
        batch.end();
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
