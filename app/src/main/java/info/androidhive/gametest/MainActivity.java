package info.androidhive.gametest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import info.androidhive.gametest.abstractClasses.Foreground;
import info.androidhive.gametest.buildinginside.BuildingInsideFirstLayer;
import info.androidhive.gametest.fight.FightActivity;
import info.androidhive.gametest.items.CustomItem;
import info.androidhive.gametest.map.MapForeground;
import info.androidhive.gametest.pokemons.Move;
import info.androidhive.gametest.pokemons.PokemonSprite;
import info.androidhive.gametest.sprites.MyAdapter;
import info.androidhive.gametest.sprites.TrainerSprite;


public class MainActivity extends Activity implements InteractionListener
{
    private static RelativeLayout mainLayout;
    private RelativeLayout dialogLayout;
    private ListView menuList;
    private static RelativeLayout controllerContainer;
    private static RelativeLayout moveControls;
    private static RelativeLayout menuControls;
    private RelativeLayout menuContainer;
    private RelativeLayout surfaceViewContainer;
    private static GameSurfaceView view;

    private MapForeground foreground;


    private boolean menuOpened = false;
    private boolean interactionStarted = false;

    private static Context context;


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
        DatabaseFileHandler.readItemData2(this);



        setContentView(R.layout.activity_main);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        surfaceViewContainer = (RelativeLayout) findViewById(R.id.main_layout);

        mainLayout = (RelativeLayout) findViewById(R.id.main_layout);

        view = new GameSurfaceView(this);
        view.setInteractionListener(this);
        mainLayout.addView(view, lp);

        controllerContainer = (RelativeLayout) findViewById(R.id.controller_container);
        moveControls = (RelativeLayout) findViewById(R.id.move_controls);
        menuControls = (RelativeLayout) findViewById(R.id.menu_controls);

        context = this;

        Utils.scrollCoords = new HashMap<>();
        Utils.scrollCoords.put("scrollX", 0);
        Utils.scrollCoords.put("scrollY", -Utils.steps * 6);

