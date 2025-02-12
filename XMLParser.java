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
    private String cardPath;
    private NodeList cardList;
    private NodeList roomList;


    XMLParser() {
        this.boardPath = "./data/board.xml";
        this.cardPath = "./data/cards.xml";
        this.init();
    }

    XMLParser(String boardPath, String cardPath) {
        this.boardPath = boardPath;
        this.cardPath = cardPath;
        this.init();
    }


    private void init() {
        Element cardRoot = null;
        Element boardRoot = null;

        try {
            Document cardDoc = getDoc(cardPath);
            Document boardDoc = getDoc(boardPath);

            cardRoot = cardDoc.getDocumentElement();
            boardRoot = boardDoc.getDocumentElement();
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex);
            System.exit(1);
        }

        // dont think this can throw exceptions
        cardList = cardRoot.getElementsByTagName("card");
        roomList = boardRoot.getChildNodes(); // not all elements the same name
    }


    private Document getDoc(String filename) throws ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = null;

        try {
            doc = db.parse(filename);
        } catch (Exception ex) {
            System.out.println("ERROR: XML parse failure");
            ex.printStackTrace();
            System.exit(1);
        }
        return doc;
    }


    // NOTE:
    //  any SoundStage's roles[] can have null values where there are less roles than MAX_ROLES
    //  any Room's adjacentRooms[] can have null values where there are less adj rooms than MAX_ADJ_ROOMS
    // TODO: dynamically allocate arrays to fix above
    public ArrayList<Room> getRooms() {
        ArrayList<Room> rooms = new ArrayList<Room>();

        // for each room
        for (int i=0; i<roomList.getLength(); i++) {
            Node room = roomList.item(i);

            if("set".equals(room.getNodeName())) {
                rooms.add(parseSet(room));
            }

            if("trailer".equals(room.getNodeName())) {
                // TODO: implement
            }

            if("office".equals(room.getNodeName())) {
                // TODO: implement
            }
        }

        return rooms;
    }



    // NOTE: any SceneCard's Role[] can have null values when there are less roles than MAX_ROLES
    // TODO: dynamically allocate array to fix above
    public SceneCard[] getSceneCards() {
        SceneCard[] cards = new SceneCard[cardList.getLength()];

        // for each card
        for (int i=0; i<cardList.getLength(); i++) {
            Node card = cardList.item(i);
            ArrayList<Role> roles = new ArrayList<Role>();
            String cardDescription = "";
            int cardNumber = 0;

            // TODO: get image
            String cardName = card.getAttributes().getNamedItem("name").getNodeValue();
            int cardBudget = Integer.parseInt(card.getAttributes().getNamedItem("budget").getNodeValue());

            NodeList cardChildren = card.getChildNodes();
            // for each subcategory of a card
            for (int j=0; j<cardChildren.getLength(); j++) {
                Node cardSub = cardChildren.item(j);

                // gets scene info
                if("scene".equals(cardSub.getNodeName())) {
                    cardNumber = Integer.parseInt(cardSub.getAttributes().getNamedItem("number").getNodeValue());
                    cardDescription = cardSub.getTextContent(); // TODO: format text (THIS IS A PAIN)
                }

                // gets role info
                if("part".equals(cardSub.getNodeName())) {
                    roles.add(parsePart(cardSub));
                }
            }

            cards[i] = new SceneCard(cardName, cardDescription, cardNumber, cardBudget, roles);
        }

        return cards;
    }


    private Role parsePart(Node part) {
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

        return new Role(roleName, roleLine, roleLevel, true);
    }


    // parses in order of appearance
    private SoundStage parseSet(Node set) {
        NodeList setChildren = set.getChildNodes();

        ArrayList<String> adjRooms = new ArrayList<String>();
        ArrayList<Role> roles = new ArrayList<Role>();
        int shotMarkers = 0;

        String name = set.getAttributes().getNamedItem("name").getNodeValue();

        // for set children
        for (int i=0; i<setChildren.getLength(); i++) {
            Node setSub = setChildren.item(i);

            // get adjacent rooms
            if("neighbors".equals(setSub.getNodeName())) {
                NodeList neighborsChildren = setSub.getChildNodes();

                for (int j=0; j<neighborsChildren.getLength(); j++) {
                    Node neighborsSub = neighborsChildren.item(j);

                    if ("neighbor".equals(neighborsSub.getNodeName())) {
                        adjRooms.add(neighborsSub.getAttributes().getNamedItem("name").getNodeValue());
                    }
                }
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
                        roles.add(parsePart(partsSub));
                    }
                }
            }
        }

        return new SoundStage(name, adjRooms, roles, shotMarkers);
    }






}
