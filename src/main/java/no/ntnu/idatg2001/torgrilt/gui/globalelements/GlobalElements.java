package no.ntnu.idatg2001.torgrilt.gui.globalelements;

import javafx.stage.Screen;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;

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
  private final double sceneWidth = Screen.getPrimary().getBounds().getWidth();
  @Getter
  private final double sceneHeight = Screen.getPrimary().getBounds().getHeight();
  @Getter @Setter
  private int previousBet = 0;
  @Getter @Setter
  private boolean restarted = false;
  @Getter @Setter
  private int gameTurn = 0;
  @Getter
  private final int defaultStartingPot = 10000;
}
