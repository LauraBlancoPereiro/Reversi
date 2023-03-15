package com.example.reversi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private Button[][] buttons = new Button[6][6];
    Integer[][] buttonsI = new Integer[6][6];
    private TextView white,black,grey, board;
    private int whiteTiles, blackTiles;
    private final Logic logic = new Logic(2);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.initialpage);

        buttonsI = new Integer[][]{
                {R.id.b00,R.id.b01,R.id.b02,R.id.b03,R.id.b04,R.id.b05},
                {R.id.b10,R.id.b11,R.id.b12,R.id.b13,R.id.b14,R.id.b15},
                {R.id.b20,R.id.b21,R.id.b22,R.id.b23,R.id.b24,R.id.b25},
                {R.id.b30,R.id.b31,R.id.b32,R.id.b33,R.id.b34,R.id.b35},
                {R.id.b40,R.id.b41,R.id.b42,R.id.b43,R.id.b44,R.id.b45},
                {R.id.b50,R.id.b51,R.id.b52,R.id.b53,R.id.b54,R.id.b55}
        };

    }

    private void playAI(){
        while (logic.getPlayerTurn() == 2 && !logic.getBResult()) {
            logic.playTurn();
            setBoard();
            setNumberOfTiles();

        }
        setTurnPlayer();
        setResult();
    }

    private void setBoard() {//0 --> vacio, 1--> negro, 2 --> blanco, 8 --> posible
        Cell[][] board = logic.getBoard();

        for (int i = 0; i < board.length; i++) {//filas
            for (int j = 0; j < board[i].length; j++) {//columnas
                System.out.print(board[i][j].getEstado() + " ");
                if (j == board.length - 1) {
                    System.out.println("");
                }
                if (board[i][j].getEstado() == 1) {//negro
                    Button b = (Button) findViewById(buttonsI[i][j]);
                    b.setBackgroundResource(R.drawable.black);
                } else if (board[i][j].getEstado() == 2) {
                    Button b = (Button) findViewById(buttonsI[i][j]);
                    b.setBackgroundResource(R.drawable.white);
                } else if (board[i][j].getEstado() == 8) {
                    Button b = (Button) findViewById(buttonsI[i][j]);
                    b.setBackgroundResource(R.drawable.grey);
                } else if (board[i][j].getEstado() == 0){
                    Button b = (Button) findViewById(buttonsI[i][j]);
                    b.setBackgroundResource(R.drawable.board);
                }
            }
        }
    }

    private void setNumberOfTiles(){
        white = (TextView) findViewById(R.id.white);
        white.setText("" + logic.getWhiteTiles());

        black = (TextView) findViewById(R.id.black);
        black.setText("" + logic.getBlackTiles());
    }

    //Cuando pulsamos un boton cargamos en v el id del boton pulsado
    public void putTile(View v){

        int button = Arrays.asList(buttons).indexOf(v.getId());
        String buttonS = v.toString().split(",")[2].split("/")[1].split("b")[1];
        System.out.println("The selected button is: " + buttonS);
        if(!logic.setPlayerPlay(buttonS) || logic.getPlayerTurn() != 1 || logic.getBResult()) return;
        logic.playTurn();
        setBoard();
        setNumberOfTiles();
        setTurnPlayer();
        setResult();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    playAI();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        Thread hilo = new Thread(runnable);
        hilo.start();
    }

    private void setResult(){
        if(logic.getBResult()){
            TextView result = (TextView) findViewById(R.id.turn);
            result.setText("The result is: " + logic.getResult());
        }
    }

    private void setTurnPlayer(){
        if(logic.getPlayerTurn() == 1 ){
            TextView result = (TextView) findViewById(R.id.turn);
            result.setText("Your Turn");
        }else{
            TextView result = (TextView) findViewById(R.id.turn);
            result.setText("AI Turn");
        }
    }

    public void iniciar(View v) {
        setContentView(R.layout.activity_main);
        setBoard();
        setNumberOfTiles();
    }

    public void exit(){
        finish();
    }

    public void help(View v){
        setContentView(R.layout.helppage);
    }

    public void inicio(View v){
        setContentView(R.layout.initialpage);

    }

    public void reset(View v){
        logic.reset();
        setBoard();
        setNumberOfTiles();
        TextView result = (TextView) findViewById(R.id.turn);
        result.setText("");
        setTurnPlayer();
    }
}