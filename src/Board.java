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
                r.dPos = new DefaultPos(630, 600);
            } else if ("Church".equals(r.getName())) {
                r.dPos = new DefaultPos(750, 670);
            } else if ("General Store".equals(r.getName())) {
                r.dPos = new DefaultPos(300, 400);
            } else if ("Hotel".equals(r.getName())) {
                r.dPos = new DefaultPos(980, 855);
            } else if ("Jail".equals(r.getName())) {
                r.dPos = new DefaultPos(280, 170);
            } else if ("Main Street".equals(r.getName())) {
                r.dPos = new DefaultPos(980, 170);
            } else if ("Ranch".equals(r.getName())) {
                r.dPos = new DefaultPos(240, 630);
            } else if ("Saloon".equals(r.getName())) {
                r.dPos = new DefaultPos(610, 400);
            } else if ("Secret Hideout".equals(r.getName())) {
                r.dPos = new DefaultPos(240, 820);
            } else if ("Train Station".equals(r.getName())) {
                r.dPos = new DefaultPos(10, 180);
            } else if ("office".equals(r.getName())) {
                r.dPos = new DefaultPos(10, 550);
            } else if ("trailer".equals(r.getName())) {
                r.dPos = new DefaultPos(1000, 300);
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
