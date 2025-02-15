public interface View {
    // Display information
    public void displayHelp();
    public void displayWho();
    public void displayLocations();
    public void displayRole();

    // Infallible actions
    public void displayEndGame();
    public void displayPassTurn();

    // Fallible actions (return 0 on success, non-zero code on fail)
    public int displayTakeRole(int role);
    public int displayMove(int room);
    public int displayUpgrade(boolean currency, int rank);
    public boolean displayRehearse();

    // Stochastic actions (return the result of a dice check)
    public boolean displayAct();
}