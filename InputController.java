import java.util.ArrayList;
import java.util.Random;

import javax.management.RuntimeErrorException;

public class InputController {
    View v;
    Moderator mod;

    InputController(Moderator mod, View v) {
        this.mod = mod;
        this.v = v;
    }

    // Used by View.displayWho()
    public Player currentPlayer() {
        return mod.getCurrentPlayer();
    }

    // Money is false, credits is true
    public int requestRankUp(boolean currency, int rank)  {
        int[] money_vals = {4, 10, 18, 28, 40};
        int[] credit_vals = {5, 10, 15, 20, 25};
        Player currPlayer = currentPlayer();

        // Check if rank is max
        if (currPlayer.getRank() == 6) {
            System.out.println("Maximum rank already attained.");
            // Fail code 1
            return 1;
        }

        // Check if requested rank is <= than current
        if (currPlayer.getRank() <= rank) {
            System.out.println("Requested rank is less than or equal to current rank.");
            // Fail code 2
            return 2;
        }

        // Check if requested rank is within bounds
        if (rank < 2 || rank > 6) {
            System.out.println("Requested rank is out of bounds, minimum 2 maximum 6.");
            // Fail code 3
            return 3;
        }

        // Check currency count and subtract if sufficient
        if (currency == true) {
            if (credit_vals[rank - 2] > currPlayer.getCredits()) {
                System.out.println("Not enough credits for chosen rank.");
                // Fail code 4
                return 4;
            }
            currPlayer.setCredits(currPlayer.getCredits() - credit_vals[rank - 2]);
        } else {
            if (money_vals[rank - 2] > currPlayer.getMoney()) {
                System.out.println("Not enough money for chosen rank.");
                // Fail code 5
                return 5;
            }
            currPlayer.setMoney(currPlayer.getMoney() - money_vals[rank - 2]);
        }

        currPlayer.setRank(rank);
        System.out.println(String.format("Upgraded to rank %d\n", rank));
        // Return 0
        return 0;
    }

    public int requestMove(int room) {
        Player currPlayer = currentPlayer();

        // Check if player can move
        if (currPlayer.getCanMove() == false) {
            System.out.println("Already moved this turn");
            // Fail code 1
            return 1;
        }

        // Check if room index is out of bounds
        if (room < 1 || room > currPlayer.getRoom().getAdjacentCount()) {
            System.out.println("Requested room not valid");
            // Fail code 2
            return 2;
        }

        currPlayer.setRoom(currPlayer.getRoom().getAdjacentRooms().get(room - 1));
        // Return 0
        return 0;
    }

    public int requestRole(int role) {
        Player currPlayer = currentPlayer();

        // Check if currently in a role
        if (currPlayer.getRole() != null) {
            System.out.println("Cannot take a role while already working one");
            // Fail code 1
            return 1;
        }

        // Check if room is SoundStage
        if (currPlayer.getRoom() instanceof InertRoom) {
            System.out.println("No roles to take on this space");
            // Fail code 2
            return 2;
        }

        // Check if index is out of bounds
        if (role < 1 || role > ((SoundStage) currPlayer.getRoom()).getRoleCount()) {
            System.out.println("Requested role out of bounds");
            // Fail code 3
            return 3;
        }

        ((SoundStage) currPlayer.getRoom()).getRole(role).setTaken(true);
        currPlayer.setRole(((SoundStage) currPlayer.getRoom()).getRole(role));
        // Return 0
        return 0;
    }

    // needs to update shot markers
    public boolean requestAct() {
        Player currPlayer = currentPlayer();
        Random rand = new Random(); // might need to get moved, hard to visualize from here

        if (rand.nextInt(6) + 1 < ((SoundStage) currPlayer.getRoom()).getCardBudget()) {
            if (currPlayer.getRole().getStarring() == false) {
                currPlayer.setMoney(currPlayer.getMoney() + 1);
            }
            return false;
        }

        // On-card role
        if (currPlayer.getRole().getStarring() == true) {
            currPlayer.setCredits(currPlayer.getCredits() + 2);
        // Off-card role
        } else {
            currPlayer.setMoney(currPlayer.getMoney() + 1);
            currPlayer.setCredits(currPlayer.getCredits() + 1);
        }
        return true;
    }

