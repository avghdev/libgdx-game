
package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Enums.ENTITYTYPE;


public class Hero extends Entity{
    
    public Hero(Vector2 pos) {
        
        type = ENTITYTYPE.HERO;
        width = 18;
        height = 26;
        this.pos.x = pos.x;
        this.pos.y = pos.y;
        this.pos3.x = pos.x;
        this.pos3.y = pos.y;
        texture = Media.hero;
        rightWalk = Media.rightAnimation;
        leftWalk = Media.leftAnimation;
        upWalk = Media.upAnimation;
        downWalk = Media.downAnimation;
        this.aDirect = downWalk;
        isMoving = false;
        speed = 1;
        
        
        
    }
    
    public void update(Control control) {
        dirX = 0;
        dirY = 0;
        
        if (control.down) {
            dirY = -1;
            this.aDirect = downWalk;
        }
        if (control.up) {
            dirY = 1;
            this.aDirect = upWalk;
        }
        if (control.left) {
            dirX = -1;
            this.aDirect = leftWalk;
        }
        if (control.right) {
            dirX = 1;
            this.aDirect = rightWalk;
        }
        
        pos.x += dirX * speed;
        pos.y += dirY * speed;
        
        pos3.x = get_camera_x();
        pos3.y = get_camera_y();
        
    }
    
    public float get_camera_x() {
        return pos.x + width/2;
    }

    public float get_camera_y() {
        return pos.y + height/2;
    }
    
}
