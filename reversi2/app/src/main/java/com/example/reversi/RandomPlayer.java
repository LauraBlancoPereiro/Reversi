
package com.example.reversi;
public class RandomPlayer {

    public RandomPlayer() {}

    public String randomSelection(String options){

        String randomOption;
        String[] optionsAux = options.split(",");
        int randNum = (int)(Math.random()*optionsAux.length);
        randomOption = optionsAux[randNum];
        System.out.println("La eleccion del Random Player es: " + randomOption);
        return randomOption;

    }
    
}