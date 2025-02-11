public class SoundStage extends Room {
    private SceneCard card;
    private Role[] roles;
    private int maxShotMarkers; // needed for restarting day
    private int curShotMarkers;

    public SoundStage(int shotMarkers) {
        card = null;
        maxShotMarkers = shotMarkers;
        curShotMarkers = shotMarkers;
    }

    public int getShotMarkers() { return curShotMarkers; }
    public void setShotMarkers(int shotMarkers) { curShotMarkers = shotMarkers; }
    public int getMaxShotMarkers() { return maxShotMarkers; }

    // returns true if scene is finished, false if not
    //      method that calls this should handle cleanup of this soundstage
    public boolean decShotMarkers() { // TODO: probably needs better method name
        curShotMarkers--;
        return (curShotMarkers == 0) ? true : false;
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
