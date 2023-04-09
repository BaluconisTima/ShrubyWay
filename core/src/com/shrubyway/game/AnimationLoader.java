package com.shrubyway.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationLoader {
       static Animator animator = new Animator();
       static Texture AnimationList;
       AnimationLoader() {

       }
       protected Animation<TextureRegion>[][][] Load(String s, int FrameCount){
                Animation<TextureRegion>[][][] CurrentAnimation = new Animation[4][2][2];
              for(int i = 0; i < 4; i++) {
                     String Direction = "";
                     switch(i) {
                            case 0: Direction += "DOWN"; break;
                            case 1: Direction += "UP"; break;
                            case 2: Direction += "LEFT"; break;
                            case 3: Direction += "RIGHT"; break;
                     }
                     for(int j = 0; j < 2; j++) {
                            String Action = "";
                            switch(j) {
                                   case 0: Action += "AFK"; break;
                                   case 1: Action += "WALK"; break;
                            }
                            String tempAnimation = s + "/" + Action + "/" + Direction + ".png";
                            AnimationList = new Texture(Gdx.files.internal(tempAnimation));
                            CurrentAnimation[i][j][0] = animator.toAnimation(AnimationList, FrameCount, 0,0);
                            CurrentAnimation[i][j][1] = animator.toAnimation(AnimationList, FrameCount, 0, 50);
                     }

              }
              return CurrentAnimation;
       }
}
