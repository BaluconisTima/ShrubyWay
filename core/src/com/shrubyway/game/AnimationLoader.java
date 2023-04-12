package com.shrubyway.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;

public class AnimationLoader {
       static Animator animator = new Animator();
       static Texture AnimationList;
       AnimationLoader() {
       }
       protected Animation<TextureRegion>[][][] Load(String s, String actions[], String actionsTypes[][], int FrameCount[]){
                Animation<TextureRegion>[][][] CurrentAnimation = new Animation[actions.length][4][2];
              for(int i = 0; i < actions.length; i++) {
                     for(int j = 0; j < 4; j++) {
                            if(actionsTypes[i][j] == null) continue;
                            String tempAnimation = s + "/" + actions[i] + "/" + actionsTypes[i][j] + ".png";
                            AnimationList = new Texture(Gdx.files.internal(tempAnimation));
                            CurrentAnimation[i][j][0] = animator.toAnimation(AnimationList, FrameCount[i], 0,0);
                            CurrentAnimation[i][j][1] = animator.toAnimation(AnimationList, FrameCount[i], 0, 130);
                     }
              }
              return CurrentAnimation;
       }
}
