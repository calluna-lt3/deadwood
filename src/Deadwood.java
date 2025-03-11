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

        View v;
        if (hasGui) {
            System.out.println("CFG: GUI");
            v = new GUIView();
        } else {
            System.out.println("CFG: TUI");
            v = new ConsoleView();
        }

        InputController ctrl = new InputController(v);
        ctrl.run();
    }
}
