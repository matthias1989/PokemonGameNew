package info.androidhive.gametest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.androidhive.gametest.abstractClasses.Foreground;
import info.androidhive.gametest.abstractClasses.Renderable;
import info.androidhive.gametest.fight.FightActivity;
import info.androidhive.gametest.items.CustomItem;
import info.androidhive.gametest.items.Item;
import info.androidhive.gametest.items.MyItems;
import info.androidhive.gametest.map.MapForeground;
import info.androidhive.gametest.map.MapRenderThread;
import info.androidhive.gametest.pokemons.Move;
import info.androidhive.gametest.pokemons.MyPokemons;
import info.androidhive.gametest.pokemons.PokemonSprite;
import info.androidhive.gametest.sprites.MyAdapter;


public class MainActivity extends Activity
{

    private RelativeLayout interactionContainer;
    private ListView menuList;
    private LinearLayout menuContainer;
    private RelativeLayout surfaceViewContainer;
    private GameSurfaceView view;

    private MapForeground foreground;


    private boolean menuOpened = false;
    private boolean interactionStarted = false;



    private Button btnUp;
    private Button btnLeft;
    private Button btnRight;
    private Button btnDown;
    private Button btnY;
    private Button btnX;
    private Button btnA;
    private Button btnB;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        DatabaseFileHandler.readData(this);
        DatabaseFileHandler.readData2(this);
        DatabaseFileHandler.readItemData(this);
        //DatabaseFileHandler.readTest(this);

        PokemonSprite myPokemon = new PokemonSprite("charmander",DatabaseFileHandler.ds);       // pikachu lukt wel!!!
        myPokemon.setCurrentExperience(400);
        myPokemon.setCurrentHP(myPokemon.getStats().getHp());
        Utils.myPokemons.addPokemon(myPokemon);

        PokemonSprite myPokemon2 = new PokemonSprite("sandslash",DatabaseFileHandler.ds);       // pikachu lukt wel!!!
        myPokemon2.setCurrentExperience(300);
        myPokemon2.setCurrentHP(myPokemon2.getStats().getHp());
        Utils.myPokemons.addPokemon(myPokemon2);

        PokemonSprite myPokemon3 = new PokemonSprite("pikachu",DatabaseFileHandler.ds);       // pikachu lukt wel!!!
        myPokemon3.setCurrentExperience(40000);
        myPokemon3.setCurrentHP(myPokemon3.getStats().getHp());
        Utils.myPokemons.addPokemon(myPokemon3);

        PokemonSprite myPokemon4 = new PokemonSprite("squirtle",DatabaseFileHandler.ds);       // pikachu lukt wel!!!
        myPokemon4.setCurrentExperience(800);
        myPokemon4.setCurrentHP(myPokemon4.getStats().getHp());
        Utils.myPokemons.addPokemon(myPokemon4);

        PokemonSprite myPokemon5 = new PokemonSprite("snorlax",DatabaseFileHandler.ds);       // pikachu lukt wel!!!
        myPokemon5.setCurrentExperience(3000);
        myPokemon5.setCurrentHP(myPokemon5.getStats().getHp());
        Utils.myPokemons.addPokemon(myPokemon5);

//        PokemonSprite myPokemon6 = new PokemonSprite("mew",DatabaseFileHandler.ds);       // pikachu lukt wel!!!
//        myPokemon6.setCurrentExperience(300000);
//        myPokemon6.setCurrentHP(myPokemon6.getStats().getHp());
//        Utils.myPokemons.addPokemon(myPokemon6);


        Utils.myItems = new MyItems();
        CustomItem item = new CustomItem("great-ball",DatabaseFileHandler.itemDs);
        //Log.d("POKEBALL","A "+item.getName()+" costs "+item.getCost() + " and is from category "+item.getCategoryName());
        Utils.myItems.addItem(item, 50);


        Utils.scrollCoords = new HashMap<>();
        Utils.scrollCoords.put("scrollX", 0);
        Utils.scrollCoords.put("scrollY", -Renderable.steps * 6);
        Utils.assetManager = getAssets();


        //Log.d("ITEM",DatabaseFileHandler.itemDs.getItemList().size()+"");


