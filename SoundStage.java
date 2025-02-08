public class SoundStage extends Room {
    private SceneCard card;
    private Role[] roles;

    public SoundStage() {
        card = null;
    }

    public Role getRole(int role) {
        // check upper bound
        if (card == null) {
            return null;
        }
        
        return (role > roles.length) ? 0/* Card role */ : 1/* Extra role */ ;
    }
}
