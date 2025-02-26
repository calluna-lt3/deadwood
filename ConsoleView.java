import java.util.Scanner;

public class ConsoleView implements View{
    Scanner sc;


    public ConsoleView() {
        sc = new Scanner(System.in);
    }


    public InVec getUserAction() {
        Enums.action action = Enums.action.UNKNOWN;
        String input = null;
        String[] input_tokens = null;

        while (action == Enums.action.UNKNOWN) {
            input = getUserInput();
            input_tokens = input.split(" ", 2);

            if ("help".equals(input_tokens[0])) {
                if (input_tokens.length != 1) {
                    System.out.println("Usage: help");
                } else {
                    action = Enums.action.HELP;
                }
            } else if ("who".equals(input_tokens[0])) {
                if (input_tokens.length != 1) {
                    System.out.println("Usage: who");
                } else {
                    action = Enums.action.WHO;
                }
            } else if ("loc".equals(input_tokens[0])) {
                if (input_tokens.length != 1) {
                    System.out.println("Usage: loc");
                } else {
                    action = Enums.action.LOCATION;
                }
            } else if ("rooms".equals(input_tokens[0])) {
                if (input_tokens.length != 1) {
                    System.out.println("Usage: rooms");
                } else {
                    action = Enums.action.ROOMS;
                }
            } else if ("roles".equals(input_tokens[0])) {
                if (input_tokens.length != 1) {
                    System.out.println("Usage: roles");
                } else {
                    action = Enums.action.ROLES;
                }
            } else if ("takerole".equals(input_tokens[0])) {
                if (input_tokens.length != 2) {
                    System.out.println("Usage: takerole <role number>");
                } else {
                    action = Enums.action.TAKE_ROLE;
                }
            } else if ("move".equals(input_tokens[0])) {
                if (input_tokens.length != 2) {
                    System.out.println("Usage: move <room number>");
                } else {
                    action = Enums.action.MOVE;
                }
            } else if ("upgrade".equals(input_tokens[0])) {
                input_tokens = input.split(" ");
                if (input_tokens.length != 3) {
                    System.out.println("Usage: upgrade <rank> <money|credits>");
                } else {
                    action = Enums.action.UPGRADE;
                }
            } else if ("act".equals(input_tokens[0])) {
                if (input_tokens.length != 1) {
                    System.out.println("Usage: act");
                } else {
                    action = Enums.action.ACT;
                }
            } else if ("rehearse".equals(input_tokens[0])) {
                if (input_tokens.length != 1) {
                    System.out.println("Usage: rehearse");
                } else {
                    action = Enums.action.REHEARSE;
                }
            } else if ("pass".equals(input_tokens[0])) {
                if (input_tokens.length != 1) {
                    System.out.println("Usage: pass");
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


    public String getUserInput() {
        System.out.print("> ");
        return sc.nextLine();
    }


    public String getPlayerName(int i) {
        System.out.println("Enter name for player " + i);
        return getUserInput();
    }


    public int getPlayerCount() {
        int count = -1;
        while (count < 2 || count > 8) {
            System.out.println("Enter number of players (2-8):");
            try {
                String input = getUserInput();
                count = Integer.parseInt(input);
            } catch (Exception e) {
                System.out.println("Player count must be a number");
            }
        }

        return count;
    }


    public void displayInit() {
        System.out.println("Welcome to Deadwood!");
    }


    // Display information
    public void displayHelp() {
        System.out.println("Available actions:\n"
            + "\thelp: View this list\n"
            + "\twho: View current player's information\n"
            + "\tloc: View all player's locations\n"
            + "\trooms: View all rooms adjacent to your current room\n"
            + "\troles: View available roles\n"
            + "\ttakerole <role name>: Take selected role\n"
            + "\tmove <room name>: Move to selected room\n"
            + "\tupgrade <rank> <money|credits>: Upgrade rank using specified currency\n"
            + "\tact: Act!\n"
            + "\trehearse: Rehearse!\n"
            + "\tpass: Pass the turn");
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

        // getRole() is 1 indexed :(
        for (int i=1; i<numRoles+1; i++) {
            Role role = ss.getRole(i);
            String starring = (role.getStarring()) ? "*" : "";
            String taken = (role.getTaken()) ? "-" : "";
            String name = role.getName();
            String rank = Integer.toString(role.getRank());
            String index = Integer.toString(i) + ".";

            System.out.println("\t" + index + taken + name + "| Rank: " + rank + starring);
        }
    }


    // End the game
    public void displayEndGame(Player[] players, int[] scores, String[] winners, int winningScore) {
        boolean plural = (winners.length > 1) ? true : false;

        System.out.println("The game is over!");
        if (plural) {
            System.out.println("Winners with " + winningScore + " points:");
            for (String winner : winners) {
                System.out.println("\t" + winner);
            }
        } else {
            System.out.println("Winner with " + winningScore + " points: " + winners[0]);
        }

        System.out.println("Player scores:");
        for (int i=0; i<players.length; i++) {
            System.out.println("\t" + players[i].getName() + ": " + scores[i]);
        }
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


    public void displayMove(Room room) {
        String name = room.getName();
        System.out.println("You moved to " + name);
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


    public void displayUpgrade(int rank) {
        System.out.println("Upgraded to rank " + Integer.toString(rank));
    }


    public void displayRole(Enums.errno errno) {
        System.out.println("This room doesn't have any roles");
    }


    public void displayAct(boolean success) {
        if (success) {
            System.out.println("You succeeded in acting");
        } else {
            System.out.println("You failed to act");
        }
    }


    public void displayRehearse(Player player) {
        System.out.println("You now have " +
            String.format("%d", player.getRehearsalTokens())
            + "rehearsal tokens");
    }


    public void displayDiceRolls(int... diceRolled) {
        System.out.print("You rolled: ");
        for (int i=0; i<diceRolled.length; i++) {
            if (i > 0) System.out.print(", ");
            System.out.print(diceRolled[i]);
        }
    }


    public void displayMove(Enums.errno errno) {
        switch (errno) {
            case FORBIDDEN_ACTION:
                System.out.println("You can't move right now");
                break;
            case OOB:
                System.out.println("Selected room isn't available");
                break;
            case BAD_ARGS:
                System.out.println("Room number must be a number");
                break;
            default:
                break;
        }
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
                System.out.println("Usage: upgrade <rank> <money|credits>");
                break;
            case BAD_ROOM:
                System.out.println("You are not at the casting office");
                break;
            default:
                break;
        }
    }


    public void displayRehearse(Enums.errno errno) {
        System.out.println("You already have the maximum number rehearsal tokens for this role");
    }


    public void displayAct(Enums.errno errno) {
        System.out.println("You don't have a role");
    }
}


