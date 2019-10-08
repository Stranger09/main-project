package com.company.mainproject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

import static com.company.mainproject.AppContext.context2;

@RestController
public class CartController {

    @GetMapping(value = "/getStartCards", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getStartCards() {
        Game game = context2.getBean(Game.class);
        return game.getCards();
    }

    @PostMapping(value = "/makeStep")
    public String makeStep(@RequestBody String jsonString) throws IOException {
        Game game = context2.getBean(Game.class);
        return game.makeStep(jsonString);
    }
}
