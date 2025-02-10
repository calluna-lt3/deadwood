public class Room {
    String name;
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
