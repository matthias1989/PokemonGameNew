package info.androidhive.gametest.items;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by matthias on 3/25/2016.
 */
public class MyItems {
    private Map<CustomItem,Integer> myItems = new HashMap<>();

    public void addItem(CustomItem item,int amount){
        boolean keyExists = setAmountByName(item.getName(),amount); // this method looks of the key already exists and then adds the amount to it
        if(!keyExists && amount>0)  // if not (and the amount is higher than 0) add the element
            myItems.put(item,amount);
    }

    public Map<CustomItem,Integer> getMyItems() {
        return myItems;
    }

    public void setMyItems(Map<CustomItem,Integer> myItems) {
        this.myItems = myItems;
    }

    public Integer getAmountByName(String name){
        for (Map.Entry<CustomItem,Integer> entry : myItems.entrySet())
        {
            if(entry.getKey().getName().equals(name)){
                return entry.getValue();
            }
        }
        return 0;
    }

    public boolean setAmountByName(String name,Integer amount){
        boolean valueSet = false;
        for (Map.Entry<CustomItem,Integer> entry : myItems.entrySet())
        {
            if(entry.getKey().getName().equals(name)){
                int nextValue = entry.getValue()+amount;
                if(nextValue>=0)
                    entry.setValue(nextValue);
                else
                    myItems.remove(entry.getKey());

                valueSet = true;
            }
        }
        return valueSet;
    }

}
