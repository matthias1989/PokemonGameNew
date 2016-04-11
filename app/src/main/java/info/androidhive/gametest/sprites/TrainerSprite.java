package info.androidhive.gametest.sprites;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.HashMap;

import info.androidhive.gametest.pokemons.MyPokemons;

/**
 * Created by matthias on 4/10/2016.
 */
public class TrainerSprite extends Sprite {
    private boolean doneFighting = false;
    private MyPokemons myPokemons;
    private HashMap<String,Bitmap> allBitmaps = new HashMap<>();
    private String status;

    public TrainerSprite(int id,int tx, int ty, int width, int height, String name, String status, String location, Context context) {
        super(id,tx, ty, width, height, name,location, context);
        setAllBitmaps();
        this.status = status;
    }

    public MyPokemons getMyPokemons() {
        return myPokemons;
    }

    public void setMyPokemons(MyPokemons myPokemons) {
        this.myPokemons = myPokemons;
    }

    public HashMap<String, Bitmap> getAllBitmaps() {
        return allBitmaps;
    }

    public void setAllBitmaps() {
        addBitmap("frontstanding");
        addBitmap("backstanding");
        addBitmap("leftstanding");
        addBitmap("rightstanding");
        addBitmap("frontwalking1");
        addBitmap("backwalking1");
        addBitmap("leftwalking1");
        addBitmap("rightwalking1");
        addBitmap("frontwalking2");
        addBitmap("backwalking2");
        addBitmap("leftwalking2");
        addBitmap("rightwalking2");
    }

    public boolean isDoneFighting() {
        return doneFighting;
    }

    public void setDoneFighting(boolean doneFighting) {
        this.doneFighting = doneFighting;
    }

    public void addBitmap(String name){
        String id = getType()+"_"+name;
        int resID = getContext().getResources().getIdentifier(id, "drawable", getContext().getPackageName());
        Bitmap a = BitmapFactory.decodeResource(getContext().getResources(), resID);
        a =Bitmap.createScaledBitmap(a, getWidth(), getHeight(), false);

        allBitmaps.put(name,a);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        setBitmap(getAllBitmaps().get(status));
    }
}
