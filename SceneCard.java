public class SceneCard {
    String name;
    String description;
    int budget;
    boolean revealed;
    Role[] roles;

    SceneCard() {
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
