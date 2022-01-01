package Controller;

import Model.*;
import Model.Chess.*;
import View.Display;

import java.util.List;

/*
 * Main controller for the game to handle all
 * actions needed.
 * */
public class CHSGameManager {
    Model.Chess.CHSBoard CHSBoard;
    CHSPlayer human;
    CHSPlayer agent;

    Display view;
    boolean turn;

    public CHSGameManager() {
        this.human = new CHSPlayer(0);
        this.agent = new CHSPlayer(1);

        this.CHSBoard = new CHSBoard(this.human, this.agent);

        this.turn = true;
        this.view = new Display(this.CHSBoard);

    }

    /**
     * This function begins the game and houses the main
     * turn checking functions to give each player a turn.
     * */
    public void startGame() {
        boolean isPlaying = true;
        boolean stalemate = false;
        initiateChess();

        //This is the main loop to check through each move.
        while (isPlaying) {
            //Print ● for each valid tile
            view.printBoardCHS();
            String pSelect = null;
            String mTile = null;
            CHSPiece pUse = null;
            
            while (pUse == null) {
                pSelect = view.prompt("Choose a piece by notation");
                pUse = this.human.getPiece(pSelect);
                if (pUse != null && !CHSBoard.getValidMove(pUse))
                    pUse = null;
            }

            //Checks if piece exists and has a valid move to play.
            if (CHSBoard.getValidMove(pUse)) {
                boolean canMove = false;

                while (!canMove) {
                    mTile = view.prompt("Choose a valid tile to move ("+pUse.getCoords().toStringC()+")");

                    //Checks if input is real and is a valid tile to step onto
                    if (mTile != null && CHSBoard.getValidMove(pUse, new Point(mTile))) {
                        //This is the default move without capture. The condition
                        //is to avoid a wrong move in a force jump.
                        boolean capture = CHSBoard.movePiece(pUse, new Point(mTile));
                        canMove = true;

                    }
                }

            }

            //Piece promotion
            if (pUse.getType() == 1 && pUse.getCoords().equals(new Point(pUse.getCoords().getX(),7))) {
                int type = 1;
                while (type==1) {
                    pSelect = view.prompt("Promote your pawn (N - Knight | B - Bishop | R - Rook | Q - Queen");
                    switch (pSelect) {
                        case "N" -> type = 2;
                        case "B" -> type = 3;
                        case "R" -> type = 4;
                        case "Q" -> type = 5;
                    }
                }
                this.CHSBoard.promotePiece(pUse,type);
            }

            //AI Utility Calculations
            CHSTree enemyUtilitySystem = new CHSTree(this.CHSBoard);
            CHSNode path = null;
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }

            //Checks for valid moves for agent before calculating
            List<CHSPiece> aCHSPieces = CHSBoard.getPieces("Agent");
            int numMoves = 0;
            for (CHSPiece CHSPiece : aCHSPieces) {
                List<Point> validM = CHSBoard.validMoves(CHSPiece);
                if (validM != null) numMoves++;
            }

            //This statement checks if agent has no possible moves to continue the game
            if (numMoves == 0) {
                //Stalemate
                isPlaying = false;
                if (!CHSBoard.isAttackedBy(CHSBoard.getAgent().getKing().getCoords(),"Human"))
                    stalemate = true;
            }
            else {
                enemyUtilitySystem.prepareTreeAtDepth(3);
                path = enemyUtilitySystem.minimax(enemyUtilitySystem.getRoot());
                this.CHSBoard = path.getBoard();
            }
            this.human = CHSBoard.getHuman();
            this.agent = CHSBoard.getAgent();

            view.setBoard(this.CHSBoard);
            view.sethMove(mTile);



            if (isPlaying) {
                view.setaMove(path.getDest());
                view.sethValue(enemyUtilitySystem.getHeuristics());
            }
            else {
                if (stalemate)
                    view.setaMove("Stalemate.");
                else
                    view.setaMove("Looks like that's check, you win.");
                view.sethValue(-9999);
            }

            //Checks if player has any moves after this
            List<CHSPiece> hCHSPieces = CHSBoard.getPieces("Human");
            int humMoves = 0;
            for (CHSPiece CHSPiece : hCHSPieces) {
                List<Point> validM = CHSBoard.validMoves(CHSPiece);
                if (validM != null) humMoves++;
            }

            if (humMoves == 0) {
                isPlaying = false;
                if (!CHSBoard.isAttackedBy(CHSBoard.getHuman().getKing().getCoords(),"Agent"))
                    stalemate = true;
            }
            CHSBoard.nextMove();
        }

        view.printBoardCHS();

    }

    /**
     * This function helps set the board for a chess
     * game. It creates references for the new pieces
     * for each player in terms of chess.
     * */
    public void initiateChess() {
        CHSPiece[] hCHSPieces = new CHSPiece[16];
        CHSPiece[] aCHSPieces = new CHSPiece[16];
        int pieceCounter = 0;

        //Player Pieces
        //Pawns
        for (int x = 0; x < 8; x++) {
            hCHSPieces[pieceCounter] = new CHSPiece(this.human, new Point(x,1), 1);
            pieceCounter++;
        }
        //Knights
        hCHSPieces[pieceCounter] = new CHSPiece(this.human, new Point(1,0),2);
        pieceCounter++;

        hCHSPieces[pieceCounter] = new CHSPiece(this.human, new Point(6,0),2);
        pieceCounter++;

        //Bishops
        hCHSPieces[pieceCounter] = new CHSPiece(this.human, new Point(2,0),3);
        pieceCounter++;

        hCHSPieces[pieceCounter] = new CHSPiece(this.human, new Point(5,0),3);
        pieceCounter++;

        //Rooks
        hCHSPieces[pieceCounter] = new CHSPiece(this.human, new Point(0,0),4);
        pieceCounter++;

        hCHSPieces[pieceCounter] = new CHSPiece(this.human, new Point(7,0),4);
        pieceCounter++;

        //Queen
        hCHSPieces[pieceCounter] = new CHSPiece(this.human, new Point(3,0),5);
        pieceCounter++;

        //King
        hCHSPieces[pieceCounter] = new CHSPiece(this.human, new Point(4,0),0);

        pieceCounter = 0;
        //Agent Pieces
        //Pawns
        for (int x = 0; x < 8; x++) {
            aCHSPieces[pieceCounter] = new CHSPiece(this.agent, new Point(x,6), 1);
            pieceCounter++;
        }
        //Knights
        aCHSPieces[pieceCounter] = new CHSPiece(this.agent, new Point(1,7),2);
        pieceCounter++;

        aCHSPieces[pieceCounter] = new CHSPiece(this.agent, new Point(6,7),2);
        pieceCounter++;

        //Bishops
        aCHSPieces[pieceCounter] = new CHSPiece(this.agent, new Point(2,7),3);
        pieceCounter++;

        aCHSPieces[pieceCounter] = new CHSPiece(this.agent, new Point(5,7),3);
        pieceCounter++;

        //Rooks
        aCHSPieces[pieceCounter] = new CHSPiece(this.agent, new Point(0,7),4);
        pieceCounter++;

        aCHSPieces[pieceCounter] = new CHSPiece(this.agent, new Point(7,7),4);
        pieceCounter++;

        //Queen
        aCHSPieces[pieceCounter] = new CHSPiece(this.agent, new Point(3,7),5);
        pieceCounter++;

        //King
        aCHSPieces[pieceCounter] = new CHSPiece(this.agent, new Point(4,7),0);

        this.human.setChess(hCHSPieces);
        this.agent.setChess(aCHSPieces);
    }
}
