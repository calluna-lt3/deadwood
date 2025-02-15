import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    ArrayList<SceneCard> cards;

    public Deck() {
        XMLParser xmlp = new XMLParser();
        cards = xmlp.getSceneCards();
        Collections.shuffle(cards);
    }

    public SceneCard draw() {
        return cards.remove(0);
    }
}
