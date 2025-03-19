package ca.mcmaster.se2aa4.island.team011;

import org.json.JSONArray;
import org.json.JSONObject;

// Reciever breaks down info returned from decision action
public class Reciever{
    private JSONArray biomes;
    private JSONArray creeks;
    private JSONArray sites;

    public Reciever(){
        this.biomes = new JSONArray();
        this.creeks = new JSONArray(); 
        this.sites = new JSONArray();
    }

    public void intakeResponse(JSONObject response){
        this.biomes = response.getJSONArray("biomes");
        this.creeks = response.getJSONArray("creeks");
        this.sites = response.getJSONArray("sites");

    }
}