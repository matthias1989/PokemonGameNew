package info.androidhive.gametest.fight;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import info.androidhive.gametest.R;
import info.androidhive.gametest.abstractClasses.Renderable;

import static info.androidhive.gametest.R.drawable.battle_bg1;
import static info.androidhive.gametest.R.drawable.battle_bg1_part1;
import static info.androidhive.gametest.R.drawable.log_screen;

/**
 * Created by matthias on 3/23/2016.
 */
public class FightBackground extends Renderable {
    private int counter;
    private Bitmap background;
    private Bitmap logScreen;
    private SurfaceView mView;
    private Paint paint;

    public FightBackground(SurfaceView view,SurfaceHolder holder){
        mView = view;
        int counter=0;

        background = BitmapFactory.decodeResource(view.getResources(), battle_bg1_part1);
        logScreen = BitmapFactory.decodeResource(view.getResources(), log_screen);

        //Log.d("BACKGROUND",background.getWidth()+","+background.getHeight());
        background = Bitmap.createScaledBitmap(background, mView.getWidth(), 640, false);
        logScreen = Bitmap.createScaledBitmap(logScreen, 1000, 145, false);
        paint = new Paint();
        paint.setColor(Color.WHITE);
    }

    @Override
    public void playfield(int width, int height) {

    }

    @Override
    public void update(RectF dirty, double timeDelta) {

    }

    @Override
    public void draw(Canvas c) {
        //if(counter==0) {
            c.drawBitmap(background, 0, 0, null);
            c.drawBitmap(logScreen, 0, 640, null);
         //   counter++;
        //}

        //c.drawRect(0, 640, 1000, 785, paint);
    }
}
