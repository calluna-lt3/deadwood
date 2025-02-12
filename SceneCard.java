public class SceneCard {
    String name;
    String description;
    int number;
    int budget;
    Role[] roles;
    boolean revealed;


    SceneCard(String name, String description, int number, int budget, Role[] roles) {
        this.name = name;
        this.description = description;
        this.number = number;
        this.budget = budget;
        this.roles = roles;
        revealed = false;
    }

    public Role[] getRoles() {
        return roles;
    }

    public int getRoleCount() {
        return roles.length;
    }

    public int getBudget() {
        return budget;
    }
}
