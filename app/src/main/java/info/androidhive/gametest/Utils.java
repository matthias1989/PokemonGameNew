package info.androidhive.gametest;

import android.content.res.AssetManager;
import android.graphics.Bitmap;

import java.util.Map;

import info.androidhive.gametest.items.MyItems;
import info.androidhive.gametest.pokemons.MyPokemons;

/**
 * Created by matthias on 3/24/2016.
 */
public class Utils {

    public static Bitmap wildPokemonBm;
    public static Bitmap myPokemonBm;
    public static Bitmap wildPlatform;
    public static Bitmap myPlatform;
    public static Bitmap player1;
    public static Bitmap player2;
    public static Bitmap player3;
    public static Bitmap player4;
    public static Bitmap player5;
    public static Bitmap pokeball1;
    public static Bitmap pokeball2;
    public static Bitmap pokeball3;
    public static Bitmap pokeball4;
    public static Bitmap pokeball5;
    public static Bitmap pokeball6;
    public static Bitmap pokeball7;
    public static Bitmap pokeball8;
    public static Bitmap pokeball9;
    public static Bitmap pokeball10;

    public static Bitmap wallTile;
    public static Bitmap floorTile;
    public static Bitmap grassA;
    public static Bitmap grassB;

    public static MyPokemons myPokemons = new MyPokemons();
    public static MyItems myItems = new MyItems();
    public static AssetManager assetManager;
    public static Map<String,Integer> scrollCoords;

    public static boolean pokemonFound = false;
    public static boolean searched=false;



}
