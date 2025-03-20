import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class InputController {
    View v;
    Moderator mod;
    Random random;
    boolean canMove, canWork, pass;

    InputController(View v) {
        this.v = v;
        this.random = new Random();
        canMove = true;
        canWork = true;
        pass = false;
    }


    // Money is false, credits is true
    private int requestUpgrade(int rank, boolean currency) {
        int[] money_vals = {4, 10, 18, 28, 40};
        int[] credit_vals = {5, 10, 15, 20, 25};
        Player currPlayer = mod.getCurrentPlayer();

        if (!currPlayer.getRoom().getName().equals("office")) {
            return -6;
        }

        // Check if requested rank is within bounds
        if (rank < 2 || rank > 6) {
            return -3;
        }

        // Check if rank is max
        if (currPlayer.getRank() == 6) {
            return -1;
        }

        // Check if requested rank is <= than current
        if (currPlayer.getRank() >= rank) {
            return -2;
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

    private int requestMove(int room) {
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

    private int requestRole(int role) {
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

        // Role already taken
        if (((SoundStage) currPlayer.getRoom()).getRole(role).getTaken() == true) {
            return -5;
        }

        // Rank not high enough
        if (currPlayer.getRank() < ((SoundStage) currPlayer.getRoom()).getRole(role).getRank()) {
            return -4;
        }

        ((SoundStage) currPlayer.getRoom()).getRole(role).setTaken(true);
        currPlayer.setRole(((SoundStage) currPlayer.getRoom()).getRole(role));
        return 0;
    }

    // needs to update shot markers
    private int requestAct() {
        Player currPlayer = mod.getCurrentPlayer();

        if (currPlayer.getRole() == null) {
            return -1;
        }

        if (canWork == false) {
            return -2;
        }

        // Failure case
        int dieResult = random.nextInt(6) + 1;
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

        // Scene wrap
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

                    if (starringPlayers.size() == 0) starringPlayers.add(p);

                    // Descending order, >1 element in list
                    int i = 0;
                    for (; starringPlayers.get(i).getRole().getRank() > p.getRole().getRank(); i++);
                    starringPlayers.add(i, p);
                }
            }

            if (starring) {
                int budget = ((SoundStage) currPlayer.getRoom()).getCardBudget();
                int[] diceResults = new int[budget];
                for (int i=0; i<budget; i++) {
                    diceResults[i] = random.nextInt(6) + 1;
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

            for (Player p : extraPlayers) { p.setRole(null); }
            for (Player p : starringPlayers) { p.setRole(null); }

            currPlayer.setRole(null);
            currPlayer.setRehearsalTokens(0);
            mod.decrementCardCount();
            ((SoundStage) currPlayer.getRoom()).setCard(null);
        }

        return 0;
    }

    private int requestRehearse()  {
        Player currPlayer = mod.getCurrentPlayer();

        if(currPlayer.getRole() == null) {
            return -3;
        }

        if (canWork == false) {
            return -2;
        }

        if (currPlayer.getRehearsalTokens() + 1 == ((SoundStage) currPlayer.getRoom()).getCardBudget()) {
            return -1;
        }
        currPlayer.setRehearsalTokens(currPlayer.getRehearsalTokens() + 1);
        return 0;
    }

    // start game (console/gui)
    public void run() {
        initializeGame();

        if(v instanceof GUIView) {
            startDay();
        } else {
            while (mod.getDay() <= mod.getLastDay()) {
                startDay();
                while (mod.getCardCount() > 1) {
                    takeTurn();
                    mod.setTurn(mod.getTurn() + 1);
                }
                mod.setDay(mod.getDay() + 1);
            }
            endGame();
        }
    }


    public void processAction(InVec args) {
        Enums.action action = args.action();
        String arg1 = args.arg1();
        String arg2 = args.arg2();
        int result = 1;

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
            case ROOMS:
                v.displayRooms(mod.getCurrentPlayer().getRoom());
                break;
            case ROLES:
                if (mod.getCurrentPlayer().getRoom() instanceof InertRoom) {
                    v.displayRoles(Enums.errno.BAD_ROOM);
                } else {
                    v.displayRoles((SoundStage) mod.getCurrentPlayer().getRoom());
                }
                break;
            case TAKE_ROLE:
                try {
                    result = requestRole(Integer.parseInt(arg1));
                    switch (result) {
                        case 0:
                            v.displayTakeRole(mod.getCurrentPlayer().getRole());
                            if (v instanceof GUIView) v.displayWho(mod.getCurrentPlayer());
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
                        case -4:
                            v.displayTakeRole(Enums.errno.LEQ);
                            break;
                        case -5:
                            v.displayTakeRole(Enums.errno.FORBIDDEN_ACTION);
                            break;
                        default:
                            System.err.println("fatal error");
                            System.exit(1);
                    }
                }
                catch (NumberFormatException e) {
                    v.displayTakeRole(Enums.errno.BAD_ARGS);
                }
                break;
            case MOVE:
                try {
                    result = requestMove(Integer.parseInt(arg1));
                    switch (result) {
                        case 0:
                            v.displayMove(mod.getCurrentPlayer().getRoom());
                            if (v instanceof GUIView) {
                                Room r = mod.getCurrentPlayer().getRoom();
                                if (r instanceof SoundStage) {
                                    v.displayRoles((SoundStage)r);
                                } else {
                                    v.displayRoles(Enums.errno.BAD_ROOM);
                                }
                                v.displayRooms(r);
                            }
                            canMove = false;
                            break;
                        case -1:
                            v.displayMove(Enums.errno.FORBIDDEN_ACTION);
                            break;
                        case -2:
                            v.displayMove(Enums.errno.OOB);
                            break;
                        default:
                            System.err.println("fatal error");
                            System.exit(1);
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

                    result = requestUpgrade(Integer.parseInt(arg1), "credits".equals(arg2));
                    switch (result) {
                        case 0:
                            v.displayUpgrade(mod.getCurrentPlayer().getRank());
                            if (v instanceof GUIView) v.displayWho(mod.getCurrentPlayer());
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
                        default:
                            System.err.println("fatal error");
                            System.exit(1);
                    }
                }
                catch (NumberFormatException e) {
                    v.displayUpgrade(Enums.errno.BAD_ARGS);
                }
                break;
            case ACT:
                result = requestAct();
                switch (result) {
                    case 0:
                        v.displayAct(true);
                        v.displayWho(mod.getCurrentPlayer());
                        canWork = false;
                        break;
                    case 1:
                        v.displayAct(false);
                        v.displayWho(mod.getCurrentPlayer());
                        canWork = false;
                        break;
                    case -1:
                        v.displayAct(Enums.errno.FORBIDDEN_ACTION);
                        break;
                    case -2:
                        v.displayAct(Enums.errno.DUP_ACTION);
                        break;
                    default:
                        System.err.println("fatal error");
                        System.exit(1);
                }
                break;
            case REHEARSE:
                result = requestRehearse();
                switch (result) {
                    case 0:
                        v.displayRehearse(mod.getCurrentPlayer());
                        if (v instanceof GUIView) v.displayWho(mod.getCurrentPlayer());
                        canWork = false;
                        break;
                    case -1:
                        v.displayRehearse(Enums.errno.FORBIDDEN_ACTION);
                        break;
                    case -2:
                        v.displayRehearse(Enums.errno.DUP_ACTION);
                        break;
                    case -3: // player doesnt have a role
                        v.displayRehearse(Enums.errno.IN_ROLE);
                        break;
                    default:
                        System.err.println("fatal error");
                        System.exit(1);
                }
                break;
            case PASS:
                canMove = true;
                canWork = true;
                pass = true;
                break;
            default:
                System.err.println("fatal error");
                System.exit(1);
        }

        if (v instanceof ConsoleView) return;

        if (pass == true) {
            pass = false;

            mod.setTurn(mod.getTurn() + 1);
            Player p = mod.getCurrentPlayer();
            v.displayPassTurn(p);


            if (mod.getCardCount() < 2) {
                mod.setDay(mod.getDay() + 1);

                if (mod.getDay() > mod.getLastDay()) {
                    endGame();
                }
                startDay();
            }

            Room r = mod.getCurrentPlayer().getRoom();
            if (r instanceof SoundStage) v.displayRoles((SoundStage)r);
            else v.displayRoles(Enums.errno.BAD_ROOM);
            v.displayWho(p);
            v.displayRooms(r);
        }
    }


    // console view turn handler
    private void takeTurn() {
        if (mod.getCurrentPlayer().getRole() != null) {
            canMove = false;
        }

        pass = false;
        while (pass == false) {
            InVec args = v.getUserAction();
            processAction(args);
        }
    }


    private void initializeGame() {
        v.displayInit();
        if (v instanceof GUIView) {
            ((GUIView)v).setController(this);
            ((GUIView)v).setVisible(true);
        }

        InitInfo info = v.getPlayerInfo();
        mod = new Moderator(info.count());


        mod.setTurn(random.nextInt(info.count()));

        int startCredits = 1;
        int startRank = 1;
        switch (mod.getPlayerCount()) {
            case 2: case 3:
                mod.setLastDay(3);
                break;
            case 4:
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
                System.err.println("fatal error");
                System.exit(1);
        }

        for (int i=0; i<info.count(); i++) {
            mod.setPlayer(i, new Player(info.names()[i], startCredits, startRank));
        }

    }

    private void startDay() {
        mod.board.resetShotMarkers();

        Room trailer = null;
        for (Room r : mod.getBoard().getRooms()) {
            if (r.getName().equals("trailer")) {
                trailer = r;
                break;
            }
        }

        for (Player p : mod.getPlayers()) {
            p.setRehearsalTokens(0);
            p.setRoom(trailer);
            p.setRole(null);
            // Update player positions
        }

        assignScenes();

        if (v instanceof GUIView) {
            // ORDER MATTERS HERE
            ((GUIView)v).displayStartDay(mod.getBoard().getRooms());
            v.displayLocations(mod.getCurrentPlayer(), mod.getPlayers());
            ((GUIView)v).endFirstDay();
            v.displayWho(mod.getCurrentPlayer());
        }
    }

    private void assignScenes() {
        for (Room r : mod.board.getRooms()) {
            if (r instanceof InertRoom) {
                continue;
            }

            ((SoundStage) r).setCard(mod.deck.draw());
        }
    }


    // calculate scores, determines winner, etc.
    private void endGame() {
        ArrayList<String> winners = new ArrayList<String>();
        Player[] players = mod.getPlayers();
        int[] scores = new int[players.length];
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
                winners.add(p.getName());
            } else if (score == highestScore) {
                winners.add(p.getName());
            }
        }

        int count = winners.size();
        String[] winnerArr = new String[count];
        for (int i=0; i<count; i++) {
            winnerArr[i] = winners.get(i);
        }

        v.displayEndGame(players, scores, winnerArr, highestScore);
    }
}
