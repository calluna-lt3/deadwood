import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

public class XMLParser {
    private static final int MAX_ROLES = 3;
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
        roomList = boardRoot.getElementsByTagName("room");
    }


    // NOTE: any SceneCard's Role[] can have null values where there are more roles than MAX_ROLES
    // TODO: dynamically allocate array to fix above
    public SceneCard[] getSceneCards() {
        SceneCard[] cards = new SceneCard[cardList.getLength()];

        // for each card
        for (int i=0; i<cardList.getLength(); i++) {
            Node card = cardList.item(i);
            Role[] roles = new Role[MAX_ROLES]; // see above's todo
            String cardDescription = "";
            int cardNumber = 0;

            // TODO: get image
            String cardName = card.getAttributes().getNamedItem("name").getNodeValue();
            int cardBudget = Integer.parseInt(card.getAttributes().getNamedItem("budget").getNodeValue());

            int roleCount = 0;
            NodeList cardChildren = card.getChildNodes();
            // for each subcategory of a card
            for (int j=0; j<cardChildren.getLength(); j++) {
                Node cardSub = cardChildren.item(j);

                // gets scene information
                if("scene".equals(cardSub.getNodeName())) {
                    cardNumber = Integer.parseInt(cardSub.getAttributes().getNamedItem("number").getNodeValue());
                    cardDescription = cardSub.getTextContent(); // TODO: format text (THIS IS A PAIN)
                }

                // gets role information
                if("part".equals(cardSub.getNodeName())) {
                    String roleLine = "";
                    String roleName = cardSub.getAttributes().getNamedItem("name").getNodeValue();
                    int roleLevel = Integer.parseInt(cardSub.getAttributes().getNamedItem("level").getNodeValue());

                    // TODO: get area info
                    NodeList partChildren = cardSub.getChildNodes();
                    for (int k=0; k<partChildren.getLength(); k++) {
                        Node roleSub = partChildren.item(k);

                        if ("line".equals(roleSub.getNodeName())) {
                            roleLine = roleSub.getTextContent();
                        }
                    }

                    roles[roleCount] = new Role(roleName, roleLine, roleLevel, true);
                    roleCount++;
                }
            }

            cards[i] = new SceneCard(cardName, cardDescription, cardNumber, cardBudget, roles);
        }

        return cards;
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


}
