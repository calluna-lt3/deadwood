import java.util.ArrayList;
import java.util.ListIterator;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;



public class GUIView extends JFrame implements View {
    InputController ctrl;
    private static final String cardPath = "./data/card/";
    private static final String dicePath = "./data/dice/";
    private static final char[] colorArray = {'b', 'c', 'g', 'o', 'p', 'r', 'v', 'y'};
    private static boolean firstDay = true;

    public void endFirstDay() {
        firstDay = false;
    }


    // default player locations for each room
    // h, w => const
    // x, y


    // JLabels
    JLabel boardlabel;
    JLabel cardlabel;
    JLabel playerlabel;
    JLabel mLabel;

    /* Buttons */
    // No extra input
    JButton bHelp; // popup
    JButton bAct;
    JButton bRehearse;
    JButton bPass;

    // Extra input
    JButton bMove;
    JButton bRole;
    JButton bUpgrade;

    // Combo box
    JComboBox<String> cbMove;
    JComboBox<String> cbRole;

    // JLayered Pane
    JLayeredPane lPane;

    GUIView() {
        super("Deadwood");
    }

    public void setController(InputController ctrl) {
        this.ctrl = ctrl;
    }


    /* immediate:
     *  help, act, rehearse, pass
     *
     * irrelevant: (can be repurposed)
     *  who: player info
     *  loc: update player locations
     *  roles: update roles combo box
     *  rooms: update rooms combo box
     *
     * needs other input:
     *  move: dropdown
     *  takerole: dropdown
     *  upgrade: popup
     * 
     * new methods:
     * updateboard: change cards and shot markers
     *
     * errors:
     */
    class boardMouseListener implements MouseListener {
        public void mouseClicked(MouseEvent e) {

            if (e.getSource() == bHelp) {
                System.out.println("bHelp");
                InVec args = new InVec(Enums.action.HELP, null, null);
                ctrl.processAction(args);
            } else if (e.getSource() == bAct) {
                System.out.println("bAct");
                InVec args = new InVec(Enums.action.ACT, null, null);
                ctrl.processAction(args);
            } else if (e.getSource() == bRehearse) {
                System.out.println("bRehearse");
                InVec args = new InVec(Enums.action.REHEARSE, null, null);
                ctrl.processAction(args);
            } else if (e.getSource() == bPass) {
                System.out.println("bPass");
                InVec args = new InVec(Enums.action.PASS, null, null);
                ctrl.processAction(args);
            } else if (e.getSource() == bMove) {
                System.out.println("bMove");
                int index = cbMove.getSelectedIndex();
                InVec args = new InVec(Enums.action.MOVE, Integer.toString(index), null);
                ctrl.processAction(args);
            } else if (e.getSource() == bRole) {
                System.out.println("bRole");
                int index = cbRole.getSelectedIndex();
                InVec args = new InVec(Enums.action.TAKE_ROLE, Integer.toString(index), null);
                ctrl.processAction(args);
            }
        }

        public void mousePressed(MouseEvent e) { }
        public void mouseReleased(MouseEvent e) { }
        public void mouseEntered(MouseEvent e) { }
        public void mouseExited(MouseEvent e) { }
    }


    private JButton createButton(String name, int x, int y, int h, int w) {
        JButton button = new JButton(name);
        button.setBackground(Color.white);
        button.setBounds(x, y, h, w);
        button.addMouseListener(new boardMouseListener());
        return button;
    }


    /* Layers
     *  0: board
     *  1: cards
     *  2: buttons, players
     *  3: alerts
    */
    public void displayInit() {

        // Set the exit option for the JFrame
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Create the JLayeredPane to hold the display, cards, dice and buttons
        lPane = getLayeredPane();

        // Create the deadwood board
        boardlabel = new JLabel();
        ImageIcon icon =  new ImageIcon("./data/board.jpg");
        boardlabel.setIcon(icon);

        int bw = icon.getIconWidth();
        int bh = icon.getIconHeight();

        boardlabel.setBounds(0, 0, bw, bh);

        // Add the board to the lowest layer
        lPane.add(boardlabel, 0);

        // Set the size of the GUI
        setSize(bw + 200, bh);

        // Add a scene card to this room
        // cardlabel = new JLabel();
        // ImageIcon cIcon =  new ImageIcon("./data/card/01.png");
        // cardlabel.setIcon(cIcon);
        // cardlabel.setBounds(20, 65, cIcon.getIconWidth()+2, cIcon.getIconHeight());
        // cardlabel.setOpaque(true);

        // Add the card to the lower layer
        // lPane.add(cardlabel, 1);


        // Add a dice to represent a player.
        // Role for Crusty the prospector. The x and y co-ordiantes are taken from Board.xml file
        playerlabel = new JLabel();
        ImageIcon pIcon = new ImageIcon("./data/dice/r2.png");
        playerlabel.setIcon(pIcon);
        //playerlabel.setBounds(114,227,pIcon.getIconWidth(),pIcon.getIconHeight());
        playerlabel.setBounds(114, 227, 46, 46);
        playerlabel.setVisible(true);
        lPane.add(playerlabel, 3);

        // Create the Menu for action buttons
        mLabel = new JLabel("MENU");
        mLabel.setBounds(bw+40, 0, 100, 20);
        lPane.add(mLabel,2);


        /* Create buttons */

        // left column
        bAct = createButton("ACT", bw + 10, 30, 100, 20);
        bRehearse = createButton("REHEARSE", bw + 10, 60, 100, 20);
        bPass = createButton("PASS", bw + 10, 90, 100, 20);
        bHelp = createButton("HELP", bw + 10, 120, 100, 20);

        // right column
        bMove = createButton("MOVE", bw + 120, 30, 100, 20);
        bRole = createButton("ROLE", bw + 120, 90, 100, 20);

        // Combo Buttons
        cbMove = new JComboBox<String>();
        cbMove.setBackground(Color.white);
        cbMove.setBounds(bw + 120, 60, 100, 20);
        cbMove.addItem("<None>");
        cbMove.addItem("Main Street");
        cbMove.addItem("Saloon");
        cbMove.addItem("Hotel");

        cbRole = new JComboBox<String>();
        cbRole.setBackground(Color.white);
        cbRole.setBounds(bw + 120, 120, 100, 20);
        cbRole.addItem("<None>");

        // Place the action buttons in the top layer
        lPane.add(bAct, 2);
        lPane.add(bPass, 2);
        lPane.add(bRole, 2);
        lPane.add(bHelp, 2);

        lPane.add(bRehearse, 2);
        lPane.add(bMove, 2);

        lPane.add(cbMove, 2);
        lPane.add(cbRole, 2);
    }


