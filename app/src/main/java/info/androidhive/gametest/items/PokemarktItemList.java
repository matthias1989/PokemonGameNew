package info.androidhive.gametest.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.androidhive.gametest.Utils;

/**
 * Created by matthias on 4/8/2016.
 */
public class PokemarktItemList {
    private Map<String,List<CustomItem>> pokemarktStorage = new HashMap<>();
    private List<CustomItem> list;
    private ItemDataSource ds;

    public PokemarktItemList(ItemDataSource ds){
        this.ds = ds;
    }

    public void addPokemarkt(String cityName){
        list = new ArrayList<>();
        pokemarktStorage.put(cityName,list);
    }

    public void addItemToPokemarkt (CustomItem item){
        list.add(item);
       //list.add(itemName);
    }

    public CustomItem getItemByName(String itemName, String cityName){
        List<CustomItem> itemList = pokemarktStorage.get(cityName);
        for(CustomItem item: itemList){
            if(item.getName().equals(itemName))
                return item;
        }
        return null;
    }

    public Map<String,List<CustomItem>> getPokemarktStorage() {
        return pokemarktStorage;
    }

    public void setItems(Map<String,List<CustomItem>> pokemarktStorage) {
        this.pokemarktStorage = pokemarktStorage;
    }
}
