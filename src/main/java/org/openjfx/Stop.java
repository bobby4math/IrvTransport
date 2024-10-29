package org.openjfx;
import java.util.Vector;
import java.net.URLEncoder;
import java.util.Vector;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import io.github.cdimascio.dotenv.Dotenv;

//Class that represents a stop in a load

public class Stop implements java.io.Serializable{
    Dotenv dotenv = Dotenv.load();
    private String stopCity;
    private String stopState;

    public Stop(String cit, String stat)  {
        this.stopCity = cit;
        this.stopState = stat;
    }
    public void test() {
        System.out.println(this.stopCity + this.stopState);
        
    }
    public String getCity(){
        return this.stopCity;
    }
    public String getState(){
        return this.stopState;
    }
    //get the zipcode form api based on city and state
    public String getZip() {
        String returnVal = "";
        HttpResponse<String> response = null;
        String url = "https://redline-redline-zipcode.p.rapidapi.com/rest/city-zips.json/" + this.stopCity + "/" + this.stopState;
        try {
            System.out.println(stopCity + stopState);
            response = Unirest.get(url)
         .header("X-RapidAPI-Key", dotenv.get("ZIPCODE_API_KEY"))
         .header("X-RapidAPI-Host", "redline-redline-zipcode.p.rapidapi.com")
         .asString();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        String zipCodes = response.getBody();
        JSONParser parser = new JSONParser();
        try {
            JSONObject object = (JSONObject) parser.parse(zipCodes);
            JSONArray array = (JSONArray) object.get("zip_codes");
            returnVal = array.get(0).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return returnVal;

    }
}
