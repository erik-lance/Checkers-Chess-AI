package Model.Chess;

import Model.Point;
import View.Colors;

import java.util.ArrayList;
import java.util.List;

/**
 * A tree to hold nodes in the game.
 * */
public class CHSTree {
    private final CHSNode root;
    private CHSNode curState;
    private int heuristics;
    private int traversal;

    /**
     * Instantiates the tree
     * @param b is the board to hold all details.
     */
    public CHSTree(CHSBoard b) {
        this.root = new CHSNode(b);
        this.curState = root;
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
                //System.out.println(j+" Children: "+ curState.getChildAt(j).getChildren().size());
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
                    //System.out.println("Depth down "+curState.getDepth());
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
    public CHSNode minimax(CHSNode state) {
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
    public void prepareChildren(CHSNode n,boolean turn) {
        List<CHSPiece> ePieces = new ArrayList<>();
        CHSBoard state = n.getBoard();

        if (turn) ePieces = state.getPieces("Agent");
        else             ePieces = state.getPieces("Human");

        List<CHSNode> possibleMoves = new ArrayList<>();
        //Finds all pieces with at least one valid move.
        for (CHSPiece piece : ePieces) {
            List<Point> validM = state.validMoves(piece);
            if (validM == null) continue;;
            //Create a new node for each valid move
            for (Point point : validM) {
                CHSBoard newNode = new CHSBoard(state);
                CHSPiece movePiece = newNode.getPiece(piece.getCoords());

                newNode.movePiece(movePiece, point);
                if (piece.getType() == 0 && point.getY() == 7 && piece.getOwner().getName().equals("Human")) {
                    //Makes a new node for each possible promotion
                    for (int i = 2; i < 6; i++) {
                        CHSBoard promoteNode = new CHSBoard(newNode);
                        promoteNode.promotePiece(promoteNode.getPiece(point),i);
                        CHSNode childNode = new CHSNode(promoteNode);
                        childNode.setDest(point);
                        possibleMoves.add(childNode);
                    }
                    continue;
                }
                else if (piece.getType() == 0 && point.getY() == 0 && piece.getOwner().getName().equals("Agent")) {
                    for (int i = 2; i < 6; i++) {
                        CHSBoard promoteNode = new CHSBoard(newNode);
                        promoteNode.promotePiece(promoteNode.getPiece(point),i);
                        CHSNode childNode = new CHSNode(promoteNode);
                        childNode.setDest(point);
                        possibleMoves.add(childNode);
                    }
                    continue;
                }

                CHSNode childNode = new CHSNode(newNode);
                childNode.setDest(point);
                possibleMoves.add(childNode);
            }
        }
        //Adds children node
        for (CHSNode possibleMove : possibleMoves) {
            n.addNode(possibleMove);
        }
    }

    public double maximizer(CHSNode state, double alpha, double beta) {
        //This is the static evaluation of the position.
        if (state.getChildren().size() == 0) {
            state.setValue(state.calculateHeuristic());
            return state.calculateHeuristic();
        }
        else if (state.getBoard().isGameOver("Human")) {
            state.setValue(state.calculateHeuristic()+9999);
            return state.calculateHeuristic()+9999;
        }

        double maximizingValue = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < state.getChildren().size(); i++) {
            CHSNode childState = state.getChildAt(i);
            maximizingValue = Math.max(maximizingValue,minimizer(childState,alpha,beta));
            childState.setValue((int) maximizingValue);

            //Move Ordering
            traversal++;
            if (maximizingValue >= beta)
                return maximizingValue;
            alpha = Math.max(alpha,maximizingValue);
        }

        return maximizingValue;
    }

    public double minimizer(CHSNode state, double alpha, double beta) {
        //This is the static evaluation of the position.
        if (state.getChildren().size() == 0) {
            state.setValue(state.calculateHeuristic());
            return state.calculateHeuristic();
        }
        else if (state.getBoard().isGameOver("Agent")) {
            state.setValue(state.calculateHeuristic()-9999);
            return state.calculateHeuristic()-9999;
        }

        double minimizingValue = Double.POSITIVE_INFINITY;
        for (int i = 0; i < state.getChildren().size(); i++) {
            CHSNode childState = state.getChildAt(i);
            minimizingValue = Math.min(minimizingValue,maximizer(childState,alpha,beta));
            childState.setValue((int) minimizingValue);

            //Move Ordering
            traversal++;
            if (minimizingValue <= alpha) {
                return minimizingValue;
            }
            beta = Math.min(beta,minimizingValue);
        }

        return minimizingValue;
    }


    public int getHeuristics() {return this.heuristics;}
    public int getTraversal() { return this.traversal;}
    public CHSNode getRoot() { return  this.root;}

}
