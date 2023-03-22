package no.ntnu.idatg2001.torgrilt.gui.globalelements;

import java.awt.Dimension;
import java.awt.Toolkit;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;

/**
 * It's a class that holds all the global variables that are used throughout the program.
 */
@UtilityClass
public class GlobalElements {
  @Getter
  private final double cardWidth = 300;
  @Getter
  private final double cardHeight = 425;
  @Getter
  private final double cardDepth = 4;
  @Getter
  private final double buttonWidth = 200;
  @Getter
  private final String defaultStyle = "-fx-font-size: 20px;";
  private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
  @Getter
  private final double sceneWidth = screenSize.getWidth();
  @Getter
  private final double sceneHeight = screenSize.getHeight();
  @Getter @Setter
  private double previousBet = 0.0;
  @Getter @Setter
  private boolean restarted = false;
  @Getter @Setter
  private int gameTurn = 0;
  @Getter
  private final double defaultStartingPot = 10000.0;
}
