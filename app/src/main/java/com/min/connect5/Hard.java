package com.min.connect5;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class Hard extends AppCompatActivity {


    final static int maxN = 15;
    private static ImageView[][] cell = new ImageView[maxN][maxN];

    private Drawable[] drawCell = new Drawable[4]; //0 is empty, 1 is player, 2 is bot, 3 is background
    private Context context;

    private int[][] valueCell = new int[maxN][maxN]; // 0 is empty, 1 is player 2 is bot
    private int winner_play;
    private boolean firstMove;
    private int turnPlay;
    private int xMove, yMove;
    private boolean isClicked;

    TextView prompt;


    private boolean gameFinished = false;

    Board board;
    Game game;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        board = new Board();
        game = new Game(board);
        game.setMinimaxDepth(4);
        context = this;
        gameFinished = false;

        prompt = findViewById(R.id.prompt);

        loadResources();
        designBoardGame();
        init_game();
        play_game();

        Button home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Button restart = findViewById(R.id.restart);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                board = new Board();
                game = new Game(board);
                game.setMinimaxDepth(1);
                gameFinished = false;
                prompt = findViewById(R.id.prompt);
                String str = "";
                prompt.setText(str);
                init_game();
                play_game();
            }
        });

    }


    private void init_game(){
        firstMove = true;
        winner_play = 0;
        for(int i=0; i<maxN; i++){
            for(int j=0; j<maxN; j++){
                cell[i][j].setImageDrawable(drawCell[0]);
                valueCell[i][j] = 0;
            }
        }
    }

    Runnable updater;
    private void play_game(){
        Random r = new Random();
        turnPlay = r.nextInt(2) + 1;

        if(turnPlay == 1){ // player first
            Toast.makeText(context, "Player play first", Toast.LENGTH_SHORT).show();
            playerTurn();
            firstMove = false;
        }
        else{
            Toast.makeText(context, "Bot play first", Toast.LENGTH_SHORT).show();
            botTurn();
        }

    }


    public void botTurn(){
        if(firstMove){
            firstMove = false;
            xMove = 7;
            yMove = 7;
            make_a_move();
            game.saveComputerMove(xMove, yMove);
            valueCell[xMove][yMove] = 2;
        }
        else{
            //find best move
            int[] aiMove = game.getAIMove();
            xMove = aiMove[1];
            yMove = aiMove[0];
            game.saveComputerMove(xMove, yMove);
            if(!gameFinished) {
                make_a_move();


                final Handler timerHandler = new Handler();
                updater = new Runnable() {
                    @Override
                    public void run() {
                        timerHandler.postDelayed(updater,1000);

                    }
                };
                timerHandler.post(updater);
            }
        }
    }

    private void playerTurn(){
        firstMove = false;
        isClicked = false;
    }

    private void make_a_move(){
        cell[xMove][yMove].setImageDrawable(drawCell[turnPlay]);
        valueCell[xMove][yMove] = turnPlay;

        checkWinner();


        if(turnPlay == 1){ // player
            firstMove = false;
            turnPlay=(1+2)-turnPlay;
            botTurn();

        }else{ // bot
            firstMove = false;
            turnPlay=(1+2)-turnPlay;
            playerTurn();

        }
    }

    private void checkWinner(){
        int winner = game.checkWinner();
        Log.d("msg", ""+game.checkWinner());
        String[] playerWin = {"You barely won", "You Won!", "Was it too easy for you?", "Didn't expect you'd win", "Good job"};
        String[] botWin = {"Loser", "You lose", "Was I too hard for you?", "Too easy", "I'll go easy on you next game"};
        Random random = new Random();


        int i = random.nextInt(5);
        if(noEmptyCell()){
            prompt.setText("It's a draw!");
            gameFinished = true;
        }
        else if(winner == 1){ // bot win
            String str = botWin[i];
            prompt.setText(str);
            gameFinished = true;
        }
        else if(winner == 2){ // player win
            String str = playerWin[i];
            prompt.setText(str);
            gameFinished = true;
        }
    }

    private boolean noEmptyCell(){
        for(int i=0; i<maxN; i++){
            for(int j=0; j<maxN; j++){
                if(valueCell[i][j] == 0)
                    return false;
            }
        }
        return true;
    }

    private void loadResources(){
        drawCell[3]  = context.getResources().getDrawable(R.drawable.cell_bg); // background
        drawCell[0] = null;
        drawCell[1] = context.getResources().getDrawable(R.drawable.o); // user
        drawCell[2] = context.getResources().getDrawable(R.drawable.x); // computer
    }


    private void designBoardGame(){
        int sizeOfCell = Math.round(screenWidth()/maxN);
        LinearLayout.LayoutParams row = new LinearLayout.LayoutParams(sizeOfCell*maxN,sizeOfCell);
        LinearLayout.LayoutParams lpCell = new LinearLayout.LayoutParams(sizeOfCell, sizeOfCell);

        LinearLayout linBoardGame = (LinearLayout) findViewById(R.id.linBoardGame);

        for(int i=0; i<maxN; i++){
            LinearLayout linRow = new LinearLayout(context);

            for(int j=0; j<maxN; j++){

                cell[i][j] = new ImageView(context);
                cell[i][j].setBackground(drawCell[3]);

                final int x=i;
                final int y=j;

                cell[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(valueCell[x][y] == 0) {
                            if (turnPlay == 1 || !isClicked) { // if player's turn
                                isClicked = true;
                                xMove = x;
                                yMove = y;
                                game.savePlayerMove(xMove, yMove);

                                if(!gameFinished) {

                                    make_a_move();

                                }

                            }
                        }
                    }
                });
                linRow.addView(cell[i][j], lpCell);
            }
            linBoardGame.addView(linRow, row);
        }
    }

    private float screenWidth(){
        Resources res = context.getResources();
        DisplayMetrics d = res.getDisplayMetrics();
        return d.widthPixels;
    }
}
