package info.androidhive.gametest.buildinginside;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.gametest.GameSurfaceView;
import info.androidhive.gametest.abstractClasses.FirstLayer;
import info.androidhive.gametest.R;
import info.androidhive.gametest.sprites.Sprite;

/**
 * Created by matthias on 3/20/2016.
 */
public class BuildingInsideFirstLayer extends FirstLayer{
    //private Sprite helpingPerson;
    private SurfaceView mParent;
    private List<Sprite> sprites = new ArrayList<>();

    private int scrollX = 0;
    private int scrollY = 0;

    public BuildingInsideFirstLayer(SurfaceView view, String buildingName){
        mParent = view;
        if(buildingName.equals("pokecenter")) {
            addSprite(createSprite(R.drawable.nurse_joy_front_standing,9*tileSize-tileSize/2,tileSize));
        }
        else if(buildingName.equals("pokemarkt")){
            Log.d("HELPER","helper");
        }
        //sprites.put("nurseJoy",nurseJoy);
        if(sprites!= null){
            for(Sprite sprite : sprites){
                sprite.update();
            }
        }

    }

    @Override
    public void playfield(int width, int height) {

    }

    @Override
    public void update(RectF dirty, double timeDelta) {

    }

    @Override
    public void draw(Canvas c) {
        if(sprites!= null){
            for(Sprite sprite : sprites){
                sprite.draw(c);
            }
        }
    }

    @Override
    public int getScrollX() {
        return 0;
    }

    @Override
    public void setScrollX(int scrollX) {
        this.scrollX = scrollX;
        if(sprites!= null){
            for(Sprite sprite : sprites){
                sprite.setScrollX(sprites.get(0).getScrollX() + scrollX);
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
        if(sprites!= null){
            for(Sprite sprite : sprites){
                sprite.setScrollY(sprites.get(0).getScrollY() + scrollY);
            }
        }
    }

    public Sprite createSprite(int id,int x, int y){
        Bitmap a = BitmapFactory.decodeResource(mParent.getResources(), id);
        a = Bitmap.createScaledBitmap(a, tileSize, 3/2*tileSize, false);
        return new Sprite(a, x,y,32,48);
    }

    public List<Sprite> getSprites() {
        return sprites;
    }

    @Override
    public void addSprite(Sprite sprite) {
        sprites.add(sprite);
    }

    public void setSprites(List<Sprite> sprites) {
        this.sprites = sprites;
    }
}
