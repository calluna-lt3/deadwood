import java.util.ArrayList;


public abstract class Room {
    String name;
    ArrayList<String> adjacentRoomsStr;
    ArrayList<Room> adjacentRooms;
    DefaultPos dPos;
    DisplayInfo dInfo;

    public String getName() {
        return name;
    }

    public int getAdjacentCount() {
        return adjacentRooms.size();
    }

    public ArrayList<Room> getAdjacentRooms() {
        return adjacentRooms;
    }

    public ArrayList<String> getAdjStrings() {
        return adjacentRoomsStr;
    }

    public void setAdjacentRooms(ArrayList<Room> arrList) {
        adjacentRooms = arrList;
    }
}
