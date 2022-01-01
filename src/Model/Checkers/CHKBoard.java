package Model.Checkers;

import Model.Point;

/**
 * This contains the entire game. It houses the human and agent
 * as well as the pieces of the game through tiles and players.
 */
public class CHKBoard {
    CHKPlayer human;
    CHKPlayer agent;
    int moveNum;

    /**
     * Instantiates the board of the game
     * @param h is the human player
     * @param a is the agent player
     * @param c is the type of game
     * */
    public CHKBoard(CHKPlayer h, CHKPlayer a, String c) {
        this.human  = h;
        this.agent  = a;
        this.moveNum = 1;
    }

    /**
     * Cloning function to deep copy all contents.
     * @param clone is the object to copy
     * */
    public CHKBoard(CHKBoard clone) {
        this.human  = new CHKPlayer(this, clone.human);
        this.agent  = new CHKPlayer(this, clone.agent);

        this.moveNum = clone.moveNum;

        tetherBoard();
    }

    /**
     * Moves the piece on the board and updates
     * piece ownership on tiles
     * @param pc is the piece to move
     * @param pt is the point to move to
     * */
    public void movePiece(CHKPiece pc, Point pt) {
        Point newTile = new Point(pt);
        pc.setCoords(pt);

        if (pc.getOwner().toString().equals("Human")) {
            if (newTile.getY() == 7)
                pc.setType(1);
        }
        else {
            if (newTile.getY() == 0)
                pc.setType(1);
        }
    }

    /**
     * [OVERLOAD FUNCTION]
     * Moves the piece on the board and updates
     * piece ownership on tiles, captures as well.
     * @param pc is the piece to move
     * @param pt is the point to move to
     * @param epc is the enemy piece to capture
     * */
    public void movePiece(CHKPiece pc, Point pt, CHKPiece epc) {
        Point newTile = new Point(pt);

        int eX = (int) Math.ceil((double)(pt.getX() + pc.getCoords().getX())/2);
        int eY = (int) Math.ceil((double)(pt.getY() + pc.getCoords().getY())/2);

        pc.setCoords(pt);
        epc.setPlaying();

        if (pc.getOwner().toString().equals("Human")) {
            if (newTile.getY() == 7)
                pc.setType(1);
        }
        else {
            if (newTile.getY() == 0)
                pc.setType(1);
        }
    }

    public boolean isAlly(CHKPiece pc, Point pt) {
        CHKPiece enCHKPiece = getPiece(pt);
        return pc.getOwner().equals(enCHKPiece.getOwner());

    }

    /**
     * Checks the 4 diagonal moves where a piece
     * is allowed to move.
     * @param pc to check which piece to find valid moves for
     * @return a Point array with 4 indices directing NW, NE, SW, SE respectively.
     * */
    public Point[] validMoves(CHKPiece pc) {
        Point nw = getCoordDir(pc,0,false);
        Point ne = getCoordDir(pc,1,false);
        boolean nwVal = nw.isValidPoint();
        boolean neVal = ne.isValidPoint();

        Point nwEx = getCoordDir(pc,0,true);
        Point neEx = getCoordDir(pc,1,true);
        boolean nweVal = nwEx.isValidPoint();
        boolean neeVal = neEx.isValidPoint();

        Point sw = getCoordDir(pc,2,false);
        Point se = getCoordDir(pc,3,false);
        boolean swVal = sw.isValidPoint();
        boolean seVal = se.isValidPoint();

        Point swEx = getCoordDir(pc,2,true);
        Point seEx = getCoordDir(pc,3,true);
        boolean sweVal = swEx.isValidPoint();
        boolean seeVal = seEx.isValidPoint();

        Point[] vMoves = new Point[4];

        //Regular Checker
        //North West Tile
        if (pc.getType() == 0) {
            if (pc.getOwner().toString().equals("Human")) {
                if (nwVal && getPiece(nw) == null)
                    vMoves[0] = nw;
                else if (nweVal && getPiece(nwEx) == null)
                    if (!isAlly(pc,nw)) vMoves[0] = nwEx;

                //North East Tile
                if (neVal && getPiece(ne) == null)
                    vMoves[1] = ne;
                else if (neeVal && getPiece(neEx) == null)
                    if (!isAlly(pc,ne)) vMoves[1] = neEx;

            }
            else {
                //South West Tile
                if (swVal && getPiece(sw) == null)
                    vMoves[2] = sw;
                else if (sweVal && getPiece(swEx) == null)
                    if (!isAlly(pc,sw)) vMoves[2] = swEx;

                //South East Tile
                if (seVal && getPiece(se) == null)
                    vMoves[3] = se;
                else if (seeVal && getPiece(seEx) == null)
                    if (!isAlly(pc,se)) vMoves[3] = seEx;
            }
        }
        //Kinged Checker
        if (pc.getType() > 0) {

            if (nwVal && getPiece(nw) == null)
                vMoves[0] = nw;
            else if (nweVal && getPiece(nwEx) == null)
                if (!isAlly(pc,nw)) vMoves[0] = nwEx;

            //North East Tile
            if (neVal && getPiece(ne) == null)
                vMoves[1] = ne;
            else if (neeVal && getPiece(neEx) == null)
                if (!isAlly(pc,ne)) vMoves[1] = neEx;

            //South West Tile
            if (swVal && getPiece(sw) == null)
                vMoves[2] = sw;
            else if (sweVal && getPiece(swEx) == null)
                if (!isAlly(pc,sw)) vMoves[2] = swEx;

            //South East Tile
            if (seVal && getPiece(se) == null)
                vMoves[3] = se;
            else if (seeVal && getPiece(seEx) == null)
                if (!isAlly(pc,se)) vMoves[3] = seEx;
        }

        if (vMoves[0] != null || vMoves[1] != null || vMoves[2] != null || vMoves[3] != null) return vMoves;
        else {
            return null;
        }
    }