        setContentView(R.layout.activity_main);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 1000);
        surfaceViewContainer = (RelativeLayout) findViewById(R.id.main_layout);

        view = (GameSurfaceView) findViewById(R.id.view);
        //view = new GameSurfaceView(this);
        //surfaceViewContainer.addView(view,lp);

    }



    @Override
    protected void onStart() {
        super.onStart();



        String status = "frontStanding";
        if(!getValueForKey("scrollX").equals("VALUE NOT SAVED")){

            status = getValueForKey("status");

            saveKeyValueString("scrollXBG","VALUE NOT SAVED");
            saveKeyValueString("scrollYBG","VALUE NOT SAVED");
            saveKeyValueString("status", "VALUE NOT SAVED");

        }

        interactionContainer = (RelativeLayout) findViewById(R.id.interaction_container);
        menuContainer= (LinearLayout) findViewById(R.id.menu_container);

        btnUp = (Button) findViewById(R.id.up);
        btnUp.setOnTouchListener(new MyOnTouchListener("up"));
        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view.getmThread().getForeground() != null)
                    view.getmThread().getForeground().goUp();

                if (Utils.pokemonFound) {
                    startFightActivity();
                }

            }
        });

        btnDown = (Button) findViewById(R.id.down);
        btnDown.setOnTouchListener(new MyOnTouchListener("down"));
        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view.getmThread().getForeground() != null)
                    view.getmThread().getForeground().goDown();
                if (Utils.pokemonFound) {
                    startFightActivity();
                }

            }
        });

        btnLeft = (Button) findViewById(R.id.left);
        btnLeft.setOnTouchListener(new MyOnTouchListener("left"));
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view.getmThread().getForeground() != null)
                    view.getmThread().getForeground().goLeft();
                if (Utils.pokemonFound) {
                    startFightActivity();
                }
            }
        });

        btnRight = (Button) findViewById(R.id.right);
        btnRight.setOnTouchListener(new MyOnTouchListener("right"));
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view.getmThread().getForeground() != null)
                    view.getmThread().getForeground().goRight();
                if (Utils.pokemonFound) {
                    startFightActivity();
                }
            }
        });

        btnY = (Button) findViewById(R.id.y);
        btnY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String buildingName = view.getmThread().getForeground().checkInteractionPossible();
                if(interactionStarted) {
                    interactionContainer.removeAllViewsInLayout();
                    interactionStarted = false;
                }
                else {
                    if (!buildingName.equals(""))
                        doInteraction(buildingName);
                        interactionStarted = true;
                }
            }
        });

        btnX = (Button) findViewById(R.id.x);
        btnX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!menuOpened)
                    openMenu();
                else
                    closeMenu();
            }
        });


    }


    private class MyOnTouchListener implements View.OnTouchListener {

        private Handler mHandler;
        private String direction;

        public MyOnTouchListener(String direction) {
            this.direction = direction;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (mHandler != null) return true;
                    mHandler = new Handler();
                    mHandler.postDelayed(mAction, 200);
                    if(view.getmThread().getForeground() != null)
                        view.getmThread().getForeground().setActionStopped(false);
                    break;
                case MotionEvent.ACTION_UP:
                    if (mHandler == null) return true;
                    mHandler.removeCallbacks(mAction);
                    mHandler = null;
                    if(view.getmThread().getForeground()!= null)        // when entering the pokemon center, this action is still executed
                        view.getmThread().getForeground().setActionStopped(true);
                    break;
            }
            return false;
        }

        Runnable mAction = new Runnable() {
            @Override
            public void run() {
                //Log.d("pokemon_found2", MapForeground.pokemonFound + "");
                if (!Utils.pokemonFound) {

                    Foreground foreground = view.getmThread().getForeground();

                    switch (direction) {
                        case "up":
                            foreground.goUp();
                            break;
                        case "down":
                            foreground.goDown();
                            break;
                        case "left":
                            foreground.goLeft();
                            break;
                        case "right":
                            foreground.goRight();
                            break;
                    }
                    if (Utils.pokemonFound) {
                        startFightActivity();
                    }
                    mHandler.postDelayed(this, 200);
                }
            }
        };
    }

    public void startFightActivity(){
        // HashMap<String,PokemonSprite> fight
        // save data (scrollX and scrollY and sprite direction

        int scrollX = ((MapRenderThread) view.getmThread()).getBackground().getScrollX();
        int scrollY = ((MapRenderThread) view.getmThread()).getBackground().getScrollY();
        String status = ((MapRenderThread)view.getmThread()).getForeground().getSprite().getStatus();

        saveKeyValueString("status",status);

        Utils.scrollCoords.put("scrollX", scrollX);
        Utils.scrollCoords.put("scrollY",scrollY);
        Intent fightIntent = new Intent(this,FightActivity.class);

        startActivityForResult(fightIntent, 001);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == 001)
        {
            if(resultCode == RESULT_OK)
            {
                String value = data.getStringExtra("FIGHT");
                Log.d("FIGHT",value);
                if(value.contains("caught"))
                    Log.d("NEWPOKE",Utils.myPokemons.getMyPokemonByOrderNr(5).getName());
            }
            else if(resultCode == RESULT_CANCELED)
            {
                Log.d("FIGHT","Run away");
            }
            Utils.pokemonFound = false;
            MapForeground.pokemonSprite =null;
            Utils.searched = false;
            //view.openMapThread(view.getHolder());
        }
    }

    private void saveKeyValueString(String key, String value)
    {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    private String getValueForKey(String key)
    {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String value = sharedPref.getString(key, "VALUE NOT SAVED");
        return value;
    }


    private void doInteraction(String buildingName) {
        Log.d("INTERACTION", "interaction!!!");

        // interactioncontainer should be removed and instead a drawtext must come, the yes/no buttons (and the menu) can appear from here
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 150);
        final TextView interaction = new TextView(this);
        interaction.setGravity(Gravity.CENTER_HORIZONTAL);
        interaction.setTextColor(Color.BLACK);
        interaction.setBackgroundColor(Color.WHITE);
        interactionContainer.addView(interaction, lp);

        if (buildingName.equals("pokecenter")) {
            pokecenterInteraction(interaction);
        }
        else if(buildingName.equals("pokemarkt")){
            Log.d("MARKT","Buy balls and stuff!");
            pokemarktInteraction(interaction);
        }
    }

    private void pokecenterInteraction(TextView interaction){
        interaction.setText("Welcome to our PokéCenter\n Would you like me to heal your pokémon back to perfect health?");

        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 150);
        lp2.topMargin=130;
        lp2.leftMargin = 100;
        LinearLayout dialogLayout = new LinearLayout(this);
        dialogLayout.setOrientation(LinearLayout.HORIZONTAL);
        Button btnYes = new Button(this);
        btnYes.setText("Yes");
        btnYes.setBackgroundColor(Color.WHITE);
        dialogLayout.addView(btnYes);
        Button btnNo = new Button(this);
        btnNo.setText("No");
        btnNo.setBackgroundColor(Color.WHITE);
        dialogLayout.addView(btnNo);
        dialogLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        interactionContainer.addView(dialogLayout,lp2);


        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interactionContainer.removeAllViewsInLayout();
                interactionStarted = false;
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interactionContainer.removeAllViewsInLayout();
                interactionStarted = false;
                for (int i = 0; i < Utils.myPokemons.getSize(); i++) {
                    PokemonSprite currentPokemon = Utils.myPokemons.getMyPokemonByOrderNr(i);
                    currentPokemon.setCurrentHP(currentPokemon.getStats().getHp());
                    List<Move> moves = currentPokemon.getLearnedMoves();
                    for (Move move : moves) {
                        move.setCurrentPp(move.getPp());
                    }
                }
            }
        });
    }

    private void pokemarktInteraction(TextView interaction){
        interaction.setText("Welcome \n What do you need?");
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.topMargin=-100;
        lp.leftMargin=50;
        lp.width=100;



        // dont put this in the interactioncontainer
        menuList = new ListView(this);
        menuList.setBackgroundColor(Color.WHITE);
        String[] values = new String[] { "BUY", "SELL", "QUIT"};

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {list.add(values[i]);}
        final MyAdapter myAdapter = new MyAdapter(this, android.R.layout.simple_list_item_1, list);
        menuList.setAdapter(myAdapter);

        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                switch (item) {
                    case "BUY":
                        Log.d("SELL", "buy some stuff");
                        break;
                    case "SELL":
                        Log.d("SELL","sell some stuff");
                        break;
                    case "QUIT":
                        interactionContainer.removeAllViewsInLayout();
                        interactionStarted = false;
                        break;
                }
            }
        });
        interactionContainer.addView(menuList, lp);
    }

    private void openMenu(){
        menuOpened = true;

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        menuList = new ListView(this);
        menuList.setBackgroundColor(Color.WHITE);
        String[] values = new String[] { "POKéDEX", "POKéMON", "ME","BAG","SAVE","OPTIONS","QUIT"};

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {list.add(values[i]);}
        final MyAdapter myAdapter = new MyAdapter(this, android.R.layout.simple_list_item_1, list);
        menuList.setAdapter(myAdapter);

        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                switch(item){
                    case "POKéMON":
                        showMyPokemon();
                        break;
                    case "QUIT":
                        finish();
                        break;
                }
                //Log.d("ITEM",item+ " selected");
            }
        });
        menuContainer.addView(menuList,lp);
    }

    private void closeMenu(){
        menuOpened = false;
        menuContainer.removeAllViewsInLayout();
    }

    private void showMyPokemon(){
        menuContainer.removeAllViewsInLayout();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final ArrayList<String> list = new ArrayList<String>();
        for(int i =0;i<Utils.myPokemons.getSize();i++){
            PokemonSprite currentPokemon = Utils.myPokemons.getMyPokemonByOrderNr(i);
            list.add(currentPokemon.getName());
        }
        final MyAdapter myAdapter = new MyAdapter(this, android.R.layout.simple_list_item_1, list);
        menuList.setAdapter(myAdapter);
        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                Utils.myPokemons.setFirstPokemon(item);
                closeMenu();
                Log.d("ITEM",item+ " selected");
            }
        });
        menuContainer.addView(menuList,lp);
        Log.d("POKEMON","Pokémonnn");

    }




}