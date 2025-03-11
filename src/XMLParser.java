import java.util.ArrayList;


import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.sql.Array;

public class XMLParser {
    private String boardPath;
    private String cardsPath;
    private NodeList cardList;
    private NodeList roomList;
    private static final String defaultBoardPath = "./data/xml/board.xml";
    private static final String defaultCardsPath = "./data/xml/cards.xml";

    public enum type {
        CARDS,
        BOARD,
        ALL,
    }

    XMLParser(type type) {
        this.boardPath = defaultBoardPath;
        this.cardsPath = defaultCardsPath;
        this.init(type);
    }


    XMLParser(type type, String boardPath, String cardsPath) {
        this.boardPath = boardPath;
        this.cardsPath = cardsPath;
        this.init(type);
    }


    // lots of duplicate code, idc
    private void init(type type) {

        try {
            Element cardRoot = null;
            Element boardRoot = null;

            Document cardDoc = null;
            Document boardDoc = null;

            switch (type) {
                case ALL:
                    cardDoc = getDoc(cardsPath);
                    boardDoc = getDoc(boardPath);

                    cardRoot = cardDoc.getDocumentElement();
                    boardRoot = boardDoc.getDocumentElement();

                    cardList = cardRoot.getElementsByTagName("card");
                    roomList = boardRoot.getChildNodes();
                    break;
                case BOARD:
                    boardDoc = getDoc(boardPath);
                    boardRoot = boardDoc.getDocumentElement();
                    roomList = boardRoot.getChildNodes();
                    break;
                case CARDS:
                    cardDoc = getDoc(cardsPath);
                    cardRoot = cardDoc.getDocumentElement();
                    cardList = cardRoot.getElementsByTagName("card");
                    break;
                default:
                    System.err.println("fatal error");
                    System.exit(1);
            }
        } catch (Exception ex) {
            System.err.println("ERROR: " + ex);
            System.exit(1);
        }
    }


    private Document getDoc(String filename) throws ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = null;

