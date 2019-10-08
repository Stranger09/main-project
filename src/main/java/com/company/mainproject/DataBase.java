package com.company.mainproject;

import com.couchbase.client.java.*;
import com.couchbase.client.java.document.*;
import com.couchbase.client.java.document.json.*;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import com.couchbase.client.java.query.*;
import com.couchbase.client.java.query.dsl.Sort;

import java.util.List;

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


    public void addPlayerToDB(String name, int level, int scores, int lives, boolean isActive, boolean playerStatus) {



        playerData.put("name", name);
        playerData.put("level", level);
        playerData.put("score", scores);
        playerData.put("lives", lives);
        playerData.put("active", isActive);
        playerData.put("answer", playerStatus);
        JsonDocument document = JsonDocument.create(name, playerData);
        bucket.insert(document);
    }


    public boolean checkNameInDB(String name) {

        boolean existDoc = bucket.exists(name);

        if (!existDoc) return false;
        else return true;
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
        Player player = new Player();
        return player.getPlayerFromJSON(bucket.get(name).content());
    }


    public void updatePlayerDataToDB(String name, int level, int score, int lives, boolean isActive, boolean playerStatus){


        playerData.put("name", name);
        playerData.put("level", level);
        playerData.put("score", score);
        playerData.put("lives", lives);
        playerData.put("active", isActive);
        playerData.put("answer", playerStatus);

        JsonDocument document = JsonDocument.create(name, playerData);
        bucket.upsert(document);
    }


}

