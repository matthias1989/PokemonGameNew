package info.androidhive.gametest.items;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matthias on 3/22/2016.
 */
public class ItemDataSource {
    List<Item> itemList;

    public ItemDataSource(){
        itemList = new ArrayList<>();
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public void addItem(Item item){
        itemList.add(item);
    }

    public Item getItemByName(String name){
        for(Item item : itemList){
            if(item.getName().equals(name))
                return item;
        }
        return null;
    }
}
