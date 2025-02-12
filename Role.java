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

    public boolean getTaken() {
        return taken;
    }

    public void setTaken(boolean val) {
        taken = val;
    }

    public boolean getStarring() {
        return starring;
    }
}
