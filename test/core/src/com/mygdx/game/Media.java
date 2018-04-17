package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Media {
    public static Texture hero;
    public static Animation<TextureRegion> upAnimation, downAnimation, rightAnimation, leftAnimation;
    
    //spritesheet data
    private static final int FRAME_COLS = 5;
    private static final int FRAME_ROWS = 4;
    
    public static void load_assets() {
        
        //load spritesheet
        hero = new Texture("entities/hero/spritesheet.png");
        
        //2d array for spritesheet
        TextureRegion[][] tmp = TextureRegion.split(hero, hero.getWidth() / FRAME_COLS,
				hero.getHeight() / FRAME_ROWS);
        
        //1d array
        TextureRegion[] downWalk, upWalk, rightWalk, leftWalk;
        upWalk = initAnim(3, tmp);
        downWalk = initAnim(0, tmp);
        rightWalk = initAnim(1, tmp);
        leftWalk = initAnim(2, tmp);
	
        upAnimation = new Animation<TextureRegion>(0.1f, upWalk);
        downAnimation = new Animation<TextureRegion>(0.1f, downWalk);
        rightAnimation = new Animation<TextureRegion>(0.1f, rightWalk);
        leftAnimation = new Animation<TextureRegion>(0.1f, leftWalk);
        
    
    }
    
    public static TextureRegion[] initAnim(int start, TextureRegion[][] tmp) {
        
        TextureRegion[] walk = new TextureRegion[4];
        int count = 0;
        int k = 0;
        
        if ((start*4) > 4) {
            k = (start*4)/FRAME_COLS;
            start = (start*4) - (k*FRAME_COLS);
        }
        else {
            start *= 4;
        }
        
        
	for (int i = k; i < FRAME_ROWS; i++) {
            for (int j = 0 + start; j < FRAME_COLS; j++) {
		walk[count] = tmp[i][j];
                count++;
                
                if (count > 3) {
                    
                    return walk;
                    
                }
            }
            start = 0;
	}
        
        return walk;
        
        
    }
    
}
