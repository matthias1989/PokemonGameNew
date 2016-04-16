package info.androidhive.gametest;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.androidhive.gametest.items.CustomItem;
import info.androidhive.gametest.items.Item;
import info.androidhive.gametest.items.ItemDataSource;
import info.androidhive.gametest.items.MyItems;
import info.androidhive.gametest.items.PokemarktItemList;
import info.androidhive.gametest.pokemons.Effect;
import info.androidhive.gametest.pokemons.Move;
import info.androidhive.gametest.pokemons.MyPokemons;
import info.androidhive.gametest.pokemons.Pokemon;
import info.androidhive.gametest.pokemons.PokemonDataSource;
import info.androidhive.gametest.pokemons.PokemonSprite;
import info.androidhive.gametest.pokemons.Stat;
import info.androidhive.gametest.pokemons.TypeEfficacy;
import info.androidhive.gametest.sprites.Sprite;
import info.androidhive.gametest.sprites.TrainerSprite;

/**
 * Created by matthias on 3/22/2016.
 */
public class DatabaseFileHandler {

    private static File textDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

    private static List<Effect> effects;
    public static void readData(Context context){
        getEffects(context);

        //File file = new File(textDir,"db_config.txt");
        Utils.ds = new PokemonDataSource();
        try {
            //BufferedReader br = new BufferedReader(new FileReader(file));
            InputStream is = context.getAssets().open("db_config.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
            String line;

            while ((line = br.readLine()) != null) {

                String object[] = line.split("/");          // elke pokemon
                for(int i =0;i<object.length;i++){
                    String data[] = object[i].split(";");      // elk attribuut (kolom) binnen pokemon
                    Pokemon pokemon = new Pokemon(data[1]);
                    pokemon.setId(Integer.parseInt(data[0]));
                    pokemon.setCaptureRate(Integer.parseInt(data[2]));
                    pokemon.setBaseExp(Integer.parseInt(data[3]));

                    if(!data[5].equals("")) {
                        pokemon.setEvolvedSpeciesId(Integer.parseInt(data[4]));
                        pokemon.setEvolutionTriggerId(Integer.parseInt(data[5]));
                        if (!data[6].equals("null"))
                            pokemon.setTriggerEvolutionItemId(Integer.parseInt(data[6]));
                        if (!data[7].equals("null"))
                            pokemon.setMinimumLevelEvolution(Integer.parseInt(data[7]));
                        //Log.d("BLA",pokemon.getId()+"=>"+pokemon.getEvolvedSpeciesId());
                    }

                    //Log.d("BLA",pokemon.getId()+"=>"+pokemon.getEvolvedSpeciesId());
                    String types = data[8];
                    String types_array[] = types.split(",");
                    for(String type : types_array) {
                        pokemon.addType(type);
                    }

                    String experience = data[9];    // nog opsplitsen
                    String experience_array[] = experience.split(",");
                    Map<Integer,Integer> experienceMap = new HashMap<>();
                    for(String element : experience_array){
                        String entrySet[] = element.split(":");
                        experienceMap.put(Integer.parseInt(entrySet[0]),Integer.parseInt(entrySet[1]));
                    }
                    pokemon.setExperienceTable(experienceMap);

                    String moves = data[10];         // nog opsplitsen
                    String moves_array[] = moves.split(",");
                    for(String element : moves_array){
                        String move_details[] = element.split("_");
                        int id = Integer.parseInt(move_details[0]);
                        String name = move_details[1];
                        String genId = move_details[2];
                        String typeId = move_details[3];
                        int power= (!move_details[4].equals("null") && !move_details[4].equals("")) ? Integer.parseInt(move_details[4]):0;
                        int pp = (!move_details[5].equals("null")  && !move_details[5].equals("")) ? Integer.parseInt(move_details[5]):0;
                        int accuracy = (!move_details[6].equals("null")  && !move_details[6].equals("")) ? Integer.parseInt(move_details[6]):0;
                        int priority = (!move_details[7].equals("null")  && !move_details[7].equals("")) ? Integer.parseInt(move_details[7]):0;
                        String targetId = move_details[8];
                        String damageClassId = move_details[9];
                        String effectId = move_details[10];
                        int effectChance = (!move_details[11].equals("null")  && !move_details[11].equals("")) ? Integer.parseInt(move_details[11]):0;
                        int moveLevel = (!move_details[12].equals("null") && !move_details[12].equals("")) ? Integer.parseInt(move_details[12]):0;
                        Move move = new Move(id,name,power,pp,moveLevel,accuracy,priority,effectChance,typeId);
                        move.setDamageClassId(Integer.parseInt(damageClassId));
                        move.setEffectId(Integer.parseInt(effectId));
                        move.setEffectChance(effectChance);
                        move.setTargetId(Integer.parseInt(targetId));
                        addEffect(move);
                        pokemon.addMove(move);

                    }


                    String stats = data[11];
                    String statsArray[] = stats.split(",");
                    Stat stat = new Stat();
                    for(String element : statsArray){
                        String entrySet[] = element.split(":");

                        switch (entrySet[0]){
                            case "Attack":
                                stat.setAttack(Integer.parseInt(entrySet[1]));
                                break;
                            case "Defense":
                                stat.setDefense(Integer.parseInt(entrySet[1]));
                                break;
                            case "HP":
                                stat.setHp(Integer.parseInt(entrySet[1]));
                                break;
                            case "Special Attack":
                                stat.setsAttack(Integer.parseInt(entrySet[1]));
                                break;
                            case "Special Defense":
                                stat.setsDefense(Integer.parseInt(entrySet[1]));
                                break;
                            case "Speed":
                                stat.setSpeed(Integer.parseInt(entrySet[1]));
                                break;
                            case "Accuracy":
                                stat.setAccuracy(Integer.parseInt(entrySet[1]));
                                break;
                            case "Evasion":
                                stat.setEvasion(Integer.parseInt(entrySet[1]));
                                break;
                        }
                    }
                    pokemon.setStats(stat);


                    Utils.ds.addPokemon(pokemon);
                }
            }
            br.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void readData2(Context context){
        try {
            InputStream is = context.getAssets().open("db_config2.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
            String line;

            while ((line = br.readLine()) != null) {

                String object[] = line.split("/");          // elke pokemon
                for(int i =0;i<object.length;i++){
                    String data[] = object[i].split(";");      // elk attribuut (kolom) binnen pokemon
                    int damageTypeId = Integer.parseInt(data[0]);
                    int targetTypeId = Integer.parseInt(data[1]);
                    int damageFactor = Integer.parseInt(data[2]);

                    TypeEfficacy typeEfficacy = new TypeEfficacy(damageTypeId,targetTypeId,damageFactor);

                    Utils.ds.addTypeEfficacy(typeEfficacy);
                }
            }
            br.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void readItemData(Context context){
        Utils.itemDs = new ItemDataSource();

        try {
            InputStream is = context.getAssets().open("db_config_items.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
            String line;
            while ((line = br.readLine()) != null) {

                String object[] = line.split("//");          // elke pokemon
                for(int i =0;i<object.length;i++){
                    String data[] = object[i].split(";");      // elk attribuut (kolom) binnen pokemon
                    int itemId = Integer.parseInt(data[0]);
                    String itemName = data[1];
                    int cost = Integer.parseInt(data[2]);
                    String itemCategory = data[3];
                    String itemShortDescription = data[4];

                    //Log.d("BLA",itemId+"=>"+itemName);
                    Utils.itemDs.addItem(new Item(itemId, itemName, itemCategory, itemShortDescription, cost));
                }
            }
            br.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void readItemData2(Context context){

        try {
            InputStream is = context.getAssets().open("pokemarkt_items.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
            String line;
            while ((line = br.readLine()) != null) {
                String object[] = line.split(";");          // each city
                for(int i =0;i<object.length;i++){
                    String data[] = object[i].split(":");
                    String cityName = data[0];
                    String items = data[1];
                    String itemList[] = items.split(",");
                    Utils.pokemarktItemList.addPokemarkt(cityName);
                    for(String itemName : itemList){
                        CustomItem item = new CustomItem(itemName,Utils.itemDs);
                        Utils.pokemarktItemList.addItemToPokemarkt(item);
                    }
                }
            }
            br.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void readSpriteInfo(Context context){
        Utils.allSprites = new ArrayList<>();
        try {
            InputStream is = context.getAssets().open("sprite_map.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
            String line;
            while ((line = br.readLine()) != null) {
                Log.d("LINE",line);
                String objects[] = line.split(";");
                int id = Integer.parseInt(objects[0]);
                String location = objects[1];
                String name = objects[2];
                int x = (int) (Float.parseFloat(objects[3])*Utils.tileSize);
                int y = (int) (Float.parseFloat(objects[4]) *Utils.tileSize);
                int width = (int) (Float.parseFloat(objects[5])*Utils.tileSize);
                int height= (int) (Float.parseFloat(objects[6])*Utils.tileSize);
                String status = objects[7];

                //int id, int tx, int ty, int width, int height, String type,String status

                String pokemons = objects[8];


                if(!pokemons.equals("empty")){
                    MyPokemons myPokemons = new MyPokemons();
                    String pokemonList[] = pokemons.split(",");
                    for(String pokemonElement : pokemonList){
                        String elementList[] = pokemonElement.split(":");
                        String pokemonName = elementList[0];
                        int level = Integer.parseInt(elementList[1]);
                        PokemonSprite myPokemon = new PokemonSprite(pokemonName,Utils.ds);
                        myPokemon.setLevel(level);
                        myPokemon.setCurrentHP(myPokemon.getStats().getHp());
                        myPokemons.addPokemon(myPokemon);
                    }
                    TrainerSprite trainerSprite = new TrainerSprite(id,x,y,width,height,name,status,location, context);
                    trainerSprite.setBitmap(name, status);
                    trainerSprite.setMyPokemons(myPokemons);
                    trainerSprite.setScrollY(Utils.scrollCoords.get("scrollY"));
                    trainerSprite.setScrollX(Utils.scrollCoords.get("scrollX"));
                    //trainerSprite.setAllBitmaps();
                    Utils.allSprites.add(trainerSprite);
                    Log.d("POKE",myPokemons.getMyPokemonByOrderNr(1).getLevel()+"");

                }
                else{
                    Sprite currentSprite = new Sprite(id,x,y,width,height,name,location,context);
                    currentSprite.setBitmap(name, status);
                    currentSprite.setScrollY(Utils.scrollCoords.get("scrollY"));
                    currentSprite.setScrollX(Utils.scrollCoords.get("scrollX"));
                    Utils.allSprites.add(currentSprite);
                }


            }

            br.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void readTest(Context context){


        try {
            InputStream is = context.getAssets().open("db_config.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void getEffects(Context context) {
        effects = new ArrayList<>();
        try {
            InputStream is = context.getAssets().open("db_config_effects.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line;
            while ((line = br.readLine()) != null) {
                //Log.d("LINE", line);
                String objects[] = line.split("//");
                for(String row : objects) {
                    String items[] = row.split(";");
                    int id = Integer.parseInt(items[0]);
                    String statId = items[1];
                    String statChange = items[2];
                    String effectProse = items[3];
                    Effect effect = new Effect(id, effectProse);
                    if (!statId.equals("")) {
                        effect.setStatId(Integer.parseInt(statId));
                        effect.setStatChange(Integer.parseInt(statChange));
                    }
                    effects.add(effect);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void addEffect(Move move){
        //Log.d("BLA",move.getId()+"");
        for(Effect effect : effects){
            if(move.getId()==effect.getMoveId()){
                move.setEffect(effect);
            }
        }
    }
}
