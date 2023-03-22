package no.ntnu.idatg2001.torgrilt.poker.enums;

import lombok.Getter;

/** An enum that contains all the possible hands in poker, and their score. */
public enum Hands {
  HIGH_CARD(0.0),
  ONE_PAIR(20.0),
  TWO_PAIR(60.0),
  THREE_OF_A_KIND(100.0),
  STRAIGHT(140.0),
  FLUSH(180.0),
  FULL_HOUSE(220.0),
  FOUR_OF_A_KIND(260.0),
  STRAIGHT_FLUSH(300.0),
  ROYAL_STRAIGHT_FLUSH(1000.0);

  @Getter
  private final double handScore;

  Hands(double handScore) {
    this.handScore = handScore;
  }
}
