package info.androidhive.gametest.items;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by matthias on 3/25/2016.
 */
public class MyItems {
    private Map<CustomItem,Integer> myItems = new HashMap<>();

    public void addItem(CustomItem item,int amount){

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

    public void setAmountByName(String name,Integer amount){
        for (Map.Entry<CustomItem,Integer> entry : myItems.entrySet())
        {
            if(entry.getKey().getName().equals(name)){
                int nextValue = entry.getValue()+amount;
                if(nextValue>=0)
                    entry.setValue(nextValue);
            }
        }

    }

}
