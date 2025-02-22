import java.util.ArrayList;

public class Board {
    ArrayList<Room> rooms;

    public Board() {
        XMLParser xmlp = new XMLParser();
        rooms = xmlp.getRooms();
        calculateAdjacency();
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

    private void calculateAdjacency() {
        for (Room r : rooms) {
            ArrayList<Room> adj = r.getAdjacentRooms();

            for (String s : r.getAdjStrings()) {
                for (Room r2 : rooms) {
                    if (s.equals(r2.getName())) {
                        adj.add(r2);
                    }
                }
            }
        }
    }
}
