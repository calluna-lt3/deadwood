public interface View {
    // Display information
    public void displayHelp();
    public void displayWho();
    public void displayLocations();
    public void displayRole();

    // Infallible actions
    public void endGame();
    public void passTurn();

    // Fallible actions (return 0 on success, non-zero code on fail)
    public int takeRole(int role);
    public int move(int room);
    public int upgrade(boolean currency, int rank);
    public boolean rehearse();

    // Stochastic actions (return the result of a dice check)
    public int act();
}