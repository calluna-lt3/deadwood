import java.util.Random;
import java.util.ArrayList;

public class Player {
    int money;
    int credits;
    boolean canMove;
    int rehearsalTokens;
    int rank;
    Room currSpace;
    String name;
    Role role;

    Player(String name, int credits, int rank) {
        money = 0;
        canMove = false;
        rehearsalTokens = 0;
        currSpace = null;
        this.credits = credits;
        this.rank = rank;
        this.name = name;
        role = null;
    }

    public Room getRoom() { return currSpace; }
    public void setRoom(Room room) { currSpace = room; }

    public void setCredits(int credits) {this.credits = credits; }
    public void setMoney(int money) {this.money = money; }
    public void setRank(int rank) {this.rank = rank; }
    public void setCanMove(boolean val) {this.canMove = val; }
    public void setRole(Role role) {this.role = role; }
    public void setRehearsalTokens(int tokens) {this.rehearsalTokens = tokens; }

    public String getName() { return name; }
    public int getCredits() { return credits; }
    public int getMoney() { return money; }
    public int getRank() { return rank; }
    public boolean getCanMove() { return canMove; }
    public Role getRole() { return role; }
    public int getRehearsalTokens() { return rehearsalTokens; }

}
