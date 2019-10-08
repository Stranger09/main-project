package com.company.mainproject;

import lombok.Getter;
import lombok.ToString;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;


@ToString
public class Card {
    @Getter
    private String stepImageName;

    public boolean compareResults(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            Iterator x = jsonObject.keys();
            JSONArray jsonArray = new JSONArray();

            while (x.hasNext()) {
                for(int i=0;i<jsonObject.length();i++) {
                    String key = (String) x.next();
                    jsonArray.put(jsonObject.get(key));
                }
            }
            String currentStepImage = getStepImageName();

            for(int i=0; i<jsonArray.length(); i++) {
                if(!jsonArray.get(i).equals(currentStepImage)) {
                    return false;
                }
            }
        }catch (JSONException err){}
        return true;
    }

    public String createCards() {

        JSONObject obj = new JSONObject();
        JSONArray arr = new JSONArray();
        // TODO: 29.09.2019 change count of elements depends from level
        int countOfElements = 9;
        // TODO: 29.09.2019 change count of matched elements depends from level
        int shouldBeMatched = 3;
        // TODO: 05.10.2019 set directory path as const
        String directoryPath = "/src/main/resources/static/image/";
        // TODO: 05.10.2019 made time changeable depends from level
        int timeBeforeShowImages = 3;
        // TODO: 05.10.2019 made time changeable depends from level
        int timeForMakeChoice = 10;
        // TODO: 05.10.2019 made time changeable depends from level
        int showImagesTime = 3;

        ImageLoader imageLoader = new ImageLoader();
        ArrayList<String> images = imageLoader
                .getRenderedImages(directoryPath, countOfElements, shouldBeMatched);

        stepImageName = imageLoader.getCurrentStepImageName();


        for (int i = 0; i < countOfElements; i++) {
            obj.put("id", i);
            obj.put("image", "/image/" + images.get(i));
            obj.put("name", images.get(i));
            obj.put("isMain", images.get(i).equals(stepImageName));
            obj.put("matchCount", shouldBeMatched);
            obj.put("timeBeforeShowImages", timeBeforeShowImages);
            obj.put("timeForMakeChoice", timeForMakeChoice);
            obj.put("showImagesTime", showImagesTime);
            arr.put(obj);

            obj = new JSONObject();
        }
        return arr.toString();
    }
}
