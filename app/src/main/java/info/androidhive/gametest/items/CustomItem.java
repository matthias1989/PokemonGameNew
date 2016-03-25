package info.androidhive.gametest.items;

import android.util.Log;

import info.androidhive.gametest.pokemons.PokemonDataSource;

/**
 * Created by matthias on 3/25/2016.
 */
public class CustomItem extends Item {
    private ItemDataSource ds;
    private float pokeballCatchRate;
    public CustomItem(int id, String name, String categoryName, String shortDescription, int cost) {
        super(id, name, categoryName, shortDescription, cost);
    }

    public CustomItem(String name,ItemDataSource ds){
        super(name);
        this.ds = ds;
        setId(ds.getItemByName(name).getId());
        setCategoryName(ds.getItemByName(name).getCategoryName());
        setShortDescription(ds.getItemByName(name).getShortDescription());
        setCost(ds.getItemByName(name).getCost());


    }


}
