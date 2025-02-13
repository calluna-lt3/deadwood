import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        XMLParser xmlp = new XMLParser();
        ArrayList<SceneCard> cards = xmlp.getSceneCards();
        for (SceneCard card : cards) {
            System.out.println("name: " + card.name);
            System.out.println("\tdescription: " + card.description);
            System.out.println("  number: " + card.number);
            System.out.println("  budget: " + card.budget);
            System.out.println("  Roles: ");
            for (Role role : card.roles) {
                if (role == null) continue;
                System.out.println("    name: " + role.name);
                System.out.println("    line: " + role.line);
                System.out.println("    rank: " + role.rank);
                System.out.println("");
            }
        }

        ArrayList<Room> rooms = xmlp.getRooms();
        for (Room room : rooms) {
            System.out.println("name: " + room.name);
            System.out.println("  Adjacent Rooms: ");
            for (String adjRoom : room.adjacentRoomsStr) {
                System.out.println("    " + adjRoom);
            }

            if (room instanceof InertRoom) continue;
            SoundStage ss = (SoundStage)room;
            System.out.println("  shotMarkers: " + ss.shotMarkers);

            System.out.println("  roles: ");
            for (Role role : ss.roles) {
                System.out.println("    name: " + role.name);
                System.out.println("    line: " + role.line);
                System.out.println("    rank: " + role.rank);
                System.out.println("");
            }
        }
    }
}
