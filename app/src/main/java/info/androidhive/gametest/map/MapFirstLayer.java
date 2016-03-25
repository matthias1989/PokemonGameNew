package info.androidhive.gametest.map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.gametest.GameSurfaceView;
import info.androidhive.gametest.Utils;
import info.androidhive.gametest.abstractClasses.FirstLayer;
import info.androidhive.gametest.MainActivity;
import info.androidhive.gametest.sprites.PokecenterSprite;
import info.androidhive.gametest.sprites.PokemarktSprite;
import info.androidhive.gametest.R;
import info.androidhive.gametest.sprites.Sprite;

/**
 * Created by matthias on 3/19/2016.
 */
public class MapFirstLayer extends FirstLayer {
    private Rect rectangle;
    private Paint paint;
    private Bitmap pokemonCenter;

    private List<Sprite> sprites = new ArrayList<>();
    private SurfaceView mParent;
    private int scrollX = Utils.scrollCoords.get("scrollX");
    private int scrollY = Utils.scrollCoords.get("scrollY");
    private int pokecenterPosX;
    private int pokecenterPosY;
    private int width;
    private int height;

    public MapFirstLayer(SurfaceView view){
        mParent = view;
        paint = new Paint();
        paint.setColor(Color.GRAY);




        addSprite(createSprite(R.drawable.pokecenter, 9 * tileSize, 9 * tileSize, 5 * tileSize, 4 * tileSize,"pokecenter"));
        addSprite(createSprite(R.drawable.pokecenter,20*tileSize,5*tileSize,5*tileSize,4*tileSize,"pokecenter"));
        addSprite(createSprite(R.drawable.pokecenter,30*tileSize,8*tileSize,5*tileSize,4*tileSize,"pokecenter"));
        addSprite(createSprite(R.drawable.pokemarkt,8*tileSize,4*tileSize,4*tileSize,4*tileSize,"pokemarkt"));

        for(Sprite sprite : sprites){
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
        for(Sprite sprite : sprites)
        {
            sprite.draw(c);
        }

        //c.drawBitmap(pokemonCenter, pokecenterPosX- scrollX, pokecenterPosY- scrollY, null);
        //sprites.get("pokecenter").draw(c);
    }

    public int getScrollX() {
        return scrollX;
    }

    public void setScrollX(int scrollX) {
        this.scrollX = scrollX;
        for(Sprite sprite : sprites)
        {
            sprite.setScrollX(scrollX);
        }
        //Log.d("scrollX",scrollX+"<->"+sprites.get("pokecenter").getScrollX());
    }

    public int getScrollY() {
        return scrollY;

    }

    public void setScrollY(int scrollY) {
        this.scrollY = scrollY;
        for(Sprite sprite : sprites){
            sprite.setScrollY(scrollY);
        }
        //sprites.get("pokecenter").setScrollY(scrollY);
        //Log.d("scrollY",scrollY+"<->"+sprites.get("pokecenter").getScrollY());
    }

    @Override
    public List<Sprite> getSprites() {
        return sprites;
    }


    public Sprite createSprite(int id, int x, int y, int width, int height,String building){
        Sprite sprite = null;
        Bitmap a = BitmapFactory.decodeResource(mParent.getResources(), id);


        //Log.d("PCcoords",x+","+y);
        a = Bitmap.createScaledBitmap(a, width, height, false);

        if(building.equals("pokecenter"))
            sprite = new PokecenterSprite(a, x,y,width,height);
        else
            if(building.equals("pokemarkt"))
                sprite = new PokemarktSprite(a, x,y,width,height);
            else
                sprite = new Sprite(a, x,y,width,height);

        sprite.setScrollX(scrollX);
        sprite.setScrollY(scrollY);
        return sprite;
    }

    @Override
    public void addSprite(Sprite sprite) {
        sprites.add(sprite);
    }

    @Override
    public void setSprites(List<Sprite> sprites) {
        this.sprites = sprites;
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
}
