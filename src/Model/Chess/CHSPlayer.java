package Model.Chess;

import Model.Point;

/**
 * This class contains statistics and data on the
 * player of a certain side. This can either be
 * the human or the agent.
 * */
public class CHSPlayer {

    String name;
    CHSPiece[] CHSPiece;

    /**
     * Constructs the player
     * @param side is the determinant if player is human or agent.
     * */
    public CHSPlayer(int side) {
        if (side == 0) { this.name="Human";}
        else           { this.name="Agent";}
    }

    /**
     * Constructor for cloning in deep nodes, this is a board variant
     * to make up for the same reference to pass between board and player.
     * @param clone object instance of player to copy
     * */
    public CHSPlayer(CHSPlayer clone) {
        this.name = clone.name;
        this.CHSPiece = new CHSPiece[clone.CHSPiece.length];
        for (int i = 0; i < clone.CHSPiece.length; i++) {
            this.CHSPiece[i] = new CHSPiece(clone.CHSPiece[i]);
            this.CHSPiece[i].setOwner(this);
        }
    }

    public String getName() { return  this.name; }

    public CHSPiece[] getPiece() {
        return this.CHSPiece;
    }

    public CHSPiece getKing() {
//        return this.piece[15];
        for (CHSPiece p : this.CHSPiece)
            if (p.getType() == 0)
                return p;
        return null;
    }

    /**
     * Overloaded function that finds a piece
     * based on chess notation.
     * @param not is the chess notation to translate
     * @return piece at said tile
     * */
    public CHSPiece getPiece(String not) {
        for (CHSPiece p : this.CHSPiece)
            if (p.getCoords().equals(not) && p.getPlaying())
                return p;

        return null;
    }

    /**
     * Overloaded function that finds a piece
     * based on coordinates.
     * @param c coordinates
     * @return piece at said coordinate
     * */
    public CHSPiece getPiece(Point c) {
        for (CHSPiece p : this.CHSPiece)
            if (p.getCoords().equals(c) && p.getPlaying())
                return p;

        return null;
    }

    /**
     * A function that gets all pieces in play
     * @return number of pieces that are still in the board
     */
    public int getAvailablePieces() {
        int nAvailable = 0;

        for (CHSPiece p : this.CHSPiece)
            if (p.getPlaying())
                nAvailable++;
        return nAvailable;
    }

    /**
     * Gets all pieces of a certain type in the game
     * @param t the type of piece (1/2/3/4/5) (P/N/B/R/Q)
     * @return number of pieces of that type
     */
    public int getAvailablePiecesType(int t) {
        int nAvailable = 0;

        for (CHSPiece p : this.CHSPiece)
            if (p.getPlaying() && p.getType() == t)
                nAvailable++;
        return nAvailable;
    }

    /**
     * Gets all pieces under this player that are kinged.
     * @return number of kinged pieces under said player
     */
    public int getKingedPieces() {
        int nKings = 0;
        for (CHSPiece p : this.CHSPiece)
            if(p.getPlaying() && p.getType() > 0)
                nKings++;

        return nKings;
    }

    /**
     * Sets the player's piece to chess.
     * @param p array of pieces for chess
     * */
    public void setChess(CHSPiece[] p) {
        this.CHSPiece = new CHSPiece[16];
        this.CHSPiece = p;
    }

    /**
     * Gets the name of the player whether player is
     * human or agent through toString.
     * @return human or agent
     * */
    @Override
    public String toString() { return this.name; }


}
