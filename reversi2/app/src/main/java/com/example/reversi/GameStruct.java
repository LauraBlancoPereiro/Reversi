
package com.example.reversi;
import java.util.ArrayList;

// struct con las salidas para la IA

public class GameStruct {

    int myTyles;
    int opponentTyles;
    boolean gameOver;
    ArrayList<String> possibleMovements;
    ArrayList<String> movementsDone;
    boolean MyTurn;
    boolean squarePosition;
    boolean borderPosition;

    public GameStruct(int mytyles, int opponenttyles, ArrayList<String> possibleMovements,
                      ArrayList<String> movementsDone, boolean myturn, boolean gameOver, boolean SP, boolean BP) {

        this.myTyles = mytyles;
        this.opponentTyles = opponenttyles;
        this.possibleMovements = possibleMovements;
        this.movementsDone = movementsDone;
        this.MyTurn = myturn;
        this.gameOver = gameOver;
        squarePosition = SP;
        borderPosition = BP;
        //this.movementsDone = movementsDone;

    }
    
    public void imprimeGameStruct() {
        
        String coordenadas = "";
        System.out.println("myTyles: " + myTyles);
        System.out.println("opponentTyles: " + opponentTyles);
        System.out.println("myTurn: " + MyTurn);
        
        for(int i = 0; i < possibleMovements.size(); i++) {
            coordenadas = coordenadas + "(";
            for(int w = 0; w < 2; w++) {
                coordenadas = coordenadas + possibleMovements.get(i).charAt(w);
            }
            coordenadas = coordenadas + "),";
        }
        
        System.out.println("possibleMovements posicion en el array tablero[][](XY): " + coordenadas);
        /*coordenadas = "";
        for(int i = 0; i < movementsDone.size(); i++) {
            coordenadas = coordenadas + "(";
            for(int w = 0; w < 2; w++) {
                coordenadas = coordenadas + movementsDone.get(i).charAt(w);
            }
            coordenadas = coordenadas + "),";
        }

        System.out.println("movementsDone: " + coordenadas);*/
    }

}
