package info.androidhive.gametest.pokemons;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by matthias on 3/16/2016.
 */
public class MyPokemons implements Serializable {
    private Map<Integer,PokemonSprite> myPokemons = new HashMap<>();
    private int counter = 0;

    public void addPokemon(PokemonSprite pokemonSprite){
        myPokemons.put(counter++, pokemonSprite);
    }

    public Map<Integer,PokemonSprite> getMyPokemons() {
        return myPokemons;
    }

    public void setMyPokemons(Map<Integer,PokemonSprite> myPokemons) {
        this.myPokemons = myPokemons;
    }

    public PokemonSprite getMyPokemonByOrderNr(int orderNr){
        return myPokemons.get(orderNr);
    }

    public PokemonSprite getMyPokemonByName(String name){
        for(Map.Entry<Integer,PokemonSprite> myPokemon : myPokemons.entrySet()){
            if(myPokemon.getValue().getName().equals(name)){
                return myPokemon.getValue();
            }
        }
        return null;
    }

    public Integer getMyOrderId(PokemonSprite pokemon){
        for(Map.Entry<Integer,PokemonSprite> myPokemon : myPokemons.entrySet()){
            if(myPokemon.getValue().getName().equals(pokemon.getName())){
                return myPokemon.getKey();
            }
        }
        return 0;
    }

    public void setFirstPokemon(String name){
        PokemonSprite selectedPokemon = getMyPokemonByName(name);
        Integer currentOrderId = getMyOrderId(selectedPokemon);
        PokemonSprite firstPokemon = getMyPokemonByOrderNr(0);
        myPokemons.put(0,selectedPokemon);
        myPokemons.put(currentOrderId,firstPokemon);
    }



//    public PokemonSprite getMyPokemonByUID(String UID){
//
//    }

    public int getSize(){
        return myPokemons.size();
    }
}
