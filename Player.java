import java.util.Random;

public class Player {
    boolean myTurn;
    int money;
    int credits;
    boolean canMove;
    int rehearsalTokens;
    int rank;
    Room currSpace;
    String name;
    Role role;

    Player(String name, int credits, int rank) {
        myTurn = false;
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

    public boolean getTurn() { return myTurn; }
    public void setTurn(boolean turn) { myTurn = turn; }

    public String getName() { return name; }
    public int getCredits() { return credits; }
    public int getMoney() { return money; }
    public int getRank() { return rank; }

    // Money is false, credits is true
    public boolean requestRankUp(boolean currency, int rank)  {
        int[] money_vals = {4, 10, 18, 28, 40};
        int[] credit_vals = {5, 10, 15, 20, 25};

        // Check if rank is max
        if (this.rank == 6) {
            System.out.println("Maximum rank already attained.");
            return false;
        }

        // Check if requested rank is <= than current
        if (this.rank <= rank) {
            System.out.println("Requested rank is less than or equal to current rank.");
            return false;
        }

        // Check if requested rank is within bounds
        if (rank < 2 || rank > 6) {
            System.out.println("Requested rank is out of bounds, minimum 2 maximum 6.");
            return false;
        }

        // Check currency count and subtract if sufficient
        if (currency == true) {
            if (credit_vals[rank - 2] > credits) {
                System.out.println("Not enough credits for chosen rank.");
                return false;
            }
            System.out.println(String.format("Upgraded to rank %d\n", rank));
            credits -= credit_vals[rank - 2];
        } else {
            if (money_vals[rank - 2] > money) {
                System.out.println("Not enough credits for chosen rank.");
                return false;
            }
            System.out.println(String.format("Upgraded to rank %d\n", rank));
            money -= money_vals[rank - 2];
        }

        System.out.println(String.format("Upgraded to rank %d\n", rank));
        return true;
    }

    public boolean requestMove(int room) {
        // Check if player can move
        if (canMove == false) {
            System.out.println("Already moved this turn");
            return false;
        }

        // Check if room index is out of bounds
        if (room < 1 || room > currSpace.getAdjacentCount()) {
            System.out.println("Requested room not valid");
            return false;
        }

        currSpace = currSpace.getAdjacentRooms()[room - 1];
        return true;
    }

    public boolean requestRole(int role) {
        // Check if currently in a role
        if (this.role != null) {
            System.out.println("Cannot take a role while already working one");
            return false;
        }

        // Check if room is SoundStage
        if (currSpace instanceof InertRoom) {
            System.out.println("No roles to take on this space");
            return false;
        }

        // Check if index is out of bounds
        if (role < 1 || role > ((SoundStage) currSpace).getRoleCount()) {
            System.out.println("Requested role out of bounds");
            return false;
        }

        ((SoundStage) currSpace).getRole(role).setTaken(true);
        this.role = ((SoundStage) currSpace).getRole(role);
        return true;
    }

    // needs to update shot markers
    public boolean requestAct() {
        Random rand = new Random(); // might need to get moved, hard to visualize from here

        if (rand.nextInt(6) + 1 < ((SoundStage) currSpace).getCardBudget()) {
            if (role.getStarring() == false) {
                money++;
            }
            return false;
        }

        // On-card role
        if (role.getStarring() == true) {
            credits += 2;
        // Off-card role
        } else {
            money++;
            credits++;
        }
        return true;
    }

    public boolean requestRehearse()  {
        if (rehearsalTokens + 1 == ((SoundStage) currSpace).getCardBudget()) {
            System.out.println("Already at maximum rehearsal tokens");
            return false;
        }
        rehearsalTokens++;
        return true;
    }
    
    public void requestPassTurn() {
        canMove = true;
    }
}
