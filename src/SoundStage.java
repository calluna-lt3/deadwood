import java.util.ArrayList;

public class SoundStage extends Room {
    SceneCard card;
    ArrayList<Role> roles;
    int maxShotMarkers; // needed for restarting day
    int shotMarkers;
    ArrayList<DisplayInfo> takeInfo = new ArrayList<DisplayInfo>();


    // ;;;;;;;;;
    public SoundStage(String name, ArrayList<String> adjacentRoomsStr, ArrayList<Role> roles,
                      int shotMarkers, DisplayInfo dInfo, ArrayList<DisplayInfo> takeInfo) {
        this.name = name;
        this.adjacentRoomsStr = adjacentRoomsStr;
        card = null;
        this.roles = roles;
        this.shotMarkers = shotMarkers;
        this.dInfo = dInfo;
        this.takeInfo = takeInfo;
        maxShotMarkers = shotMarkers;
    }

    public int getShotMarkers() { return shotMarkers; }
    public void setShotMarkers(int shotMarkers) { this.shotMarkers = shotMarkers; }
    public int getMaxShotMarkers() { return maxShotMarkers; }

    public void setCard(SceneCard c) {
        this.card = c;
    }

    // returns true if scene is finished, false if not
    //   caller should handle cleanup of this soundstage
    public boolean decrementShotMarkers() {
        shotMarkers--;
        return (shotMarkers == 0) ? true : false;
    }

    public Role getRole(int role) {
        if (card == null) {
            return null;
        }

        if (role > roles.size()) {
            if (role - roles.size() > card.getRoleCount()) {
                // System.out.println("Requested role out of bounds");
                return null;
            }

            return card.getRoles().get(role - roles.size() - 1);
        } else {
            if (role < 1) {
                // System.out.println("Requested role out of bounds");
                return null;
            }

            return roles.get(role - 1);
        }
    }

    public int getRoleCount() {
        if (card == null) {
            return 0;
        }

        return roles.size() + card.getRoleCount();
    }

    public int getCardBudget() {
        if (card == null) {
            return 0;
        }

        return card.getBudget();
    }
}
