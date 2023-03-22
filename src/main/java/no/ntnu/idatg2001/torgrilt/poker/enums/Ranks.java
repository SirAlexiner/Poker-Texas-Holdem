package no.ntnu.idatg2001.torgrilt.poker.enums;

import lombok.Getter;

/** An enum that contains all the possible ranks in poker,
 *  and the rank as an int starting at 0 for Two.
 */
public enum Ranks {
  TWO(0),
  THREE(1),
  FOUR(2),
  FIVE(3),
  SIX(4),
  SEVEN(5),
  EIGHT(6),
  NINE(7),
  TEN(8),
  JACK(9),
  QUEEN(10),
  KING(11),
  ACE(12);

  @Getter
  private final int rankInt;

  Ranks(int rankInt) {
    this.rankInt = rankInt;
  }
}
