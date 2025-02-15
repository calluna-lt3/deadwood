public class Role {
    String name;
    String line;
    int rank;
    boolean taken;
    boolean starring;

    Role(String name, String line, int rank, boolean starring) {
        taken = false;
        this.name = name;
        this.line = line;
        this.rank = rank;
        this.starring = starring;
    }

    public String getName() { return name; }
    public String getLine() { return line; }
    public int getRank() { return rank; }
    public boolean getTaken() { return taken; }
    public boolean getStarring() { return starring; }


    public void setTaken(boolean val) { taken = val; }

}
