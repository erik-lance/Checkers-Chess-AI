package Model.Chess;

import Model.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * This contains the entire game. It houses the human and agent
 * as well as the pieces of the game through tiles and players.
 */
public class CHSBoard {
    CHSPlayer human;
    CHSPlayer agent;
    int moveNum;

    /**
     * Instantiates the board of the game
     * @param h is the human player
     * @param a is the agent player
     * */
    public CHSBoard(CHSPlayer h, CHSPlayer a) {
        this.human  = h;
        this.agent  = a;
        this.moveNum = 1;
    }

    /**
     * Cloning function to deep copy all contents.
     * @param clone is the object to copy
     * */
    public CHSBoard(CHSBoard clone) {
        this.human  = new CHSPlayer(clone.human);
        this.agent  = new CHSPlayer(clone.agent);
        this.moveNum = clone.moveNum;
    }

    /**
     * Moves the piece on the board and updates
     * piece ownership on tiles
     * @param pc is the piece to move
     * @param pt is the point to move to
     * */
    public boolean movePiece(CHSPiece pc, Point pt) {
        boolean hasPiece = getPiece(pt) != null;
        if (hasPiece) getPiece(pt).setPlaying();

        //Castling Rights, Checks if coordinate to move to is greater than 2
        if (pc.getType()==0 && pc.getMoves() == 0) {
            //Castle O-O-O and O-O, this moves rook manually.
            if (pc.getCoords().getX() == pt.getX()+2)
                movePiece(getPiece(new Point(0,pt.getY())), new Point(3,pt.getY()));
            else if (pc.getCoords().getX() == pt.getX()-2)
                movePiece(getPiece(new Point(7,pt.getY())), new Point(5,pt.getY()));
        }

        pc.setCoords(pt);
        //Checks for promotion of pawn at end of board

        pc.addMove();

        //This is just for checking if piece decided to capture.
        return hasPiece;
    }

    public boolean isAlly(CHSPiece pc, Point pt) {
        CHSPiece enCHSPiece = getPiece(pt);
        return pc.getOwner().equals(enCHSPiece.getOwner());
    }

    /**
     * Filters all possible moves of the
     * piece. Removes friendly fire.
     * @param pc piece to check moves of
     * @param moves all current possible moves
     * @return filtered possible moves
     */
    public List<Point> possibleMoves(CHSPiece pc, List<Point> moves) {
        List<Point> validM = new ArrayList<>();

        //Note: King and Knight will always be allowed to go in their moves.
        if (pc.getType()==0) {
            for (Point move : moves) {
                //Castle Moves O-O-O and O-O respectively. Checks if it's null. (Note: Rooks moved automatically)
                if (pc.getCoords().getX() == move.getX()+2 && getPiece(move) == null) {
                    //First checks if the rook exists and hasn't moved
                    //Checks owner of piece to move and if there is a piece attacking next to it
                    CHSPiece rook = getPiece(new Point(0,move.getY()));
                    if (rook != null && rook.getMoves() == 0) {
                        if (pc.getOwner().getName().equals("Human") && !isAttackedBy(new Point(3, move.getY()), "Agent")) {
                            validM.add(move);
                        }
                        else if (pc.getOwner().getName().equals("Agent") && !isAttackedBy(new Point(3, move.getY()), "Human")) {
                            validM.add(move);
                        }
                    }

                }
                else if (pc.getCoords().getX() == move.getX()-2 && getPiece(move) == null) {
                    CHSPiece rook = getPiece(new Point(7,move.getY()));
                    if (rook != null && rook.getMoves() == 0) {
                        if (pc.getOwner().getName().equals("Human") && !isAttackedBy(new Point(5, move.getY()), "Agent")) {
                            validM.add(move);
                        }
                        else if (pc.getOwner().getName().equals("Agent") && !isAttackedBy(new Point(5, move.getY()), "Human")) {
                            validM.add(move);
                        }
                    }
                }
                else {
                    //Then checks if  tile is empty, else if piece found is enemy.
                    if (getPiece(move) == null) validM.add(move);
                    else if (!isAlly(pc, move)) validM.add(move);
                }
            }
        }
        if (pc.getType()==1) {
            //Pawn is mostly filtered already.
            for (Point move : moves) {
                //If tile is empty, else if piece found is enemy
                if (getPiece(move) == null) validM.add(move);
                else if (!isAlly(pc, move))  validM.add(move);
            }
            //En Passant to be added
        }
        else if (pc.getType()>=2) {
            //Knight || Bishop || Rook || Queen
            // NOTE: Bishops+ already filtered their move until they found a piece in each direction.
            for (Point move : moves) {
                //If tile is empty, else if piece found is enemy
                if (getPiece(move) == null) validM.add(move);
                else if (!isAlly(pc, move))  validM.add(move);
            }
        }
        return  validM;
    }

