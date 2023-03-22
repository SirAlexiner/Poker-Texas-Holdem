package no.ntnu.idatg2001.torgrilt.gui.utilities;

import java.io.File;
import java.util.stream.IntStream;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.media.AudioClip;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import lombok.experimental.UtilityClass;
import no.ntnu.idatg2001.torgrilt.gui.globalelements.GlobalElements;
import no.ntnu.idatg2001.torgrilt.gui.scenes.GameFloor;

/**
 * It's a class that can be used to animate a sprite.
 */
@UtilityClass
public class Animate {


  private static AudioClip shufflePlayer;
  private static AudioClip dealPlayer;

  /**
   * "Show the board cards one by one, then show the all-in cards."
   *
   * <p>.
   * The first thing we do is create a new `Timeline` object.
   * This is a JavaFX object that allows us to create a sequence
   * of events that will happen over time.
   */
  public static void showBoard() {
    Timeline timeline = new Timeline();
    int turn = GlobalElements.getGameTurn();
    int from;
    switch (turn) {
      case 0 -> from = 4;
      case 1 -> from = 7;
      case 2 -> from = 8;
      default -> throw new IllegalArgumentException("Invalid turn: " + turn);
    }

    IntStream.range(from, 9)
        .forEach(i -> timeline.getKeyFrames().add(
            new KeyFrame(Duration.millis(500.0 * (i - 4)),
                event -> allInFlop(GameFloor.getDeckOfCard(), i))
        ));

    timeline.setOnFinished(event -> {
      PauseTransition delay = new PauseTransition(new Duration(1000));
      delay.setOnFinished(event1 -> GameFloor.allIn());
      delay.play();
    });
    timeline.play();
  }

  private static void allInFlop(Group deck, int i) {
    ParallelTransition parallelTransition = getAllInParallelTransition(deck, i);
    playDealAudio();

    SequentialTransition flop = new SequentialTransition(parallelTransition);
    flop.play();
  }

  private static ParallelTransition getAllInParallelTransition(Group deck, int i) {
    TranslateTransition move = new TranslateTransition(Duration.millis(500));
    move.setNode(deck.getChildren().get(i));
    move.setToX(-890.0 + 145.0 * (i - 4));
    move.setToZ(0);
    move.setToY(25);

    RotateTransition rotate = new RotateTransition(Duration.millis(500));
    rotate.setNode(deck.getChildren().get(i));
    rotate.setAxis(Rotate.X_AXIS);
    rotate.setToAngle(-5);

    RotateTransition flip = new RotateTransition(Duration.millis(500));
    flip.setNode(deck.getChildren().get(i));
    flip.setAxis(Rotate.Y_AXIS);
    flip.setToAngle(180);

    ParallelTransition parallelTransition = new ParallelTransition(move, rotate);
    parallelTransition.setOnFinished(event -> {
      if (i < 6) {
        playDealAudio();
      }
      flip.play();
    });
    return parallelTransition;
  }

  /**
   * "Play the deal audio, then draw the next three cards from the deck and add them to the flop."
   *
   * <p>.
   * The first thing we do is create a new SequentialTransition object called flop.
   * This is the object that will contain all the animations for the flop.
   *
   * @param deck The Group object that contains all the cards
   */
  public static void flop(Group deck) {
    SequentialTransition flop = new SequentialTransition();
    IntStream.range(4, 7)
        .mapToObj(i -> getParallelTransition(deck, i))
        .forEach(draw -> {
          playDealAudio();
          flop.getChildren().addAll(draw);
        });
    flop.play();
  }

  /**
   * The turn function plays the deal audio, and then plays the parallel transition of the deck,
   * which is the animation of the cards being dealt.
   *
   * @param deck The Group object that contains all the cards.
   */
  public static void turn(Group deck) {
    int i = 7;
    playDealAudio();
    ParallelTransition draw = getParallelTransition(deck, i);

    SequentialTransition turn = new SequentialTransition();
    turn.getChildren().addAll(draw);
    turn.play();
  }

