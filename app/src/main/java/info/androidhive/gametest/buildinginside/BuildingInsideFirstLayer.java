package info.androidhive.gametest.buildinginside;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.gametest.GameSurfaceView;
import info.androidhive.gametest.InteractionListener;
import info.androidhive.gametest.Utils;
import info.androidhive.gametest.abstractClasses.FirstLayer;
import info.androidhive.gametest.R;
import info.androidhive.gametest.sprites.Sprite;

/**
 * Created by matthias on 3/20/2016.
 */
public class BuildingInsideFirstLayer extends FirstLayer{
    private SurfaceView mParent;
    private String buildingName;
    private String welcomeMessage = "";
    private String interactionMessage = "";
    private String endMessage = "";
    private int strCounter = 0;
    private int timeCounter = 0;
    private Paint paint;
    private String line1="";

    private int scrollX = 0;
    private int scrollY = 0;

    public BuildingInsideFirstLayer(SurfaceView view, String buildingName){
        this.buildingName = buildingName;
        mParent = view;


        for(Sprite sprite : Utils.allSprites) {
            if(sprite.getLocation().contains("Inside")) {
                sprite.setScrollY(0);
                sprite.setScrollX(0);
            }
        }

        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(35);
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
            if(sprite.getLocation().contains("Inside")) {
                if((buildingName.equals("pokecenter") && sprite.getName().equals("nurse")) || (buildingName.equals("pokemarkt") && sprite.getName().equals("seller")))
                    sprite.draw(c);
            }
        }

        if(endMessage.equals("") && welcomeMessage.equals(""))
            Utils.interactionMessageDisplayed = false;

        if(!welcomeMessage.equals(""))
            drawMessage(welcomeMessage, c, 0);

        if(!endMessage.equals(""))
            drawMessage(endMessage,c,1);

    }

    private void drawMessage(String message,Canvas c, int messageId){
        if(message.charAt(strCounter)==';')
            line1 = message.substring(0,strCounter);


        if(line1.equals(""))
            c.drawText(message, 0, strCounter, 50, 840, paint);
        else{
            c.drawText(line1, 0, line1.length(), 50, 840, paint);
            if(message.charAt(strCounter)!=';')
                c.drawText(message, line1.length()+1, strCounter, 50, 875, paint);
        }


        if (strCounter < message.length()-1) {
            if(timeCounter%3==0)
                strCounter++;
        }
        else if(timeCounter==strCounter*3){  // only do this once, after the message is completely displayed
            if(messageId == 0)
                interactionListener.welcomeScreenLoaded(buildingName);
            else if(messageId==1) {
                endMessage = "";
                interactionListener.interactionFinished();
            }
        }


        timeCounter++;
    }

    @Override
    public int getScrollX() {
        return 0;
    }

    @Override
    public void setScrollX(int scrollX) {
        this.scrollX = scrollX;
        if(Utils.allSprites!= null){
            for(int i=0;i<Utils.allSprites.size();i++){
                Utils.allSprites.get(i).setScrollX(Utils.allSprites.get(i).getScrollX() + scrollX);
            }
        }
    }

    @Override
    public int getScrollY() {
        return 0;
    }

    @Override
    public void setScrollY(int scrollY) {
        this.scrollY = scrollY;
        if(Utils.allSprites!= null){
            for(int i=0;i<Utils.allSprites.size();i++){
                Utils.allSprites.get(i).setScrollY(Utils.allSprites.get(i).getScrollY() + scrollY);
            }
        }
    }



    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public void setWelcomeMessage(String welcomeMessage) {
        line1 = "";
        timeCounter = 0;
        strCounter = 0;
        Utils.interactionMessageDisplayed = true;
        this.welcomeMessage = welcomeMessage;
    }

    public void pokecenterButtonClicked(String buttonName){
        line1 = "";
        welcomeMessage = "";
        endMessage = "";
        timeCounter = 0;
        strCounter = 0;
        if(buttonName.equals("yes")){
            endMessage = "We've restored your pokemon to full health.;";
        }
        endMessage += "We hope to see you again  ";
    }

    public void pokemarktButtonClicked(String buttonName){
        line1 = "";
        welcomeMessage = "";
        interactionMessage = "";
        timeCounter = 0;
        strCounter = 0;
        interactionListener.interactionFinished();
    }




}