    /**
     * Simply gets all possible tiles for a piece. The exception is the
     * bishop,rook,queen, as they simply stop until they find a piece.
     * @param pc piece to shift through
     * @return all tiles they can move to
     */
    public List<Point> getMoves(CHSPiece pc) {
        List<Point> validM = new ArrayList<>();
        int x = pc.getCoords().getX();
        int y = pc.getCoords().getY();

        if (pc.getType()== 0) {
            //King
            if (isValidPoint(x-1,y+1))  validM.add(new Point(x-1,y+1,1));
            if (isValidPoint(x,y+1))       validM.add(new Point(x, y+1,2));
            if (isValidPoint(x+1,y+1))  validM.add(new Point(x+1,y+1,3));
            if (isValidPoint(x-1,y))       validM.add(new Point(x-1, y,4));
            if (isValidPoint(x+1,y))       validM.add(new Point(x+1,y,6));
            if (isValidPoint(x-1,y-1))  validM.add(new Point(x-1,y-1,7));
            if (isValidPoint(x,y-1))       validM.add(new Point(x,y-1,8));
            if (isValidPoint(x+1,y-1))  validM.add(new Point(x+1,y-1,9));

            //Castling Rights
            if (pc.getMoves() == 0) {
                if (isValidPoint(x+2,y) && getPiece(new Point(x+1,y)) == null) validM.add(new Point(x+2,y));
                if (isValidPoint(x-2,y) && getPiece(new Point(x-1,y)) == null) validM.add(new Point(x-2,y));
            }
        }
        else if (pc.getType() == 1) {
            //Pawn (Almost completely filtered)
            if (pc.getOwner().toString().equals("Human")) {
                if (isValidPoint(x,y+1) && getPiece(new Point (x,y+1)) == null) {
                    validM.add(new Point(x,y+1,2));
                    if (pc.getMoves() == 0 && isValidPoint(x,y+2) && getPiece(new Point (x,y+2)) == null)
                        validM.add(new Point(x,y+2,2));
                }
                if (isValidPoint(x-1,y+1) && getPiece(new Point(x-1,y+1)) != null) validM.add(new Point(x-1,y+1,1));
                if (isValidPoint(x+1,y+1) && getPiece(new Point(x+1,y+1)) != null) validM.add(new Point(x+1,y+1,3));
            }
            else {
                if (isValidPoint(x,y-1) && getPiece(new Point (x,y-1)) == null) {
                    validM.add(new Point(x,y-1,8));
                    if (pc.getMoves() == 0 && isValidPoint(x,y-2) && getPiece(new Point (x,y-2)) == null)
                        validM.add(new Point(x,y-2,8));
                }
                if (isValidPoint(x-1,y-1) && getPiece(new Point(x-1,y-1)) != null) validM.add(new Point(x-1,y-1,7));
                if (isValidPoint(x+1,y-1) && getPiece(new Point(x+1,y-1)) != null) validM.add(new Point(x+1,y-1,9));
            }
        }
        else if (pc.getType() == 2) {
            //Knight
            if (isValidPoint(x-2,y+1)) validM.add(new Point(x-2,y+1,1));
            if (isValidPoint(x-2,y-1)) validM.add(new Point(x-2,y-1,7));
            if (isValidPoint(x+2,y+1)) validM.add(new Point(x+2,y+1,3));
            if (isValidPoint(x+2,y-1)) validM.add(new Point(x+2,y-1,9));

            if (isValidPoint(x-1,y+2)) validM.add(new Point(x-1,y+2,1));
            if (isValidPoint(x+1,y+2)) validM.add(new Point(x+1,y+2,3));
            if (isValidPoint(x-1,y-2)) validM.add(new Point(x-1,y-2,7));
            if (isValidPoint(x+1,y-2)) validM.add(new Point(x+1,y-2,9));
        }
        else if (pc.getType() == 3) {
            //Bishop
            //North East
            for (int i = 1; i+x < 8 && i+y < 8; i++) {
                if (isValidPoint(x+i,y+i)) {
                    validM.add(new Point(x+i,y+i,1));
                    if (getPiece(new Point(x+i,y+i)) != null) break;
                }
                else break;
            }

            //North West
            for (int i = 1; x-i >= 0 && i+y < 8; i++) {
                if (isValidPoint(x-i,y+i)) {
                    validM.add(new Point(x-i,y+i,3));
                    if (getPiece(new Point(x-i,y+i)) != null) break;
                }
                else break;
            }

            //South East
            for (int i = 1; i+x < 8 && y-i >= 0; i++) {
                if (isValidPoint(x+i,y-i)) {
                    validM.add(new Point(x+i,y-i,7));
                    if (getPiece(new Point(x+i,y-i)) != null) break;
                }
                else break;
            }

            //South West
            for (int i = 1; x-i >= 0 && y-i >= 0; i++) {
                if (isValidPoint(x-i,y-i)) {
                    validM.add(new Point(x-i,y-i,9));
                    if (getPiece(new Point(x-i,y-i)) != null) break;
                }
                else
                    break;
            }
        }
        else if (pc.getType() == 4) {
            //Rook
            //North
            for (int i = 1; i+y < 8; i++) {
                if (isValidPoint(x,y+i)) {
                    validM.add(new Point(x,y+i,2));
                    if (getPiece(new Point(x,y+i)) != null) break;
                }
                else break;
            }

            //South
            for (int i = 1; y-i >= 0; i++) {
                if (isValidPoint(x,y-i)) {
                    validM.add(new Point(x,y-i,8));
                    if (getPiece(new Point(x,y-i)) != null) break;
                }
                else break;
            }

            //East
            for (int i = 1; i+x < 8; i++) {
                if(isValidPoint(x+i,y)) {
                    validM.add(new Point(x+i,y,6));
                    if (getPiece(new Point(x+i,y)) != null) break;
                }
                else break;
            }

            //West
            for (int i = 1; x-i >= 0; i++) {
                if (isValidPoint(x-i,y)) {
                    validM.add(new Point(x-i,y,4));
                    if (getPiece(new Point(x-i,y)) != null) break;
                }
                else break;
            }
        }
        else if (pc.getType() == 5) {
            //Queen
            //North West
            for (int i = 1; x-i >= 0 && i+y < 8; i++) {
                if (isValidPoint(x-i,y+i)) {
                    validM.add(new Point(x-i,y+i,3));
                    if (getPiece(new Point(x-i,y+i)) != null) break;
                }
                else break;
            }
            //North
            for (int i = 1; i+y < 8; i++) {
                if (isValidPoint(x,y+i)) {
                    validM.add(new Point(x,y+i,2));
                    if (getPiece(new Point(x,y+i)) != null) break;
                }
                else break;
            }
            //North East
            for (int i = 1; i+x < 8 && i+y < 8; i++) {
                if (isValidPoint(x+i,y+i)) {
                    validM.add(new Point(x+i,y+i,1));
                    if (getPiece(new Point(x+i,y+i)) != null) break;
                }
                else break;
            }
            //East
            for (int i = 1; i+x < 8; i++) {
                if(isValidPoint(x+i,y)) {
                    validM.add(new Point(x+i,y,6));
                    if (getPiece(new Point(x+i,y)) != null) break;
                }
                else break;
            }
            //South East
            for (int i = 1; i+x < 8 && y-i >= 0; i++) {
                if (isValidPoint(x+i,y-i)) {
                    validM.add(new Point(x+i,y-i,7));
                    if (getPiece(new Point(x+i,y-i)) != null) break;
                }
                else break;
            }
            //South
            for (int i = 1; y-i >= 0; i++) {
                if (isValidPoint(x,y-i)) {
                    validM.add(new Point(x,y-i,8));
                    if (getPiece(new Point(x,y-i)) != null) break;
                }
                else break;
            }
            //South West
            for (int i = 1; x-i >= 0 && y-i >= 0; i++) {
                if (isValidPoint(x-i,y-i)) {
                    validM.add(new Point(x-i,y-i,9));
                    if (getPiece(new Point(x-i,y-i)) != null) break;
                }
                else
                    break;
            }

            //West
            for (int i = 1; x-i >= 0; i++) {
                if (isValidPoint(x-i,y)) {
                    validM.add(new Point(x-i,y,4));
                    if (getPiece(new Point(x-i,y)) != null) break;
                }
                else break;
            }
        }
        return validM;
    }

