public interface View {
    public void displayInit();
    public InVec getUserInput();

    // Display information
    public void displayHelp();
    public void displayWho(Player p);
    public void displayLocations(Player activePlayer, Player[] players);
    public void displayRole(SoundStage ss);
    public void displayRole(Enums.errno errno);

    // Infallible actions
    public void displayEndGame();
    public void displayPassTurn();

    // Fallible actions (return 0 on success, non-zero code on fail)
    public void displayTakeRole(int role);
    public void displayTakeRole(Enums.errno errno);
    public void displayMove(int room);
    public void displayMove(Enums.errno errno);
    public void displayUpgrade(boolean currency, int rank);
    public void displayUpgrade(Enums.errno errno);
    public void displayRehearse();

    // Stochastic actions (return the result of a dice check)
    public void displayAct();
}