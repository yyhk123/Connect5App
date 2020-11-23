package com.min.connect5;

import java.util.*;

public class Board {

    int n = 15;
    private char[][] boardMatrix;

    ArrayList<int[]> moveList;

    public Board() {
        this.boardMatrix = new char[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.boardMatrix[i][j] = '-';
            }
        }
    }

    // copies boardMatrix
    public Board(Board board) {
        char[][] matrixToCopy = board.getBoardMatrix();
        boardMatrix = new char[matrixToCopy.length][matrixToCopy.length];
        for(int i=0;i<matrixToCopy.length; i++) {
            for(int j=0; j<matrixToCopy.length; j++) {
                boardMatrix[i][j] = matrixToCopy[i][j];
            }
        }
    }

    public boolean addMove(int posX, int posY, boolean black) {
        // Check for empty cell
        if(boardMatrix[posY][posX] != '-')
            return false;

        boardMatrix[posY][posX] = black ? 'O' : 'X';
        return true;
    }
    public char[][] getBoardMatrix() {

        return boardMatrix;
    }


    public ArrayList<int[]> generateMoves() {
        moveList = new ArrayList<int[]>();

        int boardSize = boardMatrix.length;

        // Look for any adjcent cell
        for(int i=0; i<boardSize; i++) {
            for(int j=0; j<boardSize; j++) {
                if(boardMatrix[i][j] != '-')
                    continue;

                if(i > 0) {
                    if(j > 0) {
                        if(boardMatrix[i-1][j-1] != '-' ||
                                boardMatrix[i][j-1] != '-' ) {
                            int[] move = {i,j};
                            moveList.add(move);
                            continue;
                        }
                    }
                    if(j < boardSize-1) {
                        if(boardMatrix[i-1][j+1] != '-'  ||
                                boardMatrix[i][j+1] != '-' ) {
                            int[] move = {i,j};
                            moveList.add(move);
                            continue;
                        }
                    }
                    if(boardMatrix[i-1][j] != '-' ) {
                        int[] move = {i,j};
                        moveList.add(move);
                        continue;
                    }
                }

                if( i < boardSize-1) {
                    if(j > 0) {
                        if(boardMatrix[i+1][j-1] != '-'  ||
                                boardMatrix[i][j-1] != '-' ) {
                            int[] move = {i,j};
                            moveList.add(move);
                            continue;
                        }
                    }
                    if(j < boardSize-1) {
                        if(boardMatrix[i+1][j+1] != '-'  ||
                                boardMatrix[i][j+1] != '-' ) {
                            int[] move = {i,j};
                            moveList.add(move);
                            continue;
                        }
                    }
                    if(boardMatrix[i+1][j] != '-' ) {
                        int[] move = {i,j};
                        moveList.add(move);
                        continue;
                    }
                }

            }
        }
        return moveList;
    }


}

