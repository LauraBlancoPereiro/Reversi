package com.example.reversi;

// Java program to demonstrate
// working of Alpha-Beta Pruning
import java.io.*;
import java.util.ArrayList;

public class AI{
 
    // Initial values of
    // Alpha and Beta
    private static int MAX = 10000; //infinity --> beta
    private static int MIN = -MAX;  //-infinity --> alfa
    private static int DEPTH = 6;   //max depth
    private static int SQ = 100;
    private static int BP = 30;
    private static int GO = 1000;
    //Logic
    private Logic logic = new Logic(1);
    //pssibleMovents
    private String[] possibleMovements;
    //MovementsMade
    private ArrayList<String> movementsMade = new ArrayList<String>(); //ArrayList para los movimientos hechos en el juego, decisiones tomadas.
    private String selectedMovement = "";
    private int sizeMovementsMade;

    public AI(){}

    //función para llamar desde la lógica para obtener el movimiento de la IA
    //devuelve una String que es el movimiento seleccionado.
    public String getMovement(String possibleMovements){

        sizeMovementsMade =  movementsMade.size();
        this.possibleMovements = possibleMovements.split(",");
        if(possibleMovements.length() == 1){
            //System.out.println("AI Solo hay un movimiento posible");
            return this.possibleMovements[0];
        }else {
            //System.out.println("AI Voy a hacer el alfa beta prunning");
            ArrayList<String> mmm = new ArrayList<>();
            mmm.addAll(movementsMade);
            valueMovement vM = minimax(0, 2, MIN, MAX, mmm);
            //System.out.println("AI La longitud de vM.movements es: " + vM.ALMovements.size());
            //System.out.println("AI " +vM.ALMovements);
            //System.out.println("AI el index a pillar es:" + sizeMovementsMade);
            selectedMovement =  vM.ALMovements.get(sizeMovementsMade);
            movementsMade.add(selectedMovement);
            //System.out.println("AI El movimiento seleccionado es: " + selectedMovement);
            return selectedMovement;
        }
    }


    public void setHumanPlay(String play){
        play = "" + play.charAt(0) + play.charAt(1);
        //System.out.println("AI La longitud de movementsMade es: " + movementsMade.size());
        movementsMade.add(play);
        //System.out.println("AI La longitud de movementsMade es: " + movementsMade.size());
    }