    public boolean requestRehearse()  {
        Player currPlayer = currentPlayer();

        if (currPlayer.getRehearsalTokens() + 1 == ((SoundStage) currPlayer.getRoom()).getCardBudget()) {
            System.out.println("Already at maximum rehearsal tokens");
            return false;
        }
        currPlayer.setRehearsalTokens(currPlayer.getRehearsalTokens() + 1);
        return true;
    }

    public void requestPassTurn() {
        currentPlayer().setCanMove(true);
    }

    // main game loop
    public void run() {
        initializeGame();
        while (mod.getDay() <= mod.getLastDay()) {
            startDay();
            while (mod.getTurn() < mod.getPlayerCount()) {
                takeTurn();
                mod.setTurn(mod.getTurn() + 1);
            }
            endDay();
        }
        endGame();
    }

    private void takeTurn() {
        InVec args = v.getUserInput();
        Enums.action action = args.action();
        String arg1 = args.arg1();
        String arg2 = args.arg2();

        switch (action) {
            case WHO:
                v.displayWho(mod.getCurrentPlayer());
            break;
            case LOCATION:
                v.displayLocations(mod.getCurrentPlayer(), mod.getPlayers());
            break;
            case ROLES:
                if (mod.getCurrentPlayer().getRoom() instanceof InertRoom) {
                    v.displayRole(Enums.errno.BAD_ROOM);

                } else {
                    v.displayRole((SoundStage) mod.getCurrentPlayer().getRoom());
                }
            break;
            case TAKE_ROLE:
                
            break;
            case MOVE:

            break;
            case UPGRADE:

            break;
            case ACT:

            break;
            case REHEARSE:

            break;
            case PASS:

            break;
            default:
            System.exit(1);
            break;
        }
    }

    private void initializeGame() {

        // TODO: call controller to get playercount, valid range: [2,8]
        v.displayInit();
        // TODO Get player count

        Random rand = new Random();
        mod.setTurn(rand.nextInt(mod.getPlayerCount()));

        int startCredits = 1;
        int startRank = 1;
        switch (mod.getPlayerCount()) {
            case 2: case 3:
                mod.setLastDay(3);
                break;
            case 5:
                startCredits = 2;
                break;
            case 6:
                startCredits = 4;
                break;
            case 7: case 8:
                startRank = 2;
                break;
            default:
                break;
        }

        for (int i=0; i<mod.getPlayerCount(); i++) {
            // TODO: call controller to get name
            mod.setPlayer(i, new Player("TEMP", startCredits, startRank));
        }
    }

    private void startDay() {
        mod.setDay(mod.getDay() + 1);

        Room trailer = null;
        for (Room r : mod.getBoard().getRooms()) {
            if (r.getName().equals("trailer")) {
                trailer = r;
                break;
            }
        }
        for (Player p : mod.getPlayers()) p.setRoom(trailer);
        assignScenes();
    }

    public void assignScenes() {
        for (Room r : mod.board.getRooms()) {
            if (r instanceof InertRoom) {
                continue;
            }
            ((SoundStage) r).setCard(mod.deck.draw());
        }
    }

    public void endDay() {
        mod.board.resetShotMarkers();
    }

    // calculate scores, determines winner, etc.
    private void endGame() {
        String[] winners = new String[0];
        int winnerIndex = 0;
        int highestScore = 0;

        for (Player p : mod.getPlayers()) {
            int score = 0;
            score += p.getCredits();
            score += p.getMoney();
            score += (5 * p.getRank());

            if (score > highestScore) {
                highestScore = score;
                winnerIndex = 0;
                winners = new String[0];
                winners[winnerIndex] = p.getName();
            } else if (score == highestScore) {
                winnerIndex++;
                winners[winnerIndex] = p.getName();
            }
        }

        // TODO: call controller to display winners

    }
}
