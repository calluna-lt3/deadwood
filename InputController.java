import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class InputController {
    View v;
    Moderator mod;
    boolean canMove;

    InputController(View v) {
        this.v = v;
        canMove = true;
    }


    // Money is false, credits is true
    public int requestRankUp(int rank, boolean currency)  {
        int[] money_vals = {4, 10, 18, 28, 40};
        int[] credit_vals = {5, 10, 15, 20, 25};
        Player currPlayer = mod.getCurrentPlayer();

        if (!currPlayer.getRoom().getName().equals("office")) {
            return -6;
        }

        // Check if rank is max
        if (currPlayer.getRank() == 6) {
            return -1;
        }

        // Check if requested rank is <= than current
        if (currPlayer.getRank() > rank) {
            return -2;
        }

        // Check if requested rank is within bounds
        if (rank < 2 || rank > 6) {
            return -3;
        }

        // Check currency count and subtract if sufficient
        if (currency == true) {
            if (credit_vals[rank - 2] > currPlayer.getCredits()) {
                return -4;
            }
            currPlayer.setCredits(currPlayer.getCredits() - credit_vals[rank - 2]);
        } else {
            if (money_vals[rank - 2] > currPlayer.getMoney()) {

                return -5;
            }
            currPlayer.setMoney(currPlayer.getMoney() - money_vals[rank - 2]);
        }

        currPlayer.setRank(rank);
        return 0;
    }

    public int requestMove(int room) {
        Player currPlayer = mod.getCurrentPlayer();

        // Check if player can move
        if (canMove == false) {
            return -1;
        }

        // Check if room index is out of bounds
        if (room < 1 || room > currPlayer.getRoom().getAdjacentCount()) {
            return -2;
        }

        currPlayer.setRoom(currPlayer.getRoom().getAdjacentRooms().get(room - 1));
        // Return 0
        return 0;
    }

    public int requestRole(int role) {
        Player currPlayer = mod.getCurrentPlayer();

        // Check if currently in a role
        if (currPlayer.getRole() != null) {
            return -1;
        }

        // Check if room is SoundStage
        if (currPlayer.getRoom() instanceof InertRoom) {
            return -2;
        }

        // Check if index is out of bounds
        if (role < 1 || role > ((SoundStage) currPlayer.getRoom()).getRoleCount()) {
            return -3;
        }

        ((SoundStage) currPlayer.getRoom()).getRole(role).setTaken(true);
        currPlayer.setRole(((SoundStage) currPlayer.getRoom()).getRole(role));
        return 0;
    }

    // needs to update shot markers
    public int requestAct() {
        Random rand = new Random();
        Player currPlayer = mod.getCurrentPlayer();

        if (currPlayer.getRole() == null) {
            return -1;
        }


        // Failure case
        int dieResult = rand.nextInt(6);
        v.displayDiceRolls(dieResult);
        if (dieResult + currPlayer.getRehearsalTokens() < ((SoundStage) currPlayer.getRoom()).getCardBudget()) {
            if (currPlayer.getRole().getStarring() == false) {
                currPlayer.setMoney(currPlayer.getMoney() + 1);
            }

            return 1;
        }

        // On-card role
        if (currPlayer.getRole().getStarring() == true) {
            currPlayer.setCredits(currPlayer.getCredits() + 2);
        // Off-card role
        } else {
            currPlayer.setMoney(currPlayer.getMoney() + 1);
            currPlayer.setCredits(currPlayer.getCredits() + 1);
        }

        if (((SoundStage) currPlayer.getRoom()).decrementShotMarkers()) {
            boolean starring = false;
            ArrayList<Player> extraPlayers = new ArrayList<Player>();
            LinkedList<Player> starringPlayers = new LinkedList<Player>();

            for (Player p : mod.getPlayers()) {
                if (p.getRole() != null && currPlayer.getRoom().getName().equals(p.getRoom().getName())) {
                    if (!p.getRole().getStarring()) {
                        extraPlayers.add(p);
                        continue;
                    }
                    starring = true;

                    // Descending order, >1 element in list
                    int i = 0;
                    for (; starringPlayers.get(i).getRole().getRank() > p.getRole().getRank(); i++);
                    starringPlayers.add(i, p);

                    if (starringPlayers.size() == 0) starringPlayers.add(p);
                }
            }

            if (starring) {
                int budget = ((SoundStage) currPlayer.getRoom()).getCardBudget();
                int[] diceResults = new int[budget];
                for (int i=0; i<budget; i++) {
                    diceResults[i] = rand.nextInt(6);
                }
                v.displayDiceRolls(diceResults);

                for (int i = 0; i < diceResults.length; i++) {
                    Player p = starringPlayers.get(i % starringPlayers.size());
                    p.setMoney(p.getMoney() + diceResults[i]);
                }

                for (int i = 0; i < extraPlayers.size(); i++) {
                    Player p = extraPlayers.get(i);
                    p.setMoney(p.getMoney() + p.getRole().getRank());
                }
            }

            ((SoundStage) currPlayer.getRoom()).setCard(null);
        }

        return 0;
    }

    public boolean requestRehearse()  {
        Player currPlayer = mod.getCurrentPlayer();

        if (currPlayer.getRehearsalTokens() + 1 == ((SoundStage) currPlayer.getRoom()).getCardBudget()) {
            return false;
        }
        currPlayer.setRehearsalTokens(currPlayer.getRehearsalTokens() + 1);
        return true;
    }

    // main game loop
    public void run() {
        initializeGame();
        while (mod.getDay() <= mod.getLastDay()) {
            startDay();
            while (mod.getCardCount() > 1) {
                takeTurn();
                mod.setTurn(mod.getTurn() + 1);
            }

            endDay();
        }

        endGame();
    }

    private void takeTurn() {
        if (mod.getCurrentPlayer().getRole() != null) {
            canMove = false;
        }

        boolean pass = false;
        while (pass == false) {
            InVec args = v.getUserAction();
            Enums.action action = args.action();
            String arg1 = args.arg1();
            String arg2 = args.arg2();

            switch (action) {
                case HELP:
                    v.displayHelp();
                    break;
                case WHO:
                    v.displayWho(mod.getCurrentPlayer());
                    break;
                case LOCATION:
                    v.displayLocations(mod.getCurrentPlayer(), mod.getPlayers());
                    break;
                case ROOMS: // ONLY IN CONSOLE VIEW
                    ((ConsoleView) v).displayRooms(mod.getCurrentPlayer().getRoom());
                    break;
                case ROLES:
                    if (mod.getCurrentPlayer().getRoom() instanceof InertRoom) {
                        v.displayRole(Enums.errno.BAD_ROOM);
                    } else {
                        v.displayRole((SoundStage) mod.getCurrentPlayer().getRoom());
                    }
                    break;
                case TAKE_ROLE:
                    try {
                        int result = requestRole(Integer.parseInt(arg1));
                        switch (result) {
                            case 0:
                                v.displayTakeRole(mod.getCurrentPlayer().getRole());
                                break;
                            case -1:
                                v.displayTakeRole(Enums.errno.IN_ROLE);
                                break;
                            case -2:
                                v.displayTakeRole(Enums.errno.BAD_ROOM);
                                break;
                            case -3:
                                v.displayTakeRole(Enums.errno.OOB);
                                break;
                        }
                    }
                    catch (NumberFormatException e) {
                        v.displayRole(Enums.errno.BAD_ARGS);
                    }
                    break;
                case MOVE:
                    try {
                        int result = requestMove(Integer.parseInt(arg1));
                        switch (result) {
                            case 0:
                                v.displayMove(mod.getCurrentPlayer().getRoom());
                                break;
                            case -1:
                                v.displayMove(Enums.errno.FORBIDDEN_ACTION);
                                break;
                            case -2:
                                v.displayMove(Enums.errno.OOB);
                            default:
                                break;
                        }
                    } catch (Exception e) {
                        v.displayMove(Enums.errno.BAD_ARGS);
                    }
                    break;
                case UPGRADE:
                    try {
                        if (!("money".equals(arg2) || "credits".equals(arg2))) {
                            v.displayUpgrade(Enums.errno.BAD_ARGS);
                            break;
                        }

                        int result = requestRankUp(Integer.parseInt(arg1), "credits".equals(arg2));
                        switch (result) {
                            case 0:
                                v.displayUpgrade(mod.getCurrentPlayer().getRank());
                                break;
                            case -1:
                                v.displayUpgrade(Enums.errno.MAX_RANK);
                                break;
                            case -2:
                                v.displayUpgrade(Enums.errno.LEQ);
                                break;
                            case -3:
                                v.displayUpgrade(Enums.errno.OOB);
                                break;
                            case -4:
                                v.displayUpgrade(Enums.errno.NO_CREDITS);
                                break;
                            case -5:
                                v.displayUpgrade(Enums.errno.NO_MONEY);
                                break;
                            case -6:
                                v.displayUpgrade(Enums.errno.BAD_ROOM);
                                break;
                        }
                    }
                    catch (NumberFormatException e) {
                        v.displayUpgrade(Enums.errno.BAD_ARGS);
                    }
                    break;
                case ACT:
                    int result = requestAct();
                    switch (result) {
                        case 0:
                            v.displayAct(true);
                            break;
                        case 1:
                            v.displayAct(false);
                            break;
                        case -1:
                            v.displayAct(Enums.errno.FORBIDDEN_ACTION);
                            break;
                        default:
                            System.out.println("fatal error");
                            System.exit(1);
                            break;
                    }
                    break;
                case REHEARSE:
                    if (requestRehearse()) {
                        v.displayRehearse(mod.getCurrentPlayer());
                    } else {
                        v.displayRehearse(Enums.errno.FORBIDDEN_ACTION);
                    }
                    break;
                case PASS:
                    canMove = true;
                    pass = true;
                    break;
                default:
                    System.out.println("fatal error");
                    System.exit(1);
                    break;
            }
        }
    }

    private void initializeGame() {
        v.displayInit();
        int playerCount = v.getPlayerCount();
        mod = new Moderator(playerCount);


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
            String name = v.getPlayerName(i + 1);
            mod.setPlayer(i, new Player(name, startCredits, startRank));
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
        Player[] players = mod.getPlayers();
        int[] scores = new int[players.length];
        int winnerIndex = 0;
        int highestScore = 0;

        for (int i=0; i<players.length; i++) {
            Player p = players[i];
            int score = 0;
            score += p.getCredits();
            score += p.getMoney();
            score += (5 * p.getRank());
            scores[i] = score;

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

        v.displayEndGame(players, scores, winners, highestScore); // FUCK
    }
}
