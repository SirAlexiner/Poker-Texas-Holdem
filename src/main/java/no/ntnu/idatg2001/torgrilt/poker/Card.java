package no.ntnu.idatg2001.torgrilt.poker;

import lombok.Getter;
import no.ntnu.idatg2001.torgrilt.poker.enums.Ranks;
import no.ntnu.idatg2001.torgrilt.poker.enums.Suits;

/**
 * A Card object has a suit and a rank, and can return the image file name for the card.
 */
public record Card(@Getter Ranks rank, @Getter Suits suit) {

  public String getCardImage() {
    return "playingCards/" + this.suit() + "/" + this.rank() + ".png";
  }
}
