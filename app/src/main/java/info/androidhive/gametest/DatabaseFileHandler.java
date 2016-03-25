package info.androidhive.gametest;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import info.androidhive.gametest.items.Item;
import info.androidhive.gametest.items.ItemDataSource;
import info.androidhive.gametest.pokemons.Move;
import info.androidhive.gametest.pokemons.Pokemon;
import info.androidhive.gametest.pokemons.PokemonDataSource;
import info.androidhive.gametest.pokemons.Stat;
import info.androidhive.gametest.pokemons.TypeEfficacy;

/**
 * Created by matthias on 3/22/2016.
 */
public class DatabaseFileHandler {
    public static PokemonDataSource ds;
    public static ItemDataSource itemDs;

    public static void readData(){
        File textDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(textDir,"db_config.txt");
        ds = new PokemonDataSource();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {

                String object[] = line.split("/");          // elke pokemon
                for(int i =0;i<object.length;i++){
                    String data[] = object[i].split(";");      // elk attribuut (kolom) binnen pokemon
                    String pokeId = data[0];
                    String pokeName = data[1];
                    String captureRate = data[2];
                    int baseExp = Integer.parseInt(data[3]);

                    Pokemon pokemon = new Pokemon(pokeName);
                    pokemon.setCaptureRate(Integer.parseInt(captureRate));
                    pokemon.setId(Integer.parseInt(pokeId));
                    pokemon.setBaseExp(baseExp);

                    String types = data[4];
                    String types_array[] = types.split(",");
                    for(String type : types_array) {
                        pokemon.addType(type);
                    }

                    String experience = data[5];    // nog opsplitsen
                    String experience_array[] = experience.split(",");
                    Map<Integer,Integer> experienceMap = new HashMap<>();
                    for(String element : experience_array){
                        String entrySet[] = element.split(":");
                        experienceMap.put(Integer.parseInt(entrySet[0]),Integer.parseInt(entrySet[1]));
                    }
                    pokemon.setExperienceTable(experienceMap);

                    String moves = data[6];         // nog opsplitsen
                    String moves_array[] = moves.split(",");
                    for(String element : moves_array){
                        String move_details[] = element.split("_");
                        String name = move_details[0];
                        String genId = move_details[1];
                        String typeId = move_details[2];
                        int power= (!move_details[3].equals("null") && !move_details[3].equals("")) ? Integer.parseInt(move_details[3]):0;
                        int pp = (!move_details[4].equals("null")  && !move_details[4].equals("")) ? Integer.parseInt(move_details[4]):0;
                        int accuracy = (!move_details[5].equals("null")  && !move_details[5].equals("")) ? Integer.parseInt(move_details[5]):0;
                        int priority = (!move_details[6].equals("null")  && !move_details[6].equals("")) ? Integer.parseInt(move_details[6]):0;
                        String targetId = move_details[7];
                        String damageClassId = move_details[8];
                        String effectId = move_details[9];
                        int effectChance = (!move_details[10].equals("null")  && !move_details[10].equals("")) ? Integer.parseInt(move_details[10]):0;
                        int moveLevel = (!move_details[11].equals("null") && !move_details[11].equals("")) ? Integer.parseInt(move_details[11]):0;
                        Move move = new Move(name,power,pp,moveLevel,accuracy,priority,effectChance,typeId);
                        pokemon.addMove(move);

                    }


                    String stats = data[7];
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


                    ds.addPokemon(pokemon);
                }
            }
            br.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void readData2(){
        File textDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(textDir,"db_config2.txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {

                String object[] = line.split("/");          // elke pokemon
                for(int i =0;i<object.length;i++){
                    String data[] = object[i].split(";");      // elk attribuut (kolom) binnen pokemon
                    int damageTypeId = Integer.parseInt(data[0]);
                    int targetTypeId = Integer.parseInt(data[1]);
                    int damageFactor = Integer.parseInt(data[2]);

                    TypeEfficacy typeEfficacy = new TypeEfficacy(damageTypeId,targetTypeId,damageFactor);

                    ds.addTypeEfficacy(typeEfficacy);
                }
            }
            br.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void readItemData(){
        itemDs = new ItemDataSource();
        File textDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(textDir,"db_config_items.txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
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

                    itemDs.addItem(new Item(itemId, itemName, itemCategory, itemShortDescription,cost));
                }
            }
            br.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
