package Model.Chess;

import Model.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * A single cell or node that houses all data needed
 * for tree functions.
 * */
public class CHSNode {

    CHSNode parent;
    CHSBoard CHSBoard;
    List<CHSNode> children;
    int value;
    int depth;
    Point dest;

    /**
     * Root node function to start the node tree
     * @param b board parameter to hold the entire game.
     */
    public CHSNode(CHSBoard b) {
        this.parent = null;
        this.CHSBoard = b;
        this.children = new ArrayList<>();
        this.depth = 0;
    }

    /**
     * Creates a new node using board as a basis.
     * @param n parent node branching off from
     * @param b board node to get details from
     */
    public CHSNode(CHSNode n, CHSBoard b) {
        System.out.println("Adding Move node");
        this.parent = n;
        this.CHSBoard = b;
        this.children = new ArrayList<>();
        this.depth = n.getDepth()+1;
    }

    /**
     * Adds a new child node to the list of possible moves
     * @param n is the node to add to the list
     */
    public void addNode(CHSNode n) {
        this.children.add(n);
        n.setParent(this);
        n.setDepth(n.getParent().getDepth()+1);
    }

    public int calculateHeuristic() {
        String turn;
        if (this.depth == 0) turn = "Agent";
        else turn = CHSBoard.getPiece(dest).getOwner().getName();
        boolean side2move = turn.equals("Agent");
        PeSTO RofChade = new PeSTO(this.CHSBoard, side2move);
        RofChade.init_tables();
        int eval = RofChade.eval();
        int turnHeuristic = RofChade.gethHeuristic();
        int otheHeuristic = RofChade.getaHeuristic();
        /*
        * https://www.chessprogramming.org/PeSTO%27s_Evaluation_Function
        * Utilizes RofChade engine, mostly focuses on the PeSTO evaluation.
        * Higher is better.
        * */

        if (!side2move) {
            eval *= -1;
        }

        return eval;

    }

    public CHSNode getParent() { return this.parent;}
    public CHSBoard getBoard() { return  this.CHSBoard; }
    public List<CHSNode> getChildren() { return this.children; }
    public CHSNode getChildAt(int c) {
        if (this.children.size() <= c)
            return null;
        else
            return this.getChildren().get(c);
    }

    public int getValue() { return  this.value;}
    public int getDepth() { return this.depth; }
    public String getDest() {
        int iX = dest.getX()+'a';
        char cX = (char) iX;
        return cX +Integer.toString(dest.getY()+1);

    }

    public void setParent(CHSNode n) { this.parent = n; }
    public void setDest(Point p) {this.dest = p; }
    public void setDepth(int d) {this.depth = d; }
    public void setValue(int v) { this.value = v; }

}
