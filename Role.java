public class Role {
    String name;
    String description;
    int rank;
    boolean taken;
    boolean starring;

    Role(String name, String desc, int rank, boolean starring) {
        taken = false;
        this.name = name;
        this.description = desc;
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
