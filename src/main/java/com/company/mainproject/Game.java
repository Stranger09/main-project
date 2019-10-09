package com.company.mainproject;

import com.couchbase.client.java.document.Document;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import lombok.Getter;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;

import static com.company.mainproject.AppContext.context2;

@Getter
public class Game {



    public void setPlayerName(String playerName) {
        Player player = context2.getBean(Player.class);
        player.setName(playerName);
        ifUserExist(playerName);

    }

    public void ifUserExist(String playerName) {
        DataBase dataBase = context2.getBean(DataBase.class);
        if (dataBase.checkNameInDB(playerName)) {
            setUser();
        } else {
            createUser();
        }
    }

    public void setUser() {
        Player player = context2.getBean(Player.class);
        player.setName(player.getName());
        DataBase dataBase = context2.getBean(DataBase.class);
        dataBase.getPlayerFromDB(player.getName());

    }

    public void createUser() {
        Player player = context2.getBean(Player.class);
        DataBase dataBase = context2.getBean(DataBase.class);
        dataBase.addPlayerToDB(player.getName(), player.getLevel(), player.getScore(),
                player.getLives(), player.isActive(), player.getStatus(), player.getBestScore());
    }



    public String getCards() {
        Card card = context2.getBean(Card.class);
        return card.createCards();
    }

    public String makeStep(String jsonString) {
        Card card = context2.getBean(Card.class);
        StepResult stepResult = context2.getBean(StepResult.class);


        stepResult.setCurrentStepResult(card.compareResults(jsonString));
        updatePlayerData();

        return convertPlayerData();
    }

    private String convertPlayerData() {
        Player player = context2.getBean(Player.class);
        StepResult stepResult = context2.getBean(StepResult.class);
        JSONObject obj = new JSONObject();
        obj.put("name", player.getName());
        obj.put("score", player.getScore());
        obj.put("level", player.getLevel());
        obj.put("lives", player.getLives());
        obj.put("active", player.getStatus());
        obj.put("bestScore", player.getBestScore());
        obj.put("answer", stepResult.getCurrentStepResult());
        return obj.toString();
    }

    private void updatePlayerData() {
        updatePlayerLevel();
        updatePlayerScore();
        updatePlayerLives();
        updatePlayerStatus();
        updatePlayerBestScore();

        Player player = context2.getBean(Player.class);
        DataBase dataBase = context2.getBean(DataBase.class);


        dataBase.updatePlayerDataToDB(player.getName(), player.getLevel(), player.getScore(),
                player.getLives(), player.isActive(), player.getStatus(), player.getBestScore());

        dataBase.getNextScoreFromDB(player.getScore());

    }


    private void updatePlayerScore() {
        Player player = context2.getBean(Player.class);
        StepResult stepResult = context2.getBean(StepResult.class);
        if(stepResult.getCurrentStepResult()) {
            player.setScore(player.getScore() + 100);
        }
    }

    private  void updatePlayerStatus() {
        Player player = context2.getBean(Player.class);
        player.setActive(player.getLives() > 0);
    }

    private void updatePlayerLives() {
        Player player = context2.getBean(Player.class);
        StepResult stepResult = context2.getBean(StepResult.class);
        if(!stepResult.getCurrentStepResult()) {
            player.setLives(player.getLives() - 1);
        }
    }

    private void updatePlayerLevel() {
        Player player = context2.getBean(Player.class);
        StepResult stepResult = context2.getBean(StepResult.class);
        if(stepResult.getCurrentStepResult()) {
            player.setLevel(player.getLevel() + 1);
        }

    }

    private void updatePlayerBestScore(){
        DataBase dataBase = context2.getBean(DataBase.class);
        dataBase.checkBestScore();
    }

    // method nextMaxScore
}
