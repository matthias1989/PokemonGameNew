package info.androidhive.gametest.buildinginside;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.SurfaceView;

import info.androidhive.gametest.GameSurfaceView;
import info.androidhive.gametest.Utils;
import info.androidhive.gametest.abstractClasses.Background;
import info.androidhive.gametest.R;

import static info.androidhive.gametest.R.drawable.log_screen;

/**
 * Created by matthias on 3/20/2016.
 */
public class BuildingInsideBackground extends Background {
    private Bitmap buildingBm;
    private Bitmap logScreen;

    private int startPosX = 0;
    private int startPosY = 0;
    private int scrollX = 0;
    private int scrollY = 0;

    private String buildingName;


    private SurfaceView mParent;







    public BuildingInsideBackground(SurfaceView view, String buildingName){
        this.buildingName = buildingName;
        if(buildingName.equals("pokecenter"))
            buildingBm = BitmapFactory.decodeResource(view.getResources(), R.drawable.pokemon_center_inside2_part1);
        else
            buildingBm = BitmapFactory.decodeResource(view.getResources(), R.drawable.pokemarkt_inside);

        setScrollX(scrollX);
        if(scrollY != 0) setScrollY(scrollY);



        logScreen = BitmapFactory.decodeResource(view.getResources(), log_screen);
        logScreen = Bitmap.createScaledBitmap(logScreen, 1000, 145, false);

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

    @Override
    public void getPlayField() {

    }

    @Override
    public void playfield(int width, int height) {
        if(buildingName.equals("pokecenter")){
            buildingBm = Bitmap.createScaledBitmap(buildingBm, width, height*2/3, false);
            setmFieldWidth(width-Utils.tileSize);
            setmFieldHeight(height*2/3-Utils.tileSize);
        }
        else
        {//buildingBm = Bitmap.createScaledBitmap(buildingBm, width, height*2/3, false);
            buildingBm = Bitmap.createScaledBitmap(buildingBm, width*2/3, height*2/3, false);
            setmFieldWidth(width*2/3);
            setmFieldHeight(height*2/3);
            setStartPosX(5*Utils.tileSize);
            setStartPosY(-1*Utils.tileSize);
        }



    }

    @Override
    public void update(RectF dirty, double timeDelta) {

    }

    @Override
    public void draw(Canvas c) {
        c.drawBitmap(buildingBm, startPosX-scrollX, startPosY-scrollY, null);
        if(Utils.interactionMessageDisplayed)
            c.drawBitmap(logScreen, 0, 790, null);  // only draw  this when there is an interaction going on

    }

    public int getStartPosX() {
        return startPosX;
    }

    public void setStartPosX(int startPosX) {
        this.startPosX = startPosX;
    }

    public int getStartPosY() {
        return startPosY;
    }

    public void setStartPosY(int startPosY) {
        this.startPosY = startPosY;
    }


}
