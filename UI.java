import java.util.Scanner;

public class UI {
    Scanner scanner;

    UI() {
        scanner = new Scanner(System.in);
    }
    public void runUI(Player p) {
        displayStats(p);
        runCommand();
    }
    private void displayStats(Player p) {
        /* Game stats */
        // TEMP player turn
        // day

        /* Player stats */
        // TEMP can move
        // name
        // rank
        // credits
        // money
        // rehearsal rokens
        // location
    }


    private void runCommand() {
        while(true) {
           String cmd = scanner.nextLine().toLowerCase();
           
           if (cmd.equals("help")) {              // game help
           } else if (cmd.equals("end")) {        // end game
                System.exit(1);
           } else if (cmd.equals("who")) {        // active player  
           } else if (cmd.equals("loc")) {        // all player locations
           } else if (cmd.equals("role info")) {  // role information of current set
           } else if (cmd.equals("take role")) {  // take a role
           } else if (cmd.equals("move")) {
           } else if (cmd.equals("upgrade")) {
           } else if (cmd.equals("act")) {
           } else if (cmd.equals("rehearse")) {
           } else if (cmd.equals("pass")) {
           }

        }
    }
}
