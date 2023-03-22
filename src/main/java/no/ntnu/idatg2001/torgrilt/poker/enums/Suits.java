package no.ntnu.idatg2001.torgrilt.poker.enums;

import java.util.stream.Stream;
import lombok.Getter;

/** An enum that contains all the possible suits in poker,
 *  and the suit as an int starting at 0 for Clubs,
 *  incrementing alphabetically.
 */
public enum Suits {
  CLUBS(0),
  DIAMONDS(1),
  HEARTS(2),
  SPADES(3);

  @Getter
  private final int suitInt;

  Suits(int suitInt) {
    this.suitInt = suitInt;
  }

  public static Stream<Suits> stream() {
    return Stream.of(Suits.values());
  }
}
