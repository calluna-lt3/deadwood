public class Room {
    String name;
    String[] adjacentRoomsStr;
    Room[] adjacentRooms;

    public String getName() {
        return name;
    }

    public int getAdjacentCount() {
        return adjacentRooms.length;
    }

    public Room[] getAdjacentRooms() {
        return adjacentRooms;
    }
}
