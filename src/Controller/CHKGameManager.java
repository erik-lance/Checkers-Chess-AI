package Controller;

import Model.Checkers.*;
import Model.Point;
import View.Display;

/*
 * Main controller for the game to handle all
 * actions needed.
 * */
public class CHKGameManager {
    CHKBoard board;
    CHKPlayer human;
    CHKPlayer agent;

    Display view;
    boolean turn;

    public CHKGameManager() {
        this.human = new CHKPlayer(0);
        this.agent = new CHKPlayer(1);

        this.board = new CHKBoard(this.human, this.agent, "Checkers");
        this.human.setBoard(this.board);
        this.agent.setBoard(this.board);

        this.turn = true;
        this.view = new Display(this.board);

    }

    /**
     * This function begins the game and houses the main
     * turn checking functions to give each CHKPlayer a turn.
     * */
    public void startGame() {
        boolean isPlaying = true;
        initiateCheckers();

        //This is the main loop to check through each move.
        while (isPlaying) {
            view.printBoardCHK();
            String pSelect;
            CHKPiece pUse = null;

            while (pUse == null) {
                pSelect = view.prompt("Choose a piece by notation");
                pUse = this.human.getPiece(pSelect);
                if (pUse != null && !board.getValidMove(pUse))
                    pUse = null;
            }
            //Checks if piece exists and has a valid move to play.
            if (board.getValidMove(pUse)) {
                boolean canMove = false;
                boolean forced = false;
                String mTile = null;

                while (!canMove) {
                    //Print ● for each valid tile!!!
                    mTile = view.prompt("Choose a valid tile to move ("+pUse.getCoords().toStringC()+")");

                    //Checks if input is real and is a valid tile to step onto
                    if (mTile != null && board.getValidMove(pUse, new Point(mTile))) {

                        //In the case piece is forced to move
                        if (forced && board.isJumpMove(pUse.getCoords(), new Point(mTile))) {
                            view.readClean(("Forced move! Jumping to a valid jump!"));
                            //Get enemy piece between
                            Point pPiece = pUse.getCoords();
                            Point newT = new Point(mTile);
                            int eX = (int) Math.ceil((double) (newT.getX() + pPiece.getX()) / 2);
                            int eY = (int) Math.ceil((double) (newT.getY() + pPiece.getY()) / 2);
                            //ePiece gets the midpoint of two points
                            Point ePiece = new Point(eX, eY);
                            //Uses overloaded capture move piece
                            board.movePiece(pUse, new Point(mTile), board.getPiece(ePiece));

                            //Detects if forced move after capturing.
                            if (board.canJump(pUse)) {
                                forced = true;
                                view.readClean(("Forced move!"));
                                view.printBoardCHK();
                            } else {
                                forced = false;
                                canMove = true;
                            }
                        }
                        else if (!forced && board.canJump(pUse) && board.isJumpMove(pUse.getCoords(), new Point(mTile))) {
                            //If capture move
                            //Get enemy piece between
                            Point pPiece = pUse.getCoords();
                            Point newT = new Point(mTile);
                            int eX = (int) Math.ceil((double)(newT.getX() + pPiece.getX())/2);
                            int eY = (int) Math.ceil((double)(newT.getY() + pPiece.getY())/2);
                            //ePiece gets the midpoint of two points
                            Point ePiece = new Point(eX, eY);
                            //Uses overloaded capture move piece
                            board.movePiece(pUse, new Point(mTile), board.getPiece(ePiece));

                            //Detects if forced move after capturing.
                            if (board.canJump(pUse)) {
                                forced = true;
                                view.readClean(("Forced move!"));
                                view.printBoardCHK();
                            }
                            else {
                                forced = false;
                                canMove = true;
                            }
                        }
                        else if (!forced) {
                            //This is the default move without capture. The condition
                            //is to avoid a wrong move in a force jump.
                            board.movePiece(pUse, new Point(mTile));
                            canMove = true;
                        }

                    }

                }

                CHKTree enemyUtilitySystem = new CHKTree(this.board);
                CHKNode path = null;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }

                //Checks if agent has any valid moves left.
                if (agent.getValidPieces().length == 0) {
                    isPlaying = false;
                }
                else {
                    enemyUtilitySystem.prepareTreeAtDepth(3);
                    path = enemyUtilitySystem.minimax(enemyUtilitySystem.getRoot());
                    this.board = path.getBoard();
                }

                this.human = board.getHuman();
                this.agent = board.getAgent();

                view.setBoard(this.board);
                view.sethMove(mTile);

                if (isPlaying) {
                    view.setaMove(path.getDest());
                    view.sethValue(enemyUtilitySystem.getHeuristics());
                }
                else {
                    view.setaMove("No more valid moves. You lost");
                    view.sethValue(-16);
                }

                if (this.human.getValidPieces().length == 0) {
                    isPlaying = false;
                }
                if (board.isGameOver()) isPlaying = false;
                board.nextMove();

            }

        }
        view.printBoardCHK();
        //Game has ended
    }

    /**
     * This function helps set the board for a checkers
     * game. It creates references for the new pieces
     * for each CHKPlayer in terms of checkers.
     * */
    public void initiateCheckers() {
        CHKPiece[] hPieces = new CHKPiece[12];
        CHKPiece[] aPieces = new CHKPiece[12];
        int pieceCounter = 0;

        //CHKPlayer Pieces
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 8; x += 2) {
                //If y axis is not even
                if (y % 2 != 0 && x == 0) { x = 1;}

                hPieces[pieceCounter] = new CHKPiece(this.human, new Point(x,y));
                pieceCounter++;
            }
        }
        pieceCounter = 0;

        //Agent Pieces
        for (int y = 7; y > 4 ; y--) {
            for (int x = 1; x < 8; x += 2) {
                //If y axis is even
                if (y % 2 == 0 && x == 1) { x = 0;}

                aPieces[pieceCounter] = new CHKPiece(this.agent, new Point(x,y));
                pieceCounter++;
            }
        }

        this.human.setCheckers(hPieces);
        this.agent.setCheckers(aPieces);
    }
}