    /**
     * Checks if there's a single valid move for said piece.
     * @param pc gets specific piece to check
     * @return true if there's a valid move, else false.
     * */
    public boolean getValidMove(CHKPiece pc) {
        return validMoves(pc) != null;
    }

    /**
     * Gets the number of pieces under a certain player
     * @param play is the human or agent in string form
     * @return the number of pieces said player has in play
     */
    public int getNumPieces(String play) {
        if      (play.equals("Human"))
            return this.human.getAvailablePieces();
        else if (play.equals("Agent"))
            return this.agent.getAvailablePieces();
        else
            return -1;
    }
    
    /**
     * Gets the number of kinged pieces under a certain player
     * @param play is the human or agent in string form
     * @return the number of pieces said player has in play
     */
    public int getNumKinged(String play) {
        if      (play.equals("Human"))
            return this.human.getKingedPieces();
        else if (play.equals("Agent"))
            return this.agent.getKingedPieces();
        else
            return -1;
    }

    /**
     * Grabs the point of a coordinate for a piece at a certain direction.
     * @param  pc is the piece object to check
     * @param dir is the direction to look at (0-3) NW/NE/SW/SE respectively
     * @param ext is to verify if looking for extended coordinate for captures
     * @return point coordinate
     * */
    public Point getCoordDir(CHKPiece pc, int dir, boolean ext) {
        Point pCoord = pc.getCoords();
        Point dirCoord;
        int pLength = (ext) ? 2 : 1;
        if      (dir == 0) dirCoord = new Point(pCoord.getX()-pLength, pCoord.getY()+pLength);
        else if (dir == 1) dirCoord = new Point(pCoord.getX()+pLength, pCoord.getY()+pLength);
        else if (dir == 2) dirCoord = new Point(pCoord.getX()-pLength, pCoord.getY()-pLength);
        else if (dir == 3) dirCoord = new Point(pCoord.getX()+pLength, pCoord.getY()-pLength);
        else dirCoord = null;

        return dirCoord;
    }

    /**
     * Overloaded function to check if a specific point is valid.
     * @param pc gets specific piece to check
     * @param pt gets specific point to check
     * @return true if said point is valid, else false.
     * */
    public boolean getValidMove(CHKPiece pc, Point pt) {
        Point[] validM = validMoves(pc);
        if (validM == null) return false;

        for (Point point : validM)
            if (point != null && point.equals(pt))
                return true;
        return false;
    }

    /**
     * A simple checker if two points lead to a jump move.
     * @param start starting point
     * @param end ending point
     * @return true if jump, else false
     */
    public boolean isJumpMove(Point start, Point end) {
        if (end.getX() == start.getX()-2) return true;
        if (end.getX() == start.getX()+2) return true;
        return false;
    }

    /**
     * Checker for forced jumps or if piece can jump over a piece.
     * @param pc to reference and check
     * @return true if can jump, else false
     */
    public boolean canJump(CHKPiece pc) {
        Point[] validM = validMoves(pc);
        if (validM == null) return false;

        //Checkers for extensions
        if (validM[0] != null && validM[0].getX() == pc.getCoords().getX()-1) validM[0] = null;
        if (validM[1] != null && validM[1].getX() == pc.getCoords().getX()+1) validM[1] = null;
        if (validM[2] != null && validM[2].getX() == pc.getCoords().getX()-1) validM[2] = null;
        if (validM[3] != null && validM[3].getX() == pc.getCoords().getX()+1) validM[3] = null;

        //Checks forward facing
        if (pc.getType()==0) {
            if (pc.getOwner().toString().equals("Human")) {
                return validM[0] != null || validM[1] != null;
            }
            else {
                return validM[2] != null || validM[3] != null;
            }
        }
        else {
            return validM[0] != null || validM[1] != null || validM[2] != null || validM[3] != null;
        }
    }

    /**
     * Helps tether players to the board after cloning
     */
    public void tetherBoard() {
        this.human.setBoard(this);
        this.agent.setBoard(this);
    }

    /**
     * Gets human player in board
     * @return human player
     * */
    public CHKPlayer getHuman()  { return this.human; }

    /**
     * Gets agent player in board
     * @return agent player
     */
    public CHKPlayer getAgent()  { return this.agent; }

    /**
     * Checks what move number the game already is at
     * @return move number the game is at
     */
    public int getMoveNum() { return this.moveNum; }

    public CHKPiece getPiece(Point p) {
        if (this.human.getPiece(p) != null)
            return this.human.getPiece(p);
        else if (this.agent.getPiece(p) != null)
            return this.agent.getPiece(p);
        else
            return null;
    }

    /**
     * Checks if game is over if the number of pieces in play is 0
     * for any side.
     * @return true if no more pieces, else false
     */
    public boolean isGameOver() {
        if      (getNumPieces("Human") == 0) return true;
        else if (getNumPieces("Agent")== 0)  return true;

        return false;
    }

    /**
     * Adds moveNumber to indicate it's back to the first player's move
     * signifying a new move number.
     */
    public void nextMove() { this.moveNum += 1; }
}