    // Returns optimal value for
    // current player (Initially called
    // for root and maximizer)
    private valueMovement minimax(int depth, int maximizingPlayer, int alpha,int beta, ArrayList<String> m){
         //System.out.println("AI Los movimientos hechos son: " + m);
         ArrayList<String> m2 = new ArrayList<String>();
         m2.addAll(m);
        // Terminating condition. i.e
        // leaf node is reached
        //llamar función para obtener estado del tablero
        //llamar función evaluar, y si hemos llegado la profuncidad maxima devolvemos el valor.
        GameStruct GM = logic.futurosMovimientos(m2);
        //System.out.println("AI Los movimientos posibles son: " + GM.possibleMovements);

        if (depth == DEPTH || GM.gameOver){
            //System.out.println("AI Llegué al final de la profundidad, o el juego acabó");
            valueMovement vM = new valueMovement(evaluationFunction(GM), m2);
            //System.out.println("AI al llegar al final el VALUE es: " + vM.value + " y los movements son: " + vM.ALMovements);
            return vM;
        }

        if (maximizingPlayer == 2){//maximize my play

            int best = MIN;
            valueMovement bestStruct =  new valueMovement(best, m2);
            // Recur for left and
            // right children
            int index = m.size();
            for (int i = 0; i < GM.possibleMovements.size() ; i++){
                m2.add(GM.possibleMovements.get(i));
                //System.out.println("Los movimientos posibles que le paso a la funcion son: " + m2) ;
                valueMovement val = minimax(depth + 1,1, alpha, beta, m2);
                //System.out.println("EL VAL QUE ME DEVUELVE LA FUNCIONS MINIMAX ES: " + val.value +" y los mov son: " + val.ALMovements);
                //System.out.println("EL BEST ES Max: " + bestStruct.value);
                bestStruct.value = Math.max(bestStruct.value, val.value);
                //System.out.println("EL BEST ES Max: " + bestStruct.value);
                alpha = Math.max(alpha, bestStruct.value);
                if(bestStruct.value == val.value){
                    bestStruct = new valueMovement(val.value, val.ALMovements);
                    //System.out.println("La bestStruct es con value: " +bestStruct.value +" y movs: " + bestStruct.ALMovements);
                }

                m2.remove(m2.size() - 1);
                //System.out.println("Los movimientos posibles que le paso a la funcion son: " + m2) ;
                //System.out.println("La bestStruct es con value: " +bestStruct.value +" y movs: " + bestStruct.ALMovements);
                // Alpha Beta Pruning
                if (beta <= alpha) {
                    //System.out.println("ALfaBetaPrunning");
                    break;
                }


            }
            //System.out.println("////////////////////////////////////////////////////////////////////////////////");
            //System.out.println("La logintud del mejor movimiento MAX es: " + bestStruct.ALMovements.size() + " Los movimientos son: " + bestStruct.ALMovements);
            return bestStruct;

        }else{//minimize my opponent’s play

            int best = MAX;
            valueMovement bestStruct =  new valueMovement(best, m2);
            // Recur for left and
            // right children
            int index = m.size();
            for (int i = 0; i < GM.possibleMovements.size() ; i++){
                m2.add(GM.possibleMovements.get(i));
                valueMovement val = minimax(depth + 1,2, alpha, beta, m2);
                //System.out.println("EL VAL QUE ME DEVUELVE LA FUNCIONS MINIMAX ES: " + val.value +" y los mov son: " + val.ALMovements);
                //System.out.println("EL BEST ES Min: " + bestStruct.value);
                bestStruct.value = Math.min(bestStruct.value, val.value);
                //System.out.println("EL BEST ES Min: " + bestStruct.value);
                beta = Math.min(beta, bestStruct.value);
                if(bestStruct.value == val.value) {
                    bestStruct = new valueMovement(bestStruct.value, val.ALMovements);
                    //System.out.println("La bestStruct es con value: " +bestStruct.value +" y movs: " + bestStruct.ALMovements);
                }
                m2.remove(m2.size() - 1);
                //System.out.println("Los movimientos posibles que le paso a la funcion son: " + m) ;
                //System.out.println("La bestStruct es con value: " +bestStruct.value +" y movs: " + bestStruct.ALMovements);
                // Alpha Beta Pruning
                if (beta <= alpha) {
                    //System.out.println("ALfaBetaPrunning");
                    break;
                }

            }
            //System.out.println("////////////////////////////////////////////////////////////////////////////////");
            //System.out.println("La logintud del mejor movimiento MIN es: " + bestStruct.ALMovements.size() + " Los movimientos son: " + bestStruct.ALMovements);
            return bestStruct;
        }
    }

    private int evaluationFunction(GameStruct gameVariables){// Basic evaluation function
        int value = (gameVariables.myTyles - gameVariables.opponentTyles)*3;
        if(logic.squarePosition(gameVariables.movementsDone.get(sizeMovementsMade))){
            //System.out.println("Suma 1");
            value += (SQ * 2);
        }
        if(logic.borderPosition(gameVariables.movementsDone.get(sizeMovementsMade))){
            //System.out.println("Suma 2");
            value += (BP * 2);

        }
        if(gameVariables.gameOver){
            if(gameVariables.MyTurn) value -= GO;
            else value += GO;
        }
        if(gameVariables.squarePosition){
            //System.out.println("Suma 3");
            if(gameVariables.MyTurn) value -= SQ;
            else value += SQ;
        }else{
            if(gameVariables.borderPosition){
                //System.out.println("Suma 4");
                if(gameVariables.MyTurn) value -= BP;
                else value += BP;
            }
        }

        return value;

    }

    private class valueMovement{
        public int value;
        public ArrayList<String> ALMovements;

        valueMovement(int v, ArrayList<String> alm){
            value = v;
            ALMovements = alm;
        }
    }
 

}