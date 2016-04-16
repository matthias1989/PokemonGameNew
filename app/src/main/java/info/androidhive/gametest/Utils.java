package info.androidhive.gametest;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.androidhive.gametest.fight.FightActivity;
import info.androidhive.gametest.items.CustomItem;
import info.androidhive.gametest.items.ItemDataSource;
import info.androidhive.gametest.items.MyItems;
import info.androidhive.gametest.items.PokemarktItemList;
import info.androidhive.gametest.pokemons.MyPokemons;
import info.androidhive.gametest.pokemons.PokemonDataSource;
import info.androidhive.gametest.pokemons.PokemonSprite;
import info.androidhive.gametest.sprites.MyAdapter;
import info.androidhive.gametest.sprites.Sprite;
import info.androidhive.gametest.sprites.TrainerSprite;

/**
 * Created by matthias on 3/24/2016.
 */
public class Utils {

    public static Bitmap enemyPokemonBm;
    public static Bitmap enemyTrainerFrontBm;
    public static Bitmap myPokemonBm;
    public static Bitmap enemyPlatform;
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
    public static Bitmap pokeball11;
    public static Bitmap pokeball12;
    public static Bitmap pokeball13;
    public static Bitmap pokeball14;
    public static Bitmap pokeball15;
    public static Bitmap pokeball16;
    public static Bitmap pokeball17;
    public static Bitmap pokeball18;
    public static Bitmap pokeball19;
    public static Bitmap pokeball20;
    public static Bitmap pokeball21;

    public static TrainerSprite mySprite;

    public static Bitmap wallTile;
    public static Bitmap floorTile;
    public static Bitmap grassA;
    public static Bitmap grassB;
    public static Bitmap padTile;
    public static Bitmap exclamationMark;

    public static PokemonDataSource ds;
    public static ItemDataSource itemDs;
    public static PokemarktItemList pokemarktItemList = new PokemarktItemList(itemDs);

    public static List<Sprite> allSprites;

    //public static MyPokemons myPokemons = new MyPokemons();
    public static MyItems myItems = new MyItems();
    public static int myMoney;

    public static String currentCity;
    public static String currentEnvironment;
    public static TrainerSprite currentTrainer;
    public static PokemonSprite currentWildPokemon;

    public static AssetManager assetManager;
    public static Map<String,Integer> scrollCoords;

    public static boolean pokemonFound = false;
    public static boolean trainerFoundMe = false;
    public static boolean searched=false;

    public static boolean interactionMessageDisplayed = false;
    public static boolean menuOpened = false;

    public static final int steps=64;
    public static final int tileSize = 64;

    public static int scrollX=0;
    public static int scrollY=0;

    public static ListView menuList;
    public static PokemonSprite selectedPokemon=null;

    //public static Button btnCancel;

    public static void setupGame(Context context){
        Utils.currentCity = "Sandgem_Town";
        Utils.myMoney = 3500;
        Utils.mySprite = new TrainerSprite(0,Utils.tileSize*8,Utils.tileSize*8,tileSize,(int) (tileSize*1.5),"me","frontstanding","Sandgem_Town", context);
        Utils.mySprite.setBitmap("me", "frontstanding");

        //TrainerSprite trainerSprite = new TrainerSprite(id,x,y,width,height,name,status,location, context);


        Utils.assetManager = context.getAssets();




        MyPokemons myPokemons = new MyPokemons();
        PokemonSprite myPokemon1 = new PokemonSprite("charmander",Utils.ds);       // pikachu lukt wel!!!
        myPokemon1.setCurrentExperience(2500);
        myPokemon1.setCurrentHP(myPokemon1.getStats().getHp());
        myPokemons.addPokemon(myPokemon1);

        PokemonSprite myPokemon2 = new PokemonSprite("pikachu",Utils.ds);       // pikachu lukt wel!!!
        myPokemon2.setCurrentExperience(40500);
        myPokemon2.setCurrentHP(myPokemon2.getStats().getHp());
        myPokemons.addPokemon(myPokemon2);

        PokemonSprite myPokemon3 = new PokemonSprite("sandslash",Utils.ds);       // pikachu lukt wel!!!
        myPokemon3.setCurrentExperience(3000);
        myPokemon3.setCurrentHP(myPokemon3.getStats().getHp());
        myPokemons.addPokemon(myPokemon3);

        PokemonSprite myPokemon4 = new PokemonSprite("squirtle",Utils.ds);       // pikachu lukt wel!!!
        myPokemon4.setCurrentExperience(800);
        myPokemon4.setCurrentHP(myPokemon4.getStats().getHp());
        myPokemons.addPokemon(myPokemon4);

        PokemonSprite myPokemon5 = new PokemonSprite("snorlax",Utils.ds);       // pikachu lukt wel!!!
        myPokemon5.setCurrentExperience(3000);
        myPokemon5.setCurrentHP(myPokemon5.getStats().getHp());
        myPokemons.addPokemon(myPokemon5);

        Utils.mySprite.setMyPokemons(myPokemons);
    }




