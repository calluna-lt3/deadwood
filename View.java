public interface View {
    public void displayInit();
    public String getUserInput();
    public InVec getUserAction();
    public InitInfo getPlayerInfo();

    // Display information
    public void displayHelp();
    public void displayWho(Player p);
    public void displayLocations(Player activePlayer, Player[] players);
    public void displayRole(SoundStage ss);
    public void displayRole(Enums.errno errno);

    // Infallible actions
    // this method signature is bad, but its the easiest way to implement this with for now
    public void displayEndGame(Player[] players, int[] scores, String[] winners, int winningScore);
    public void displayPassTurn(Player player);
    public void displayDiceRolls(int... diceRolled); // NOTE: only view method called outside of takeTurn()

    // Fallible actions (return 0 on success, non-zero code on fail)
    public void displayTakeRole(Role role);
    public void displayTakeRole(Enums.errno errno);
    public void displayMove(Room room);
    public void displayMove(Enums.errno errno);
    public void displayUpgrade(int rank);
    public void displayUpgrade(Enums.errno errno);
    public void displayRehearse(Player player);
    public void displayRehearse(Enums.errno errno);

    // Stochastic actions (return the result of a dice check)
    public void displayAct(boolean success);
    public void displayAct(Enums.errno errno);
}
