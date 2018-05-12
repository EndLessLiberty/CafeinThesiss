package com.example.weysi.firabaseuserregistration.parsers;

import com.example.weysi.firabaseuserregistration.informations.Data;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 1.11.2017.
 */

public class DataParser {

    public List<Data> parser(double x, double y){


        List<Data> dataList=new ArrayList<Data>();

        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location="+x+","+y);
        googlePlaceUrl.append("&radius="+300);
        googlePlaceUrl.append("&type="+"");
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key="+"AIzaSyDxkiV1vWtL97mZqpgAmooNE3zsjRWPYt8");
        String url=googlePlaceUrl.toString();

        JSONObject objRoot = null;
        String  json = null;
        try {

            json = Jsoup.connect(url).ignoreContentType(true).execute().body();
            JSONParser parser = new JSONParser();

                objRoot = (JSONObject) parser.parse(json);
        } catch (IOException e) {
            e.printStackTrace();

        } catch (ParseException e) {
            e.printStackTrace();
        }


        JSONArray places = (JSONArray) objRoot.get("results");

        for (int i = 0; i <places.size(); i++)
        {
            JSONObject country = (JSONObject) places.get(i);
            Data data=new Data();
            data.setName(country.get("name").toString());
            data.setPlaceId(country.get("id").toString());
            data.setUrl(country.get("icon").toString());
            data.setAddress(country.get("vicinity").toString());
            dataList.add(data);

        }


        return dataList;
    }}
