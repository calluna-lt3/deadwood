import javax.swing.JLabel;

public class Player {
    int money;
    int credits;
    int rehearsalTokens;
    int rank;
    Room currSpace;
    String name;
    Role role;
    JLabel label;

    Player(String name, int credits, int rank) {
        money = 0;
        rehearsalTokens = 0;
        currSpace = null;
        this.credits = credits;
        this.rank = rank;
        this.name = name;
        role = null;
    }

    public Room getRoom() { return currSpace; }
    public void setRoom(Room room) { currSpace = room; }

    public void setCredits(int credits) { this.credits = credits; }
    public void setMoney(int money) { this.money = money; }
    public void setRank(int rank) { this.rank = rank; }
    public void setRole(Role role) { this.role = role; }
    public void setRehearsalTokens(int tokens) { this.rehearsalTokens = tokens; }
    public void setLabel(JLabel label) { this.label = label; }

    public String getName() { return name; }
    public int getCredits() { return credits; }
    public int getMoney() { return money; }
    public int getRank() { return rank; }
    public Role getRole() { return role; }
    public int getRehearsalTokens() { return rehearsalTokens; }
    public JLabel getLabel() { return label; }

}