  /**
   * "Play the deal audio, then draw the next card from the deck and place it on the board."
   *
   * <p>.
   * The first thing we do is set the number of cards to draw to 8.
   * This is because we are drawing the river card.
   *
   * @param deck The Group that contains all the cards
   */
  public static void river(Group deck) {
    int i = 8;
    playDealAudio();
    ParallelTransition draw = getParallelTransition(deck, i);

    SequentialTransition river = new SequentialTransition();
    river.getChildren().addAll(draw);
    river.play();
  }

  /**
   * "Rotate and move the player's cards and the opponent's cards to the center of the screen."
   *
   * <p>.
   * The first thing we do is create a `RotateTransition` for the player's cards.
   * We set the node to the player's cards,
   * the axis to rotate around, and the angle to rotate to.
   *
   * @param player   The Group that contains the player's cards.
   * @param opponent The Group that contains the opponent's cards.
   */
  public static void showCards(Group player, Group opponent) {
    RotateTransition rotatePlayer = new RotateTransition(new Duration(500));
    rotatePlayer.setNode(player);
    rotatePlayer.setAxis(new Point3D(-0.1, 1, 0));
    rotatePlayer.setToAngle(-180);

    ParallelTransition show = new ParallelTransition();
    show.getChildren().add(rotatePlayer);

    TranslateTransition movePlayer = new TranslateTransition();
    movePlayer.setNode(player);
    movePlayer.setToX(-40);

    show.getChildren().add(movePlayer);

    RotateTransition rotateOpponent = new RotateTransition(new Duration(500));
    rotateOpponent.setNode(opponent);
    rotateOpponent.setAxis(new Point3D(-0.1, 1, 0));
    rotateOpponent.setToAngle(-180);

    show.getChildren().add(rotateOpponent);

    TranslateTransition moveOpponent = new TranslateTransition();
    moveOpponent.setNode(opponent);
    moveOpponent.setToX(25);

    show.getChildren().add(moveOpponent);

    show.play();
  }

  private static ParallelTransition getParallelTransition(Group deck, int i) {
    TranslateTransition move = new TranslateTransition(new Duration(500));
    move.setNode(deck.getChildren().get(i));
    move.setToX(-890.0 + 145.0 * (i - 4));
    move.setToZ(0);
    move.setToY(25);

    ParallelTransition draw = new ParallelTransition();
    draw.getChildren().add(move);

    RotateTransition rotate = new RotateTransition(new Duration(500));
    rotate.setNode(deck.getChildren().get(i));
    rotate.setAxis(Rotate.X_AXIS);
    rotate.setToAngle(-5);
    draw.getChildren().add(rotate);

    RotateTransition flip = new RotateTransition(new Duration(500));
    flip.setNode(deck.getChildren().get(i));
    flip.setAxis(Rotate.Y_AXIS);
    flip.setToAngle(180);

    draw.setOnFinished(event -> {
      if (i < 6) {
        playDealAudio();
      }
      flip.play();
    });
    return draw;
  }


  /**
   * "Shuffle the deck by rotating it, then move each card to the left,
   * then rotate it back and deal the cards."
   *
   * <p>.
   * The first thing we do is create a `RotateTransition` called `skew`.
   * This will rotate the deck 22.5 degrees around the Y axis.
   * We then create another `RotateTransition` called `skewBack`
   * that will rotate the deck back to 0 degrees.
   *
   * @param deck The deck of cards to be shuffled.
   */
  public static void shuffle(Group deck) {
    RotateTransition skew = new RotateTransition(new Duration(250));
    skew.setNode(deck);
    skew.setAxis(Rotate.Y_AXIS);
    skew.setFromAngle(0);
    skew.setToAngle(22.5);
    skew.play();

    RotateTransition skewBack = new RotateTransition(new Duration(250));
    skewBack.setNode(deck);
    skewBack.setAxis(Rotate.Y_AXIS);
    skewBack.setFromAngle(22.5);
    skewBack.setToAngle(0);

    SequentialTransition shuffle = new SequentialTransition(skew);
    shuffle.setOnFinished(event -> {
      skewBack.play();
      if (shufflePlayer != null && (shufflePlayer.isPlaying())) {
        shufflePlayer.stop();
      }
      dealCards(deck);
    });

    deck.getChildren().forEach(node -> {
      TranslateTransition move = new TranslateTransition(new Duration(250));
      move.setNode(node);
      move.setToX(-150);
      shuffle.getChildren().add(move);
    });
    playShuffleAudio();
    shuffle.play();
  }

