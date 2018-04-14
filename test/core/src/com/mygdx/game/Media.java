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
        upWalk = new TextureRegion[4];
        downWalk = new TextureRegion[4];
        rightWalk = new TextureRegion[4];
        leftWalk = new TextureRegion[4];
        
        initAnim(upWalk, tmp);
        initAnim(downWalk, tmp);
        initAnim(rightWalk, tmp);
        initAnim(leftWalk, tmp);
	
        upAnimation = new Animation<TextureRegion>(0.025f, upWalk);
        downAnimation = new Animation<TextureRegion>(0.025f, downWalk);
        rightAnimation = new Animation<TextureRegion>(0.025f, rightWalk);
        leftAnimation = new Animation<TextureRegion>(0.025f, leftWalk);
        
    
    }
    
    public static void initAnim(TextureRegion[] walk, TextureRegion[][] tmp) {
        
        int index = 0;
	for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < 4; j++) {
		walk[index++] = tmp[i][j];
            }
	}
        
        
    }
    
}
