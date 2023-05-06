package com.shrubyway.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.shrubyway.game.TextDrawer;
import com.badlogic.gdx.video.*;

public class ErrorInformationScreen extends Screen {
    static FileHandle fileHandle;
    String ErrorInformation = null;
    private VideoPlayer videoPlayer;
    Batch batch;
    public ErrorInformationScreen(String ErrorInformation) {
       this.ErrorInformation = ErrorInformation;
       batch = new SpriteBatch();
       videoPlayer = VideoPlayerCreator.createVideoPlayer();
       videoPlayer.setOnCompletionListener(new VideoPlayer.CompletionListener() {
           @Override
           public void onCompletionListener(FileHandle file) {

           }
       } );

       try {
         videoPlayer.play(Gdx.files.absolute("interface/ERROR.webm"));
         } catch (Exception e) {
            Gdx.app.exit();
       }

    }

    @Override public void updateScreen() {
        videoPlayer.update();
    }

    @Override public void renderScreen() {
        ScreenUtils.clear(1, 1, 1, 1);

         batch.begin();
        Texture frame = videoPlayer.getTexture();
        if(frame != null) {
            batch.draw(frame, 0, 0);
        }
        if(!videoPlayer.isPlaying()) {
            Gdx.app.exit();
        }
        if(ErrorInformation != null) {
            TextDrawer.drawBlack(batch, ErrorInformation, 200, 100, 1);
        }
        batch.end();
    }
}
