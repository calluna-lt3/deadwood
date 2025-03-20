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


    // JLabels
    JLabel boardlabel;
    JLabel cardlabel;
    JLabel playerlabel;
    JLabel mLabel;
    JLabel dLabel;


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
    JComboBox<String> cbUpgradeRank;
    JComboBox<String> cbUpgradeType;

    // Player information box
    JPanel playerInfoPane;
    JTextArea playerInfoText;

    // Error display box
    JPanel errPanel;
    JLabel errText;

    // Show dice for each player
    JLabel turnIcon;

    // For dice
    JPanel dicePanel;
    JLabel[] rollDice;

    // For succ/failure of actin
    JLabel actState;
    JPanel actStatePanel;

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
                InVec args = new InVec(Enums.action.HELP, null, null);
                ctrl.processAction(args);
            } else if (e.getSource() == bAct) {
                InVec args = new InVec(Enums.action.ACT, null, null);
                ctrl.processAction(args);
            } else if (e.getSource() == bRehearse) {
                InVec args = new InVec(Enums.action.REHEARSE, null, null);
                ctrl.processAction(args);
            } else if (e.getSource() == bPass) {
                InVec args = new InVec(Enums.action.PASS, null, null);
                ctrl.processAction(args);
            } else if (e.getSource() == bMove) {
                int index = cbMove.getSelectedIndex();
                InVec args = new InVec(Enums.action.MOVE, Integer.toString(index), null);
                ctrl.processAction(args);
            } else if (e.getSource() == bUpgrade) {
                int rank = cbUpgradeRank.getSelectedIndex();
                String type = cbUpgradeType.getSelectedItem().toString().toLowerCase();
                InVec args = new InVec(Enums.action.UPGRADE, Integer.toString(rank+1), type);
                ctrl.processAction(args);
            } else if (e.getSource() == bRole) {
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
        setBounds(0, 0, 600, 600);
        setSize(1000, 1000);

        // Create the deadwood board
        boardlabel = new JLabel();
        ImageIcon icon =  new ImageIcon("./data/board.jpg");
        boardlabel.setIcon(icon);

        int bw = icon.getIconWidth();
        int bh = icon.getIconHeight();

        boardlabel.setBounds(0, 0, bw, bh);

        // Add the board to the lowest layer
        lPane.add(boardlabel, 0);

        // GUI size, for some reason had to do this manually as bw/bh was not real bh/bw
        setSize(bw + 240, bh+25);

        // Create the Menu for action buttons
        mLabel = new JLabel("MENU");
        mLabel.setBounds(bw+95, 0, 210, 20);

        dLabel = new JLabel("DICE");
        dLabel.setBounds(bw+95, bh-550, 210, 20);

        lPane.add(mLabel,2);
        lPane.add(dLabel,2);


        /* Text field */
        playerInfoPane = new JPanel();
        playerInfoPane.setBounds(bw+10, bh-400, 210, 200);
        playerInfoPane.setBackground(Color.white);

        playerInfoText = new JTextArea("Player Information");
        playerInfoText.setEditable(false);
        playerInfoText.setTabSize(2);

        playerInfoPane.add(playerInfoText);

        errPanel = new JPanel();
        errPanel.setBounds(bw+10, bh-620, 210, 30);
        errPanel.setBackground(new Color(1.0f, 0.5f, 0.5f));
        errPanel.setVisible(false);

        errText = new JLabel();
        errText.setBounds(0, 0, 210, 30);

        errPanel.add(errText);

        turnIcon = new JLabel();
        turnIcon.setOpaque(true);
        turnIcon.setBounds(bw + 95, bh - 260, 40, 40);

        dicePanel = new JPanel();
        dicePanel.setLayout(new GridLayout(2, 3, 0, 20));
        dicePanel.setBounds(bw+10, bh-530, 210, 120);
        dicePanel.setBackground(Color.white);
        dicePanel.setVisible(true);


        rollDice = new JLabel[6];
        for (int i = 0; i < 6; i++) {
            JLabel j = rollDice[i] = new JLabel();
            dicePanel.add(j);
            j.setBounds(20 + 60 * (i % 3), 20 + 60 * (i / 3), 60, 40);
            j.setVisible(false);
        }

        actStatePanel = new JPanel();
        actStatePanel.setBounds(bw+10, bh-590, 210, 30);
        actStatePanel.setVisible(false);

        actState = new JLabel();

        actStatePanel.add(actState);

        /* Create buttons */

        // left column
        bAct = createButton("ACT", bw + 10, 30, 100, 20);
        bRehearse = createButton("REHEARSE", bw + 10, 60, 100, 20);
        bPass = createButton("PASS", bw + 10, 90, 100, 20);
        bHelp = createButton("HELP", bw + 10, 120, 100, 20);

        // right column
        bMove = createButton("MOVE", bw + 120, 30, 100, 20);
        bRole = createButton("TAKE ROLE", bw + 120, 90, 100, 20);
        bUpgrade = createButton("UPGRADE", bw + 120, 150, 100, 20);

        // Combo Buttons
        cbMove = new JComboBox<String>();
        cbMove.setBackground(Color.white);
        cbMove.setBounds(bw + 120, 60, 100, 20);
        // These values are hard-coded to avoid accessing Board during initialization
        cbMove.addItem("<None>");
        cbMove.addItem("Main Street");
        cbMove.addItem("Saloon");
        cbMove.addItem("Hotel");

        cbRole = new JComboBox<String>();
        cbRole.setBackground(Color.white);
        cbRole.setBounds(bw + 120, 120, 100, 20);
        cbRole.addItem("<None>");

        cbUpgradeType = new JComboBox<String>();
        cbUpgradeType.setBackground(Color.white);
        cbUpgradeType.setBounds(bw + 120, 180, 100, 20);
        cbUpgradeType.addItem("Money");
        cbUpgradeType.addItem("Credits");

        cbUpgradeRank = new JComboBox<String>();
        cbUpgradeRank.setBackground(Color.white);
        cbUpgradeRank.setBounds(bw + 120, 210, 100, 20);
        for (int i=1; i<7; i++) { cbUpgradeRank.addItem("Rank " + i); }


        /* Add to layered pane */
        lPane.add(playerInfoPane, 2);
        lPane.add(errPanel, 2);

        lPane.add(bAct, 2);
        lPane.add(bPass, 2);
        lPane.add(bRole, 2);
        lPane.add(bHelp, 2);
        lPane.add(bUpgrade, 2);

        lPane.add(bRehearse, 2);
        lPane.add(bMove, 2);

        lPane.add(cbMove, 2);
        lPane.add(cbRole, 2);
        lPane.add(cbUpgradeRank, 2);
        lPane.add(cbUpgradeType, 2);

        lPane.add(turnIcon, 2);

        lPane.add(dicePanel, 2);

        lPane.add(actStatePanel, 2);
    }


    public InitInfo getPlayerInfo() {
        int numPlayers = 0;
        do {
            try {
                String input = JOptionPane.showInputDialog(this, "How many players? (2-8)");
                if (input == null) continue;
                numPlayers = Integer.parseInt(input);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Player count must be a number.");
                continue;
            }

            if (numPlayers < 2 || numPlayers > 8) {
                JOptionPane.showMessageDialog(this, "Player count must be between 2 and 8.");
                continue;
            }

            break;
        } while (true);

        String[] players = new String[numPlayers];
        int curPlayer = 0;
        do {
            String input = JOptionPane.showInputDialog(this, "Enter name for player number " + (curPlayer + 1) + ".");
            if (input == null) continue;
            if ("".equals(input)) {
                JOptionPane.showMessageDialog(this, "Name must be at least one character.");
                continue;
            }

            boolean nameExists = false;
            for (int i=0; i<curPlayer; i++) {
                if (players[i].equals(input)) {
                    nameExists = true;
                    break;
                }
            }

            if (nameExists) {
                JOptionPane.showMessageDialog(this, "Name cannot be an already entered name.");
                continue;
            }

            players[curPlayer] = input;
            curPlayer++;
        } while (curPlayer < numPlayers);

        return new InitInfo(numPlayers, players);
    }


    public void displayHelp() {
        JOptionPane.showMessageDialog(null, "See Deadwood rules for information on how to play the game.", "Help", JOptionPane.INFORMATION_MESSAGE);
    }


    public void displayWho(Player player) {
        String name = player.getName();
        String rank = Integer.toString(player.getRank());
        String money = Integer.toString(player.getMoney());
        String credits = Integer.toString(player.getCredits());
        String tokens = Integer.toString(player.getRehearsalTokens());
        String role = (player.getRole() != null) ? player.getRole().getName() : "none";
        String line = (player.getRole() != null) ? player.getRole().getLine() : "";

        playerInfoText.setText("Active player: " + name
            + "\n\tRank: " + rank
            + "\n\tMoney: " + money
            + "\n\tCredits: " + credits
            + "\n\tTokens: " + tokens
            + "\n\tJob: " + role
            + "\n\tLine: " + line);
    }


    // initialization method, dont call more than once
    public void displayLocations(Player activePlayer, Player[] players) {
        int offset = 0;
        for (int i=0; i<players.length; i++) {
            Player p = players[i];

            if (firstDay) {
                JLabel label = new JLabel();
                p.setLabel(label);
                String imgName = colorArray[i] + "1.png";
                label.setIcon(new ImageIcon(dicePath + imgName));
                label.setOpaque(true);
                lPane.add(label, 2);
            }

            JLabel label = p.getLabel();
            int x = p.getRoom().getDefaultPos().x() + offset;
            int y = p.getRoom().getDefaultPos().y();
            label.setBounds(x, y, 40, 40);
            offset += 20;
        }
        turnIcon.setIcon(activePlayer.getLabel().getIcon());
    }


    public void displayRoles(SoundStage ss) {
        cbRole.removeAllItems();
        cbRole.addItem("<None>");

        int numRoles = ss.getRoleCount();
        // getRole() is 1 indexed :(
        for (int i=1; i<numRoles+1; i++) {
            Role role = ss.getRole(i);
            String name = role.getName();
            cbRole.addItem(name);
        }
        errPanel.setVisible(false);
    }

    // only one failure condition, don't have to check errno
    public void displayRoles(Enums.errno errno) {
        cbRole.removeAllItems();
        cbRole.addItem("<None>");
    }

    public void displayRooms(Room room) {
        ArrayList<Room> adjRooms = room.getAdjacentRooms();
        cbMove.removeAllItems();
        cbMove.addItem("<None>");
        for (Room r : adjRooms) {
            cbMove.addItem(r.getName());
        }
    }

    public void displayEndGame(Player[] players, int[] scores, String[] winners, int winningScore) {
        boolean plural = (winners.length > 1) ? true : false;
        StringBuilder output = new StringBuilder();
        output.append("The game is over!" + "\n");

        if (plural) {
            output.append("Winners with " + winningScore + " points: ");
            for (String winner : winners) {
                output.append(winner + " ");
            }
            output.append("\n");
        } else {
            output.append("Winner with " + winningScore + " points: " + winners[0] + "\n");
        }

        output.append("Player scores:" + "\n");
        for (int i=0; i<players.length; i++) {
            output.append("\t" + players[i].getName() + ": " + scores[i] + "\n");
        }

        JOptionPane.showMessageDialog(this, output);
        System.exit(0);
    }


    public void displayPassTurn(Player player) {
        turnIcon.setIcon(player.getLabel().getIcon());
        errPanel.setVisible(false);
        for (int i = 0; i < 6; i++) {
            rollDice[i].setVisible(false);
        }
        actStatePanel.setVisible(false);
    }


    public void displayStartDay(ArrayList<Room> rooms) {
        ListIterator<Room> it = rooms.listIterator();
        while (it.hasNext()) {
            Room r;
            if ((r = it.next()) instanceof SoundStage) {
                SoundStage ss = (SoundStage)r;
                initShotMarkers(ss);
                if (firstDay) {
                    DisplayInfo dInfo = ss.getCardDisplayInfo();

                    JLabel cardLabel = new JLabel();
                    cardLabel.setBounds(dInfo.x(), dInfo.y(), dInfo.w(), dInfo.h());
                    ss.setCardLabel(cardLabel);

                    JLabel cardNotVisibleLabel = new JLabel();
                    cardNotVisibleLabel.setBounds(dInfo.x(), dInfo.y(), dInfo.w(), dInfo.h());
                    cardNotVisibleLabel.setIcon(new ImageIcon("./data/card/00.png"));
                    ss.setCardNotVisibleLabel(cardNotVisibleLabel);
                }

                String imgName = ss.getCard().getImg();
                ss.getCardLabel().setIcon(new ImageIcon(cardPath + imgName));
                ss.getCardLabel().setOpaque(true);
                lPane.add(ss.getCardLabel(), 0);

                ss.getCardNotVisibleLabel().setVisible(true);
                lPane.add(ss.getCardNotVisibleLabel(), 0); // so it shows over
            }
        }
    }


    public void displayDiceRolls(int... diceRolled) {
        for (int i = 0; i < diceRolled.length; i++) {
            JLabel j = rollDice[i];
            j.setVisible(true);
            j.setIcon(new ImageIcon(dicePath + "w" + diceRolled[i] + ".png"));
        }
        dicePanel.setVisible(true);
    } // NOTE: only view method called outside of takeTurn()


    public void displayTakeRole(Role role) {
        Player p = ctrl.mod.getCurrentPlayer();
        JLabel label = p.getLabel();
        int x = role.getDisplayInfo().x();
        int y = role.getDisplayInfo().y();
        label.setBounds(x, y, 40, 40);
        errPanel.setVisible(false);
    }


    public void displayTakeRole(Enums.errno errno) {
        switch (errno) {
            case OOB:
                errText.setText("Role does not exist");
                break;
            case BAD_ROOM:
                errText.setText("Room has no roles");
                break;
            case IN_ROLE:
                errText.setText("You already have a role");
                break;
            case BAD_ARGS:
                errText.setText("Role index must be a number");
                break;
            case LEQ:
                errText.setText("Your rank is not high enough for this role");
                break;
            case FORBIDDEN_ACTION:
                errText.setText("This role is already taken");
                break;
            default:
                System.err.println("fatal error");
                System.exit(1);
        }
        errPanel.setVisible(true);
    }


    public void displayMove(Room room) {
        Player p = ctrl.mod.getCurrentPlayer();
        int turn = ctrl.mod.getTurn();
        JLabel label = p.getLabel();
        int x = p.getRoom().getDefaultPos().x() + 20 * turn;
        int y = p.getRoom().getDefaultPos().y();
        label.setBounds(x, y, 40, 40);
        errPanel.setVisible(false);

        if (room instanceof SoundStage) {
            ((SoundStage)(room)).getCardNotVisibleLabel().setVisible(false);
        }
    }


    public void displayMove(Enums.errno errno) {
        switch (errno) {
            case FORBIDDEN_ACTION:
                errText.setText("You can't move right now");
                break;
            case OOB:
                errText.setText("Selected room isn't available");
                break;
            case BAD_ARGS:
                errText.setText("Room number must be a number");
                break;
            default:
                System.err.println("fatal error");
                System.exit(1);
        }
        errPanel.setVisible(true);
    }


    public void displayUpgrade(int rank) {
        Player p = ctrl.mod.getCurrentPlayer();
        JLabel label = p.getLabel();
        String color = Character.toString(colorArray[ctrl.mod.getTurn()]);
        String imgName = color + p.getRank() + ".png";
        label.setIcon(new ImageIcon(dicePath + imgName));
        errPanel.setVisible(false);
    }


    public void displayUpgrade(Enums.errno errno) {
        switch (errno) {
            case NO_CREDITS:
                errText.setText("Not enough credits");
                break;
            case NO_MONEY:
                errText.setText("Not enough money");
                break;
            case MAX_RANK:
                errText.setText("Already max rank");
                break;
            case OOB:
                errText.setText("Invalid input, accepted values between 2-6");
                break;
            case LEQ:
                errText.setText("Requested rank less than or equal to your current rank");
                break;
            case BAD_ARGS:
                errText.setText("Usage: upgrade <rank> <money|credits>");
                break;
            case BAD_ROOM:
                errText.setText("You are not at the casting office");
                break;
            default:
                System.err.println("fatal error");
                System.exit(1);
        }
        errPanel.setVisible(true);
    }


    public void displayRehearse(Player player) {
        displayWho(player);
        errPanel.setVisible(false);
    }


    public void displayRehearse(Enums.errno errno) {
        switch (errno) {
            case DUP_ACTION:
                errText.setText("You have already worked this turn");
                break;
            case FORBIDDEN_ACTION:
                errText.setText("You already have the maximum number rehearsal tokens for this role");
                break;
            case IN_ROLE: // player not in role
                errText.setText("You don't have a role");
                break;
            default:
                System.err.println("fatal error");
                System.exit(1);
        }
        errPanel.setVisible(true);
    }


    private void initShotMarkers(SoundStage ss) {
        if (firstDay) {
            for (DisplayInfo di : ss.takeInfo) {
                JLabel label = new JLabel();
                label.setIcon(new ImageIcon("./data/shot.png"));
                label.setBounds(di.x(), di.y(), di.w(), di.h());
                ss.getShotLabels().add(label);
                lPane.add(label, 0);
            }
        }

        for (JLabel label : ss.getShotLabels()) {
            label.setVisible(true);
        }
    }


    public void displayAct(boolean success) {
        SoundStage ss = (SoundStage)ctrl.mod.getCurrentPlayer().getRoom();

        actStatePanel.setVisible(true);
        if (success) {
            ss.getShotLabels().get(ss.getShotMarkers()).setVisible(false);;
            if (ss.getShotMarkers() == 0) {
                ss.getCardLabel().setVisible(false);
            }

            actStatePanel.setBackground(Color.green);
            actState.setText("You succeeded at acting");
        } else {
            actStatePanel.setBackground(Color.red);
            actState.setText("You failed at acting");
        }

        errPanel.setVisible(false);
    }

    public void displayAct(Enums.errno errno) {
        switch (errno) {
            case FORBIDDEN_ACTION:
                errText.setText("You don't have a role");
                break;
            case DUP_ACTION:
                errText.setText("You have already worked this turn");
                break;
            default:
                System.err.println("fatal error");
                System.exit(1);
        }
        errPanel.setVisible(true);
    }

    /* UNUSED */
    public InVec getUserAction() { return null; }
}
