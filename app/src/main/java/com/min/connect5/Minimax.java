package com.min.connect5;

import java.util.ArrayList;


public class Minimax {

    public static int evaluationCount = 0;
    private Board board;
    private static final int winScore = 100000000;



    public Minimax(Board board) {
        this.board = board;
    }

    public static int getWinningScore() {
        return winScore;
    }
    public static double calcBoard(Board board, boolean blacksTurn) {
        evaluationCount++;

        double blackScore = getScore(board, true, blacksTurn);
        double whiteScore = getScore(board, false, blacksTurn);

        if(blackScore == 0)
            blackScore = 1.0;

        return whiteScore / blackScore;

    }
    public static int getScore(Board board, boolean forBlack, boolean blacksTurn) {

        char[][] boardMatrix = board.getBoardMatrix();
        return checkHorizontal(boardMatrix, forBlack, blacksTurn) + checkVertical(boardMatrix, forBlack, blacksTurn) + evaluateDiagonal(boardMatrix, forBlack, blacksTurn);
    }

    public int[] getNextMove(int depth) {
        int[] move = new int[2];
        // Check if any available move can finish the game
        Object[] bestMove = searchWinningMove(board);
        if(bestMove != null ) {
            move[0] = (Integer)(bestMove[1]);
            move[1] = (Integer)(bestMove[2]);

        }
        else {
            // If there is no such move, search the minimax tree with suggested depth.
            bestMove = minimaxSearch(depth, board, true, -1.0, getWinningScore());
            if(bestMove[1] == null) {
                move = null;
            } else {
                move[0] = (Integer)(bestMove[1]);
                move[1] = (Integer)(bestMove[2]);
            }
        }


        evaluationCount=0;

        return move;


    }


    private static Object[] minimaxSearch(int depth, Board board, boolean max, double alpha, double beta) {
        if(depth == 0) {

            Object[] x = {calcBoard(board, !max), null, null};
            return x;
        }

        ArrayList<int[]> allPossibleMoves = board.generateMoves();

        if(allPossibleMoves.size() == 0) {

            Object[] x = {calcBoard(board, !max), null, null};
            return x;
        }

        Object[] bestMove = new Object[3];


        if(max) {
            bestMove[0] = -1.0;
            // Iterate thru all possible moves
            for(int[] move : allPossibleMoves) {
                // Create a temporary board
                Board dummyBoard = new Board(board);
                // Play the move
                dummyBoard.addMove(move[1], move[0], false);

                // call the minimax function  to look for a minimum score.
                Object[] tempMove = minimaxSearch(depth-1, dummyBoard, !max, alpha, beta);

                if((Double)(tempMove[0]) > alpha) {
                    alpha = (Double)(tempMove[0]);
                }

                if((Double)(tempMove[0]) >= beta) {
                    return tempMove;
                }
                if((Double)tempMove[0] > (Double)bestMove[0]) {
                    bestMove = tempMove;
                    bestMove[1] = move[0];
                    bestMove[2] = move[1];
                }
            }

        }
        else {
            bestMove[0] = 100000000.0;
            bestMove[1] = allPossibleMoves.get(0)[0];
            bestMove[2] = allPossibleMoves.get(0)[1];
            for(int[] move : allPossibleMoves) {
                Board dummyBoard = new Board(board);
                dummyBoard.addMove(move[1], move[0], true);

                Object[] tempMove = minimaxSearch(depth-1, dummyBoard, !max, alpha, beta);

                if(((Double)tempMove[0]) < beta) {
                    beta = (Double)(tempMove[0]);
                }

                if((Double)(tempMove[0]) <= alpha) {
                    return tempMove;
                }

                if((Double)tempMove[0] < (Double)bestMove[0]) {
                    bestMove = tempMove;
                    bestMove[1] = move[0];
                    bestMove[2] = move[1];
                }
            }
        }
        return bestMove;
    }

    private static Object[] searchWinningMove(Board board) {
        ArrayList<int[]> allPossibleMoves = board.generateMoves();
        Object[] winningMove = new Object[3];

        // Iterate thru all possible moves
        for(int[] move : allPossibleMoves) {
            evaluationCount++;
            // Create a temporary board
            Board dummyBoard = new Board(board);
            // Play the move
            dummyBoard.addMove(move[1], move[0], false);

            // check if player has winning score
            if(getScore(dummyBoard,false,false) >= winScore) {
                winningMove[1] = move[0];
                winningMove[2] = move[1];
                return winningMove;
            }
        }
        return null;

    }
    public static int checkHorizontal(char[][] boardMatrix, boolean forBlack, boolean playersTurn ) {

        int consecutive = 0;
        int blocks = 2;
        int score = 0;

        for(int i=0; i<boardMatrix.length; i++) {
            for(int j=0; j<boardMatrix[0].length; j++) {
                if(boardMatrix[i][j] == (forBlack ? 'O' : 'X')) {
                    consecutive++;
                }
                else if(boardMatrix[i][j] == '-') {
                    if(consecutive > 0) {
                        blocks--;
                        score += getConsecutiveSetScore(consecutive, blocks, forBlack == playersTurn);
                        consecutive = 0;
                        blocks = 1;
                    }
                    else {
                        blocks = 1;
                    }
                }
                else if(consecutive > 0) {
                    score += getConsecutiveSetScore(consecutive, blocks, forBlack == playersTurn);
                    consecutive = 0;
                    blocks = 2;
                }
                else {
                    blocks = 2;
                }
            }
            if(consecutive > 0) {
                score += getConsecutiveSetScore(consecutive, blocks, forBlack == playersTurn);

            }
            consecutive = 0;
            blocks = 2;

        }
        return score;
    }

