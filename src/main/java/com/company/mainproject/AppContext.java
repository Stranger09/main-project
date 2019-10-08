package com.company.mainproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppContext {

    public static ApplicationContext context2;

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        context2 = applicationContext;
    }

    @Bean(initMethod = "resizeDirectoryImages")
    public JavaImageResizer javaImageResizer() {
        return new JavaImageResizer();
    }

    @Bean
    public StepResult stepResult() {
        return new StepResult();
    }

    @Bean
    public Game game() {
        return new Game();
    }

    @Bean
    public Card card() {
        return new Card();
    }


    @Bean
    public Player player() {
        return new Player();
    }

    @Bean
    public DataBase dataBase(){
        return new DataBase();
    }
}
