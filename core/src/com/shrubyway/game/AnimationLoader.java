package com.shrubyway.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationLoader {
       static Animator animator = new Animator();
       static Texture animationList;
       AnimationLoader() {
       }
       protected Animation<TextureRegion>[][][] load(String s, String actions[], String actionsTypes[][], int frameCount[]){
                Animation<TextureRegion>[][][] currentAnimation = new Animation[actions.length][4][2];
              for(int i = 0; i < actions.length; i++) {
                     for(int j = 0; j < 4; j++) {
                            if(actionsTypes[i][j] == null) continue;
                            String tempAnimation = s + "/" + actions[i] + "/" + actionsTypes[i][j] + ".png";
                            animationList = new Texture(Gdx.files.internal(tempAnimation));
                            currentAnimation[i][j][0] = animator.toAnimation(animationList, frameCount[i], 0,0);
                            currentAnimation[i][j][1] = animator.toAnimation(animationList, frameCount[i], 0, 130);
                     }
              }
              return currentAnimation;
       }
}
