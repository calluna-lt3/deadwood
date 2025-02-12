public class SoundStage extends Room {
    SceneCard card;
    Role[] roles;
    int maxShotMarkers; // needed for restarting day
    int shotMarkers;


    public SoundStage(String name, String[] adjacentRoomsStr, Role[] roles, int shotMarkers) {
        this.name = name;
        this.adjacentRoomsStr = adjacentRoomsStr;
        card = null;
        this.roles = roles;
        this.shotMarkers = shotMarkers;
        maxShotMarkers = shotMarkers;
    }

    public int getShotMarkers() { return shotMarkers; }
    public void setShotMarkers(int shotMarkers) { this.shotMarkers = shotMarkers; }
    public int getMaxShotMarkers() { return maxShotMarkers; }

    // returns true if scene is finished, false if not
    //      method that calls this should handle cleanup of this soundstage
    public boolean decShotMarkers() { // TODO: probably needs better method name
        shotMarkers--;
        return (shotMarkers == 0) ? true : false;
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
