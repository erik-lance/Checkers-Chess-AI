import Controller.CHKGameManager;
import Controller.CHSGameManager;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        CHKGameManager CHK = null;
        CHSGameManager CHS = null;
        boolean start = false;
        String config;

        System.out.print("Which board setup to use? (CHK - 0 / CHS - 1): ");
        config = sc.nextLine();

        while (!start) {
            if (config.charAt(0) == '0') {
                CHK = new CHKGameManager();
                start = true;
            } else if (config.charAt(0) == '1') {
                CHS = new CHSGameManager();
                start = true;
            } else {
                System.out.print("\nWhich board setup to use? (CHK - 0 / CHS - 1): ");
                config = sc.nextLine();
            }
        }
        if      (CHK != null) CHK.startGame();
        else CHS.startGame();
    }
}

