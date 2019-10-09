package com.company.mainproject;

import com.couchbase.client.java.document.json.JsonObject;
import lombok.Data;

import static com.company.mainproject.AppContext.context2;

@Data
public class Player {
    // TODO: 06.10.2019 set default value from settings
    private String name;
    private boolean active = true;
    private int score = 0;
    private int level = 1;
    private int lives = 3;
    private int bestScore = 0;


    public boolean getStatus() {
        return active;
    }

    public Player getPlayerFromJSON(JsonObject playerFromDB) {
        Player player = context2.getBean(Player.class);
        DataBase dataBase = context2.getBean(DataBase.class);
        dataBase.checkLives(playerFromDB);
        return player;
    }

}
