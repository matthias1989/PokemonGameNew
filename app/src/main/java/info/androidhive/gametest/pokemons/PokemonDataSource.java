package info.androidhive.gametest.pokemons;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by matthias on 3/15/2016.
 */
public class PokemonDataSource implements Serializable {
    private List<Pokemon> pokemons = new ArrayList<Pokemon>();
    private List<TypeEfficacy> typeEfficacies = new ArrayList<>();

    public void addPokemon(Pokemon pokemon){
        pokemons.add(pokemon);
    }

    public List<Pokemon> getPokemons() {
        return pokemons;
    }

    public void setPokemons(List<Pokemon> pokemons) {
        this.pokemons = pokemons;
    }

    public Pokemon getPokemonByName(String name){
        for(Pokemon pokemon : pokemons){
            if(pokemon.getName().equals(name)){
                return pokemon;
            }
        }
        return null;
    }

    public int getSize(){
        return pokemons.size();
    }

    public List<TypeEfficacy> getTypeEfficacies() {
        return typeEfficacies;
    }

    public void setTypeEfficacies(List<TypeEfficacy> typeEfficacies) {
        this.typeEfficacies = typeEfficacies;
    }

    public void addTypeEfficacy(TypeEfficacy typeEfficacy){
        typeEfficacies.add(typeEfficacy);
    }
}