    public boolean isValidPoint(int x, int y) {
        if (x < 0 || x > 7) return false;
        if (y < 0 || y > 7) return false;
        return true;
    }

    /**
     * Last place for piece validation. Helps review
     * Checkmate by creating a new state each piece.
     * @param pc checks all valid moves of piece
     * @return all valid moves/attacks of piece.
     */
    public List<Point> validMoves(CHSPiece pc) {
        if (!pc.getPlaying()) return null;
        List<Point> moves = possibleMoves(pc, getMoves(pc));
        List<Point> vMoves = new ArrayList<>();
        for (Point move : moves) {
            CHSBoard state = new CHSBoard(this);
            state.movePiece(state.getPiece(pc.getCoords()),move);
            Point kingCoords;
            if (pc.getType() == 0) kingCoords = move;
            else
                kingCoords = new Point(pc.getOwner().getKing().getCoords());

            String opposition = pc.getOwner().getName().equals("Human") ? "Agent" : "Human";

            if (!state.isAttackedBy(kingCoords, opposition))
                vMoves.add(move);

        }
        if (vMoves.size()>0)
            return  vMoves;
        else
            return null;
    }

    /**
     * Simply promotes the piece
     * @param pc is the piece to promote
     * @param t is the type it wants to be
     */
    public void promotePiece(CHSPiece pc, int t) {
        pc.setType(t);
    }

