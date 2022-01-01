package Model.Checkers;

import Model.Point;
import View.Colors;

/*
 * Represents a piece on the board.
 * */
public class CHKPiece {
    CHKPlayer owner;
    Point coords;
    boolean isPlaying;
    int type;

    /**
     * Constructor to create a new piece on the board.
     * @param o is the player who owns the piece
     * @param c is the coordinate where the piece will be
     * */
    public CHKPiece(CHKPlayer o, Point c) {
        this.owner = o;
        this.coords = c;
        this.isPlaying = true;
        this.type = 0;
    }
    /**
     * Constructor to create a new piece on the board with a rank.
     * @param o is the player who owns the piece
     * @param c is the coordinate where the piece will be
     * @param t is the rank of the piece for chess
     * */
    public CHKPiece(CHKPlayer o, Point c, int t) {
        this.owner = o;
        this.coords = c;
        this.isPlaying = true;
        this.type = t;
    }

    /**
     * Cloning constructor to create a new instance
     * of the same piece.
     * @param clone is the object to copy
     * */
    public CHKPiece(CHKPiece clone) {
        this.owner = clone.owner;
        this.coords = new Point(clone.getCoords());
        this.isPlaying = clone.isPlaying;
        this.type = clone.type;
    }

    public CHKPlayer getOwner() { return this.owner; }
    public Point getCoords() { return this.coords; }
    public boolean getPlaying() { return this.isPlaying; }
    public int getType() { return this.type; }

    public void setCoords(Point c) { this.coords = c; }
    public void setPlaying() {this.isPlaying = false; }
    public void setPlaying(boolean b) {this.isPlaying = b; }
    public void setType(int t) { this.type = t; }

    /**
     * Function to tether piece to owner during cloning
     * @param o owner to tether to
     */
    public void setOwner(CHKPlayer o) {
        this.owner = o;
    }

    /**
     * Helps visualize what the piece is
     * @return white if agent, black if human player.
     * */
    @Override
    public String toString() {
        //This returns the color of agent for now. If chess will be implemented,
        //it will be changed.
        if (owner.name.equals("Human")) {
            if (type == 0)
                return Colors.RESET+"B";
            else if (type == 1)
                return Colors.RESET+"K";
            else
                return ".";
        }
        else {
            if (type == 0)
                return Colors.RED+"W";
            else if (type == 1)
                return Colors.RED+"K";
            else
                return ".";
        }
    }
}
