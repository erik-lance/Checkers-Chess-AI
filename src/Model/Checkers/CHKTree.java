package Model.Checkers;

import Model.Point;
import View.Colors;

import java.util.ArrayList;
import java.util.List;

/**
 * A tree to hold nodes in the game.
 * */
public class CHKTree {

    private final CHKNode root;
    private CHKNode curState;
    private int heuristics;
    private int traversal;

    /**
     * Instantiates the tree
     * @param b is the board to hold all details.
     */
    public CHKTree(CHKBoard b) {
        this.root = new CHKNode(b);
        this.curState = root;
        this.heuristics = 0;
        this.traversal = 0;
    }

    /**
     * Prepares the nodes of a tree until a certain depth.
     * @param depth the maximum depth to reach
     */
    public void prepareTreeAtDepth(int depth) {
        //Depth 0 is merely to assess the situation, Depth 1 is when the AI makes its move
        //therefore 3rd parameter is true since it is AI's turn always.
        prepareChildren(root, true);
        System.out.println("\n\n"+Colors.RED+"Agent Data Depth "+depth+Colors.RESET+"\nRoot Children: "+root.getChildren().size());

        curState = root;
        int nodeCount = 0;
        boolean agentTurn = false;
        boolean start = true;

        while(curState.depth < depth-1) {
            //Prepares the children of each child of current node
            for (int j = 0; j < curState.getChildren().size(); j++) {
                prepareChildren(curState.getChildAt(j), agentTurn);
            }

            //Node selector
            //Goes through each node at a certain depth
            if (start) {
                start = false;
                curState = root.getChildAt(0);
            }
            else {
                nodeCount++;
                //When all nodes at this depth have been filled
                if (nodeCount >= curState.getParent().getChildren().size()) {
                    //Check if there is a child at leaf node
                    if (curState.getParent().getChildAt(0).getChildAt(0) == null) {
                        int childCheck = 1;
                        //Checks for each child if they have children. Always checks
                        //first child because it is a list.
                        while (childCheck < curState.getParent().getChildren().size()) {
                            if (curState.getParent().getChildAt(childCheck).getChildAt(0) != null) {
                                curState = curState.getParent().getChildAt(childCheck).getChildAt(0);
                                nodeCount = childCheck;

                                //Ends the loop
                                childCheck = curState.getParent().getChildren().size();
                            }
                            else
                                childCheck++;
                        }
                    }
                    else {
                        //Go to the leaf node at the next depth
                        curState = curState.getParent().getChildAt(0).getChildAt(0);
                        nodeCount = 0;
                    }
                    agentTurn = !agentTurn;

                    //If we discover that there are no children at the next depth,
                    //end while loop as there is no reason to proceed further.
                    if (nodeCount >= curState.getParent().getChildren().size())
                        break;
                }
                else {
                    //Move to next node at same depth
                    curState = curState.getParent().getChildAt(nodeCount);
                }

            }



        }
    }

    /**
     * Finds the most optimal move for agent through Alpha-Beta
     * pruning algorithm with minimax.
     * @param state current state of the board represented as a node
     * @return node to copy board of
     */
    public CHKNode minimax(CHKNode state) {
        double alpha = Double.NEGATIVE_INFINITY;
        double beta = Double.POSITIVE_INFINITY;

        int evalUtility = (int) maximizer(state, alpha, beta);
        int i;
        for (i = 0; i < state.getChildren().size(); i++) {
            if (state.getChildAt(i).getValue()==evalUtility) break;
        }

        System.out.println("minimax node: "+i+" value: "+evalUtility);
        System.out.println("Node Traversal: "+traversal);
        this.heuristics = evalUtility;
        return state.getChildAt(i);
    }

