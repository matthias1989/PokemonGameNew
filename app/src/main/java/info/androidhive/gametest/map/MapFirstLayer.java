package info.androidhive.gametest.map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.SurfaceView;

import java.util.List;

import info.androidhive.gametest.Utils;
import info.androidhive.gametest.abstractClasses.FirstLayer;
import info.androidhive.gametest.sprites.Sprite;
import info.androidhive.gametest.sprites.TrainerSprite;

/**
 * Created by matthias on 3/19/2016.
 */
public class MapFirstLayer extends FirstLayer {
    private Rect rectangle;
    private Paint paint;
    private Bitmap pokemonCenter;

    //private List<Sprite> sprites = new ArrayList<>();
    private SurfaceView mParent;
    private int scrollX = Utils.scrollCoords.get("scrollX");
    private int scrollY = Utils.scrollCoords.get("scrollY");
    private int pokecenterPosX;
    private int pokecenterPosY;
    private int width;
    private int height;

    private int markX=0;
    private int markY=0;
    private int destX=0;
    private int destY=0;
    private int trainerX=0;
    private int trainerY=0;
    private String greetingMessage="";
    private TrainerSprite currentSprite=null;
    private int timeCounter = 0;
    private int timeCounter2 = 0;
    private int strCounter = 0;

    private Bitmap bug_catcher_walking1;
    private Bitmap bug_catcher_walking2;

    public MapFirstLayer(SurfaceView view){
        mParent = view;
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(35);

        Log.d("SCROLL",scrollY+"");


        for(Sprite sprite : Utils.allSprites){
            sprite.update();
        }

        //pokecenter.update();
    }
    @Override
    public void playfield(int width, int height) {

    }

    @Override
    public void update(RectF dirty, double timeDelta) {

    }

    @Override
    public void draw(Canvas c) {
        for(Sprite sprite : Utils.allSprites) {
             sprite.draw(c);
        }

        if(markX>0){
            trainerBeforeFightAnimation(c);
        }
        timeCounter++;
    }

    private void trainerBeforeFightAnimation(Canvas c){
        if(timeCounter<10)
            c.drawBitmap(Utils.exclamationMark,markX, markY, null);

        else if((destX!=trainerX || destY!=trainerY)) {
            approachingMeAnimation(c);
        }
        else{
            if(timeCounter2==0) {
                readyForConversationAnimation(c);
            }
            if(!greetingMessage.equals("")){
                if(timeCounter2<60) {
                    Utils.interactionMessageDisplayed = true;
                    c.drawText(greetingMessage, 0, strCounter, 70, 840, paint);
                    if (strCounter < greetingMessage.length())
                        strCounter++;
                }
                else if(timeCounter2==60){
                    Utils.interactionMessageDisplayed = false;
                    greetingMessage = "";
                    interactionListener.trainerFightStarted(currentSprite);
                }
            }
            timeCounter2++;
        }
    }

    private void approachingMeAnimation(Canvas c){
        if(timeCounter%3==0) {
            if (destX > trainerX)
                trainerX += Utils.tileSize / 2;
            else if (destX < trainerX)
                trainerX -= Utils.tileSize / 2;
            else if (destY > trainerY)
                trainerY += Utils.tileSize / 2;
            else if (destY < trainerY)
                trainerY -= Utils.tileSize / 2;
            currentSprite.setX(trainerX + currentSprite.getScrollX() + Utils.tileSize / 2); // trainerX is destermined by X, scrollX and the extra
            currentSprite.setY(trainerY + currentSprite.getScrollY() + Utils.tileSize);

            // All other cases, and other sprites
            if (currentSprite.getStatus().startsWith("front")) {
                if (currentSprite.getStatus().equals("frontwalking2"))
                    currentSprite.setStatus("frontwalking1");
                else
                    currentSprite.setStatus("frontwalking2");
            }
            else if (currentSprite.getStatus().startsWith("back")) {
                if (currentSprite.getStatus().equals("backwalking2"))
                    currentSprite.setStatus("backwalking1");
                else
                    currentSprite.setStatus("backwalking2");

            }
            else if (currentSprite.getStatus().startsWith("left")) {
                if (currentSprite.getStatus().equals("leftwalking2"))
                    currentSprite.setStatus("leftwalking1");
                else
                    currentSprite.setStatus("leftwalking2");
            }
            else if (currentSprite.getStatus().startsWith("right")) {
                if (currentSprite.getStatus().equals("rightwalking2"))
                    currentSprite.setStatus("rightwalking1");
                else
                    currentSprite.setStatus("rightwalking2");
            }
        }
    }

