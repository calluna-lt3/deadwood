import java.util.Scanner;

public class ConsoleView implements View{
    Scanner sc;

    public ConsoleView() {
        sc = new Scanner(System.in);
    }

    public String getUserInput() {
        System.out.print("> ");
        return sc.nextLine();
    }

    public String getPlayerName(int i) {
        System.out.println("Enter name for player " + i);
        return getUserInput();
    }

    public InVec getUserAction() {
        Enums.action action = Enums.action.UNKNOWN;
        String input = null;
        String[] input_tokens = null;

        while (action == Enums.action.UNKNOWN) {
            input = getUserInput();
            input_tokens = input.split(" ", 2);

            if ("who".equals(input_tokens[0])) {
                if (input_tokens.length != 1) {
                    // TODO
                } else {
                    action = Enums.action.WHO;
                }
            } else if ("loc".equals(input_tokens[0])) {
                if (input_tokens.length != 1) {
                    // TODO
                } else {
                    action = Enums.action.LOCATION;
                }
            } else if ("rooms".equals(input_tokens[0])) {
                if (input_tokens.length != 1) {
                    // TODO
                } else {
                    action = Enums.action.ROOMS;
                }
            } else if ("roles".equals(input_tokens[0])) {
                if (input_tokens.length != 1) {
                    // TODO
                } else {
                    action = Enums.action.ROLES;
                }
            } else if ("takerole".equals(input_tokens[0])) {
                if (input_tokens.length != 2) {
                    // TODO
                } else {
                    action = Enums.action.TAKE_ROLE;
                }
            } else if ("move".equals(input_tokens[0])) {
                if (input_tokens.length != 2) {
                    // TODO
                } else {
                    action = Enums.action.MOVE;
                }
            } else if ("upgrade".equals(input_tokens[0])) {
                input_tokens = input.split(" ");
                if (input_tokens.length != 3) {
                    // TODO
                } else {
                    action = Enums.action.UPGRADE;
                }
            } else if ("act".equals(input_tokens[0])) {
                if (input_tokens.length != 1) {
                    // TODO
                } else {
                    action = Enums.action.ACT;
                }
            } else if ("rehearse".equals(input_tokens[0])) {
                if (input_tokens.length != 1) {
                    // TODO
                } else {
                    action = Enums.action.REHEARSE;
                }
            } else if ("pass".equals(input_tokens[0])) {
                if (input_tokens.length != 1) {
                    // TODO
                } else {
                    action = Enums.action.PASS;
                }
            }
        }

        // Assumes valid length
        InVec vec = null;
        switch (input_tokens.length) {
            case 1:
            vec = new InVec(action, null, null);
            break;
            case 2:
            vec = new InVec(action, input_tokens[1], null);
            break;
            case 3:
            vec = new InVec(action, input_tokens[1], input_tokens[2]);
        }

        return vec;
    }

    public void displayInit() {
        System.out.println("Welcome to Deadwood!\n"
            + "Enter a number of players");
    }

    // Display information
    public void displayHelp() {
        System.out.println("Available actions:"
            + "who: View current player's information\n"
            + "loc: View all player's locations\n"
            + "rooms: View all rooms adjacent to your current room\n"
            + "roles: View available roles\n"
            + "takerole <role name>: Take selected role\n"
            + "move <room name>: Move to selected room\n"
            + "upgrade <rank> <money|credits>: Upgrade rank using specified currency\n"
            + "act: Act!\n"
            + "rehearse: Rehearse!\n"
            + "pass: Pass the turn");
    }

    public void displayWho(Player player) {
        String name = player.getName();
        String money = Integer.toString(player.getMoney());
        String credits = Integer.toString(player.getCredits());
        String role = (player.getRole() != null) ? player.getRole().getName() : "none";
        String line = (player.getRole() != null) ? player.getRole().getLine() : "";

        System.out.println("Active player: " + name
                           + "\n\tMoney: " + money
                           + "\n\tCredits: " + credits
                           + "\n\tJob: " + role + ", " + line);
    }

    public void displayLocations(Player activePlayer, Player[] players) {
        System.out.println("Locations:");
        for (Player player : players) {
            String current = (player == activePlayer) ? "*" : "";
            String name = player.getName();
            String room = player.getRoom().getName();
            System.out.println("\t" + current + name + ": " + room);
        }
    }

    public void displayRooms(Room room) {
        System.out.println("Adjacent Rooms:");
        int i = 0;
        for(Room r : room.getAdjacentRooms()) {
            i++;
            System.out.println("\t" + i + ". " + r.getName());
        }
    }

    public void displayRole(SoundStage ss) {
        System.out.println("Roles:");

        int numRoles = ss.getRoleCount();
        for (int i=0; i<numRoles; i++) {
            Role role = ss.getRole(i);
            String starring = (role.getStarring()) ? "*" : "";
            String taken = (role.getTaken()) ? "-" : "";
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

    
    public void displayTakeRole(Enums.errno errno) {
        switch (errno) {
            case OOB:
                System.out.println("Role does not exist");
                break;
            case BAD_ROOM:
                System.out.println("Room has no roles");
                break;
            case IN_ROLE:
                System.out.println("You already have a role");
                break;
            case BAD_ARGS:
                System.out.println("Role index must be a number");
            default:
                break;
        }
    }


    public void displayMove(Room room) {
        String name = room.getName();
        System.out.println("You moved to " + name);
    }

    public void displayRole(Enums.errno errno) {
        System.out.println("This room doesn't have any roles");
    }

    public void displayAct(Enums.errno errno) {
        System.out.println("You don't have a role");
    }

    public void displayMove(Enums.errno errno) {
        switch (errno) {
            case FORBIDDEN_ACTION:
                System.out.println("You can't move right now");
                break;
            case OOB:
                System.out.println("Room isn't available");
                break;
            case BAD_ARGS:
                System.out.println("Improper arguments for command upgrade");
                break;
            default:
                break;
        }
    }

    public void displayUpgrade(int rank) {
        System.out.println("Upgraded to rank " + Integer.toString(rank));
    }

    public void displayUpgrade(Enums.errno errno) {
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
            case BAD_ARGS:
                System.out.println("Improper arguments for command upgrade");
                break;
            case BAD_ROOM:
                System.out.println("You are not at the casting office");
                break;
            default:
                break;
        }
    }

    public void displayRehearse(Player player) {
        System.out.println("You now have " +
            String.format("%d", player.getRehearsalTokens())
            + "rehearsal tokens");
    }

    public void displayRehearse(Enums.errno errno) {
        System.out.println("You already have the maximum number rehearsal"
            + "tokens for this role");
    }


    public void displayAct() {
        System.out.println("Acting!"); // TODO
    }
}
