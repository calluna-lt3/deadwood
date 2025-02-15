import java.util.Scanner;

public class ConsoleView {
    InputController inCtrl;
    Scanner sc;

    static enum Errno {
        BAD_ROLE,
        NO_CREDITS,
        NO_MONEY,
        MAX_RANK,
        OOB,
        LEQ,
        IN_ROLE,
        CANT_MOVE,
        BAD_ROOM,
    }

    public ConsoleView() {
        sc = new Scanner(System.in);
    }

    public String getUserInput() {
        System.out.print("> ");
        String input = sc.nextLine();
        return input;
    }

    // Display information
    public void displayHelp() {
        System.out.println("Available actions:"
            + "who: View current player's information"
            + "loc: View all player's locations"
            + "roles: View available roles"
            + "takerole <role name>: Take selected role"
            + "move <room name>: Move to selected room"
            + "upgrade <rank> <money|credits>: Upgrade rank using specified currency"
            + "act: Act!"
            + "rehearse: Rehearse!"
            + "pass: Pass the turn");
    }

    public void displayWho(Player player) {
        String name = player.getName();
        String money = Integer.toString(player.getMoney());
        String credits = Integer.toString(player.getCredits());
        String role = player.getRole().getName();
        String line = player.getRole().getLine();

        System.out.println("Active player: " + name
                           + "\tMoney: " + money
                           + "\tCredits: " + credits
                           + "\tJob: " + role + ", " + line);
    }

    public void displayLocations(Player activePlayer, Player[] players) {
        System.out.println("Locations:");
        for (Player player : players) {
            String taken = (player == activePlayer) ? "*" : "";
            String name = player.getName();
            String room = player.getRoom().getName();
            System.out.println("\t" + taken + name + ": " + room);
        }
    }

    public void displayRoles(SoundStage ss) {
        System.out.println("Roles:");

        int numRoles = ss.getRoleCount();
        for (int i=0; i<numRoles; i++) {
            Role role = ss.getRole(i);
            String starring = (role.getStarring()) ? "+" : "";
            String taken = (role.getTaken()) ? "*" : "";
            String name = role.getName();
            String rank = Integer.toString(role.getRank());
            String index = Integer.toString(i+1) + ".";

            System.out.println("\t" + index + taken + name + "| Rank: " + rank + starring);
        }
    }

    // End the game
    public void displayEndGame() {
        System.out.println("End game");
    }

    // takes next player as parameter
    public void displayPassTurn(Player player) {
        System.out.println("Passed turn, it is now " + player.getName() + "'s turn");
    }

    public void displayTakeRole(Role role) {
        String name = role.getName();
        String line = role.getLine();
        System.out.println("You took role " + name + ", " + line);
    }


    public void displayTakeRole(Errno errno) {
        switch (errno) {
            case BAD_ROLE:
                System.out.println("Role does not exist");
                break;
            case BAD_ROOM:
                System.out.println("Room has no roles");
                break;
            case IN_ROLE:
                System.out.println("You already have a role");
                break;
            default:
                break;
        }
    }


    public void displayMove(Room room) {
        if (room == null) {
            System.out.println("Room does not exist");
        } else {
            String name = room.getName();
            System.out.println("You moved to " + name);
        }
    }


    public void displayMove(Errno errno) {
        switch (errno) {
            case BAD_ROOM:
                System.out.println("Room isn't available");
                break;
            case CANT_MOVE:
                System.out.println("You can't move");
                break;
            default:
                break;
        }
    }

    public void displayUpgrade(int rank) {
        System.out.println("Upgraded to rank " + Integer.toString(rank));
    }

    public void displayUpgrade(Errno errno) {
        switch (errno) {
            case NO_CREDITS:
                System.out.println("Not enough credits");
                break;
            case NO_MONEY:
                System.out.println("Not enough money");
                break;
            case MAX_RANK:
                System.out.println("Already max rank");
                break;
            case OOB:
                System.out.println("Invalid input, accepted values between 2-6");
                break;
            case LEQ:
                System.out.println("Requested rank less than or equal to your current rank");
                break;
            default:
                break;
        }
    }

    public void displayRehearse(Player player) {
        System.out.println("You now have "); // TODO
    }


    public void displayAct() {
        System.out.println("Acting!"); // TODO
    }
}
