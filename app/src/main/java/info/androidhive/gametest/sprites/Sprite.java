package info.androidhive.gametest.sprites;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by matthias on 3/12/2016.
 */
public class Sprite {
    private int id;
    private Bitmap bitmap;
    private String name;
    private String type;
    private String location;
    private int x;
    private int y;
    private int width;
    private int height;

    Random random = new Random();
    private int scrollX=0;
    private int scrollY=0;

    private Context context;


    final int MAX = 99;

    public Sprite(int id, int tx, int ty, int width, int height, String name,String location, Context context){
        //bitmap = bm;
        this.type = name;
        this.name = name;
        x = tx;
        y = ty;
        this.width = width;
        this.height = height;
        this.id = id;
        this.context = context;
        this.location = location;
        //setBitmap(type,status);
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(String name, String status){
        String id = (status.equals("empty"))?name:name+"_"+status;
        int resID = context.getResources().getIdentifier(id, "drawable", context.getPackageName());
        Bitmap a = BitmapFactory.decodeResource(context.getResources(), resID);
        bitmap = Bitmap.createScaledBitmap(a, width, height, false);

    }
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
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


    public void draw(Canvas canvas){
        //Log.d("SpriteScroll",scrollX+","+scrollY);
        //if(!getName().startsWith())
        canvas.drawBitmap(bitmap, x-width-scrollX, y-height-scrollY, null);
    }


    public void update(){
//        x += (dirX * moveX);
//        y += (dirY * moveY);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
