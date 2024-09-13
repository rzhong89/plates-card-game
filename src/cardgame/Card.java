package cardgame;

public class Card {
   private Suit suit;
   private Rank rank;
   private boolean faceUp;

   public enum Suit {
      HEARTS, SPADES, DIAMONDS, CLUBS;
   }

   public enum Rank {
      ACE(50), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10), JACK(11), QUEEN(12), KING(13);
      
      private final int value;

       Rank(int value) {
           this.value = value;
       }
       
       public int getValue() {
         return value;
       }
   }

   // Constructor to initialize the card with its suit and rank
   public Card(Suit suit, Rank rank) {
      this.suit = suit;
      this.rank = rank;
      this.faceUp = false; // Start face down
   }
   
   public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    public int getValue() {
        return rank.getValue();
    }

    // Flip card face up to reveal it
    public void flipFaceUp() {
      faceUp = true;
    }

   public String toString() {
      return faceUp ? rank + " of " + suit : "?"; // Show "?" if face down
   }
}
