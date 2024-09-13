package cardgame;

import java.util.ArrayList;
import java.util.Collections;
import cardgame.Card.Rank;
import cardgame.Card.Suit;

public class Deck {
   private ArrayList<Card> cards;
   
   // Constructor to initialize deck for the given number of players
   public Deck(int numPlayers) {
      cards = new ArrayList<>();
      
      // Calculate the number of decks based on number of players
      int numDecks = (int) Math.ceil(numPlayers * 18 / 52.0);
      
      // Create cards for the calculated number of decks
      for (int i = 0; i < numDecks; i++) {
            for (Suit s: Suit.values()) {
                for (Rank r: Rank.values()) {
                    cards.add(new Card(s, r));
                }
            }
        }
      
      // Shuffle the deck
      Collections.shuffle(cards);
   }
   
   // Draw a card from the deck
   public Card draw() {
      return cards.remove(0);
   }
}
