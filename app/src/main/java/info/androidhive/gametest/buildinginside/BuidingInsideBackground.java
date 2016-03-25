package info.androidhive.gametest.buildinginside;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.SurfaceView;

import info.androidhive.gametest.GameSurfaceView;
import info.androidhive.gametest.abstractClasses.Background;
import info.androidhive.gametest.R;

/**
 * Created by matthias on 3/20/2016.
 */
public class BuidingInsideBackground extends Background {
    private Bitmap buildingBm;
    private int startPosX = 0;
    private int startPosY = -tileSize*3/2;
    private int scrollX = 0;
    private int scrollY = 0;

    private SurfaceView mParent;






    public BuidingInsideBackground(SurfaceView view,String buildingName){
        if(buildingName.equals("pokecenter"))
            buildingBm = BitmapFactory.decodeResource(view.getResources(), R.drawable.pokemon_center_inside);
        else
            buildingBm = BitmapFactory.decodeResource(view.getResources(), R.drawable.pokemarkt_inside);

        setScrollX(scrollX);
        if(scrollY != 0) setScrollY(scrollY);

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
        buildingBm = Bitmap.createScaledBitmap(buildingBm, width, height*2/3, false);
        setmFieldWidth(width);
        setmFieldHeight(height*2/3);
    }

    @Override
    public void update(RectF dirty, double timeDelta) {

    }

    @Override
    public void draw(Canvas c) {
        c.drawBitmap(buildingBm, startPosX-scrollX, startPosY-scrollY, null);
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
