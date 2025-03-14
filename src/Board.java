import java.util.ArrayList;

public class Board {
    ArrayList<Room> rooms;

    public Board() {
        XMLParser xmlp = new XMLParser(XMLParser.type.BOARD);
        rooms = xmlp.getRooms();
        calculateAdjacency();
        setDefaultPositions();
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

    public void setDefaultPositions() {
        for (Room r : rooms) {
            if ("Bank".equals(r.getName())) {
                r.dPos = new DefaultPos(1000, 190);
            } else if ("Church".equals(r.getName())) {
                r.dPos = new DefaultPos(770, 690);
            } else if ("General Store".equals(r.getName())) {
                r.dPos = new DefaultPos(320, 420);
            } else if ("Hotel".equals(r.getName())) {
                r.dPos = new DefaultPos(1000, 875);
            } else if ("Jail".equals(r.getName())) {
                r.dPos = new DefaultPos(300, 190);
            } else if ("Main Street".equals(r.getName())) {
                r.dPos = new DefaultPos(1000, 190);
            } else if ("Ranch".equals(r.getName())) {
                r.dPos = new DefaultPos(260, 650);
            } else if ("Saloon".equals(r.getName())) {
                r.dPos = new DefaultPos(630, 420);
            } else if ("Secret Hideout".equals(r.getName())) {
                r.dPos = new DefaultPos(260, 840);
            } else if ("Train Station".equals(r.getName())) {
                r.dPos = new DefaultPos(30, 200);
            } else if ("office".equals(r.getName())) {
                r.dPos = new DefaultPos(30, 570);
            } else if ("trailer".equals(r.getName())) {
                r.dPos = new DefaultPos(1000, 250);
            }
        }
    }


    private void calculateAdjacency() {
        for (Room r : rooms) {
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
