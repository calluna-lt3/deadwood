public class Player {
    boolean myTurn;
    int money;
    int credits;
    boolean canMove;
    int rehearsalTokens;
    int rank;
    Room currSpace;
    String name;

    Player(String name) {
        myTurn = false;
        money = 0;
        credits = 0;
        canMove = false;
        rehearsalTokens = 0;
        rank = 1;
        currSpace = null;
        this.name = name;
    }

    Player(String name, int credits, int rank) {
        myTurn = false;
        money = 0;
        canMove = false;
        rehearsalTokens = 0;
        currSpace = null;
        this.credits = credits;
        this.rank = rank;
        this.name = name;
    }

    public boolean requestRankUp(boolean currency, int rank)  { return false; }
    public boolean requestMove(int room)  { return false; }
    public boolean requestRole(int role)  { return false; }
    public boolean requestAct()  { return false; }
    public boolean requestRehearse()  { return false; }
    public void requestPassTurn() { }
}
