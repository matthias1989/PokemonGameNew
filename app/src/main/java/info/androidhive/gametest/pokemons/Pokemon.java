package info.androidhive.gametest.pokemons;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by matthias on 3/14/2016.
 */
public class Pokemon implements Serializable {
    //private int mData;

    private int id;
    private String name;
    private List<String> types = new ArrayList<>();         // IDs!
    private Stat stats;
    private int experience;
    private int captureRate;
    private int baseExp;
    private int evolvedSpeciesId=0;
    private int evolutionTriggerId=0;
    private int triggerEvolutionItemId=0;
    private int minimumLevelEvolution=0;

    private ArrayList<Move> moves = new ArrayList<>();
    private Map<Integer,Integer> experienceTable = new HashMap<>();

    public Pokemon(String name){
        setName(name);
    }

    public Pokemon(int id, String name, List<String> types, Stat stats, int experience, int captureRate, ArrayList<Move> moves) {
        this.id = id;
        this.name = name;
        this.types = types;
        this.stats = stats;
        this.experience = experience;
        this.captureRate = captureRate;
        this.moves = moves;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<Integer, Integer> getExperienceTable() {
        return experienceTable;
    }

    public void setExperienceTable(Map<Integer, Integer> experienceTable) {
        this.experienceTable = experienceTable;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }


    public Stat getStats(){
        return stats;
    }
    public void setStats(Stat stats){
        this.stats = stats;
    }

    public List<String> getType() {
        return types;
    }

    public void addType(String type){
        types.add(type);
    }

    public void setType(List<String> type) {
        this.types = type;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getBaseExp() {
        return baseExp;
    }

    public void setBaseExp(int baseExp) {
        this.baseExp = baseExp;
    }

    public int getCaptureRate() {
        return captureRate;
    }

    public void setCaptureRate(int captureRate) {
        this.captureRate = captureRate;
    }

    public ArrayList<Move> getMoves() {
        return moves;
    }

    public void setMoves(ArrayList<Move> moves) {
        this.moves = moves;
    }

    public void addMove(Move move){
        this.moves.add(move);
    }


    @Override
    public String toString(){
        return getName();
    }

    public int getEvolvedSpeciesId() {
        return evolvedSpeciesId;
    }

    public void setEvolvedSpeciesId(int evolvedSpeciesId) {
        this.evolvedSpeciesId = evolvedSpeciesId;
    }

    public int getEvolutionTriggerId() {
        return evolutionTriggerId;
    }

    public void setEvolutionTriggerId(int evolutionTriggerId) {
        this.evolutionTriggerId = evolutionTriggerId;
    }

    public int getTriggerEvolutionItemId() {
        return triggerEvolutionItemId;
    }

    public void setTriggerEvolutionItemId(int triggerEvolutionItemId) {
        this.triggerEvolutionItemId = triggerEvolutionItemId;
    }

    public int getMinimumLevelEvolution() {
        return minimumLevelEvolution;
    }

    public void setMinimumLevelEvolution(int minimumLevelEvolution) {
        this.minimumLevelEvolution = minimumLevelEvolution;
    }
}
