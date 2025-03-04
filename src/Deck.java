import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Deck {
    ArrayList<SceneCard> cards;

    public Deck() {
        XMLParser xmlp = new XMLParser(XMLParser.type.CARDS);
        cards = xmlp.getSceneCards();
        Collections.shuffle(cards, new Random());
    }

    public SceneCard draw() {
        return cards.remove(0);
    }
}
