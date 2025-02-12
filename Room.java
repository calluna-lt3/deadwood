import java.util.ArrayList;


public class Room {
    String name;
    ArrayList<String> adjacentRoomsStr;
    ArrayList<Room> adjacentRooms;

    public String getName() {
        return name;
    }

    public int getAdjacentCount() {
        return adjacentRooms.size();
    }

    public ArrayList<Room> getAdjacentRooms() {
        return adjacentRooms;
    }
}
