package info.androidhive.gametest.sprites;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import java.util.Random;

/**
 * Created by matthias on 3/12/2016.
 */
public class Sprite {
    private Bitmap bitmap;
    private int x;
    private int y;
    float bitmap_halfWidth, bitmap_halfHeight;

    Random random = new Random();
    private int scrollX=0;
    private int scrollY=0;

    private String status;


    final int MAX = 99;

    public Sprite(Bitmap bm, int tx, int ty, int width, int height){
        bitmap = bm;

        x = tx;
        y = ty;

//        bitmap_halfWidth = width/2;
//        bitmap_halfHeight = height/2;
        bitmap_halfWidth = width;
        bitmap_halfHeight = height;

    }

    public void setX(int tx){
        x = tx;
    }

    public void setY(int ty){
        y = ty;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int getScrollX() {
        return scrollX;
    }

    public void setScrollX(int scrollX) {
        this.scrollX = scrollX;
    }

    public int getScrollY() {
        return scrollY;
    }

    public void setScrollY(int scrollY) {
        this.scrollY = scrollY;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void draw(Canvas canvas){
        //Log.d("SpriteScroll",scrollX+","+scrollY);
        canvas.drawBitmap(bitmap, x-bitmap_halfWidth-scrollX, y-bitmap_halfHeight-scrollY, null);
    }


    public void update(){
//        x += (dirX * moveX);
//        y += (dirY * moveY);
    }

    public float getWidth() {
        return bitmap_halfWidth;
    }

    public void setWidth(float bitmap_halfWidth) {
        this.bitmap_halfWidth = bitmap_halfWidth;
    }

    public float getHeight() {
        return bitmap_halfHeight;
    }

    public void setHeight(float bitmap_halfHeight) {
        this.bitmap_halfHeight = bitmap_halfHeight;
    }
}
