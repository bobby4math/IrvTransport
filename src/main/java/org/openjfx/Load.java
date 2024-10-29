package org.openjfx;

import java.util.Vector;
import java.net.URLEncoder;
import java.util.Vector;
import java.io.*;
import java.math.BigDecimal;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.BaseRequest;
import com.mashape.unirest.request.GetRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner; 
import java.time.LocalDate;
import io.github.cdimascio.dotenv.Dotenv;

//Class that represents an individual Load

public class Load implements java.io.Serializable{
    Dotenv dotenv = Dotenv.load();
    Float gasPrices[] = new Float[50];
    String[] states = {"AL", "AK", "AZ", "CA", "CO", "CT", "DC", "DE", "FL", "GA", "", "IA", "ID", "IL", "IN", "KA", "KY", "LA", "MA", "MD", "ME", "MI", "MN", "MO", "MS", "MT", "NC", "ND", "NE", "NH", "NJ", "NM", "NV", "NY", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VA", "VT", "WA", "WI", "WV", "WY"};
    public String puDate;
    public String delDate;
    public String status;
    public int weight;
    public int rate;
    public float miles;
    public int loadProfit;
    public int tollTotal;
    //miles per gallon mpg[i] = estimated mpg of freight weighing i*10000
    static int[] mpg = {6, 5, 5, 4, 4};
    public Vector<Stop> allStops = new Vector<Stop>();
    public void addStop(String stopC, String stopS){
        allStops.add(new Stop(stopC, stopS));
    }
    //get diesel prices for each state using api 
    public void getAllGasP() throws IOException{
        try {
            HttpResponse<JsonNode> response = Unirest.get("https://gas-price.p.rapidapi.com/allUsaPrice")
.header("X-RapidAPI-Key", dotenv.get("GAS_PRICE_API_KEY"))
.header("X-RapidAPI-Host", "gas-price.p.rapidapi.com")
.asJson();
            JsonNode node = response.getBody(); 
            org.json.JSONObject first= node.getObject();
            org.json.JSONArray second = first.getJSONArray("result");
            for(int i = 1; i < 50; i++){
                this.gasPrices[i] = Float.parseFloat(second.getJSONObject(i).getString("diesel"));
                System.out.println(this.gasPrices[i]);
            }
            try {
                FileOutputStream file = new FileOutputStream("gasPrices.txt");
                ObjectOutputStream out = new ObjectOutputStream(file);
                out.writeObject(this.gasPrices);
                out.close();
                file.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            
        } catch (UnirestException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    public int evalRate(boolean updateGas){
        if(updateGas){
            try {
                this.getAllGasP();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        else{
            try {
                FileInputStream file = new FileInputStream("gasPrices.txt");
                ObjectInputStream in = new ObjectInputStream(file);
                this.gasPrices = (Float[])in.readObject();
                in.close();
                file.close();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        int profit = 0;
        int gallons = 0;
        int gasCost = 0;
        Vector<String> zips = this.getZips();
        miles = 0;
        for(int i = 1; i < zips.size(); i++){
            String url2 = "https://redline-redline-zipcode.p.rapidapi.com/rest/distance.json/" + zips.get(i - 1) + "/" + zips.get(i) + "/mile";
            HttpResponse<String> response2 = null;
            try {
                response2 = Unirest.get(url2)
             .header("X-RapidAPI-Key", dotenv.get("ZIPCODE_API_KEY"))
             .header("X-RapidAPI-Host", "redline-redline-zipcode.p.rapidapi.com")
             .asString();
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            String dist = response2.getBody().substring(12, response2.getBody().length() - 1);
            miles += Float.parseFloat(dist);
            if(i == 1){
                gallons = (int) (miles / mpg[0]);
            }
            else {
            gallons = (int) (miles / mpg[weight/10000]);
            }
            int stateIndex = Arrays.asList(states).indexOf(this.allStops.get(0).getState());
            gasCost = gallons * gasPrices[stateIndex].intValue();
        }
        profit = rate - gasCost;
        loadProfit = profit;
        this.tollTotal = this.calcTolls();
        loadProfit -= this.tollTotal;
        return profit;
    }

    //Calculate toll cost between two stops using tollguru api
    public int getTollCost(String startCity, String startState, String endCity, String endState) {

        int returnVal = 0;
        HttpResponse<JsonNode> response3 = null;
        String url3 = "https://apis.tollguru.com/toll/v2/origin-destination-waypoints";
        try {
            response3 =  Unirest.post("https://apis.tollguru.com/toll/v2/origin-destination-waypoints")
            .header("Content-Type", "application/json")
            .header("x-api-key", dotenv.get("TOLLGURU_API_KEY"))
            .body("{\"from\":{\"address\":\""+startCity+", "+startState+", USA"+"\"},\"to\":{\"address\":\""+endCity+", "+endState +", USA"+"\"},\"serviceProvider\":\"here\",\"vehicle\":{\"type\":\"5AxlesTruck\"}}")
            .asJson();
          
            
            JsonNode node = response3.getBody();
            org.json.JSONObject polyLine = node.getObject();
            org.json.JSONArray poly5 = polyLine.getJSONArray("routes");
            org.json.JSONObject poly4 = poly5.getJSONObject(0);
            org.json.JSONArray poly3 = poly4.getJSONArray("tolls");
            for(int i = 0; i < poly3.length(); i++){
                org.json.JSONObject poly6= poly3.getJSONObject(i);
                BigDecimal tollR = poly6.getBigDecimal("tagCost");
                returnVal += tollR.intValue();
            }
            //org.json.JSONObject poly4 = poly5.getJSONObject("tolls");
        } catch (UnirestException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return returnVal;
        
    }
    //calculate tolls between all stops in the load
    public int calcTolls(){
        int total = 0;
        for(int i = 1; i < allStops.size(); i++){
            total += getTollCost(this.allStops.get(i-1).getCity(), this.allStops.get(i-1).getState(), this.allStops.get(i).getCity(), this.allStops.get(i).getState());
        }
        return total;
    }
    public Vector<String> getZips() {
        Vector<String> returnVal = new Vector<String>();
        for(int i = 0; i<this.allStops.size()-1; i++){
            returnVal.add(this.allStops.get(i).getZip()); 
         }
         return returnVal;
    }

}