        try {
            doc = db.parse(filename);
        } catch (Exception ex) {
            System.err.println("ERROR: XML parse failure");
            ex.printStackTrace();
            System.exit(1);
        }
        return doc;
    }


    public ArrayList<Room> getRooms() {
        ArrayList<Room> rooms = new ArrayList<Room>();

        // for each room
        for (int i=0; i<roomList.getLength(); i++) {
            Node room = roomList.item(i);

            if("set".equals(room.getNodeName())) {
                rooms.add(parseSet(room));
            }

            if("trailer".equals(room.getNodeName())) {
                rooms.add(parseTrailer(room));
            }

            if("office".equals(room.getNodeName())) {
                rooms.add(parseOffice(room));
            }
        }

        return rooms;
    }

    // TODO: get image
    public ArrayList<SceneCard> getSceneCards() {
        ArrayList<SceneCard> cards = new ArrayList<SceneCard>();

        // for each card
        for (int i=0; i<cardList.getLength(); i++) {
            Node card = cardList.item(i);
            ArrayList<Role> roles = new ArrayList<Role>();
            String cardDescription = "";
            int cardNumber = 0;

            String cardName = card.getAttributes().getNamedItem("name").getNodeValue();
            int cardBudget = Integer.parseInt(card.getAttributes().getNamedItem("budget").getNodeValue());

            NodeList cardChildren = card.getChildNodes();
            // could do this in different method, seems unnecessary
            for (int j=0; j<cardChildren.getLength(); j++) {
                Node cardSub = cardChildren.item(j);

                // gets scene info
                if("scene".equals(cardSub.getNodeName())) {
                    cardNumber = Integer.parseInt(cardSub.getAttributes().getNamedItem("number").getNodeValue());
                    cardDescription = cardSub.getTextContent(); // TODO: format text (THIS IS A PAIN)     ■ TODO: format text (THIS IS A PAIN)
                }

                // gets role info
                if("part".equals(cardSub.getNodeName())) {
                    roles.add(parsePart(cardSub, true));
                }
            }

            cards.add(new SceneCard(cardName, cardDescription, cardNumber, cardBudget, roles));
        }

        return cards;
    }


    private Role parsePart(Node part, boolean card) {
        String roleLine = "";
        String roleName = part.getAttributes().getNamedItem("name").getNodeValue();
        int roleLevel = Integer.parseInt(part.getAttributes().getNamedItem("level").getNodeValue());

        // TODO: get area info
        NodeList partChildren = part.getChildNodes();
        for (int k=0; k<partChildren.getLength(); k++) {
            Node roleSub = partChildren.item(k);

            if ("line".equals(roleSub.getNodeName())) {
                roleLine = roleSub.getTextContent();
            }
        }

        return new Role(roleName, roleLine, roleLevel, card);
    }


    // parses in order of appearance
    private SoundStage parseSet(Node set) {
        NodeList setChildren = set.getChildNodes();

        ArrayList<String> adjRooms = new ArrayList<String>(0);
        ArrayList<Role> roles = new ArrayList<Role>();
        int shotMarkers = 0;

        String name = set.getAttributes().getNamedItem("name").getNodeValue();

        // for set children
        for (int i=0; i<setChildren.getLength(); i++) {
            Node setSub = setChildren.item(i);

            // get adjacent rooms
            if("neighbors".equals(setSub.getNodeName())) {
                adjRooms = parseNeighbors(setSub);
            }


            // TODO: get area info


            // get shotmarkers
            if("takes".equals(setSub.getNodeName())) {
                NodeList takesChildren = setSub.getChildNodes();

                for (int j=0; j<takesChildren.getLength(); j++) {
                    Node takesSub = takesChildren.item(j);

                    if("take".equals(takesSub.getNodeName())) {
                        shotMarkers++;
                    }
                }
            }

            // get roles
            if("parts".equals(setSub.getNodeName())) {
                NodeList partsChildren = setSub.getChildNodes();

                for (int j=0; j<partsChildren.getLength(); j++) {
                    Node partsSub = partsChildren.item(j);

                    if("part".equals(partsSub.getNodeName())) {
                        roles.add(parsePart(partsSub, false));
                    }
                }
            }
        }

        return new SoundStage(name, adjRooms, roles, shotMarkers);
    }


    private ArrayList<String> parseNeighbors(Node neighbors) {
        ArrayList<String> adjRooms = new ArrayList<>();
        NodeList neighborsChildren = neighbors.getChildNodes();

        for (int j=0; j<neighborsChildren.getLength(); j++) {
            Node neighborsSub = neighborsChildren.item(j);

            if ("neighbor".equals(neighborsSub.getNodeName())) {
                adjRooms.add(neighborsSub.getAttributes().getNamedItem("name").getNodeValue());
            }
        }

        return adjRooms;
    }


    // TODO: parse upgrades
    private Room parseOffice(Node office) {
        ArrayList<String> adjRooms = new ArrayList<String>(0);
        final String name = "office";

        NodeList officeChildren = office.getChildNodes();
        for (int i=0; i<officeChildren.getLength(); i++) {
            Node officeSub = officeChildren.item(i);

            if ("neighbors".equals(officeSub.getNodeName())) {
                adjRooms = parseNeighbors(officeSub);
            }

        }

        return new InertRoom(name, adjRooms, true);
    }


    // TODO: parse area
    private Room parseTrailer(Node trailer) {
        ArrayList<String> adjRooms = new ArrayList<String>(0);
        final String name = "trailer";

        NodeList trailerChildren = trailer.getChildNodes();
        for (int i=0; i<trailerChildren.getLength(); i++) {
            Node trailerSub = trailerChildren.item(i);

            if ("neighbors".equals(trailerSub.getNodeName())) {
                adjRooms = parseNeighbors(trailerSub);
            }

        }

        return new InertRoom(name, adjRooms, false);
    }
}