    public static Button pokemonMenu(final RelativeLayout container, final Context context){
        final MyPokemons myPokemons = Utils.mySprite.getMyPokemons();
        double x= 1;
        if( context instanceof MainActivity){

            x = 1.3;
            Log.d("XXX",x+"");
        }


        container.setBackgroundResource(R.drawable.empty_pokemon_selection);

        container.removeAllViewsInLayout();

        for(int i =0;i<myPokemons.getSize();i++){
            if(i<6) {
                final RelativeLayout pokemonSlot = new RelativeLayout(context);
                RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp1.width = 490;
                lp1.height = (int) (170*x);

                final PokemonSprite currentPokemon = myPokemons.getMyPokemonByOrderNr(i);
                int extraTopMargin = 0;
                int extraLeftMargin = 0;
                switch (i) {
                    case 0:
                        pokemonSlot.setBackgroundResource(R.drawable.unselected_first_pokemon);
                        lp1.height = (int) (170*x);
                        lp1.width = 495;
                        lp1.leftMargin = 0;
                        lp1.topMargin = (int) (15*x);
                        break;
                    case 1:
                        pokemonSlot.setBackgroundResource(R.drawable.unselected_pokemon);
                        lp1.leftMargin = 505;
                        lp1.topMargin = (int) (40*x);
                        break;
                    case 2:
                        pokemonSlot.setBackgroundResource(R.drawable.unselected_pokemon);
                        lp1.leftMargin = 0;
                        lp1.width = 495;
                        lp1.topMargin = (int) (190*x);
                        break;
                    case 3:
                        pokemonSlot.setBackgroundResource(R.drawable.unselected_pokemon);
                        lp1.leftMargin = 505;
                        lp1.topMargin = (int) (220*x);
                        break;
                    case 4:
                        pokemonSlot.setBackgroundResource(R.drawable.unselected_pokemon);
                        lp1.leftMargin = 0;
                        lp1.topMargin = (int) (370*x);
                        lp1.width = 495;
                        break;
                    case 5:
                        pokemonSlot.setBackgroundResource(R.drawable.unselected_pokemon);
                        lp1.leftMargin = 505;
                        lp1.topMargin = (int) (400*x);
                        break;

                }
                RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp2.leftMargin = 190 + extraLeftMargin;
                lp2.topMargin = (int) (20*x) + extraTopMargin;
                TextView txtName = new TextView(context);
                String upperString = currentPokemon.getName().substring(0, 1).toUpperCase() + currentPokemon.getName().substring(1);
                txtName.setText(upperString);
                txtName.setTextColor(Color.WHITE);
                pokemonSlot.addView(txtName, lp2);

                RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp3.leftMargin = 90 + extraLeftMargin;
                lp3.topMargin = (int) (105*x) + extraTopMargin +5;
                TextView txtLevel = new TextView(context);
                txtLevel.setText(currentPokemon.getLevel() + "");
                txtLevel.setTextColor(Color.WHITE);
                pokemonSlot.addView(txtLevel, lp3);

                // Current and max HP
                int currentLeftMargin = 0;
                RelativeLayout.LayoutParams lp4 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextView txtCurrentHP = new TextView(context);
                txtCurrentHP.setText(currentPokemon.getCurrentHP() + " / " + currentPokemon.getStats().getHp());
                if ((currentPokemon.getCurrentHP() + "").length() == 3)
                    currentLeftMargin += extraLeftMargin - 40;
                if ((currentPokemon.getStats().getHp() + "").length() == 3)
                    currentLeftMargin += extraLeftMargin - 40;
                lp4.leftMargin = 285 + extraLeftMargin;
                lp4.topMargin = (int) (110*x) + extraTopMargin;
                txtCurrentHP.setTextColor(Color.WHITE);
                pokemonSlot.addView(txtCurrentHP, lp4);

                RelativeLayout.LayoutParams lp5 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp5.leftMargin = 245 + extraLeftMargin;
                lp5.topMargin = (int) (85*x) + extraTopMargin;
                lp5.width = 192;
                lp5.height = (int) (18*x);
                ProgressBar hpBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
                hpBar.setMax(currentPokemon.getStats().getHp());

                float progressPerc = currentPokemon.getCurrentHP() * 100 / hpBar.getMax() * 1.0f;
                if (progressPerc > 70)
                    hpBar.setProgressDrawable(context.getResources().getDrawable(R.drawable.progressbar_high_hp));
                else if (progressPerc > 40)
                    hpBar.setProgressDrawable(context.getResources().getDrawable(R.drawable.progressbar_medium_hp));
                else
                    hpBar.setProgressDrawable(context.getResources().getDrawable(R.drawable.progressbar_low_hp));
                hpBar.setProgress(currentPokemon.getCurrentHP());

                pokemonSlot.addView(hpBar, lp5);

                final int counter = i;

                pokemonSlot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Log.d("CURRENT",selectedPokemon.getName());
                        if(selectedPokemon==null)
                            popChoiceMenu(currentPokemon,context,container,counter,pokemonSlot);
                        else {
                            Utils.mySprite.getMyPokemons().switchPokemon(currentPokemon, selectedPokemon);
                            selectedPokemon = null;
                            pokemonMenu(container, context); // refresh list
                        }
                    }
                });