    /**
     * A function for checking if a move will cause check at a certain state.
     * This is reviewed by checking if any piece can currently capture a king.
     * @return true if check, else false
     */
    public boolean isCheck() {
        List<Point> moves = new ArrayList<>();
        CHSPiece[] hCHSPieces = this.getHuman().getPiece();
        CHSPiece[] aCHSPieces = this.getAgent().getPiece();

        //First grabs EVERY possible move in the game from both sides
        for (CHSPiece hCHSPiece : hCHSPieces) {
            if (this.possibleMoves(hCHSPiece, this.getMoves(hCHSPiece)).size() > 0) {
                moves.addAll(this.possibleMoves(hCHSPiece, this.getMoves(hCHSPiece)));
            }
        }
        for (CHSPiece aCHSPiece : aCHSPieces) {
            if (this.possibleMoves(aCHSPiece, this.getMoves(aCHSPiece)).size() > 0) {
                moves.addAll(this.possibleMoves(aCHSPiece, this.getMoves(aCHSPiece)));
            }
        }

        //This will now check if a certain move will eat the king.
        for (Point move : moves) {
            if (getPiece(move) != null && getPiece(move).getType() == 0)
                return true;
        }
        return false;
    }

    /**
     * A function for checking if a move will cause check at a certain state.
     * This is reviewed by checking if any piece can currently capture a king.
     * @param p is the player whose king is in check
     * @return true if check, else false
     */
    public boolean isCheck(String p) {
        List<Point> moves = new ArrayList<>();
        //Piece[] pieces;
        List<CHSPiece> CHSPieces;
        if (p.equals("Agent")) CHSPieces = getPieces("Human");
        else CHSPieces = getPieces("Agent");

        //Grabs every possible move from the opposition
        for (CHSPiece CHSPiece : CHSPieces) {
            moves.addAll(this.possibleMoves(CHSPiece, this.getMoves(CHSPiece)));
        }

        //This will now check if a certain move will eat the king.
        for (Point move : moves) {
            if (getPiece(move) != null && getPiece(move).getType() == 0)
                return true;
        }
        return false;
    }

