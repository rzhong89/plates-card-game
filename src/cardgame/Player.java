package cardgame;

import java.util.ArrayList;

public class Player {
   private String name;
   private ArrayList<Card> primary;
   private ArrayList<Card> revealedSecondary;
   private ArrayList<Card> unrevealedSecondary;

   // Constructor to initialize player with hands
   public Player(String name) {
      this.name = name;
      primary = new ArrayList<>();
      revealedSecondary = new ArrayList<>();
      unrevealedSecondary = new ArrayList<>();
   }

   // Getter for player name
   public String getName() {
      return name;
   }

   // Add a card to the primary hand
   public void addPrimaryCard(Card card) {
      primary.add(card);
      card.flipFaceUp();
   }

   // Add a card to the revealed secondary hand
   public void addRevealedSecondaryCard(Card card) {
      revealedSecondary.add(card);
      card.flipFaceUp(); // Automatically reveal the card when adding
   }

   // Add a card to the unrevealed secondary hand
   public void addUnrevealedSecondaryCard(Card card) {
      unrevealedSecondary.add(card);
   }

   // Reveal one card from unrevealed to revealed secondary hand
   public void revealSecondaryCard() {
      if (!unrevealedSecondary.isEmpty()) {
         Card card = unrevealedSecondary.remove(0); // Remove first unrevealed card
         revealedSecondary.add(card); // Add it to revealed hand
         card.flipFaceUp(); // Flip the card face up to reveal it
      }
   }

   // Get the player's primary hand
   public ArrayList<Card> getPrimaryHand() {
      return primary;
   }

   // Get the player's revealed secondary hand
   public ArrayList<Card> getRevealedSecondary() {
      return revealedSecondary;
   }

   // Get the player's unrevealed secondary hand
   public ArrayList<Card> getUnrevealedSecondary() {
      return unrevealedSecondary;
   }

   // Calculate the total value of revealed secondary hand
   public int getRevealedSecondaryTotal() {
      int sum = 0;
      for (Card card : revealedSecondary) {
         sum += card.getValue();
      }
      return sum;
   }

   public Card playFromUnrevealedSecondary() {
      // Draw and remove the top card from the unrevealed secondary hand (the first
      // card in the list)
      Card card = unrevealedSecondary.remove(0);
      card.flipFaceUp();
      return card;
   }

   // Check if the player has cards left to play (in both primary and secondary
   // hands)
   public boolean hasCardsLeft() {
      return !primary.isEmpty() || !revealedSecondary.isEmpty() || !unrevealedSecondary.isEmpty();
   }

   // toString method to represent the player's hands
   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("Primary Hand: ").append(primary).append("\n");
      sb.append("Revealed Secondary Hand: ").append(revealedSecondary).append("\n");
      sb.append("Unrevealed Secondary Hand: ").append("?".repeat(unrevealedSecondary.size())).append("\n");
      return sb.toString();
   }
}
