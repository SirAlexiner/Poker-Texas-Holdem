package no.ntnu.idatg2001.torgrilt.poker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.IntStream;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import no.ntnu.idatg2001.torgrilt.gui.globalelements.GlobalElements;
import no.ntnu.idatg2001.torgrilt.gui.scenes.GameFloor;
import no.ntnu.idatg2001.torgrilt.poker.enums.Hands;
import no.ntnu.idatg2001.torgrilt.poker.enums.Ranks;
import no.ntnu.idatg2001.torgrilt.poker.enums.Suits;

@UtilityClass
public class Poker {
  @Getter
  @Setter
  private double opponentPot = GlobalElements.getDefaultStartingPot();
  @Getter
  @Setter
  private double playerPot = GlobalElements.getDefaultStartingPot();

  public static double getHandStrength(Card[] hand) {
    Hands handType = getHand(hand);
    double strength = handType.getHandScore();

    switch (handType) {
      case HIGH_CARD, ONE_PAIR, TWO_PAIR,
          THREE_OF_A_KIND, STRAIGHT,
          FLUSH, FULL_HOUSE,
          FOUR_OF_A_KIND, STRAIGHT_FLUSH -> {
        strength += getHighest(hand)
            + Arrays.stream(hand)
            .mapToInt(card -> card.getRank().getRankInt())
            .max()
            .orElse(0) / 10.0;
        if (handType.equals(Hands.TWO_PAIR) || handType.equals(Hands.FOUR_OF_A_KIND)) {
          strength += getNextHighest(hand);
        }
      }
      case ROYAL_STRAIGHT_FLUSH -> strength = handType.getHandScore();
      default -> throw new IllegalArgumentException("Invalid hand: " + handType);
    }

    return strength;
  }

  public static Hands getHand(Card[] hand) {
    int[] rank = new int[13];
    int[] suit = new int[4];
    Arrays.stream(hand).forEach(card -> {
      rank[card.getRank().getRankInt()]++;
      suit[card.getSuit().getSuitInt()]++;
    });
    if (GameFloor.getBoard() != null) {
      GameFloor.getBoard().forEach(card -> {
        rank[card.getRank().getRankInt()]++;
        suit[card.getSuit().getSuitInt()]++;
      });
    }

    return getHandEnum(rank, suit, hand);
  }

  private static int getNextHighest(Card[] hand) {
    int maxIndex = getHighest(hand) / 2;
    int nextMaxIndex = 0;
    int[] rank = new int[13];
    Arrays.stream(hand).forEach(card -> rank[card.getRank().getRankInt()]++);
    if (GameFloor.getBoard() != null) {
      GameFloor.getBoard().forEach(card -> rank[card.getRank().getRankInt()]++);
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
    Arrays.stream(hand).forEach(card -> rank[card.getRank().getRankInt()]++);
    if (GameFloor.getBoard() != null) {
      GameFloor.getBoard().forEach(card -> rank[card.getRank().getRankInt()]++);
    }
    int maxIndex = 0;
    for (int i = 1; i < rank.length; i++) {
      if (rank[i] > rank[maxIndex] || (rank[i] == rank[maxIndex] && i > maxIndex)) {
        maxIndex = i;
      }
    }

    return maxIndex * 2;
  }

  private static Hands getHandEnum(int[] rank, int[] suit, Card[] hand) {
    if (isRoyal(hand)) {
      return Hands.ROYAL_STRAIGHT_FLUSH;
    } else {
      if (isStraightFlush(suit, hand)) {
        return Hands.STRAIGHT_FLUSH;
      } else if (isFlush(suit)) {
        return Hands.FLUSH;
      } else if (isStraight(rank)) {
        return Hands.STRAIGHT;
      } else {
        Hands x = getNumberOfPairs(rank);
        if (x != null) {
          return x;
        }
        return Hands.HIGH_CARD;
      }
    }
  }

  private static Hands getNumberOfPairs(int[] rank) {
    int pairsCount = (int) Arrays.stream(rank).filter(i -> i == 2).count();
    if (Arrays.stream(rank).anyMatch(i -> i == 4)) {
      return Hands.FOUR_OF_A_KIND;
    } else if (Arrays.stream(rank).anyMatch(i -> i == 3)) {
      return isFullHouse(rank);
    } else if (pairsCount >= 2) {
      return Hands.TWO_PAIR;
    } else if (pairsCount == 1) {
      return Hands.ONE_PAIR;
    } else {
      return Hands.HIGH_CARD;
    }
  }

  private static boolean isRoyal(Card[] hand) {
    ArrayList<Card> boardHand = new ArrayList<>();

    boardHand.addAll(GameFloor.getBoard());
    boardHand.addAll(Arrays.asList(hand));

    // Sort the cards on the board and on hand by suit
    EnumMap<Suits, ArrayList<Card>> suitMap = getSuitMap(boardHand);

    // check if any suit has 5 or more cards in consecutive order from Ten to Ace
    boolean isRoyalFlush = false;
    for (Map.Entry<Suits, ArrayList<Card>> entry : suitMap.entrySet()) {
      Suits countedSuit = entry.getKey();
      ArrayList<Card> suitCards = entry.getValue();
      if (suitCards.size() >= 5 && isRoyalAndFlush(countedSuit, suitCards)) {
        isRoyalFlush = true;
      }
    }
    return isRoyalFlush;
  }

  private static boolean isRoyalAndFlush(Suits countedSuit, ArrayList<Card> suitCards) {
    int suitCount = 0;
    for (Card suitCard : suitCards) {
      Ranks rank = suitCard.getRank();
      Suits suit = suitCard.getSuit();
      if (rank.equals(Ranks.TEN) && suit.equals(countedSuit)
          || rank.equals(Ranks.JACK) && suit.equals(countedSuit)
          || rank.equals(Ranks.QUEEN) && suit.equals(countedSuit)
          || rank.equals(Ranks.KING) && suit.equals(countedSuit)
          || rank.equals(Ranks.ACE) && suit.equals(countedSuit)) {
        suitCount++;
      } else {
        suitCount = 0;
      }
    }
    return suitCount == 5;
  }

  private static EnumMap<Suits, ArrayList<Card>> getSuitMap(
      ArrayList<Card> boardHand) {
    EnumMap<Suits, ArrayList<Card>> suitMap = new EnumMap<>(Suits.class);
    for (Card card : boardHand) {
      Suits suit = card.getSuit();
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
    Suits flushCard;
    int maxIndex = IntStream.range(1, suit.length)
        .filter(i -> suit[i] >= 5)
        .max()
        .orElse(0);
    flushCard =
        Suits.stream().filter(d -> d.getSuitInt() == maxIndex).findFirst().orElse(null);
    int[] rank = new int[13];
    boardHand.addAll(GameFloor.getBoard());
    boardHand.addAll(Arrays.asList(hand));
    boardHand.stream()
        .filter(card -> card.getSuit().equals(flushCard))
        .forEach(card -> rank[card.getRank().getRankInt()]++);
    return isStraight(rank);
  }

  private static Hands isFullHouse(int[] arr) {
    int house = 0;
    for (int j : arr) {
      if (j >= 2) {
        house++;
      }
    }
    if (house >= 2) {
      return Hands.FULL_HOUSE;
    } else {
      return Hands.THREE_OF_A_KIND;
    }
  }

  private static boolean isFlush(int[] suit) {
    return Arrays.stream(suit).anyMatch(i -> i >= 5);
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
}