    private void readyForConversationAnimation(Canvas c){
        if (currentSprite.getStatus().startsWith("front") && !Utils.mySprite.getStatus().startsWith("back")) {
            Utils.mySprite.setStatus("backstanding");
        } else if (currentSprite.getStatus().startsWith("back") && !Utils.mySprite.getStatus().startsWith("front")) {
            Utils.mySprite.setStatus("frontstanding");
        } else if (currentSprite.getStatus().startsWith("left") && !Utils.mySprite.getStatus().startsWith("right")) {
            Utils.mySprite.setStatus("rightstanding");
        } else if (currentSprite.getStatus().startsWith("right") && !Utils.mySprite.getStatus().startsWith("left")) {
            Utils.mySprite.setStatus("leftstanding");
        }
        if (currentSprite.getStatus().startsWith("front"))
            currentSprite.setStatus("frontstanding");
        else if (currentSprite.getStatus().startsWith("back"))
            currentSprite.setStatus("backstanding");
        else if (currentSprite.getStatus().startsWith("left"))
            currentSprite.setStatus("leftstanding");
        else if (currentSprite.getStatus().startsWith("right"))
            currentSprite.setStatus("rightstanding");

        currentSprite.setBitmap(currentSprite.getAllBitmaps().get(currentSprite.getStatus()));
    }

    public int getScrollX() {
        return scrollX;
    }

    public void setScrollX(int scrollX) {
        this.scrollX = scrollX;
        for(Sprite sprite : Utils.allSprites) {
            sprite.setScrollX(scrollX);
        }
        //Log.d("scrollX",scrollX+"<->"+sprites.get("pokecenter").getScrollX());
    }

    public int getScrollY() {
        return scrollY;

    }

    public void setScrollY(int scrollY) {
        this.scrollY = scrollY;
        for(Sprite sprite : Utils.allSprites){
            sprite.setScrollY(scrollY);
        }
        //sprites.get("pokecenter").setScrollY(scrollY);
        //Log.d("scrollY",scrollY+"<->"+sprites.get("pokecenter").getScrollY());
    }

    @Override
    public List<Sprite> getSprites() {
        return Utils.allSprites;
    }


//    public Sprite createSprite(int id, int x, int y, int width, int height,String type,String status){
//        Bitmap a = createBitmap(id,width,height);
//
//        Sprite sprite = new Sprite(0,a, x,y,width,height,type);
//        sprite.setStatus(status);
//        sprite.setScrollX(scrollX);
//        sprite.setScrollY(scrollY);
//        return sprite;
//    }

    private Bitmap createBitmap(int id,int width,int height){
        Bitmap a = BitmapFactory.decodeResource(mParent.getResources(), id);
        return Bitmap.createScaledBitmap(a, width, height, false);
    }

    @Override
    public void addSprite(Sprite sprite) {
        Utils.allSprites.add(sprite);
    }

    @Override
    public void setSprites(List<Sprite> sprites) {
        Utils.allSprites = sprites;
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

    public void discoveredByTrainer(int trainerX,int trainerY,int destX, int destY, TrainerSprite currentSprite){
        this.currentSprite = currentSprite;
        this.destX = destX;
        this.destY = destY;
        this.trainerX = trainerX;
        this.trainerY = trainerY;
        markX=trainerX-Utils.tileSize/2;
        markY=trainerY-(int)(1.5*Utils.tileSize);
        timeCounter = 0;
        timeCounter2 = 0;
        strCounter = 0;

        greetingMessage = "Hi there, let's battle and see who's the best!";
        Log.d("DEST",trainerX+"->"+destX+","+trainerY+"->"+destY);


    }
}
