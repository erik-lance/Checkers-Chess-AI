package Model.Checkers;

import Model.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * A single cell or node that houses all data needed
 * for tree functions.
 * */
public class CHKNode {

    CHKNode parent;
    CHKBoard CHKBoard;
    List<CHKNode> children;
    int value;
    int depth;
    Point dest;

    /**
     * Root node function to start the node tree
     * @param b board parameter to hold the entire game.
     */
    public CHKNode(CHKBoard b) {
        this.parent = null;
        this.CHKBoard = b;
        this.children = new ArrayList<>();
        this.depth = 0;
    }

    /**
     * Creates a new node using board as a basis.
     * @param n parent node branching off from
     * @param b board node to get details from
     */
    public CHKNode(CHKNode n, CHKBoard b) {
        System.out.println("Adding Move node");
        this.parent = n;
        this.CHKBoard = b;
        this.children = new ArrayList<>();
        this.depth = n.getDepth()+1;
    }

    /**
     * Adds a new child node to the list of possible moves
     * @param n is the node to add to the list
     */
    public void addNode(CHKNode n) {
        this.children.add(n);
        n.setParent(this);
        n.setDepth(n.getParent().getDepth()+1);
    }

    public int calculateHeuristic() {
        int humanPCs  = CHKBoard.getHuman().getAvailablePieces();
        int humanKPCs = CHKBoard.getHuman().getKingedPieces();
        int agentPCs  = CHKBoard.getAgent().getAvailablePieces();
        int agentKPCs = CHKBoard.getAgent().getKingedPieces();

        /*
        * https://aboorvadevarajan.github.io/Parallel-Checkers-Game/
        * The pawns of the machine are given value 1.
        * The Kings of the machine are given value 2 (since these are favorable for the machine to win)
        * The pawns of the opponent are given value -1(since these are less favorable for the machine to win)
        * The kings of the opponent are given value -2 (since these are unfavorable for the machine to win)
        * Higher is better!
        * */

        //Human is minimizer, therefore must be negative.
        int humanCalc = (humanPCs + humanKPCs*2)*-1 - (-12);
        int agentCalc = (agentPCs + agentKPCs*2) -12;
        //System.out.println("H: "+humanCalc+" A: "+agentCalc+" Heuristic: "+(agentCalc + humanCalc));
        return agentCalc + humanCalc;
    }

    public CHKNode getParent() { return this.parent;}
    public CHKBoard getBoard() { return  this.CHKBoard; }
    public List<CHKNode> getChildren() { return this.children; }
    public CHKNode getChildAt(int c) {
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

    public void setParent(CHKNode n) { this.parent = n; }
    public void setDest(Point p) {this.dest = p; }
    public void setDepth(int d) {this.depth = d; }
    public void setValue(int v) { this.value = v; }

}
