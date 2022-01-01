package Model;

/**
 * A class that houses coordinates for a certain object.
 * */
public class Point {
    int x;
    int y;
    int dir;

    /**
     * Instantiates a point for debugging coordinates
     * of a certain object.
     * @param x horizontal value of point
     * @param y   vertical value of point
     * */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(int x, int y, int d) {
        this.x = x;
        this.y = y;
        this.dir = d;
    }

    /**
     * Converts a letter into numerical values.
     * @param x letter form of coordinate in horizontal case
     * @param y vertical value of point
     * */
    public Point (char x, int y) {
        this.x = Character.toLowerCase(x) - 97;
        this.y = y;
    }

    public Point (String p) {
        this.x = Character.toLowerCase(p.charAt(0)) - 97;
        this.y = Integer.parseInt(String.valueOf(p.charAt(1)))-1;

    }

    /**
     * Overloaded constructor for cloning.
     * @param clone point object to copy
     * */
    public Point (Point clone) {
        this.x = clone.getX();
        this.y = clone.getY();
        this.dir = clone.dir;
    }

    /**
     * Gets the x coordinate of the point
     * @return x (horizontal) value
     * */
    public int getX() { return x; }

    /**
     * Gets the y coordinate of the point
     * @return y (vertical) value
     * */
    public int getY() { return y; }

    /**
     * Checks if this coordinate is valid on the board.
     * @return true if valid, false if out of bounds.
     * */
    public boolean isValidPoint() {
        if (x < 0 || x > 7) return false;
        return y >= 0 && y <= 7;
    }

    /**
     * Checks the direction (if any)
     * 1/2/3
     * 4/-/6
     * 7/8/9
     * @return direction (if any)
     */
    public int getDir() {return dir;}

    public void setDir(int d) {this.dir = d;}

    /**
     * Translates the coordinates into chess notation.
     * @return chess notation of coordinates in String format
     * */
    public String getChessNotation() {
        int cX = Character.toLowerCase(x) - 97;
        return Integer.toString(cX)+y;
    }

    /**
     * Sets the points of the coordinate manually
     * @param x is the horizontal value
     * @param y is the   vertical value
     * */
    public void setPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Checks if two coordinates are the same
     * @param c of class point to cross reference
     * @return true if same, else false
     * */
    public boolean equals(Point c) {
        return this.x == c.getX() && this.y == c.getY();
    }

    /**
     * Checks if two coordinates are the same by chess notation.
     * @param cN is a string chess notation to cross reference.
     * @return true if same, else false
     * */
    public boolean equals(String cN) {
        char cX = (char) (this.x + 97);
        int pY = this.y+1;

        String cNot = cX+Integer.toString(pY);
        return cNot.equals(cN);
    }
    /**
     * Displays a debug of the X/Y coordinates of this object.
     * @return debug string format of coordinates
     * */
    @Override
    public String toString() { return "X: "+(this.x+1)+" Y: "+(this.y+1); }

    /**
     * Displays a debug of the X/Y coordinates of this object.
     * @return debug string format of coordinates
     * */
    public String toStringC() {
        char cX = (char) (this.x+97);
        return cX+Integer.toString(this.y+1);
    }
}
