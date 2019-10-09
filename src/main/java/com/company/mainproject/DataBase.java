package com.company.mainproject;

import com.couchbase.client.java.*;
import com.couchbase.client.java.document.*;
import com.couchbase.client.java.document.json.*;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import com.couchbase.client.java.query.*;
import com.couchbase.client.java.query.dsl.Sort;

import java.util.List;

import static com.company.mainproject.AppContext.context2;
import static com.couchbase.client.java.query.Select.select;
import static com.couchbase.client.java.query.dsl.Expression.i;
import static com.couchbase.client.java.query.dsl.Expression.x;

public class DataBase {


    CouchbaseEnvironment env = DefaultCouchbaseEnvironment
            .builder()
            .mutationTokensEnabled(true)
            .queryTimeout(3000)
            .computationPoolSize(5)
            .build();
    Cluster cluster = CouchbaseCluster.create(env, "couchbase://127.0.1.1");
    Bucket bucket = cluster.openBucket("Players", "123456");
    JsonObject playerData = JsonObject.create();


    public void addPlayerToDB(String name, int level, int scores, int lives,
                              boolean isActive, boolean playerStatus, int bestScore) {



        playerData.put("name", name);
        playerData.put("level", level);
        playerData.put("score", scores);
        playerData.put("lives", lives);
        playerData.put("active", isActive);
        playerData.put("answer", playerStatus);
        playerData.put("bestScore", bestScore);
        JsonDocument document = JsonDocument.create(name, playerData);
        bucket.insert(document);
    }


    public boolean checkNameInDB(String name) {

        boolean existDoc = bucket.exists(name);

        return existDoc;
    }


    public int getNextScoreFromDB(int score) {

        Statement statement = select("score").from(i("Players")).where(x("score")
                .gt(x(score))).orderBy(Sort.desc("is_primary"), Sort.asc("score")).limit(1);
        List<N1qlQueryRow> rows = performSearchInDB(statement);
        return rows.isEmpty() ? score : rows.get(0).value().getInt("score");



    }


    private List<N1qlQueryRow> performSearchInDB(Statement statement) {
        N1qlQueryResult result = bucket.query(statement);
        return result.allRows();
    }


    public Player getPlayerFromDB(String name) {
        Player player = context2.getBean(Player.class);
        return player.getPlayerFromJSON(bucket.get(name).content());
    }

    public void checkLives(JsonObject playerFromDB){
        Player player = context2.getBean(Player.class);

        int lives = player.getLives();
        if (lives == 0){
            player.setLives(3);
            player.setScore(0);
            player.setLevel(1);
            player.setName(playerFromDB.getString("name"));
            player.setActive(playerFromDB.getBoolean("active"));
            player.setBestScore(playerFromDB.getInt("bestScore"));
        }
        else {
            player.setName(playerFromDB.getString("name"));
            player.setActive(playerFromDB.getBoolean("active"));
            player.setScore(playerFromDB.getInt("score"));
            player.setLevel(playerFromDB.getInt("level"));
            player.setLives(playerFromDB.getInt("lives"));
            player.setBestScore(playerFromDB.getInt("bestScore"));
        }


    }


    public void updatePlayerDataToDB(String name, int level, int score, int lives,
                                     boolean isActive, boolean playerStatus, int bestScore){

        playerData.put("name", name);
        playerData.put("level", level);
        playerData.put("score", score);
        playerData.put("lives", lives);
        playerData.put("active", isActive);
        playerData.put("answer", playerStatus);
        playerData.put("bestScore", bestScore);

        JsonDocument document = JsonDocument.create(name, playerData);
        bucket.upsert(document);
    }

    public void checkBestScore(){
        Player player = context2.getBean(Player.class);

        if(player.getScore() > player.getBestScore()){
            player.setBestScore(player.getScore());
        }

    }


}