    public InitInfo getPlayerInfo() {
        int numPlayers = 0;
        do {
            try {
                String input = JOptionPane.showInputDialog(this, "How many players? (2-8)");
                numPlayers = Integer.parseInt(input);
            } catch (Exception e) {
                continue;
            }
        } while (numPlayers < 2 || numPlayers > 8);

        // TODO: get player names
        //  list of input boxes would be nice, parse that for numplayers
        System.out.println(numPlayers);
        return new InitInfo(2, new String[] {"a", "b"});
    }


    public void displayHelp() {
        JOptionPane.showMessageDialog(null, "help meee", "Help", JOptionPane.INFORMATION_MESSAGE);
    }


    public void displayWho(Player p) {
        // TODO: display player info on the side bar
    }

    // initialization method, dont call more than once
    public void displayLocations(Player activePlayer, Player[] players) {
        int offset = 0;
        for (int i=0; i<players.length; i++) {
            Player p = players[i];
            if (firstDay) {
                p.setLabel(new JLabel());
            }
            JLabel label = p.getLabel();
            int x = p.getRoom().getDefaultPos().x() + offset;
            int y = p.getRoom().getDefaultPos().y();
            label.setBounds(x, y, 40, 40);
            offset += 20;
            String imgName = colorArray[i] + "1.png";
            label.setIcon(new ImageIcon(dicePath + imgName));
            label.setOpaque(true);
            lPane.add(label, 2);
        }
    }


    public void displayRoles(SoundStage ss) {
        int numRoles = ss.getRoleCount();
        cbRole.removeAll();
        cbRole.addItem("<None>");

        // getRole() is 1 indexed :(
        for (int i=1; i<numRoles+1; i++) {
            Role role = ss.getRole(i);
            String name = role.getName();
            cbRole.addItem(name);
        }

    }


    // only one failure condition, don't have to check errno
    public void displayRoles(Enums.errno errno) {
        cbRole.removeAll();
        cbRole.addItem("<None>");
    }


    public void displayRooms(Room room) {
        ArrayList<Room> adjRooms = room.getAdjacentRooms();
        cbMove.removeAll();
        cbMove.addItem("<None>");
        for (Room r : adjRooms) {
            cbMove.addItem(r.getName());
        }
    }


    // Infallible actions
    // this method signature is bad, but its the easiest way to implement this with for now
    public void displayEndGame(Player[] players, int[] scores, String[] winners, int winningScore) { }
    public void displayPassTurn(Player player) { }

    public void displayStartDay(ArrayList<Room> rooms) {
        ListIterator<Room> it = rooms.listIterator();
        while (it.hasNext()) {
            Room r;
            if ((r = it.next()) instanceof SoundStage) {
                SoundStage ss = (SoundStage)r;
                if (firstDay) {
                    JLabel label = new JLabel();
                    DisplayInfo dInfo = ss.getCardDisplayInfo();
                    label.setBounds(dInfo.x(), dInfo.y(), dInfo.w(), dInfo.h());
                    ss.setLabel(label);
                }
                String imgName = ss.getCard().getImg();
                ss.getLabel().setIcon(new ImageIcon(cardPath + imgName));
                ss.getLabel().setOpaque(true);
                lPane.add(ss.getLabel(), 1);
            } 
        }
    }

    public void displayDiceRolls(int... diceRolled) { } // NOTE: only view method called outside of takeTurn()

    // Fallible actions (return 0 on success, non-zero code on fail)
    public void displayTakeRole(Role role) { }
    public void displayTakeRole(Enums.errno errno) { }


    public void displayMove(Room room) {
        System.out.println(room.getName());
    }


    public void displayMove(Enums.errno errno) {
        System.out.println(errno);
    }


    public void displayUpgrade(int rank) { }
    public void displayUpgrade(Enums.errno errno) { }
    public void displayRehearse(Player player) { }
    public void displayRehearse(Enums.errno errno) { }

    // Stochastic actions (return the result of a dice check)
    public void displayAct(boolean success) { }
    public void displayAct(Enums.errno errno) { }

    /* UNUSED */
    public InVec getUserAction() { return null; }
}
