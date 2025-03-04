import java.util.ArrayList;


public class InertRoom extends Room {
    public boolean castingOffice;

    InertRoom(String name, ArrayList<String> adjacentRoomsStr, boolean castingOffice) {
        this.name = name;
        this.adjacentRoomsStr = adjacentRoomsStr;
        this.castingOffice = castingOffice;
    }
}