    /**
     * Prepares all possible moves under this Node state.
     * Checks through each piece that can move and each
     * valid move of each piece to add as a new state.
     * This will only prepare children depth+1
     * @param n is the node to add children to
     * @param turn is whose move to play for each children
     */
    public void prepareChildren(CHKNode n, boolean turn) {
        CHKPiece[] eCHKPieces;
        CHKBoard state = n.getBoard();

        if (turn) eCHKPieces = state.getAgent().getValidPieces();
        else             eCHKPieces = state.getHuman().getValidPieces();

        List<CHKNode> possibleMoves = new ArrayList<>();

        //Finds all pieces with at least one valid move.
        for (CHKPiece CHKPiece : eCHKPieces) {
            Point[] validM = state.validMoves(CHKPiece);
            //Checks all possible valid moves for said piece
            for (int i = 0; i < 4; i++) {
                //Skips the invalid moves
                if (validM[i] == null) continue;

                CHKBoard newNode = new CHKBoard(state);

                //Note: movePiece is not a clone, instead a child of the clone.
                //This is to ensure piece ownership integrity of a player.
                CHKPiece moveCHKPiece;
                moveCHKPiece = newNode.getPiece(CHKPiece.getCoords());

                Point newT = new Point(validM[i]);

                //This is to check if it is preparing to jump at newT
                boolean forceMove = false;
                Point pPiece = moveCHKPiece.getCoords();
                int eX;
                int eY;
                Point ePiece;
                CHKPiece captCHKPiece;
                //By checking the coordinate discrepancy, we can check if it jumped.
                //2 Tiles in a horizontal direction means it jumped.
                if (newNode.canJump(moveCHKPiece)) {
                    if (i == 0 && newT.getX() == pPiece.getX()-2) forceMove = true;
                    if (i == 1 && newT.getX() == pPiece.getX()+2) forceMove = true;
                    if (i == 2 && newT.getX() == pPiece.getX()-2) forceMove = true;
                    if (i == 3 && newT.getX() == pPiece.getX()+2) forceMove = true;
                }
                //Simply moves the piece already. Would automatically choose a jump move
                //if forceMove is true.
                if (forceMove) {
                    eX = (int) Math.ceil((double)(newT.getX() + pPiece.getX())/2);
                    eY = (int) Math.ceil((double)(newT.getY() + pPiece.getY())/2);
                    //Midpoint formula calculation for the piece to capture's coordinates

                    ePiece = new Point(eX, eY);
                    captCHKPiece = newNode.getPiece(ePiece);

                    newNode.movePiece(moveCHKPiece, newT, captCHKPiece);
                    if (!newNode.canJump(moveCHKPiece)) {
                        CHKNode childCHKNode = new CHKNode(newNode);
                        childCHKNode.setDest(newT);
                        possibleMoves.add(childCHKNode);
                        continue;
                    }
                }
                else {
                    newNode.movePiece(moveCHKPiece, newT);
                    CHKNode childCHKNode = new CHKNode(newNode);
                    childCHKNode.setDest(newT);
                    possibleMoves.add(childCHKNode);
                    continue;
                }
                //If it knows it jumped, check again!!!
                if (forceMove) {
                    //This checks if movepiece can still jump.
                    while (newNode.canJump(moveCHKPiece)) {
                        pPiece = moveCHKPiece.getCoords();
                        Point[] validJ = newNode.validMoves(moveCHKPiece);
                        //Will check all possible jumps from this position.
                        //Note: All moves that aren't jumps would be ignored.
                        for (int j = 0; j < 4; j++) {
                            //Checks if this move exists.
                            if (validJ[j] == null) continue;

                            //Checks if this move is a jump.
                            boolean moveAgain = false;
                            newT = new Point(validJ[j]);
                            if (j == 0 && newT.getX() == pPiece.getX()-2) moveAgain = true;
                            if (j == 1 && newT.getX() == pPiece.getX()+2) moveAgain = true;
                            if (j == 2 && newT.getX() == pPiece.getX()-2) moveAgain = true;
                            if (j == 3 && newT.getX() == pPiece.getX()+2) moveAgain = true;
                            if (!moveAgain) continue;

                            //This is now a branching board.
                            CHKBoard newJumpNode = new CHKBoard(newNode);
                            CHKPiece jumpCHKPiece;
                            jumpCHKPiece = newJumpNode.getPiece(moveCHKPiece.getCoords());

                            //Calculations of the jump captures
                            pPiece = jumpCHKPiece.getCoords();
                            eX = (int) Math.ceil((double)(newT.getX() + pPiece.getX())/2);
                            eY = (int) Math.ceil((double)(newT.getY() + pPiece.getY())/2);
                            ePiece = new Point(eX, eY);
                            captCHKPiece = newJumpNode.getPiece(ePiece);

                            //Captures piece while moving!
                            newJumpNode.movePiece(jumpCHKPiece, newT, captCHKPiece);

                            newNode = newJumpNode;
                            moveCHKPiece = jumpCHKPiece;

                            //Since move is found, skip all other possible moves for now.
                            break;
                        }
                    }
                    CHKNode childCHKNode = new CHKNode(newNode);
                    childCHKNode.setDest(newT);
                    possibleMoves.add(childCHKNode);


                }

            }
        }
        //Adds children node
        for (CHKNode possibleMove : possibleMoves)
            n.addNode(possibleMove);
    }

    public double maximizer(CHKNode state, double alpha, double beta) {
        //This is the static evaluation of the position.
        if (state.getChildren().size() == 0 || state.getBoard().isGameOver()) {
            state.setValue(state.calculateHeuristic());
            return state.calculateHeuristic();
        }

        double maximizingValue = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < state.getChildren().size(); i++) {
            CHKNode childState = state.getChildAt(i);
            maximizingValue = Math.max(maximizingValue,minimizer(childState,alpha,beta));
            childState.setValue((int) maximizingValue);

            //Move Orders
            traversal++;
            if (maximizingValue >= beta)
                return maximizingValue;
            alpha = Math.max(alpha,maximizingValue);
        }

        return maximizingValue;
    }

    public double minimizer(CHKNode state, double alpha, double beta) {
        //This is the static evaluation of the position.
        if (state.getChildren().size() == 0 || state.getBoard().isGameOver())
            return state.calculateHeuristic();

        double minimizingValue = Double.POSITIVE_INFINITY;
        for (int i = 0; i < state.getChildren().size(); i++) {
            CHKNode childState = state.getChildAt(i);
            minimizingValue = Math.min(minimizingValue,maximizer(childState,alpha,beta));
            childState.setValue((int) minimizingValue);

            //Move Orders
            traversal++;
            if (minimizingValue <= alpha)
                return minimizingValue;
            beta = Math.min(beta,minimizingValue);
        }

        return minimizingValue;
    }


    public CHKNode getRoot() { return  this.root;}

    public int getHeuristics() {return this.heuristics;}
}
