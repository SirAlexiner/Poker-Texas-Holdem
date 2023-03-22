package no.ntnu.idatg2001.torgrilt.poker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.Getter;
import no.ntnu.idatg2001.torgrilt.poker.enums.Ranks;
import no.ntnu.idatg2001.torgrilt.poker.enums.Suits;

/**
 * It creates a deck of cards, shuffles it, and allows you to draw cards from it.
 */
public class DeckOfCards {
  @Getter
  private final ArrayList<Card> deckList;

  /**
   * This constructor goes through each Rank, and Suit Enum, then creates a Card object.
   * The new Card is then added to the deck's list of cards before it's shuffled.
   */
  public DeckOfCards() {
    // Creating a deck of cards.
    deckList = (ArrayList<Card>) Arrays.stream(Suits.values())
        .flatMap(suit -> Arrays.stream(Ranks.values()).map(rank -> new Card(rank, suit)))
        .collect(Collectors.toList());
    shuffle();
  }

  private void shuffle() {
    Collections.shuffle(deckList);
  }

  /**
   * This function removes the first card from the deck and adds it to the drawn list,
   * then repeats this process until the
   * drawn list has the desired number of cards.
   *
   * @param amount The amount of cards to draw.
   * @return A list of cards.
   */
  public List<Card> draw(int amount) {
    return IntStream.range(0, amount)
        .mapToObj(i -> deckList.remove(0))
        .collect(Collectors.toList());
  }
}
