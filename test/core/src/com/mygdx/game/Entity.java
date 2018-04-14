package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Enums.ENTITYTYPE;


public class Entity {
    public Vector2 pos;
    public Vector3 pos3;
    public Texture texture;
    public Animation<TextureRegion> rightWalk, leftWalk, upWalk, downWalk;
    public float width;
    public float height;
    public ENTITYTYPE type;
    public float speed;
    float stateTime = 0f;
    
    float dirX = 0;
    float dirY = 0;
    
    public Entity() {
        pos = new Vector2();
        pos3 = new Vector3();
    }
    
    public void draw(SpriteBatch batch, Animation<TextureRegion> aDir) {
        stateTime += Gdx.graphics.getDeltaTime();
        
        TextureRegion frame = aDir.getKeyFrame(stateTime, true);
        batch.draw(frame, pos.x, pos.y, width, height);
    }
    
}