  /**
   * The function deals the cards by rotating the deck,
   * then sliding the deck to the side, then sliding the cards out of
   * the deck and into the hands of the players.
   *
   * @param deck The Group that contains the deck of cards
   */
  public static void dealCards(Group deck) {
    deck.setManaged(false);

    TranslateTransition move = new TranslateTransition(new Duration(750));
    move.setNode(deck);
    move.setInterpolator(Interpolator.EASE_BOTH);
    move.setToX(375);
    move.setToY(-90);

    RotateTransition place = new RotateTransition(new Duration(250));
    place.setNode(deck);
    place.setAxis(new Point3D(1, -0.075, 0));
    place.setToAngle(-30);

    RotateTransition moveToSide = new RotateTransition(new Duration(750));
    moveToSide.setNode(deck);
    moveToSide.setAxis(Rotate.X_AXIS);
    moveToSide.setToAngle(-35);


    ParallelTransition parallel = new ParallelTransition(moveToSide, move);
    SequentialTransition slide = new SequentialTransition(place);

    IntStream.range(0, 4).forEach(i -> {
      playDealAudio();
      RotateTransition rotateOne = new RotateTransition(new Duration(750));
      rotateOne.setNode(deck.getChildren().get(i));
      rotateOne.setInterpolator(Interpolator.EASE_BOTH);
      rotateOne.setToAngle(720);

      TranslateTransition slideOne = new TranslateTransition(new Duration(500));
      slideOne.setNode(deck.getChildren().get(i));
      slideOne.setInterpolator(Interpolator.EASE_BOTH);
      slideOne.setToY(i == 1 || i == 3 ? -750 : 750);

      ParallelTransition animateOne = new ParallelTransition(rotateOne, slideOne);
      animateOne.setInterpolator(Interpolator.EASE_BOTH);
      slide.getChildren().add(animateOne);
      if (i < 3) {
        animateOne.setOnFinished(event -> playDealAudio());
      }
    });

    slide.play();
    slide.setOnFinished(event -> {
      parallel.play();
      slideInHands();
    });

  }

  private static void slideInHands() {
    TranslateTransition moveInHand = new TranslateTransition(new Duration(1000));
    moveInHand.setFromY(350);
    moveInHand.setToY(0);
    moveInHand.setNode(GameFloor.getPlayerHand());
    moveInHand.setInterpolator(Interpolator.LINEAR);

    TranslateTransition moveInOpponentHand = new TranslateTransition(new Duration(1000));
    moveInOpponentHand.setNode(GameFloor.getOpponentHand());
    moveInOpponentHand.setFromY(-350);
    moveInOpponentHand.setToY(0);
    moveInOpponentHand.setInterpolator(Interpolator.LINEAR);

    ParallelTransition animateIn = new ParallelTransition(moveInHand, moveInOpponentHand);
    animateIn.setInterpolator(Interpolator.LINEAR);
    animateIn.play();
    animateIn.setOnFinished(event -> {
      stopDealAudio();
      GameFloor.awakenAi();
    });
  }

  private static void stopDealAudio() {
    if (dealPlayer != null && (dealPlayer.isPlaying())) {
      dealPlayer.stop();
    }
  }

  private static void playDealAudio() {
    try {
      String settingMuted = XmlSettings.getSettingMuted();
      if (settingMuted.equals("False")) {
        String path1 = "src/main/resources/audio/cardSlide.wav";
        dealPlayer = new AudioClip(new File(path1).toURI().toString());
        dealPlayer.play();
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void playShuffleAudio() {
    try {
      // Create a new DocumentBuilderFactory instance
      String settingMuted = XmlSettings.getSettingMuted();
      if (settingMuted.equals("False")) {
        String path = "src/main/resources/audio/cardShuffle.wav";
        shufflePlayer = new AudioClip(new File(path).toURI().toString());
        shufflePlayer.play();
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
