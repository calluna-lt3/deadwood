import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

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


    public ArrayList<SceneCard> getSceneCards() {
        ArrayList<SceneCard> cards = new ArrayList<SceneCard>();

        // for each card
        for (int i=0; i<cardList.getLength(); i++) {
            Node card = cardList.item(i);
            ArrayList<Role> roles = new ArrayList<Role>();
            String cardDescription = "";
            int cardNumber = 0;

            String cardName = card.getAttributes().getNamedItem("name").getNodeValue();
            String img = card.getAttributes().getNamedItem("img").getNodeValue();
            int cardBudget = Integer.parseInt(card.getAttributes().getNamedItem("budget").getNodeValue());

            NodeList cardChildren = card.getChildNodes();
            // could do this in different method, seems unnecessary
            for (int j=0; j<cardChildren.getLength(); j++) {
                Node cardSub = cardChildren.item(j);

                // gets scene info
                if("scene".equals(cardSub.getNodeName())) {
                    cardNumber = Integer.parseInt(cardSub.getAttributes().getNamedItem("number").getNodeValue());
                    cardDescription = cardSub.getTextContent();
                }

                // gets role info
                if("part".equals(cardSub.getNodeName())) {
                    roles.add(parsePart(cardSub, true));
                }
            }

            cards.add(new SceneCard(cardName, cardDescription, img, cardNumber, cardBudget, roles));
        }

        return cards;
    }


    private Role parsePart(Node part, boolean card) {
        DisplayInfo dInfo = null;
        String roleLine = "";
        String roleName = part.getAttributes().getNamedItem("name").getNodeValue();
        int roleLevel = Integer.parseInt(part.getAttributes().getNamedItem("level").getNodeValue());


        NodeList partChildren = part.getChildNodes();
        for (int k=0; k<partChildren.getLength(); k++) {
            Node roleSub = partChildren.item(k);

            if ("line".equals(roleSub.getNodeName())) {
                roleLine = roleSub.getTextContent();
            }

            if ("area".equals(roleSub.getNodeName())) {
                int x = Integer.parseInt(roleSub.getAttributes().getNamedItem("x").getNodeValue());
                int y = Integer.parseInt(roleSub.getAttributes().getNamedItem("y").getNodeValue());
                int h = Integer.parseInt(roleSub.getAttributes().getNamedItem("h").getNodeValue());
                int w = Integer.parseInt(roleSub.getAttributes().getNamedItem("w").getNodeValue());
                dInfo = new DisplayInfo(x, y, h, w);
            }
        }

        return new Role(roleName, roleLine, roleLevel, card, dInfo);
    }


    // parses in order of appearance
    private SoundStage parseSet(Node set) {
        NodeList setChildren = set.getChildNodes();

        ArrayList<DisplayInfo> takeInfo = new ArrayList<DisplayInfo>();
        DisplayInfo dInfo = null;
        ArrayList<String> adjRooms = new ArrayList<String>();
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

            if ("area".equals(setSub.getNodeName())) {
                int x = Integer.parseInt(setSub.getAttributes().getNamedItem("x").getNodeValue());
                int y = Integer.parseInt(setSub.getAttributes().getNamedItem("y").getNodeValue());
                int h = Integer.parseInt(setSub.getAttributes().getNamedItem("h").getNodeValue());
                int w = Integer.parseInt(setSub.getAttributes().getNamedItem("w").getNodeValue());
                dInfo = new DisplayInfo(x, y, h, w);
            }

            // get shotmarkers
            if("takes".equals(setSub.getNodeName())) {
                NodeList takesChildren = setSub.getChildNodes();

                for (int j=0; j<takesChildren.getLength(); j++) {
                    Node takesSub = takesChildren.item(j);

                    if("take".equals(takesSub.getNodeName())) {
                        shotMarkers++;

                        NodeList takeChildren = takesSub.getChildNodes();
                        for (int k=0; k<takeChildren.getLength(); k++) {
                            Node takeSub = takeChildren.item(k);

                            if ("area".equals(takeSub.getNodeName())) {
                                int x = Integer.parseInt(takeSub.getAttributes().getNamedItem("x").getNodeValue());
                                int y = Integer.parseInt(takeSub.getAttributes().getNamedItem("y").getNodeValue());
                                int h = Integer.parseInt(takeSub.getAttributes().getNamedItem("h").getNodeValue());
                                int w = Integer.parseInt(takeSub.getAttributes().getNamedItem("w").getNodeValue());
                                takeInfo.add(new DisplayInfo(x, y, h, w));
                            }

                        }

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

        return new SoundStage(name, adjRooms, roles, shotMarkers, dInfo, takeInfo);
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
        DisplayInfo dInfo = null;
        final String name = "office";

        NodeList officeChildren = office.getChildNodes();
        for (int i=0; i<officeChildren.getLength(); i++) {
            Node officeSub = officeChildren.item(i);

            if ("neighbors".equals(officeSub.getNodeName())) {
                adjRooms = parseNeighbors(officeSub);
            }

            if ("area".equals(officeSub.getNodeName())) {
                int x = Integer.parseInt(officeSub.getAttributes().getNamedItem("x").getNodeValue());
                int y = Integer.parseInt(officeSub.getAttributes().getNamedItem("y").getNodeValue());
                int h = Integer.parseInt(officeSub.getAttributes().getNamedItem("h").getNodeValue());
                int w = Integer.parseInt(officeSub.getAttributes().getNamedItem("w").getNodeValue());
                dInfo = new DisplayInfo(x, y, h, w);
            }
        }

        return new InertRoom(name, adjRooms, true, dInfo);
    }


    private Room parseTrailer(Node trailer) {
        ArrayList<String> adjRooms = new ArrayList<String>(0);
        DisplayInfo dInfo = null;
        final String name = "trailer";

        NodeList trailerChildren = trailer.getChildNodes();
        for (int i=0; i<trailerChildren.getLength(); i++) {
            Node trailerSub = trailerChildren.item(i);

            if ("neighbors".equals(trailerSub.getNodeName())) {
                adjRooms = parseNeighbors(trailerSub);
            }

            if ("area".equals(trailerSub.getNodeName())) {
                int x = Integer.parseInt(trailerSub.getAttributes().getNamedItem("x").getNodeValue());
                int y = Integer.parseInt(trailerSub.getAttributes().getNamedItem("y").getNodeValue());
                int h = Integer.parseInt(trailerSub.getAttributes().getNamedItem("h").getNodeValue());
                int w = Integer.parseInt(trailerSub.getAttributes().getNamedItem("w").getNodeValue());
                dInfo = new DisplayInfo(x, y, h, w);
            }
        }

        return new InertRoom(name, adjRooms, false, dInfo);
    }
}
