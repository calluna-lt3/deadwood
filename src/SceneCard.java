import java.util.ArrayList;

public class SceneCard {
    String name;
    String description;
    int number;
    int budget;
    ArrayList<Role> roles;
    boolean revealed;
    String img;

    SceneCard(String name, String description, String img, int number, int budget, ArrayList<Role> roles) {
        this.name = name;
        this.description = description;
        this.img = img;
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
