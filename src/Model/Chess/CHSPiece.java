package Model.Chess;

import Model.Point;
import View.Colors;

/*
 * Represents a piece on the board.
 * */
public class CHSPiece {
    private CHSPlayer owner;
    private Point coords;
    private boolean isPlaying;
    private int type;
    private int moves;

    /**
     * Constructor to create a new piece on the board.
     * @param o is the player who owns the piece
     * @param c is the coordinate where the piece will be
     * */
    public CHSPiece(CHSPlayer o, Point c) {
        this.owner = o;
        this.coords = c;
        this.isPlaying = true;
        this.type = 0;
        this.moves = 0;
    }
    /**
     * Constructor to create a new piece on the board with a rank.
     * @param o is the player who owns the piece
     * @param c is the coordinate where the piece will be
     * @param t is the rank of the piece for chess (0/1/2/3/4/5)
     * */
    public CHSPiece(CHSPlayer o, Point c, int t) {
        this.owner = o;
        this.coords = c;
        this.isPlaying = true;
        this.type = t;
        this.moves = 0;
        /**
         * 1 - Pawn
         * 2 - Knight
         * 3 - Bishop
         * 4 - Rook
         * 5 - Queen
         * 0 - King
         */
    }

    /**
     * Cloning constructor to create a new instance
     * of the same piece.
     * @param clone is the object to copy
     * */
    public CHSPiece(CHSPiece clone) {
        //this.owner = clone.owner;
        this.coords = new Point(clone.getCoords());
        this.isPlaying = clone.isPlaying;
        this.type = clone.type;
        this.moves = clone.moves;
    }

    public CHSPlayer getOwner() { return this.owner; }
    public Point getCoords() { return this.coords; }
    public boolean getPlaying() { return this.isPlaying; }
    public int getType() { return this.type; }
    public int getMoves() { return this.moves; }

    public void addMove() {this.moves++;}
    public void setCoords(Point c) { this.coords = c; }
    public void setPlaying() {this.isPlaying = false; }
    public void setPlaying(boolean b) {this.isPlaying = b; }
    public void setType(int t) { this.type = t; }

    /**
     * Function to tether piece to owner during cloning
     * @param o owner to tether to
     */
    public void setOwner(CHSPlayer o) {
        this.owner = o;
    }

    /**
     * Helps visualize what the piece is
     * @return white if player, red if agent.
     * */
    @Override
    public String toString() {
        //This returns the color of agent for now. If chess will be implemented,
        //it will be changed.
        if (owner.name.equals("Human")) {
            if      (type == 0) return Colors.RESET+"K";
            else if (type == 1) return Colors.RESET+"P";
            else if (type == 2) return Colors.RESET+"N";
            else if (type == 3) return Colors.RESET+"B";
            else if (type == 4) return Colors.RESET+"R";
            else if (type == 5) return Colors.RESET+"Q";
            else
                return ".";
        }
        else {
            if      (type == 0) return Colors.RED+"K";
            else if (type == 1) return Colors.RED+"P";
            else if (type == 2) return Colors.RED+"N";
            else if (type == 3) return Colors.RED+"B";
            else if (type == 4) return Colors.RED+"R";
            else if (type == 5) return Colors.RED+"Q";
            else
                return ".";
        }
    }
}
