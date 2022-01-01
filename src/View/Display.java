package View;

import Model.Checkers.CHKBoard;
import Model.Chess.CHSBoard;
import Model.Point;

import java.util.Scanner;

public class Display {
    CHKBoard CHKBoard;
    CHSBoard CHSBoard;
    String hMove;
    String aMove;
    int hValue;

    public Display(CHKBoard b) {
        this.CHKBoard = b;
    }
    public Display(CHSBoard b) {
        this.CHSBoard = b;
    }

    /**
     * Creates a message prompt before moving to next line.
     * @param message string type to read to player
     * @return scanner value received
     * */
    public String prompt(String message) {
        Scanner sc = new Scanner(System.in);
        int mLength = message.length();

        //printHLine(mLength);
        System.out.print(Colors.RESET+"\n"+message+" : ");
        String ans = sc.nextLine();
        //printHLine(mLength);
        //sc.close();
        return ans;
    }

    /**
     * Read Function, creates a message prompt
     * (READ-ONLY) before moving to next line.
     * @param message string type to read to player
     * */
    public void read(String message) {
        int mLength = message.length();

        printHLine(mLength);
        System.out.print("\n"+message);
        printHLine(mLength);

    }

    /**
     * Read function without emphasis. Merely a shorthand
     * function for printing in java due to the convolution
     * of System.out.print();
     * @param message is the message to print
     */
    public void readClean(String message) {
        System.out.print("\n"+message);
    }

    /**
     * Prints the main board display of the game
     */
    public void printBoardCHK() {
        if (CHKBoard.getMoveNum() > 1) {
            System.out.println("\n -- Move "+CHKBoard.getMoveNum()+" -- ");
            System.out.println("Human Move: "+Colors.BLUE+hMove+Colors.RESET);
            System.out.println("Agent Move: "+Colors.BLUE+aMove+Colors.RESET);
            System.out.println("My heuristics told me: "+hValue);
            if (hValue > 0)
                System.out.println(Colors.CYAN+"I am winning."+Colors.RESET);
            else if (hValue < 0)
                System.out.println(Colors.YELLOW+"I am losing."+Colors.RESET);
            else
                System.out.println(Colors.GREEN+"It is anyone's game."+Colors.RESET);
        }
        else
            System.out.println(Colors.GREEN+"\nLet's have a fun match."+Colors.RESET);

        for (int i = 7; i >= 0; i--) {
            printHLine();
            System.out.print("\n  ");
            for (int j = 0; j < 8; j++) {
                String tileC = Colors.RESET;
                if((i+j)%2!=0)
                    tileC = Colors.RESET;
                else
                    tileC = Colors.BLUE;

                if (CHKBoard.getPiece(new Point(j,i)) != null)
                    System.out.print(CHKBoard.getPiece(new Point(j,i)).toString()+"  ");
                else
                    System.out.print(tileC+"·  ");
            }
            System.out.print(Colors.RESET+"| "+Colors.BLUE+(i+1)+Colors.RESET);
        }
        printHLine();
        System.out.print(Colors.BLUE+"\n  A  B  C  D  E  F  G  H\n"+Colors.RESET);
    }

    /**
     * Prints the main board display of the game
     */
    public void printBoardCHS() {
        if (CHSBoard.getMoveNum() > 1) {
            System.out.println("\n -- Move "+ CHSBoard.getMoveNum()+" -- ");
            System.out.println("Human Move: "+Colors.BLUE+hMove+Colors.RESET);
            System.out.println("Agent Move: "+Colors.BLUE+aMove+Colors.RESET);
            System.out.println("My heuristics told me: "+hValue);
            if (hValue > 400)
                System.out.println(Colors.CYAN+"I am winning."+Colors.RESET);
            else if (hValue < 0)
                System.out.println(Colors.YELLOW+"I am losing."+Colors.RESET);
            else
                System.out.println(Colors.GREEN+"It is anyone's game."+Colors.RESET);
        }
        else
            System.out.println(Colors.GREEN+"Let's have a fun match."+Colors.RESET);

        for (int i = 7; i >= 0; i--) {
            printHLine();
            System.out.print("\n  ");
            for (int j = 0; j < 8; j++) {
                String tileC = Colors.RESET;
                if((i+j)%2!=0)
                    tileC = Colors.RESET;
                else
                    tileC = Colors.BLUE;

                if (CHSBoard.getPiece(new Point(j,i)) != null)
                    System.out.print(CHSBoard.getPiece(new Point(j,i)).toString()+"  ");
                else
                    System.out.print(tileC+"·  ");
            }
            System.out.print(Colors.RESET+"| "+Colors.BLUE+(i+1)+Colors.RESET);
        }
        printHLine();
        System.out.print(Colors.BLUE+"\n  A  B  C  D  E  F  G  H\n"+Colors.RESET);
    }

    /**
     * Prints a horizontal line the same size as the board
     * */
    public void printHLine() {
        System.out.print(Colors.RESET+"\n---------------------------");
    }

    /**
     * Prints a horizontal line of a certain length
     * @param l size of line
     * */
    public void printHLine(int l) {
        System.out.print("\n");
        for (int i = 0; i < l; i++)
            System.out.print("-");
    }

    public void setBoard(CHKBoard b) {this.CHKBoard = b;}
    public void setBoard(CHSBoard b) {this.CHSBoard = b;}
    public void sethMove(String m) {this.hMove = m;}
    public void setaMove(String m) {this.aMove = m;}
    public void sethValue(int h) {this.hValue = h;}
}
