package com.company.mainproject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

import static com.company.mainproject.AppContext.context2;

@Controller
public class GameController {



    @GetMapping("/")
    public String greeting() {
        return "main";
    }



    @PostMapping(path = "/setUser")
    public String setUser(
            @RequestParam(name = "name", required = true, defaultValue = "undefined") String name,
            Map<String, Object> model
    ) {
        Game game = context2.getBean(Game.class);
        Player player = context2.getBean(Player.class);
        game.setPlayerName(name);
        // TODO: 01.10.2019  check and save user
        // maybe set user id in cache

        model.put("name", player.getName());
        model.put("level", player.getLevel());
        model.put("score", player.getScore());
        model.put("lives", player.getLives());

        return "game";
    }


}

