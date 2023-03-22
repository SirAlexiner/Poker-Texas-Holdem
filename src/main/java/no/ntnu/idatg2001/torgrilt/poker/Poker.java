package no.ntnu.idatg2001.torgrilt.poker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import no.ntnu.idatg2001.torgrilt.gui.globalelements.GlobalElements;
import no.ntnu.idatg2001.torgrilt.gui.scenes.GameFloor;
import no.ntnu.idatg2001.torgrilt.poker.enums.Hands;
import no.ntnu.idatg2001.torgrilt.poker.enums.Ranks;
import no.ntnu.idatg2001.torgrilt.poker.enums.Suits;

/**
 * A class that represents a poker game.
 */
@UtilityClass
public class Poker {
  @Getter
  @Setter
  private double opponentPot = GlobalElements.getDefaultStartingPot();
  @Getter
  @Setter
  private double playerPot = GlobalElements.getDefaultStartingPot();

  /**
   * > The hand strength is the hand type score plus the highest card rank,
   * plus the next highest card rank divided by 10.
   *
   * @param hand The hand to be evaluated
   * @return The hand strength is being returned.
   */
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

  /**
   * We create two arrays, one for the rank and one for the suit.
   * We then iterate through the hand and the board (if it exists),
   * and increment the appropriate index in the array.
   *
   * @param hand The hand you want to check
   * @return The hand enum
   */
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
    return IntStream.range(1, rank.length).filter(i ->
            rank[i] != maxIndex
                && rank[i] > rank[nextMaxIndex]
                || (rank[i] == rank[nextMaxIndex]
                && i > nextMaxIndex
                && i < maxIndex))
        .findFirst()
        .orElse(nextMaxIndex);
  }

  private static int getHighest(Card[] hand) {
    int[] rank = new int[13];
    Arrays.stream(hand).forEach(card -> rank[card.getRank().getRankInt()]++);
    if (GameFloor.getBoard() != null) {
      GameFloor.getBoard().forEach(card -> rank[card.getRank().getRankInt()]++);
    }
    int maxIndex = 0;
    return IntStream.range(1, rank.length).filter(i ->
            rank[i] > rank[maxIndex]
                || (rank[i] == rank[maxIndex]
                && i > maxIndex))
        .findFirst()
        .orElse(maxIndex) * 2;
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
    EnumMap<Suits, ArrayList<Card>> suitMap = getSuitMap(boardHand);
    return suitMap.entrySet().stream()
        .filter(entry -> entry.getValue().size() >= 5)
        .anyMatch(entry -> isRoyalAndFlush(entry.getKey(), entry.getValue()));
  }

  private static boolean isRoyalAndFlush(Suits countedSuit, ArrayList<Card> suitCards) {
    return suitCards.stream().filter(card -> card.getSuit().equals(countedSuit))
        .map(Card::getRank)
        .distinct()
        .sorted()
        .toList()
        .equals(Arrays.asList(Ranks.TEN, Ranks.JACK, Ranks.QUEEN, Ranks.KING, Ranks.ACE));
  }

  private static EnumMap<Suits, ArrayList<Card>> getSuitMap(ArrayList<Card> boardHand) {
    return boardHand.stream()
        .collect(Collectors.groupingBy(Card::getSuit, () -> new EnumMap<>(Suits.class),
            Collectors.toCollection(ArrayList::new)));
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
    int house = (int) Arrays.stream(arr).filter(i -> i >= 2).count();
    return house >= 2 ? Hands.FULL_HOUSE : Hands.THREE_OF_A_KIND;
  }

  private static boolean isFlush(int[] suit) {
    return Arrays.stream(suit).anyMatch(i -> i >= 5);
  }

  private static boolean isStraight(int[] rank) {
    AtomicInteger count = new AtomicInteger();
    Arrays.stream(rank).forEach(i -> {
      if (i >= 1) {
        count.getAndIncrement();
      } else if (count.get() < 5) {
        count.set(0);
      }
    });
    return count.get() >= 5;
  }
}
