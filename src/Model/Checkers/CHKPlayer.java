package Model.Checkers;

import Model.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains statistics and data on the
 * player of a certain side. This can either be
 * the human or the agent.
 * */
public class CHKPlayer {

    String name;
    CHKBoard CHKBoard;
    CHKPiece[] CHKPiece;

    /**
     * Constructs the player
     * @param side is the determinant if player is human or agent.
     * */
    public CHKPlayer(int side) {
        if (side == 0) { this.name="Human";}
        else           { this.name="Agent";}
    }

    /**
     * Constructor for cloning in deep nodes, this is a board variant
     * to make up for the same reference to pass between board and player.
     * @param b is the board.
     * @param clone object instance of player to copy
     * */
    public CHKPlayer(CHKBoard b, CHKPlayer clone) {
        this.name = clone.name;
        this.CHKBoard = b;
        this.CHKPiece = new CHKPiece[clone.CHKPiece.length];
        for (int i = 0; i < clone.CHKPiece.length; i++)
            this.CHKPiece[i] = new CHKPiece(clone.CHKPiece[i]);
    }

    /**
     * Overloaded function that finds a piece
     * based on chess notation.
     * @param not is the chess notation to translate
     * @return piece at said tile
     * */
    public CHKPiece getPiece(String not) {
        for (CHKPiece p : this.CHKPiece)
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
    public CHKPiece getPiece(Point c) {
        for (CHKPiece p : this.CHKPiece)
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

        for (CHKPiece p : this.CHKPiece)
            if (p.getPlaying())
                nAvailable++;
        return nAvailable;
    }

    /**
     * Gets all pieces under this player that are kinged.
     * @return number of kinged pieces under said player
     */
    public int getKingedPieces() {
        int nKings = 0;
        for (CHKPiece p : this.CHKPiece)
            if(p.getPlaying() && p.getType() > 0)
                nKings++;

        return nKings;
    }

    /**
     * Gets all pieces that can be moved.
     * @return an array of pieces that can be moved
     * */
    public CHKPiece[] getValidPieces() {
        List<CHKPiece> freeCHKPieces = new ArrayList<>();
        for (int i = 0; i < 12; i++)
            if(CHKPiece[i].getPlaying() && this.CHKBoard.getValidMove(CHKPiece[i]))
                freeCHKPieces.add(CHKPiece[i]);

        //Merely converts to array
        CHKPiece[] aCHKPieces = new CHKPiece[freeCHKPieces.size()];
        for (int i = 0; i < freeCHKPieces.size(); i++)
            aCHKPieces[i] = freeCHKPieces.get(i);

        return aCHKPieces;
    }

    /**
     * Grants player a reference to the board to interact with each other.
     * @param b is the board to set unto player
     * */
    public void setBoard(CHKBoard b) { this.CHKBoard = b;}

    /**
     * Sets the player's piece to checkers.
     * @param p array of pieces for checkers
     * */
    public void setCheckers(CHKPiece[] p) {
        this.CHKPiece = new CHKPiece[12];
        this.CHKPiece = p;
    }

    /**
     * Gets the name of the player whether player is
     * human or agent through toString.
     * @return human or agent
     * */
    @Override
    public String toString() { return this.name; }


}
