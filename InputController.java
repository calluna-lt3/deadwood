import java.util.ArrayList;

public class InputController {
    Moderator mod;

    InputController(Moderator mod) {
        this.mod = mod;
    }

    // Used by View.displayWho()
    public String currentPlayer() {
        return mod.getCurrentPlayerName();
    }

    // public String[] playerLocations() {
    //     ArrayList<String> playerLocList = new ArrayList<String>();
        
    //     int playerCount = mod.getPlayerCount();
    //     for (int i = 0; i < playerCount; i++) {
            
    //     }
    // }
}
