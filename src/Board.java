import java.util.ArrayList;

public class Board {
    ArrayList<Room> rooms;

    public Board() {
        XMLParser xmlp = new XMLParser(XMLParser.type.BOARD);
        rooms = xmlp.getRooms();
        initializeRooms();
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

    private void initializeRooms() {
        for (Room r : rooms) {

            // TODO: find default positions of each room
            if ("Bank".equals(r.getName())) {
                r.dPos = new DefaultPos(0, 0);
            } else if ("Church".equals(r.getName())) {
                r.dPos = new DefaultPos(0, 0);
            } else if ("General Store".equals(r.getName())) {
                r.dPos = new DefaultPos(0, 0);
            } else if ("Hotel".equals(r.getName())) {
                r.dPos = new DefaultPos(0, 0);
            } else if ("Jail".equals(r.getName())) {
                r.dPos = new DefaultPos(0, 0);
            } else if ("Main Street".equals(r.getName())) {
                r.dPos = new DefaultPos(0, 0);
            } else if ("Ranch".equals(r.getName())) {
                r.dPos = new DefaultPos(0, 0);
            } else if ("Saloon".equals(r.getName())) {
                r.dPos = new DefaultPos(0, 0);
            } else if ("Secret Hideout".equals(r.getName())) {
                r.dPos = new DefaultPos(0, 0);
            } else if ("Train Station".equals(r.getName())) {
                r.dPos = new DefaultPos(0, 0);
            } else if ("office".equals(r.getName())) {
                r.dPos = new DefaultPos(0, 0);
            } else if ("trailer".equals(r.getName())) {
                r.dPos = new DefaultPos(0, 0);
            }


            ArrayList<Room> adj = new ArrayList<Room>();

            for (String s : r.getAdjStrings()) {
                for (Room r2 : rooms) {
                    if (s.equals(r2.getName())) {
                        adj.add(r2);
                    }
                }
            }

            r.setAdjacentRooms(adj);
        }
    }
}
