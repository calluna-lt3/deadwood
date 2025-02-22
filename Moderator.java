public class Moderator {
    Player[] players;
    Board board;
    int playerCount;
    int turn;
    int day;
    int lastDay;
    Deck deck;


    Moderator(int playerCount) {
        board = new Board();
        this.playerCount = playerCount;
        day = 0;
        lastDay = 4;
        turn = 0;
        players = new Player[playerCount];
        deck = new Deck();
    }

    public int getDay() {return day; }
    public int getLastDay() {return lastDay; }
    public int getTurn() {return turn; }
    public int getPlayerCount() {return playerCount; }
    public Player[] getPlayers() {return players; }
    public Player getCurrentPlayer() {
        return players[turn];
    }
    public String getCurrentPlayerName() {
        return players[turn].getName();
    }
    public Board getBoard() {
        return board;
    }

    public void setTurn(int turn) {this.turn = turn % playerCount; }
    public void setLastDay(int day) {this.lastDay = day; }
    public void setPlayer(int ix, Player p) {players[ix] = p; };
    public void setDay(int day) {this.day = day; }
}
