package no.ntnu.idatg2001.torgrilt.poker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import no.ntnu.idatg2001.torgrilt.gui.scenes.GameFloor;

@UtilityClass
public class Poker {
  @Getter
  @Setter
  private int opponentPot = 1000;
  @Getter
  @Setter
  private int playerPot = 1000;

  public String getPlayerPotString() {
    return String.valueOf(playerPot);
  }

  public String getOpponentPotString() {
    return String.valueOf(opponentPot);
  }

  public static double getHandStrength(Card[] hand) {
    double strength = 0;
    switch (getHand(hand)) {
      case "High Card" ->
          strength = Arrays.stream(hand).mapToInt(card -> getRank(card.getRank())).max().orElse(0);
      case "One Pair" -> strength = 20 + getHighest(hand)
          + Arrays.stream(hand).mapToInt(card -> getRank(card.getRank())).max().orElse(0) / 10.0;
      case "Two Pairs" -> strength = 60 + getHighest(hand) + getNextHighest(hand)
          + Arrays.stream(hand).mapToInt(card -> getRank(card.getRank())).max().orElse(0) / 10.0;
      case "Three of a Kind" -> strength = 100 + getHighest(hand)
          + Arrays.stream(hand).mapToInt(card -> getRank(card.getRank())).max().orElse(0) / 10.0;
      case "Straight" -> strength = 140 + getHighest(hand)
          + Arrays.stream(hand).mapToInt(card -> getRank(card.getRank())).max().orElse(0) / 10.0;
      case "Flush" -> strength = 180 + getHighest(hand)
          + Arrays.stream(hand).mapToInt(card -> getRank(card.getRank())).max().orElse(0) / 10.0;
      case "Full House" -> strength = 220 + getHighest(hand)
          + Arrays.stream(hand).mapToInt(card -> getRank(card.getRank())).max().orElse(0) / 10.0;
      case "Four of a Kind" -> strength = 260 + getHighest(hand) + getNextHighest(hand)
          + Arrays.stream(hand).mapToInt(card -> getRank(card.getRank())).max().orElse(0) / 10.0;
      case "Straight Flush" -> strength = 300 + getHighest(hand)
          + Arrays.stream(hand).mapToInt(card -> getRank(card.getRank())).max().orElse(0) / 10.0;
      case "Royal Straight Flush" -> strength += 1000;
      default -> throw new IllegalArgumentException("Invalid hand: " + getHand(hand));
    }
    return strength;
  }

  public static String getHand(Card[] hand) {
    int[] rank = new int[13];
    int[] suit = new int[4];
    Arrays.stream(hand).forEach(card -> {
      rank[getRank(card.getRank())]++;
      suit[getSuit(card.getSuit())]++;
    });
    if (GameFloor.getBoard() != null) {
      GameFloor.getBoard().forEach(card -> {
        rank[getRank(card.getRank())]++;
        suit[getSuit(card.getSuit())]++;
      });
    }

    return getHandString(rank, suit, hand);
  }

  private static int getNextHighest(Card[] hand) {
    int maxIndex = getHighest(hand) / 2;
    int nextMaxIndex = 0;
    int[] rank = new int[13];
    Arrays.stream(hand).forEach(card -> rank[getRank(card.getRank())]++);
    if (GameFloor.getBoard() != null) {
      GameFloor.getBoard().forEach(card -> rank[getRank(card.getRank())]++);
    }
    for (int i = 1; i < rank.length; i++) {
      if (rank[i] != maxIndex && rank[i] > rank[nextMaxIndex]
          || (rank[i] == rank[nextMaxIndex] && i > nextMaxIndex && i < maxIndex)) {
        nextMaxIndex = i;
      }
    }

    return nextMaxIndex;
  }

  private static int getHighest(Card[] hand) {
    int[] rank = new int[13];
    Arrays.stream(hand).forEach(card -> rank[getRank(card.getRank())]++);
    if (GameFloor.getBoard() != null) {
      GameFloor.getBoard().forEach(card -> rank[getRank(card.getRank())]++);
    }
    int maxIndex = 0;
    for (int i = 1; i < rank.length; i++) {
      if (rank[i] > rank[maxIndex] || (rank[i] == rank[maxIndex] && i > maxIndex)) {
        maxIndex = i;
      }
    }

    return maxIndex * 2;
  }

  private static String getHandString(int[] rank, int[] suit, Card[] hand) {
    if (isRoyal(hand)) {
      return "Royal Straight Flush";
    } else {
      if (isStraightFlush(suit, hand)) {
        return "Straight Flush";
      } else if (isFlush(suit)) {
        return "Flush";
      } else if (isStraight(rank)) {
        return "Straight";
      } else {
        String x = getNumberOfPairs(rank);
        if (x != null) {
          return x;
        }
        return "High Card";
      }
    }
  }

  private static String getNumberOfPairs(int[] rank) {
    int pairsCount = 0;
    String fullHouse = null;
    for (int j : rank) {
      if (j == 4) {
        return "Four of a Kind";
      } else if (j == 3) {
        return isFullHouseTres(rank);
      } else if (j == 2) {
        fullHouse = isFullHousePair(rank);
        if (++pairsCount == 2) {
          return "Two Pairs";
        }
      }
    }
    return pairsCount == 1 ? "One Pair" : fullHouse;
  }

