package no.ntnu.idatg2001.torgrilt.poker;

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

  private final Random rand = new Random();
  private Stage stage;
  private boolean raiseMade;
  private int bet;

  public static int getBet(Stage stage, Card[] hand, int bet,
                           int stack, boolean raiseMade, boolean first) {
    PokerAI.stage = stage;
    PokerAI.raiseMade = raiseMade;
    PokerAI.bet = bet;
    if (stack != 0) {
      int minBet = 20;
      if (first) {
        return gameStartBet(stack, minBet);
      } else {
        double handStrength = Poker.getHandStrength(hand);
        int aiBet = Math.min(getBetAction(handStrength), stack);
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

  private static int gameStartBet(int stack, int minBet) {
    // Raise
    int aiBet = Math.min(minBet, stack);
    if (aiBet == stack) {
      displayAction("All In!");
      GameFloor.aiAllIn();
      Animate.showBoard();
      return aiBet;
    } else {
      return raise(aiBet);
    }
  }

  private static int getBetAction(double handStrength) {
    if (handStrength < 20) {
      return calculateAction(handStrength, 3, 8);
    } else if (handStrength < 60) {
      return calculateAction(handStrength, 27.20, 37.20);
    } else if (handStrength < 100) {
      return calculateAction(handStrength, 72.20, 84.20);
    } else if (handStrength < 140) {
      return calculateAction(handStrength, 107.20, 117.20);
    } else if (handStrength < 180) {
      return calculateAction(handStrength, 153.20, 159.20);
    } else if (handStrength < 220) {
      return calculateAction(handStrength, 193.20, 199.20);
    } else if (handStrength < 260) {
      return calculateAction(handStrength, 232.20, 244.20);
    } else if (handStrength < 300) {
      return calculateAction(handStrength, 267.20, 277.20);
    } else if (handStrength < 1000) {
      return calculateAction(handStrength, 313.20, 319.20);
    } else {
      return 10000;
    }
  }

  private static int calculateAction(double handStrength, double x, double y) {
    if (handStrength <= y) {
      return foldOrCall(handStrength, x);
    } else {
      if (Math.max(y, handStrength) != handStrength) {
        int z = rand.nextInt(1, 5);
        if (z == 1) {
          //Raise
          return raise(bet * 2);
        } else {
          // Call : Check
          return callCheck();
        }
      } else {
        int z = rand.nextInt(1, 3);
        if (z == 1) {
          //Raise
          return raise(bet * 2);
        } else {
          // Call : Check
          return callCheck();
        }
      }
    }
  }

  private static int foldOrCall(double handStrength, double x) {
    if (handStrength <= x) {
      //Fold
      fold();
      return 0;
    } else {
      int z = rand.nextInt(1, 5);
      if (z == 1) {
        //Raise
        return raise(bet * 2);
      } else {
        // Call : Check
        return callCheck();
      }
    }
  }

  private static void fold() {
    displayAction("Fold");
    GameFloor.aiFolded();
  }

  private static int raise(int bet) {
    displayAction("Raise");
    GameFloor.aiRaised();
    return bet;
  }

  private static int callCheck() {
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