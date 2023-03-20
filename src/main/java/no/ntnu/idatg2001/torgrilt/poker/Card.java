package no.ntnu.idatg2001.torgrilt.poker;

import lombok.Getter;
import no.ntnu.idatg2001.torgrilt.poker.enums.Ranks;
import no.ntnu.idatg2001.torgrilt.poker.enums.Suits;

public class Card {
  @Getter
  Suits suit;
  @Getter
  Ranks rank;

  public Card(Ranks rank, Suits suit) {
    this.rank = rank;
    this.suit = suit;
  }

  public String getCardImage() {
    return "playingCards/" + this.getSuit() + "/" + this.getRank() + ".png";
  }
}
