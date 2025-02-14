import java.util.Scanner;

public class ConsoleView {
    InputController inCtrl;
    Scanner sc;

    public ConsoleView() {
        sc = new Scanner(System.in);
    }

    // Display information
    public void displayHelp() {}
    public void displayWho() {
        System.out.println("Current player: " + inCtrl.currentPlayer());
    }
    public void displayLocations() {
        
    }
    public void displayRole() {}

    // End the game
    public void endGame() {}
    public void passTurn() {}

    // Failable actions (return 0 on success, non-zero code on fail)
    public int takeRole(int role) {}
    public int move(int room) {}
    public int upgrade(boolean currency, int rank) {}
    public boolean rehearse() {}

    // Stochastic actions (return the result of a dice check)
    public int act() {}
}