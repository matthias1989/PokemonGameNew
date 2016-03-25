package info.androidhive.gametest.items;

/**
 * Created by matthias on 3/22/2016.
 */
public class Item {
    private int id;
    private String name;
    private String categoryName;
    private String shortDescription;
    private String longDescription;
    private int cost;

    public Item(String name){
        this.name = name;
    }

    public Item(int id, String name, String categoryName, String shortDescription, int cost){
        this.id = id;
        this.name = name;
        this.categoryName = categoryName;
        this.shortDescription = shortDescription;
        //this.longDescription = longDescription;
        this.cost = cost;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
