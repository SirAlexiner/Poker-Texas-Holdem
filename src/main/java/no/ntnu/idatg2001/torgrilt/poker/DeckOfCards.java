package no.ntnu.idatg2001.torgrilt.poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import no.ntnu.idatg2001.torgrilt.poker.enums.Ranks;
import no.ntnu.idatg2001.torgrilt.poker.enums.Suits;

public class DeckOfCards {
  @Getter
  private ArrayList<Card> deckList = new ArrayList<>(52);

  public DeckOfCards() {
    for (Suits cardSuit : Suits.values()) {
      for (Ranks cardRank : Ranks.values()) {
        Card card = new Card(cardRank, cardSuit);
        deckList.add(card);
      }
    }
  }

  public void shuffle() {
    Collections.shuffle(deckList);
  }

  public List<Card> draw(int amount) {
    ArrayList<Card> drawn = new ArrayList<>();
    for (int i = 0; i < amount; i++) {
      drawn.add(deckList.remove(0));
    }
    return drawn;
  }
}
