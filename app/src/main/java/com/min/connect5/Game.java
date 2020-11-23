package com.min.connect5;

public class Game {

    private Board board;
    private int minimaxDepth; // set minimax depth.
    private Minimax ai;

    private int[] aiMove;

    MainActivity main;


    public Game(Board board) {
        this.board = board;
        ai = new Minimax(board);
        main = new MainActivity();
    }

    public void setMinimaxDepth(int i){
        minimaxDepth = i;
    }

    private int getMinimaxDepth(){
        return minimaxDepth;
    }

    public void saveComputerMove(int x, int y){
        makeMove(x, y, false);
    }

    public void savePlayerMove(int x, int y){
        makeMove(x, y, true);
    }

    public boolean makeMove(int posX, int posY, boolean black) {
        return board.addMove(posX, posY, black);
    }

    public int checkWinner() {
        if(Minimax.getScore(board, true, false) >= Minimax.getWinningScore())
            return 2;
        if(Minimax.getScore(board, false, true) >= Minimax.getWinningScore())
            return 1;
        return 0;
    }

    public int[] getAIMove(){
        int i = getMinimaxDepth();
        aiMove = ai.getNextMove(i);
        return aiMove;
    }


}

