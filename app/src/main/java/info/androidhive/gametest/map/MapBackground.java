package info.androidhive.gametest.map;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.SurfaceView;

import java.io.IOException;
import java.io.InputStream;

import info.androidhive.gametest.GameSurfaceView;
import info.androidhive.gametest.Utils;
import info.androidhive.gametest.abstractClasses.Background;
import info.androidhive.gametest.MainActivity;
import info.androidhive.gametest.R;

import static info.androidhive.gametest.R.drawable.log_screen;

/**
 * Created by matthias on 3/12/2016.
 */
public class MapBackground extends Background
{

    private int scrollX = Utils.scrollX;
    private int scrollY = Utils.scrollY;
    private SurfaceView mParent;

    private Bitmap logScreen;


    private int startPosX = 0;
    private int startPosY = 0;

    private int scaleX;
    private int scaleY;


    private char[][] matrix = new char[1000][1000];


    public MapBackground(SurfaceView view)
    {
        mParent = view;
        Utils.wallTile = BitmapFactory.decodeResource(view.getResources(), R.drawable.wall_tile);



        Utils.floorTile = BitmapFactory.decodeResource(view.getResources(), R.drawable.floor_tile);
        Utils.grassA = BitmapFactory.decodeResource(view.getResources(), R.drawable.grass_a);
        Utils.grassA = Bitmap.createScaledBitmap(Utils.grassA, Utils.tileSize, Utils.tileSize, false);

        Utils.grassB = BitmapFactory.decodeResource(view.getResources(), R.drawable.grass_b);
        Utils.grassB = Bitmap.createScaledBitmap(Utils.grassB, Utils.tileSize, Utils.tileSize, false);


        Utils.exclamationMark = BitmapFactory.decodeResource(view.getResources(), R.drawable.exclamation_mark);
        Utils.exclamationMark = Bitmap.createScaledBitmap(Utils.exclamationMark, Utils.tileSize, Utils.tileSize, false);


        logScreen = BitmapFactory.decodeResource(view.getResources(), log_screen);
        logScreen = Bitmap.createScaledBitmap(logScreen, 1000, 145, false);

        getPlayField();
//        setScrollX(scrollX);
//        if(scrollY != 0) setScrollY(scrollY);





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
    public int getStartPosX() {
        return 0;
    }

    @Override
    public void setStartPosX(int startPosX) {

    }

    @Override
    public int getStartPosY() {
        return 0;
    }

    @Override
    public void setStartPosY(int startPosY) {

    }

    public void getPlayField(){
        AssetManager assets = Utils.assetManager;
        try {
            char c;
            int i=0,j=0;
            InputStream is = assets.open("map.txt");
            if(is != null) {
                while (is.available() > 0) {
                    c = (char) is.read();
                    if (c == '-' || c == 'w' || c == 'f' || c == 'g' || c == 'G') {
                        matrix[i][j] = c;
                        j++;
                    }
                    else if (c == '\n') {
                        i++;
                        j = 0;
                    }
                }
                is.close();
            }
        } catch(IOException e){
            e.printStackTrace();
        }
        //Log.d("TAG",matrix[2][2]+"");
    }
    public char getBackgroundTile(int row,int column){
        return matrix[row][column];
    }
    @Override
    public void playfield(int width, int height)
    {
        setmFieldWidth(width);
        setmFieldHeight(height);
    }

    @Override
    public void update(RectF dirty, double timeDelta)
    {

        //Log.d("TAG","test");
    }

    @Override
    public void draw(Canvas c)
    {
        //c.drawPaint(mBg);
        int x = startPosX;
        int y = startPosY;
        for (int i = 0 ; i < 1000; i++) {
            for(int j=0;j<1000;j++) {
                char tile = matrix[i][j];
                switch (tile) {
                    case 'w':
                        c.drawBitmap(Utils.wallTile, x - scrollX, y - scrollY, null);
                        x += Utils.tileSize;
                        break;
                    case 'f':
                        c.drawBitmap(Utils.floorTile, x - scrollX, y - scrollY, null);
                        x += Utils.tileSize;
                        break;
                    case 'g':
                        c.drawBitmap(Utils.grassA,x - scrollX, y - scrollY, null);
                        x += Utils.tileSize;
                        break;
                    case 'G':
                        c.drawBitmap(Utils.grassB,x - scrollX, y - scrollY, null);
                        x += Utils.tileSize;
                        break;
                    case '-':
                        x += Utils.tileSize;
                        break;
                }
            }
            y +=  Utils.tileSize;
            x = startPosX;

        }
        if(Utils.interactionMessageDisplayed)
            c.drawBitmap(logScreen, 0, 790, null);  // only draw  this when there is an interaction going on

    }





}