    public static  int checkVertical(char[][] boardMatrix, boolean forBlack, boolean playersTurn ) {

        int consecutive = 0;
        int blocks = 2;
        int score = 0;

        for(int j=0; j<boardMatrix[0].length; j++) {
            for(int i=0; i<boardMatrix.length; i++) {
                if(boardMatrix[i][j] == (forBlack ? 'O' : 'X')) {
                    consecutive++;
                }
                else if(boardMatrix[i][j] == '-') {
                    if(consecutive > 0) {
                        blocks--;
                        score += getConsecutiveSetScore(consecutive, blocks, forBlack == playersTurn);
                        consecutive = 0;
                        blocks = 1;
                    }
                    else {
                        blocks = 1;
                    }
                }
                else if(consecutive > 0) {
                    score += getConsecutiveSetScore(consecutive, blocks, forBlack == playersTurn);
                    consecutive = 0;
                    blocks = 2;
                }
                else {
                    blocks = 2;
                }
            }
            if(consecutive > 0) {
                score += getConsecutiveSetScore(consecutive, blocks, forBlack == playersTurn);

            }
            consecutive = 0;
            blocks = 2;

        }
        return score;
    }

    public static  int evaluateDiagonal(char[][] boardMatrix, boolean forBlack, boolean playersTurn ) {

        int consecutive = 0;
        int blocks = 2;
        int score = 0;
        // From bottom-left to top-right diagonally
        for (int k = 0; k <= 2 * (boardMatrix.length - 1); k++) {
            int iStart = Math.max(0, k - boardMatrix.length + 1);
            int iEnd = Math.min(boardMatrix.length - 1, k);
            for (int i = iStart; i <= iEnd; ++i) {
                int j = k - i;

                if(boardMatrix[i][j] == (forBlack ? 'O' : 'X')) {
                    consecutive++;
                }
                else if(boardMatrix[i][j] == '-') {
                    if(consecutive > 0) {
                        blocks--;
                        score += getConsecutiveSetScore(consecutive, blocks, forBlack == playersTurn);
                        consecutive = 0;
                        blocks = 1;
                    }
                    else {
                        blocks = 1;
                    }
                }
                else if(consecutive > 0) {
                    score += getConsecutiveSetScore(consecutive, blocks, forBlack == playersTurn);
                    consecutive = 0;
                    blocks = 2;
                }
                else {
                    blocks = 2;
                }

            }
            if(consecutive > 0) {
                score += getConsecutiveSetScore(consecutive, blocks, forBlack == playersTurn);

            }
            consecutive = 0;
            blocks = 2;
        }
        // From top-left to bottom-right diagonally
        for (int k = 1-boardMatrix.length; k < boardMatrix.length; k++) {
            int iStart = Math.max(0, k);
            int iEnd = Math.min(boardMatrix.length + k - 1, boardMatrix.length-1);
            for (int i = iStart; i <= iEnd; ++i) {
                int j = i - k;

                if(boardMatrix[i][j] == (forBlack ? 'O' : 'X')) {
                    consecutive++;
                }
                else if(boardMatrix[i][j] == '-') {
                    if(consecutive > 0) {
                        blocks--;
                        score += getConsecutiveSetScore(consecutive, blocks, forBlack == playersTurn);
                        consecutive = 0;
                        blocks = 1;
                    }
                    else {
                        blocks = 1;
                    }
                }
                else if(consecutive > 0) {
                    score += getConsecutiveSetScore(consecutive, blocks, forBlack == playersTurn);
                    consecutive = 0;
                    blocks = 2;
                }
                else {
                    blocks = 2;
                }

            }
            if(consecutive > 0) {
                score += getConsecutiveSetScore(consecutive, blocks, forBlack == playersTurn);

            }
            consecutive = 0;
            blocks = 2;
        }
        return score;
    }

    public static  int getConsecutiveSetScore(int count, int blocks, boolean currentTurn) {
        final int winGuarantee = 1000000;
        if(blocks == 2 && count < 5)
            return 0;
        switch(count) {
            case 5: {
                return winScore;
            }
            case 4: {
                if(currentTurn)
                    return winGuarantee;
                else {
                    if(blocks == 0)
                        return winGuarantee/4;
                    else
                        return 200;
                }
            }
            case 3: {
                if(blocks == 0) {
                    if(currentTurn)
                        return 50000;
                    else
                        return 200;
                }
                else {
                    if(currentTurn)
                        return 10;
                    else
                        return 5;
                }
            }
            case 2: {
                if(blocks == 0) {
                    if(currentTurn)
                        return 7;
                    else
                        return 5;
                }
                else {
                    return 3;
                }
            }
            case 1: {
                if(blocks == 0) {
                    if(currentTurn)
                        return 5;
                    else
                        return 3;
                }
                else {
                    return 2;
                }
            }
        }
        return winScore*2;
    }
}