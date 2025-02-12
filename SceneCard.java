import java.util.ArrayList;

public class SceneCard {
    String name;
    String description;
    int number;
    int budget;
    ArrayList<Role> roles;
    boolean revealed;


    SceneCard(String name, String description, int number, int budget, ArrayList<Role> roles) {
        this.name = name;
        this.description = description;
        this.number = number;
        this.budget = budget;
        this.roles = roles;
        revealed = false;
    }

    public ArrayList<Role> getRoles() {
        return roles;
    }

    public int getRoleCount() {
        return roles.size();
    }

    public int getBudget() {
        return budget;
    }
}
