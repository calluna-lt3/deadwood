import java.util.ArrayList;

public class Role {
    String name;
    String line;
    int rank;
    boolean taken;
    boolean starring;
    DisplayInfo dInfo;

    Role(String name, String line, int rank, boolean starring, DisplayInfo dInfo) {
        taken = false;
        this.name = name;
        this.line = line;
        this.rank = rank;
        this.dInfo = dInfo;
        this.starring = starring;
    }


    public String getName() { return name; }
    public String getLine() { return line; }
    public int getRank() { return rank; }
    public boolean getTaken() { return taken; }
    public boolean getStarring() { return starring; }
    public void setTaken(boolean val) { taken = val; }
}
