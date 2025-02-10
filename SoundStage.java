public class SoundStage extends Room {
    private SceneCard card;
    private Role[] roles;

    public SoundStage() {
        card = null;
    }

    public Role getRole(int role) {
        if (card == null) {
            return null;
        }
        
        if (role > roles.length) {
            if (role - roles.length > card.getRoleCount()) {
                System.out.println("Requested role out of bounds");
                return null;
            }

            return card.getRoles()[role - roles.length - 1];
        } else {
            if (role < 1) {
                System.out.println("Requested role out of bounds");
                return null;
            }

            return roles[role - 1];
        }
    }

    public int getRoleCount() {
        if (card == null) {
            return 0;
        }

        return roles.length + card.getRoleCount();
    }

    public int getCardBudget() {
        if (card == null) {
            return 0;
        }

        return card.getBudget();
    }
}