        Utils.setupGame(this);
        DatabaseFileHandler.readSpriteInfo(this);
    }



    @Override
    protected void onStart() {
        super.onStart();


        Utils.currentEnvironment = "outside";

        if(!getValueForKey("scrollX").equals("VALUE NOT SAVED")){
            saveKeyValueString("scrollXBG","VALUE NOT SAVED");
            saveKeyValueString("scrollYBG","VALUE NOT SAVED");
            saveKeyValueString("status", "VALUE NOT SAVED");
        }
        setupControllers();
        menuContainer= (RelativeLayout) findViewById(R.id.menu_container);
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*int scrollX = view.getmThread().getBackground().getScrollX();
        int scrollY = view.getmThread().getBackground().getScrollY();
        Utils.scrollCoords = new HashMap<>();
        Utils.scrollCoords.put("scrollX", scrollX);
        Utils.scrollCoords.put("scrollY", scrollY);*/
    }

    private void setupControllers(){
        btnUp = (Button) findViewById(R.id.up);
        btnUp.setOnTouchListener(new MyOnTouchListener("up"));
        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view.getmThread().getForeground() != null && !Utils.trainerFoundMe)
                    view.getmThread().getForeground().goUp();

                if (Utils.pokemonFound) {
                    startFightActivity(false);
                }

            }
        });

        btnDown = (Button) findViewById(R.id.down);
        btnDown.setOnTouchListener(new MyOnTouchListener("down"));
        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view.getmThread().getForeground() != null && !Utils.trainerFoundMe)
                    view.getmThread().getForeground().goDown();
                if (Utils.pokemonFound) {
                    startFightActivity(false);
                }

            }
        });

        btnLeft = (Button) findViewById(R.id.left);
        btnLeft.setOnTouchListener(new MyOnTouchListener("left"));
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view.getmThread().getForeground() != null && !Utils.trainerFoundMe)
                    view.getmThread().getForeground().goLeft();
                if (Utils.pokemonFound) {
                    startFightActivity(false);
                }
            }
        });

        btnRight = (Button) findViewById(R.id.right);
        btnRight.setOnTouchListener(new MyOnTouchListener("right"));
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view.getmThread().getForeground() != null && !Utils.trainerFoundMe)
                    view.getmThread().getForeground().goRight();
                if (Utils.pokemonFound) {
                    startFightActivity(false);
                }
            }
        });

        btnY = (Button) findViewById(R.id.y);
        btnY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String buildingName = view.getmThread().getForeground().checkInteractionPossible();
                if (interactionStarted) {
                    mainLayout.removeView(dialogLayout);
                    interactionStarted = false;
                } else {
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
                    if(view.getmThread().getForeground() != null && !Utils.trainerFoundMe)
                        view.getmThread().getForeground().setActionStopped(false);
                    break;
                case MotionEvent.ACTION_UP:
                    if (mHandler == null) return true;
                    mHandler.removeCallbacks(mAction);
                    mHandler = null;
                    if(view.getmThread().getForeground()!= null && !Utils.trainerFoundMe)        // when entering the pokemon center, this action is still executed
                        view.getmThread().getForeground().setActionStopped(true);
                    break;
            }
            return false;
        }

        Runnable mAction = new Runnable() {
            @Override
            public void run() {

                if (!Utils.pokemonFound && !Utils.trainerFoundMe) {

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
                        startFightActivity(false);
                    }
                    mHandler.postDelayed(this, 200);
                }
            }
        };
    }

    public void startFightActivity(boolean isTrainer){

        int scrollX = view.getmThread().getBackground().getScrollX();
        int scrollY = view.getmThread().getBackground().getScrollY();
        Utils.scrollX = scrollX;
        Utils.scrollY = scrollY;
        String status = ((TrainerSprite)view.getmThread().getForeground().getSprite()).getStatus();

        saveKeyValueString("status", status);

        Utils.scrollCoords.put("scrollX", scrollX);
        Utils.scrollCoords.put("scrollY",scrollY);
        Intent fightIntent = new Intent(this,FightActivity.class);
        fightIntent.putExtra("isTrainer",isTrainer);

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
                if(value.contains("caught"))
                    Log.d("NEWPOKE", Utils.mySprite.getMyPokemons().getMyPokemonByOrderNr(5).getName());
            }
            else if(resultCode == RESULT_CANCELED)
            {
                Log.d("FIGHT","Run away");
            }

            if(Utils.currentTrainer!=null){
                Utils.currentTrainer.setDoneFighting(true);
                Utils.currentTrainer = null;
                Utils.trainerFoundMe = false;
            }
            else if(Utils.currentWildPokemon!= null){
                Utils.currentWildPokemon =null;
                Utils.pokemonFound = false;
                Utils.searched = false;
            }
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
        controllerContainer.removeAllViewsInLayout();
        BuildingInsideFirstLayer firstLayer = (BuildingInsideFirstLayer) view.getmThread().getFirstLayer();

        if (buildingName.equals("pokecenter")) {
            mainLayout.removeView(dialogLayout);
            firstLayer.setWelcomeMessage("Welcome to our PokéCenter.;Would you like me to heal your pokémon back to perfect health?");
        }
        else if(buildingName.equals("pokemarkt")){
            mainLayout.removeView(dialogLayout);
            firstLayer.setWelcomeMessage("Welcome!;What do you need? ");
        }
    }

    private void pokecenterInteraction(){

        final BuildingInsideFirstLayer firstLayer = (BuildingInsideFirstLayer) view.getmThread().getFirstLayer();

        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp2.topMargin=600;
        lp2.leftMargin = 900;

        dialogLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        lp3.width = 100;
        lp3.height = 100;
        Button btnYes = new Button(this);
        btnYes.setText("Yes");
        btnYes.setBackgroundColor(Color.WHITE);
        dialogLayout.addView(btnYes, lp3);

        RelativeLayout.LayoutParams lp4 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        lp4.width = 100;
        lp4.height = 100;
        lp4.topMargin = 100;
        Button btnNo = new Button(this);
        btnNo.setText("No");
        btnNo.setBackgroundColor(Color.WHITE);
        dialogLayout.addView(btnNo, lp4);
        dialogLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        mainLayout.addView(dialogLayout, lp2);


        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogLayout.removeAllViewsInLayout();
                mainLayout.removeView(dialogLayout);
                interactionStarted = false;
                firstLayer.pokecenterButtonClicked("no");
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogLayout.removeAllViewsInLayout();
                mainLayout.removeView(dialogLayout);
                firstLayer.pokecenterButtonClicked("yes");
                interactionStarted = false;
                for (int i = 0; i < Utils.mySprite.getMyPokemons().getSize(); i++) {
                    PokemonSprite currentPokemon = Utils.mySprite.getMyPokemons().getMyPokemonByOrderNr(i);
                    currentPokemon.setCurrentHP(currentPokemon.getStats().getHp());
                    List<Move> moves = currentPokemon.getLearnedMoves();
                    for (Move move : moves) {
                        move.setCurrentPp(move.getPp());
                    }
                }


            }
        });
    }

    private void pokemarktInteraction(){

        final BuildingInsideFirstLayer firstLayer = (BuildingInsideFirstLayer) view.getmThread().getFirstLayer();

        RelativeLayout.LayoutParams lp7 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp7.width = 400;

        menuList = new ListView(this);
        menuList.setBackgroundColor(Color.WHITE);
        String[] values = new String[] { "BUY", "SELL", "SEE YA"};

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
                        //Log.d("SELL", "buy some stuff");
                        List<CustomItem> itemsInThisCity = Utils.pokemarktItemList.getPokemarktStorage().get(Utils.currentCity);
                        setUpBuyList(itemsInThisCity);
                        break;
                    case "SELL":
                        Log.d("SELL", "sell some stuff");
                        mainLayout.removeView(menuList);
                        interactionStarted = false;
                        break;
                    case "SEE YA":
                        Log.d("SEEYA","goodbye message");
                        mainLayout.removeView(menuList);
                        interactionStarted = false;

                }
                firstLayer.pokemarktButtonClicked(item);

            }
        });
        mainLayout.addView(menuList, lp7);
    }

    public static void openMenu(){

        Utils.menuOpened = true;

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        lp.topMargin=10;
        lp.leftMargin=600;
        lp.height = 900;
        lp.width = 390;
        Utils.menuList = new ListView(context);
        Utils.menuList.setBackgroundColor(Color.WHITE);
        String[] values = new String[] { "POKéDEX", "POKéMON","BAG","ME","SAVE","OPTIONS","EXIT MENU","QUIT GAME"};

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {list.add(values[i]);}
        final MyAdapter myAdapter = new MyAdapter(context, android.R.layout.simple_list_item_1, list);
        Utils.menuList.setAdapter(myAdapter);

        Utils.menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                switch (item) {
                    case "POKéMON":
                        showMyPokemon();
                        break;
                    case "EXIT MENU":
                        mainLayout.removeView(Utils.menuList);
                        Utils.menuOpened = false;

                        break;
                    case "QUIT GAME":
                        ((Activity) context).finish();
                }

            }
        });
        mainLayout.addView(Utils.menuList,lp);
    }

    private void closeMenu(){
        menuOpened = false;
        menuContainer.removeAllViewsInLayout();
    }

    private static void showMyPokemon(){
        Utils.scrollX = view.getmThread().getBackground().getScrollX();
        Utils.scrollY = view.getmThread().getBackground().getScrollY();
        mainLayout.removeAllViewsInLayout();
        controllerContainer.removeAllViewsInLayout();
        Utils.pokemonMenu(mainLayout, context);

    }

    public static void cancelPokemonMenu(){

        mainLayout.addView(view);           // playground
        controllerContainer.addView(moveControls);
        controllerContainer.addView(menuControls);
        openMenu();
    }



    private void setUpBuyList(final List<CustomItem> itemList){
        mainLayout.removeView(menuList);


        RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        lp3.height=200;
        lp3.width=300;
        final TextView textView = new TextView(this);
        textView.setBackgroundColor(Color.WHITE);
        textView.setTextColor(Color.BLACK);
        textView.setText("Money \n        €" + Utils.myMoney);
        mainLayout.addView(textView, lp3);

        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        lp2.height=600;
        lp2.width=630;
        lp2.leftMargin=350;

        List<String> list = new ArrayList<>();

        for(int i =0;i<itemList.size();i++){
            CustomItem currentItem = itemList.get(i);
            list.add(currentItem.getName()+ "  €" + currentItem.getCost());
        }
        final MyAdapter myAdapter = new MyAdapter(this, android.R.layout.simple_list_item_1, list);
        menuList.setAdapter(myAdapter);
        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                String itemName = ((String) parent.getItemAtPosition(position)).split("  €")[0];
                CustomItem currentItem = Utils.pokemarktItemList.getItemByName(itemName, Utils.currentCity);
                if(Utils.myMoney>= currentItem.getCost()) {
                    Utils.myMoney -= currentItem.getCost();
                    Utils.myItems.addItem(currentItem, 1);
                }
                mainLayout.removeView(textView);
                mainLayout.removeView(menuList);
                interactionStarted = false;
            }
        });
        mainLayout.addView(menuList, lp2);
    }

    @Override
    public void welcomeScreenLoaded(final String buildingName) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (buildingName.equals("pokecenter")) {
                    pokecenterInteraction();
                }
                if (buildingName.equals("pokemarkt")) {
                    pokemarktInteraction();
                }

            }
        });
    }

    @Override
    public void interactionFinished() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                controllerContainer.addView(moveControls);
                controllerContainer.addView(menuControls);
            }
        });

    }

    @Override
    public void trainerFightStarted(TrainerSprite currentTrainer) {
        startFightActivity(true);
        Utils.currentTrainer = currentTrainer;
        Utils.trainerFoundMe = false;
        currentTrainer.setDoneFighting(true);
        Log.d("FIGHT", "fight started with " + currentTrainer.getMyPokemons().getMyPokemonByOrderNr(0).getName() );


    }
}