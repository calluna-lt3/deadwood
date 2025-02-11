import java.util.Random;


public class Moderator {
    Player[] players;
    Board board;
    int playerCount;
    int turn;
    int day;
    int lastDay;


    Moderator() {
        board = new Board();
        playerCount = 0;
        day = 0;
        lastDay = 4;
        turn = 0;
    }


    // main game loop
    public void run() {
        initializeGame();
        while (day <= lastDay) {
            startDay();
            startNextTurn();
            endDay();
        }
        endGame();
    }


    private void initializeGame() {
        XMLParser parser = new XMLParser();
        parser.parse();

        // TODO: assign all data here (probably in differnet method)
        //  -> deck -> shuffle
        //  -> board -> adjacency

        // TODO: call controller to get playercount, valid range: [2,8]
        Random rand = new Random();
        turn = rand.nextInt(playerCount);

        int startCredits = 1;
        int startRank = 1;
        switch (playerCount) {
            case 2: case 3:
                lastDay = 3;
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

        for (int i=0; i<playerCount; i++) {
            // TODO: call controller to get name
            players[i] = new Player("TEMP", startCredits, startRank);
        }
    }


    private void assignScenes() {
        // TODO: finish once xml parser is done
    }

    private void startDay() {
        day++;
        for (Player p : players) p.setRoom(null); // TODO: set to trailers
        assignScenes();
    }


    private void endDay() {
        board.resetShotMarkers();
    }


    private void startNextTurn() {
        players[turn].myTurn = true;

        // TODO: call controller to start turn

        turn++;
    }


    // calculate scores, determines winner, etc.
    private void endGame() {
        String[] winners = new String[0];
        int winnerIndex = 0;
        int highestScore = 0;

        for (Player p : players) {
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