  private static boolean isRoyal(Card[] hand) {
    ArrayList<Card> boardHand = new ArrayList<>();

    boardHand.addAll(GameFloor.getBoard());
    boardHand.addAll(Arrays.asList(hand));

    // Sort the cards on the board and on hand by suit
    HashMap<String, ArrayList<Card>> suitMap = getSuitMap(boardHand);

    // check if any suit has 5 or more cards in consecutive order from Ten to Ace
    boolean isRoyalFlush = false;
    for (Map.Entry<String, ArrayList<Card>> entry : suitMap.entrySet()) {
      String countedSuit = entry.getKey();
      ArrayList<Card> suitCards = entry.getValue();
      if (suitCards.size() >= 5 && isRoyalAndFlush(countedSuit, suitCards)) {
        isRoyalFlush = true;
      }
    }
    return isRoyalFlush;
  }

  private static boolean isRoyalAndFlush(String countedSuit, ArrayList<Card> suitCards) {
    int suitCount = 0;
    for (Card suitCard : suitCards) {
      String rank = suitCard.getRank();
      String suit = suitCard.getSuit();
      if (rank.equals("Ten") && suit.equals(countedSuit)
          || rank.equals("Jack") && suit.equals(countedSuit)
          || rank.equals("Queen") && suit.equals(countedSuit)
          || rank.equals("King") && suit.equals(countedSuit)
          || rank.equals("Ace") && suit.equals(countedSuit)) {
        suitCount++;
      } else {
        suitCount = 0;
      }
    }
    return suitCount == 5;
  }

  private static HashMap<String, ArrayList<Card>> getSuitMap(
      ArrayList<Card> boardHand) {
    HashMap<String, ArrayList<Card>> suitMap = new HashMap<>();
    for (Card card : boardHand) {
      String suit = card.getSuit();
      if (suitMap.containsKey(suit)) {
        suitMap.get(suit).add(card);
      } else {
        ArrayList<Card> suitCards = new ArrayList<>();
        suitCards.add(card);
        suitMap.put(suit, suitCards);
      }
    }
    return suitMap;
  }

  private static boolean isStraightFlush(int[] suit, Card[] hand) {
    ArrayList<Card> boardHand = new ArrayList<>();
    String flushCard;
    int maxIndex = 0;
    for (int i = 1; i < suit.length; i++) {
      if (suit[i] >= 5) {
        maxIndex = i;
      }
    }
    switch (maxIndex) {
      case 0 -> flushCard = "Hearts";
      case 1 -> flushCard = "Diamonds";
      case 2 -> flushCard = "Clubs";
      case 3 -> flushCard = "Spades";
      default -> throw new IndexOutOfBoundsException("Illegal Index: " + maxIndex);
    }
    int[] rank = new int[13];
    boardHand.addAll(GameFloor.getBoard());
    boardHand.addAll(Arrays.asList(hand));
    for (Card card : boardHand) {
      if (card.getSuit().equals(flushCard)) {
        rank[getRank(card.getRank())]++;
      }
    }
    return isStraight(rank);
  }

  private static String isFullHouseTres(int[] arr) {
    int house = 0;
    for (int j : arr) {
      if (j >= 2) {
        house++;
      }
    }
    if (house >= 2) {
      return "Full House";
    } else {
      return "Three of a Kind";
    }
  }

  private static String isFullHousePair(int[] arr) {
    int house = 0;
    for (int j : arr) {
      if (j >= 3) {
        house++;
      }
    }
    if (house >= 1) {
      return "Full House";
    } else {
      return "Pair";
    }
  }

  private static boolean isFlush(int[] suit) {
    for (int j : suit) {
      if (j >= 5) {
        return true;
      }
    }
    return false;
  }

  private static boolean isStraight(int[] rank) {
    int currentNum = 1;
    int count = 0;
    for (int j : rank) {
      if (j >= currentNum) {
        count++;
        if (count >= 5) {
          return true;
        }
      } else {
        count = 0;
      }
    }
    return false;
  }

  private static int getSuit(String suit) {
    return switch (suit) {
      case "Hearts" -> 0;
      case "Diamonds" -> 1;
      case "Clubs" -> 2;
      case "Spades" -> 3;
      default -> throw new IllegalArgumentException("Invalid suit: " + suit);
    };
  }

  private static int getRank(String rank) {
    return switch (rank) {
      case "Two" -> 0;
      case "Three" -> 1;
      case "Four" -> 2;
      case "Five" -> 3;
      case "Six" -> 4;
      case "Seven" -> 5;
      case "Eight" -> 6;
      case "Nine" -> 7;
      case "Ten" -> 8;
      case "Jack" -> 9;
      case "Queen" -> 10;
      case "King" -> 11;
      case "Ace" -> 12;
      default -> throw new IllegalArgumentException("Invalid rank: " + rank);
    };
  }
}
