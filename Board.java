import java.util.ArrayList;

public class Board {
    ArrayList<Room> rooms;

    public Board() {
        XMLParser xmlp = new XMLParser();
        rooms = xmlp.getRooms();
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public void resetShotMarkers() {
        for (Room room : rooms) {
            if (room instanceof SoundStage) {
                ((SoundStage)room).setShotMarkers(((SoundStage)room).getMaxShotMarkers());
            }
        }
    }
}
