package no.ntnu.idatg2001.torgrilt.poker;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.experimental.UtilityClass;
import no.ntnu.idatg2001.torgrilt.gui.globalelements.GlobalElements;
import no.ntnu.idatg2001.torgrilt.gui.scenes.GameFloor;
import no.ntnu.idatg2001.torgrilt.gui.utilities.Animate;

@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
@UtilityClass
public class PokerAI {

  private final Random rand = new SecureRandom();
  private Stage stage;
  private boolean raiseMade;
  private double bet;

  public static double getBet(Stage stage, Card[] hand, double bet,
                           double stack, boolean raiseMade, boolean first) {
    PokerAI.stage = stage;
    PokerAI.raiseMade = raiseMade;
    PokerAI.bet = bet;
    if (stack != 0.0 && Poker.getPlayerPot() != 0) {
      int minBet = 20;
      if (first) {
        return gameStartBet(stack, minBet);
      } else {
        double handStrength = Poker.getHandStrength(hand);
        double aiBet = Math.min(getBetAction(handStrength), stack);
        if (aiBet == stack) {
          displayAction("All In!");
          if (GlobalElements.getGameTurn() < 3) {
            GameFloor.aiAllIn();
            Animate.showBoard();
          } else {
            GameFloor.aiAllIn();
            GameFloor.allIn();
          }
        }
        return aiBet;
      }
    } else {
      Platform.runLater(GameFloor::newGame);
      return 0;
    }
  }

  private static double gameStartBet(double stack, int minBet) {
    // Raise
    double aiBet = Math.min(minBet, stack);
    if (aiBet == stack) {
      displayAction("All In!");
      GameFloor.aiAllIn();
      Animate.showBoard();
      GlobalElements.setPreviousBet(aiBet);
      return aiBet;
    } else {
      GlobalElements.setPreviousBet(aiBet);
      return raise(aiBet);
    }
  }

  private static double getBetAction(double handStrength) {
    double[][] thresholds = {
        {20, 3, 8}, // High Card
        {60, 27.20, 37.20}, // One Pair
        {100, 72.20, 84.20}, // Two Pairs
        {140, 107.20, 117.20}, // Three of a Kind
        {180, 153.20, 159.20}, // Straight
        {220, 193.20, 199.20}, // Flush
        {260, 232.20, 244.20}, // Full House
        {300, 267.20, 277.20}, // Four of a Kind
        {1000, 313.20, 319.20} // Straight Flush
    };

    return Arrays.stream(thresholds)
        .filter(row -> handStrength < row[0])
        .findFirst()
        .map(row -> calculateAction(handStrength, row[1], row[2]))
        .orElse(10000.0);
  }

  private synchronized double calculateAction(double handStrength, double x, double y) {
    if (handStrength <= x) {
      int z = rand.nextInt(1, 5);
      return callOrFold(z);
    } else if (handStrength <= y) {
      int z = rand.nextInt(1, 5);
      return callOrRaise(z);
    } else {
      int z;
      if (Math.max(y, handStrength) != handStrength) {
        z = rand.nextInt(1, 5);
      } else {
        z = rand.nextInt(1, 3);
      }
      return callOrRaise(z);
    }
  }

  private static double callOrFold(int z) {
    if (z == 1) {
      // Fold
      return fold();
    } else {
      // Call : Check
      return callCheck();
    }
  }

  private static double callOrRaise(int z) {
    if (z == 1) {
      // Raise
      return raise(bet * 2);
    } else {
      // Call : Check
      return callCheck();
    }
  }

  private static int fold() {
    displayAction("Fold");
    GameFloor.aiFolded();
    return 0;
  }

  private static double raise(double bet) {
    displayAction("Raise");
    GameFloor.aiRaised();
    return bet;
  }

  private static double callCheck() {
    displayAction(raiseMade ? "Call" : "Check");
    GameFloor.aiCalledChecked(!raiseMade);
    return raiseMade ? bet : 0;
  }

  private static void displayAction(String text) {
    Tooltip tooltip = new Tooltip(text);
    tooltip.show(stage);

    PauseTransition aiThink = new PauseTransition(new Duration(2500));
    aiThink.setOnFinished(event -> tooltip.hide());
    aiThink.play();
  }
}