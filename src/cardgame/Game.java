package cardgame;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class Game {
   private Deck deck;
   private ArrayList<Player> players;
   private ArrayList<Card> plate;
   private Scanner scanner;

   // Constructor to initialize game with specified number of players
   public Game(int numPlayers) {
      deck = new Deck(numPlayers);
      players = new ArrayList<>();
      plate = new ArrayList<>();
      scanner = new Scanner(System.in); // Initialize scanner for input

      for (int i = 0; i < numPlayers; i++) {
         System.out.print("Enter name for player " + (i + 1) + ": ");
         String playerName = scanner.nextLine();
         players.add(new Player(playerName));
      }

      dealCards(); // Deal cards to players
      playGame(); // Start the game
   }

   // Deal cards to each player
   private void dealCards() {
      for (Player p : players) {
         // Deal primary hand and secondary hand
         for (int i = 0; i < 10; i++) {
            p.addPrimaryCard(deck.draw());
            if (i < 4) {
               p.addRevealedSecondaryCard(deck.draw()); // First 4 are revealed secondary cards
            } else if (i < 8) {
               p.addUnrevealedSecondaryCard(deck.draw()); // Next 4 are unrevealed secondary cards
            }
         }
      }
   }

   // Main game loop
   private void playGame() {
      Player currentPlayer = players.stream().max(Comparator.comparingInt(Player::getRevealedSecondaryTotal))
            .orElse(null);

      while (true) {
         System.out.println("\nCurrent Plate: " + plate);
         System.out.println(currentPlayer.getName() + "'s turn.");
         System.out.println("Primary Hand: " + currentPlayer.getPrimaryHand());
         System.out.println("Revealed Secondary Hand: " + currentPlayer.getRevealedSecondary());
         System.out.println("Unrevealed Secondary Hand: " + currentPlayer.getUnrevealedSecondary());

         boolean canPlayFromUnrevealed = currentPlayer.getRevealedSecondary().isEmpty()
               && !currentPlayer.getUnrevealedSecondary().isEmpty();

         System.out.print(
               "Do you want to play cards from (P)rimary hand, (S)econdary hand, (U)nrevealed secondary, or (B)oth? ");
         String handChoice = scanner.nextLine().trim().toUpperCase();

         ArrayList<Card> cardsToPlay = new ArrayList<>();

         if (handChoice.equals("P") || handChoice.equals("B")) {
            // Play from Primary hand
            System.out.print("Enter the index of the card(s) to play from Primary hand (comma-separated): ");
            String input = scanner.nextLine();
            String[] indices = input.split(",");

            for (String index : indices) {
               int idx = Integer.parseInt(index.trim());
               cardsToPlay.add(currentPlayer.getPrimaryHand().get(idx));
            }
         }

         if (handChoice.equals("S") || handChoice.equals("B")) {
            // Play from Revealed Secondary hand
            System.out.print("Enter the index of the card(s) to play from Revealed Secondary hand (comma-separated): ");
            String input = scanner.nextLine();
            String[] indices = input.split(",");

            for (String index : indices) {
               int idx = Integer.parseInt(index.trim());
               cardsToPlay.add(currentPlayer.getRevealedSecondary().get(idx));
            }
         }

         if (handChoice.equals("U") && canPlayFromUnrevealed) {
            // Option to play from Unrevealed Secondary hand
            System.out.println("Playing a card from your unrevealed secondary hand...");
            Card unrevealedCard = currentPlayer.playFromUnrevealedSecondary();
            cardsToPlay.add(unrevealedCard);
            System.out.println("You played: " + unrevealedCard); // Card is revealed after it's played
         }

         // Ensure that all cards being played have the same rank
         boolean allSameRank = cardsToPlay.stream().allMatch(card -> card.getRank() == cardsToPlay.get(0).getRank());

         if (!allSameRank) {
            System.out.println("Invalid move: All cards played must have the same rank.");
            continue;
         }

         if (isValidMove(cardsToPlay)) {
            plate.addAll(cardsToPlay);

            // Remove the played cards from the relevant hands
            currentPlayer.getPrimaryHand().removeAll(cardsToPlay);
            currentPlayer.getRevealedSecondary().removeAll(cardsToPlay);

            System.out.println("Played cards: " + cardsToPlay);

            // Check if the plate should be cleared
            if (shouldClearPlate(cardsToPlay)) {
               System.out.println("Plate cleared!");
               plate.clear();
               continue; // Player gets another turn
            }
         } else {
            System.out.println("Invalid move. You must pick up the plate.");
            currentPlayer.getPrimaryHand().addAll(plate);
            plate.clear();
         }

         // Check for a winner
         if (currentPlayer.getPrimaryHand().isEmpty() && currentPlayer.getRevealedSecondary().isEmpty()
               && currentPlayer.getUnrevealedSecondary().isEmpty()) {
            System.out.println(currentPlayer.getName() + " wins!");
            break;
         }

         // Move to the next player
         int currentIndex = players.indexOf(currentPlayer);
         currentPlayer = players.get((currentIndex + 1) % players.size());
      }
      
      calculateRemainingCardValues();
   }

   // Check if the player's move is valid
   private boolean isValidMove(ArrayList<Card> cardsToPlay) {
      // Check if an Ace is being played
      boolean containsAce = cardsToPlay.stream().anyMatch(card -> card.getRank() == Card.Rank.ACE);

      // If an Ace is being played, it must be the only card
      if (containsAce && cardsToPlay.size() > 1) {
         System.out.println("Invalid move: An Ace must be played by itself.");
         return false;
      } else if (containsAce) {
         return true;
      }

      // If the plate is empty, any valid card can be played
      if (plate.isEmpty()) {
         return true; // Any card can be played on an empty plate
      }

      // Get the value of the top card(s) on the plate
      Card topPlateCard = plate.get(plate.size() - 1);

      // Check if all played cards have the same or lower value than the top card
      return cardsToPlay.stream().allMatch(card -> card.getValue() <= topPlateCard.getValue());
   }

   private boolean shouldClearPlate(ArrayList<Card> cardsToPlay) {
      // Check if any of the cards played is an Ace
      for (Card card : cardsToPlay) {
         if (card.getRank() == Card.Rank.ACE) {
            return true; // Plate should be cleared if an Ace is played
         }
      }

      // Check if the last 4 cards on the plate have the same value
      if (plate.size() >= 4) {
         int lastIndex = plate.size() - 1;
         Card lastCard = plate.get(lastIndex);

         for (int i = lastIndex - 3; i <= lastIndex; i++) {
            if (plate.get(i).getRank() != lastCard.getRank()) {
               return false; // Not all four cards are the same value
            }
         }
         return true; // All four cards have the same value
      }

      return false;
   }

   // Method to calculate the total value of remaining cards for each player
   private void calculateRemainingCardValues() {
      System.out.println("\nGame Over! Calculating remaining card values...");

      for (Player player : players) {
         int totalValue = 0;

         // Sum values from primary hand
         for (Card card : player.getPrimaryHand()) {
            totalValue += card.getValue();
         }

         // Sum values from revealed secondary hand
         for (Card card : player.getRevealedSecondary()) {
            totalValue += card.getValue();
         }

         // Sum values from unrevealed secondary hand
         for (Card card : player.getUnrevealedSecondary()) {
            totalValue += card.getValue();
         }

         System.out.println(player.getName() + "'s total remaining card value: " + totalValue);
      }
   }

   public static void main(String[] args) {
      new Game(3); // Start a game with 3 players
   }
}