    /**
     * Checks if there's a single valid move for said piece.
     * This will also filter allied pieces.
     * @param pc gets specific piece to check
     * @return true if there's a valid move, else false.
     * */
    public boolean getValidMove(CHSPiece pc) {
        List<Point> moves = new ArrayList<>();
        moves = validMoves(pc);
        if (moves == null) return false;
        for (Point move : moves) {
            if (getTileEmpty(move)) {
                return true;
            } else if (!getTileEmpty(move) && !isAlly(pc, move)) {
                return true;
            }
        }
        return  false;
    }

    /**
     * Overloaded function to check if a specific point is valid.
     * @param pc gets specific piece to check
     * @param pt gets specific point to check
     * @return true if said point is valid, else false.
     * */
    public boolean getValidMove(CHSPiece pc, Point pt) {
        List<Point> validM = validMoves(pc);
        if (validM == null) return false;

        for (Point point : validM)
            if (point != null && point.equals(pt))
                return true;
        return false;
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

    public int getNumPieces(String play, int t) {
        int nCount = 0;
        if      (play.equals("Human")) {
            for (int i = 0; i < this.human.getAvailablePieces(); i++) {
                if (this.human.getPiece()[i].getType()==t)
                    nCount++;
            }
            return nCount;
        }
        else if (play.equals("Agent")) {
            for (int i = 0; i < this.agent.getAvailablePieces(); i++) {
                if (this.agent.getPiece()[i].getType()==t)
                    nCount++;
            }
            return nCount;
        }
        else
            return -1;
    }

//    public Piece getKing(String p) {
//        if (p.equals("Human")) {
////            human.getp
//        }
//    }

    /**
     * Simply grabs all pieces of a player that has at least one move. (Before filtering for check)
     * @param p player to look into
     * @return list of pieces with one move
     */
    public List<CHSPiece> getPieces(String p) {
        List<CHSPiece> listCHSPiece = new ArrayList<>();
        if (p.equals("Human")) {
            CHSPiece addP;
            for (int i = 0; i < 16; i++) {
                if (this.human.getPiece()[i].getPlaying()) {
                    addP = this.human.getPiece()[i];
                    if(possibleMoves(addP,getMoves(addP)).size() > 0)
                        listCHSPiece.add(addP);
                }
            }
        }
        else {
            CHSPiece addP;
            for (int i = 0; i < 16; i++) {
                if (this.agent.getPiece()[i].getPlaying()) {
                    addP = this.agent.getPiece()[i];
                    if(possibleMoves(addP,getMoves(addP)).size() > 0)
                        listCHSPiece.add(addP);
                }
            }
        }
        return listCHSPiece;
    }

    /**
     * Gets human player in board
     * @return human player
     * */
    public CHSPlayer getHuman()  { return this.human; }

    /**
     * Gets agent player in board
     * @return agent player
     */
    public CHSPlayer getAgent()  { return this.agent; }

    /**
     * Checks what move number the game already is at
     * @return move number the game is at
     */
    public int getMoveNum() { return this.moveNum; }

    public CHSPiece getPiece(Point p) {
        if (this.human.getPiece(p) != null)
            return this.human.getPiece(p);
        else if (this.agent.getPiece(p) != null)
            return this.agent.getPiece(p);
        else
            return null;
    }
    /**
     * Checks if tile is empty
     * @param p point coordinate to check
     * @return true if empty, else false
     * */
    public boolean getTileEmpty(Point p) { return getPiece(p) == null; }

    public boolean isAttackedBy(Point p, String o) {
        List<Point> pieces = new ArrayList<>();
        int x = p.getX();
        int y = p.getY();
        String player = o.equals("Human") ? "Agent" : "Human";

        //Check all dirs for pieces
        //North West
        for (int i = 1; x-i >= 0 && i+y < 8; i++) {
            CHSPiece pc = getPiece(new Point(x-i,y+i));
            if (isValidPoint(x-i,y+i) && pc != null){
                if (pc.getType() == 3 || pc.getType() == 5) {
                    pieces.add(new Point(x-i,y+i,3));
                    break;
                }
                else if (pc.getType() == 0 || pc.getType() == 1 && i==1) {
                    pieces.add(new Point(x-i,y+i,3));
                    break;
                }
                else break;
            }
        }
        //North
        for (int i = 1; i+y < 8; i++) {
            CHSPiece pc = getPiece(new Point(x,y+i));
            if (isValidPoint(x,y+i) && pc != null) {
                if (pc.getType() >= 4) {
                    pieces.add(new Point(x,y+i,2));
                    break;
                }
                else if (pc.getType() == 0 && i==1) {
                    pieces.add(new Point(x,y+i,2));
                    break;
                }
                else break;
            }
        }
        //North East
        for (int i = 1; i+x < 8 && i+y < 8; i++) {
            CHSPiece pc = getPiece(new Point(x+i,y+i));
            if (isValidPoint(x+i,y+i) &&  pc != null) {
                if (pc.getType() == 3 || pc.getType() == 5) {
                    pieces.add(new Point(x + i, y + i, 1));
                    break;
                }
                else if (pc.getType() == 0 || pc.getType() == 1 && i == 1) {
                    pieces.add(new Point(x + i, y + i, 1));
                    break;
                }
                else break;
            }
        }
        //East
        for (int i = 1; i+x < 8; i++) {
            CHSPiece pc = getPiece(new Point(x+i,y));
            if(isValidPoint(x+i,y) && pc != null) {
                if (pc.getType() >= 4) {
                    pieces.add(new Point(x + i, y, 6));
                    break;
                }
                else if (pc.getType() == 0 && i == 1) {
                    pieces.add(new Point(x + i, y, 6));
                    break;
                }
                else break;
            }
        }
        //South East
        for (int i = 1; i+x < 8 && y-i >= 0; i++) {
            CHSPiece pc = getPiece(new Point(x+i,y-i));
            if (isValidPoint(x+i,y-i) &&  pc != null){
                if (pc.getType() == 3 || pc.getType() == 5) {
                    pieces.add(new Point(x+i,y-i,7));
                    break;
                }
                else if (pc.getType() == 0 || pc.getType() == 1 && i==1) {
                    pieces.add(new Point(x+i,y-i,7));
                    break;
                }
                else break;
            }
        }
        //South
        for (int i = 1; y-i >= 0; i++) {
            CHSPiece pc = getPiece(new Point(x,y-i));
            if (isValidPoint(x,y-i) && pc != null) {
                if (pc.getType() >= 4) {
                    pieces.add(new Point(x,y-i,8));
                    break;
                }
                else if (pc.getType() == 0 && i==1) {
                    pieces.add(new Point(x,y-i,8));
                    break;
                }
                else break;
            }
        }
        //South West
        for (int i = 1; x-i >= 0 && y-i >= 0; i++) {
            CHSPiece pc = getPiece(new Point(x-i,y-i));
            if (isValidPoint(x-i,y-i) && pc != null){
                if (pc.getType() == 3 || pc.getType() == 5) {
                    pieces.add(new Point(x-i,y-i,9));
                    break;
                }
                else if (pc.getType() == 0 || pc.getType() == 1 && i==1) {
                    pieces.add(new Point(x-i,y-i,9));
                    break;
                }
                else break;
            }
        }
        //West
        for (int i = 1; x-i >= 0; i++) {
            CHSPiece pc = getPiece(new Point(x-i,y));
            if (isValidPoint(x-i,y) && pc != null) {
                if (pc.getType() >= 4) {
                    pieces.add(new Point(x-i,y,4));
                    break;
                }
                else if (pc.getType() == 0 && i==1) {
                    pieces.add(new Point(x-i,y,4));
                    break;
                }
                else break;
            }
        }

        //Knight's length
        if (isValidPoint(x-2,y+1) && getPiece(new Point(x-2,y+1))!= null && getPiece(new Point(x-2,y+1)).getType() == 2)
            pieces.add(new Point(x-2,y+1));
        if (isValidPoint(x-2,y-1) && getPiece(new Point(x-2,y-1))!= null && getPiece(new Point(x-2,y-1)).getType() == 2 )
            pieces.add(new Point(x-2,y-1));
        if (isValidPoint(x+2,y+1) && getPiece(new Point(x+2,y+1))!= null && getPiece(new Point(x+2,y+1)).getType() == 2)
            pieces.add(new Point(x+2,y+1));
        if (isValidPoint(x+2,y-1) && getPiece(new Point(x+2,y-1))!= null && getPiece(new Point(x+2,y-1)).getType() == 2)
            pieces.add(new Point(x+2,y-1));

        if (isValidPoint(x-1,y+2) && getPiece(new Point(x-1,y+2))!= null && getPiece(new Point(x-1,y+2)).getType() == 2)
            pieces.add(new Point(x-1,y+2));
        if (isValidPoint(x+1,y+2) && getPiece(new Point(x+1,y+2))!= null && getPiece(new Point(x+1,y+2)).getType() == 2)
            pieces.add(new Point(x+1,y+2));
        if (isValidPoint(x-1,y-2) && getPiece(new Point(x-1,y-2))!= null && getPiece(new Point(x-1,y-2)).getType() == 2)
            pieces.add(new Point(x-1,y-2));
        if (isValidPoint(x+1,y-2) && getPiece(new Point(x+1,y-2))!= null && getPiece(new Point(x+1,y-2)).getType() == 2)
            pieces.add(new Point(x+1,y-2));

        //Checking if piece is attacking
        for (int i = 0; i < pieces.size(); i++) {
            CHSPiece attacker = getPiece(pieces.get(i));
            if (attacker.getOwner().getName().equals(player)) continue;

            if (attacker.getType() == 1) {
                if (player.equals("Human")) {
                    Point pawnCoords = attacker.getCoords();
                    if (pawnCoords.equals(new Point(x - 1, y + 1)) || pawnCoords.equals(new Point(x + 1, y + 1)))
                        return true;

                }
                else {
                    Point pawnCoords = attacker.getCoords();
                    if (pawnCoords.equals(new Point(x - 1, y - 1)) || pawnCoords.equals(new Point(x + 1, y - 1)))
                        return true;

                }
            }
            else if (attacker.getType() >= 2)
                return true;
        }
        return false;
    }

    public boolean isGameOver() {

        return false;
    }

    /**
     * Checks if game is over if the number of pieces in play is 0
     * for any side.
     * @param turn is the player who's currently playing
     * @return true if no more pieces, else false
     */
    public boolean isGameOver(String turn) {
        List<CHSPiece> CHSPieces = new ArrayList<>();
        if (turn.equals("Human")) {
            CHSPieces = getPieces("Human");
            for (CHSPiece CHSPiece : CHSPieces) {
                if (validMoves(CHSPiece) == null)
                    return true;
            }
        }
        else {
            CHSPieces = getPieces("Agent");
            for (CHSPiece CHSPiece : CHSPieces) {
                if (validMoves(CHSPiece) == null)
                    return true;
            }
        }

        return false;
    }

    /**
     * Adds moveNumber to indicate it's back to the first player's move
     * signifying a new move number.
     */
    public void nextMove() { this.moveNum += 1; }
}