                container.addView(pokemonSlot, lp1);
            }

        }

        RelativeLayout.LayoutParams lp6 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp6.leftMargin=780;
        lp6.topMargin = (int) (590*x);
        lp6.width = 220;
        lp6.height = (int) (110*x);
        final Button btnCancel = new Button(context);
        btnCancel.setBackgroundResource(R.drawable.pokemon_select_cancel);

        if(context instanceof MainActivity) {
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("CANCEL", "Cancel");
                    container.removeView(btnCancel);
                    MainActivity.cancelPokemonMenu();

                }
            });
        }


        container.addView(btnCancel, lp6);
        return btnCancel;
    }

    private static void popChoiceMenu(final PokemonSprite currentPokemon, final Context context, final RelativeLayout container, final int counter, final RelativeLayout pokemonSlot){

        // popup, als daar op switch wordt geklikt, dan pas gebeurt dit
        menuOpened = true;

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        menuList = new ListView(context);
        menuList.setBackgroundColor(Color.WHITE);
        String[] values = new String[] { "STATS", "SWITCH", "CANCEL"};

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {list.add(values[i]);}
        final MyAdapter myAdapter = new MyAdapter(context, android.R.layout.simple_list_item_1, list);
        menuList.setAdapter(myAdapter);

        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                switch (item) {
                    case "SWITCH":
                        if (context instanceof FightActivity)
                            FightActivity.switchPokemon2(currentPokemon, container);
                        else {
                            selectPokemon(counter, pokemonSlot, currentPokemon);
                            container.removeView(menuList);
                        }
                        break;
                    case "CANCEL":
                        menuOpened = false;
                        pokemonMenu(container, context);
                        break;
                }
                //Log.d("ITEM",item+ " selected");
            }
        });
        container.addView(menuList, lp);


    }

    private static void selectPokemon(int counter,RelativeLayout pokemonSlot, PokemonSprite currentPokemon){
        if (counter == 0)
            pokemonSlot.setBackgroundResource(R.drawable.selected_first_pokemon);
        else pokemonSlot.setBackgroundResource(R.drawable.selected_pokemon);
        selectedPokemon = currentPokemon;
        menuOpened = false;

    }


}
