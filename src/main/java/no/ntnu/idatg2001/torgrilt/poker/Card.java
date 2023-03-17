package no.ntnu.idatg2001.torgrilt.poker;

import lombok.Getter;

public class Card {
  @Getter
  String suit;
  @Getter
  String rank;

  public Card(String rank, String suit) {
    this.rank = rank;
    this.suit = suit;
  }

  public String getCardImage() {
    return "playingCards/" + this.getSuit() + "/" + this.getRank() + ".png";
  }
}
