import java.util.ArrayList;
import java.util.Properties;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;


public class Deadwood {
    static String configName = "deadwood.config";
    static boolean hasGui = false;

    public static void main(String[] args) {
        File configFile = new File(configName);

        try {
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);

            int gui = Integer.parseInt(props.getProperty("GUI"));
            switch (gui) {
                case 0:
                    hasGui = false;
                    break;
                case 1:
                    hasGui = true;
                    break;
                default:
                    System.err.println("Invalid config values");
                    System.exit(1);
                    break;
            }

            reader.close();
        } catch (FileNotFoundException ex) {
            System.err.println("Config file doesnt exists: ./configure.sh <tui|gui>");
            System.exit(1);
        } catch (IOException ex) {
            System.err.println("Issue reading config file");
            System.exit(1);
        }

        if (hasGui) {
            System.out.println("GUI");
        } else {
            System.out.println("TUI");
        }


        ConsoleView v = new ConsoleView();
        InputController ctrl = new InputController(v);
        ctrl.run();
    }

    public static void testParser() {
        XMLParser xmlp = new XMLParser(XMLParser.type.ALL);
        ArrayList<SceneCard> cards = xmlp.getSceneCards();
        for (SceneCard card : cards) {
            System.out.println("name: " + card.name);
            System.out.println("\tdescription: " + card.description);
            System.out.println("  number: " + card.number);
            System.out.println("  budget: " + card.budget);
            System.out.println("  Roles: ");
            for (Role role : card.roles) {
                if (role == null) continue;
                System.out.println("    name: " + role.name);
                System.out.println("    line: " + role.line);
                System.out.println("    rank: " + role.rank);
                System.out.println("");
            }
        }

        ArrayList<Room> rooms = xmlp.getRooms();
        for (Room room : rooms) {
            System.out.println("name: " + room.name);
            System.out.println("  Adjacent Rooms: ");
            for (String adjRoom : room.adjacentRoomsStr) {
                System.out.println("    " + adjRoom);
            }

            if (room instanceof InertRoom) continue;
            SoundStage ss = (SoundStage)room;
            System.out.println("  shotMarkers: " + ss.shotMarkers);

            System.out.println("  roles: ");
            for (Role role : ss.roles) {
                System.out.println("    name: " + role.name);
                System.out.println("    line: " + role.line);
                System.out.println("    rank: " + role.rank);
                System.out.println("");
            }
        }

    }
}
