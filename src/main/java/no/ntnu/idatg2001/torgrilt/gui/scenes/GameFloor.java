package no.ntnu.idatg2001.torgrilt.gui.scenes;

import static atlantafx.base.theme.Styles.STATE_ACCENT;
import static atlantafx.base.theme.Styles.STATE_DANGER;
import static atlantafx.base.theme.Styles.STATE_SUCCESS;

import atlantafx.base.theme.Styles;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.Camera;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import no.ntnu.idatg2001.torgrilt.gui.elements3d.Card3D;
import no.ntnu.idatg2001.torgrilt.gui.globalelements.GlobalElements;
import no.ntnu.idatg2001.torgrilt.gui.utilities.Animate;
import no.ntnu.idatg2001.torgrilt.gui.utilities.NumberField;
import no.ntnu.idatg2001.torgrilt.gui.utilities.Web;
import no.ntnu.idatg2001.torgrilt.poker.Card;
import no.ntnu.idatg2001.torgrilt.poker.DeckOfCards;
import no.ntnu.idatg2001.torgrilt.poker.Poker;
import no.ntnu.idatg2001.torgrilt.poker.PokerAI;

/**
 * It creates a class called GameFloor.
 */
@UtilityClass
public class GameFloor {
  private Scene singleInstance = null;
  @Getter
  private Group deckOfCard;
  @Getter
  private Group playerHand;
  private NumberField playerPot;
  private Card playerCardOne;
  private Card playerCardTwo;
  @Getter
  private Group opponentHand;
  private NumberField opponentPot;
  private Card opponentCardOne;
  private Card opponentCardTwo;
  @Getter
  @Setter
  private NumberField turnPot;
  @Getter
  private VBox sideButtons;
  @Getter
  private StackPane player;
  @Getter
  @Setter
  private ArrayList<Card> board = new ArrayList<>();
  private Stage stage;
  private Button raise;
  private Button call;
  private Button check;
  private Button fold;
  @Getter
  private ArrayList<Card> drawnCards;
  private boolean playerChecked;
  private boolean aiChecked;
  private Tooltip tooltip;
  private final String allIn = "All In";
  private final String audioRaiseString = "src/main/resources/audio/raise.mp3";
  private final String audioCallString = "src/main/resources/audio/call.mp3";

  /**
   * If the singleInstance variable is null,
   * then create a new game scene and assign it to the singleInstance variable.
   * Otherwise, return the singleInstance variable
   *
   * @param stage The stage that the scene will be displayed on.
   * @return A Scene object.
   */
  public Scene getGameScene(Stage stage) {
    if (singleInstance == null) {
      singleInstance = createGameScene(stage);
    }
    return singleInstance;
  }

  /**
   * It creates the game scene.
   *
   * @param stage The stage that the scene is being added to.
   * @return A scene is being returned.
   */
  private Scene createGameScene(Stage stage) {
    GameFloor.stage = stage;

    DeckOfCards deck = new DeckOfCards();

    drawnCards = (ArrayList<Card>) deck.draw(12);
    deckOfCard = Card3D.getDeck(drawnCards);
    deckOfCard.setScaleY(1.25);
    deckOfCard.setScaleX(1.25);
    deckOfCard.setTranslateZ(300);

    playerPot = new NumberField(Poker.getPlayerPot());
    setStylePot(playerPot, STATE_SUCCESS);

    playerCardOne = drawnCards.remove(0);
    playerCardTwo = drawnCards.remove(0);

    playerHand = Card3D.getPlayerHand(playerCardOne, playerCardTwo, stage);
    playerHand.setScaleY(1.5);
    playerHand.setScaleX(1.5);
    playerHand.setTranslateY(350);
    playerHand.setTranslateX(25);
    playerHand.setRotate(-11.25);

    opponentPot = new NumberField(Poker.getOpponentPot());
    setStylePot(opponentPot, STATE_DANGER);

    opponentCardOne = drawnCards.remove(0);
    opponentCardTwo = drawnCards.remove(0);

    opponentHand = Card3D.getOpponentHand(opponentCardOne, opponentCardTwo);
    opponentHand.setTranslateY(-350);
    opponentHand.setTranslateX(75);
    opponentHand.setRotationAxis(new Point3D(1, 0, 1));
    opponentHand.setRotate(-11.25);

    turnPot = new NumberField(0.0);
    setStylePot(turnPot, STATE_ACCENT);

    Button rules = new Button("Rules");
    rules.setStyle(GlobalElements.getDefaultStyle());
    rules.setPrefWidth(GlobalElements.getButtonWidth() * 0.75);
    rules.getStyleClass().addAll(Styles.BUTTON_OUTLINED);
    rules.setOnMouseClicked(event -> {
      Web web = new Web();
      web.openRules();
    });

    Button settings = new Button("Settings");
    settings.setStyle(GlobalElements.getDefaultStyle());
    settings.setPrefWidth(GlobalElements.getButtonWidth() * 0.75);
    settings.getStyleClass().addAll(Styles.BUTTON_OUTLINED);

    settings.setOnAction(event -> Settings.addSettingsScene(stage));

    Button exitGame = new Button("Exit Game");
    exitGame.setStyle(GlobalElements.getDefaultStyle());
    exitGame.setPrefWidth(GlobalElements.getButtonWidth() * 0.75);
    exitGame.getStyleClass().addAll(Styles.DANGER, Styles.BUTTON_OUTLINED);
    exitGame.setOnAction(event -> System.exit(0));

    sideButtons = new VBox(rules, settings, exitGame);
    sideButtons.setAlignment(Pos.CENTER);
    sideButtons.setSpacing(15);
    sideButtons.setPadding(new Insets(0, 0, 0, 15));

    raise = new Button("Raise");
    raise.setStyle(GlobalElements.getDefaultStyle());
    raise.setPrefWidth(GlobalElements.getButtonWidth() / 2);
    raise.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
    raise.setDisable(true);
    raise.setOnMouseClicked(event -> {
      AudioClip raisePlayer = new AudioClip(new File(audioRaiseString).toURI().toString());
      if (!MainMenu.getMuteButton().isSelected()) {
        raisePlayer.play();
      }
      double playerBet =
          Math.min(GlobalElements.getPreviousBet() * 2, Poker.getPlayerPot());

      GlobalElements.setPreviousBet(playerBet);
      turnPot.setDouble(turnPot.getDouble() + playerBet);
      Poker.setPlayerPot(Poker.getPlayerPot() - playerBet);
      playerPot.setDouble(Poker.getPlayerPot());
      raise.setDisable(true);
      call.setDisable(true);
      check.setDisable(true);
      fold.setDisable(true);
      if (Poker.getPlayerPot() == 0) {
        if (GlobalElements.getGameTurn() < 3) {
          Animate.showBoard();
        } else {
          GameFloor.allIn();
        }
      } else {
        runAi(stage, GlobalElements.getPreviousBet(), true, false);
      }
    });

    check = new Button("Check");
    check.setStyle(GlobalElements.getDefaultStyle());
    check.setPrefWidth(GlobalElements.getButtonWidth() / 2);
    check.getStyleClass().addAll(Styles.BUTTON_OUTLINED);
    check.setDisable(true);
    check.setOnAction(event -> {
      playRaiseAndDisablePlayerButtons(audioCallString);
      if (aiChecked) {
        nextTurn(false);
      } else {
        playerChecked = true;
        runAi(stage, 0, false, false);
      }
    });

    call = new Button("Call");
    call.setStyle(GlobalElements.getDefaultStyle());
    call.setPrefWidth(GlobalElements.getButtonWidth() / 2);
    call.getStyleClass().addAll(Styles.BUTTON_OUTLINED);
    call.setDisable(true);
    call.setOnAction(event -> {
      playRaiseAndDisablePlayerButtons(audioCallString);

      turnPot.setDouble(turnPot.getDouble() + GlobalElements.getPreviousBet());
      Poker.setPlayerPot(Poker.getPlayerPot() - GlobalElements.getPreviousBet());
      playerPot.setDouble(Poker.getPlayerPot());

      nextTurn(false);
    });

    fold = new Button("Fold");
    fold.setStyle(GlobalElements.getDefaultStyle());
    fold.setPrefWidth(GlobalElements.getButtonWidth() / 2);
    fold.getStyleClass().addAll(Styles.DANGER, Styles.BUTTON_OUTLINED);
    fold.setDisable(true);
    fold.setOnAction(event -> {
      String s = "src/main/resources/audio/userFold.mp3";
      playRaiseAndDisablePlayerButtons(s);

      Poker.setOpponentPot(Poker.getOpponentPot() + turnPot.getDouble());
      opponentPot.setDouble(Poker.getOpponentPot());
      turnPot.setDouble(0.0);
      Platform.runLater(GameFloor::newTurn);
    });

    HBox buttons = new HBox(raise, call, check, fold);
    buttons.setSpacing(10);
    buttons.setTranslateY(90);

    TextField time = new TextField();
    setStylePot(time, STATE_DANGER);

    Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e ->
        time.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")))
    ),
        new KeyFrame(Duration.seconds(1))
    );
    clock.setCycleCount(Animation.INDEFINITE);
    clock.play();

    // --------------- //
    player = new StackPane(playerHand, buttons);

    AnchorPane anchor = new AnchorPane();
    anchor.setId("Root");
    anchor.setPrefSize(1920, 1080);
    anchor.getChildren()
        .addAll(opponentHand, sideButtons, player, deckOfCard, opponentPot, turnPot, playerPot,
            time);

    AnchorPane.setTopAnchor(opponentHand, 20.0);
    AnchorPane.setLeftAnchor(opponentHand, 50.0);

    AnchorPane.setTopAnchor(opponentPot, 175.0);
    AnchorPane.setLeftAnchor(opponentPot, 50.0);

    AnchorPane.setTopAnchor(sideButtons, 30.0);
    AnchorPane.setLeftAnchor(sideButtons, 20.0);

    AnchorPane.setBottomAnchor(player, 20.0);
    AnchorPane.setLeftAnchor(player, 50.0);

    AnchorPane.setBottomAnchor(playerPot, 50.0);
    AnchorPane.setLeftAnchor(playerPot, 50.0);

    AnchorPane.setTopAnchor(deckOfCard, 50.0);
    AnchorPane.setLeftAnchor(deckOfCard, 50.0);

    AnchorPane.setTopAnchor(turnPot, 50.0);
    AnchorPane.setLeftAnchor(turnPot, 50.0);

    AnchorPane.setBottomAnchor(time, 20.0);
    AnchorPane.setRightAnchor(time, 20.0);

    anchor.setOpacity(0);
    anchor.setDepthTest(DepthTest.DISABLE);

    anchor.widthProperty().addListener(
        (observable, oldValue, newValue) -> updateWidthConstraints(newValue.doubleValue()));

    anchor.heightProperty().addListener(
        (observable, oldValue, newValue) -> updateHeightConstraints(newValue.doubleValue()));

    Scene scene = new Scene(anchor, 0, 0, true);

    Camera camera = new PerspectiveCamera();
    camera.setNearClip(0.1);
    camera.setFarClip(100.0);
    scene.setCamera(camera);

    FadeTransition fadeIn = new FadeTransition(new Duration(250));
    fadeIn.setNode(anchor);
    fadeIn.setFromValue(0);
    fadeIn.setToValue(1);
    fadeIn.play();

    anchor.requestFocus();

    if (GlobalElements.isRestarted()) {
      Animate.shuffle(GameFloor.getDeckOfCard());
      GlobalElements.setRestarted(false);
    }

    return scene;
  }

  private void runAi(Stage stage, double bet, boolean raise, boolean firstTurn) {
    PauseTransition aiThink = new PauseTransition(new Duration(1500));
    aiThink.setOnFinished(event1 -> {
      Card[] cards = new Card[]{opponentCardOne, opponentCardTwo};
      double opponentBet =
          PokerAI.getBet(stage, cards, bet,
              Poker.getOpponentPot(),
              raise, firstTurn);
      turnPot.setDouble(turnPot.getDouble() + opponentBet);
      Poker.setOpponentPot(Poker.getOpponentPot() - opponentBet);
      opponentPot.setDouble(Poker.getOpponentPot());
    });
    aiThink.play();
  }

  private void playRaiseAndDisablePlayerButtons(String audioCallString) {
    AudioClip raisePlayer = new AudioClip(new File(audioCallString).toURI().toString());
    if (!MainMenu.getMuteButton().isSelected()) {
      raisePlayer.play();
    }
    raise.setDisable(true);
    call.setDisable(true);
    check.setDisable(true);
    fold.setDisable(true);
  }

  /**
   * If the user clicks the "New Game" button,
   * the program will ask the user if they want to start a new game. If the user
   * clicks "OK", the program will start a new game.
   * If the user clicks "Cancel", the program will ask the user if they
   * want to exit the game.
   * If the user clicks "OK", the program will exit. If the user clicks "Cancel", the program will
   * ask the user if they want to start a new game.
   */
  public static void newGame() {
    if (tooltip != null) {
      tooltip.hide();
    }
    GlobalElements.setGameTurn(0);
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.initModality(Modality.APPLICATION_MODAL);
    alert.initOwner(stage.getScene().getWindow());
    alert.setHeaderText("New Game?");
    alert.setContentText("Do you want to start a new game?");
    Optional<ButtonType> result = alert.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {
      Poker.setOpponentPot(GlobalElements.getDefaultStartingPot());
      Poker.setPlayerPot(GlobalElements.getDefaultStartingPot());
      refreshGameBoard();
    } else {
      exitGameAlert();
    }
  }

  private static void refreshGameBoard() {
    board.clear();
    GlobalElements.setRestarted(true);
    Scene tempInstance = getGameScene(stage);
    singleInstance = createGameScene(stage);
    StackPane root = (StackPane) stage.getScene().getRoot();
    root.getChildren().add(singleInstance.getRoot());
    root.getChildren().remove(tempInstance.getRoot());
  }

  private static void setStylePot(TextField playerPot, PseudoClass stateSuccess) {
    playerPot.setAlignment(Pos.CENTER);
    playerPot.setStyle(GlobalElements.getDefaultStyle());
    playerPot.getStyleClass().addAll(Styles.BUTTON_OUTLINED, Styles.ROUNDED);
    playerPot.pseudoClassStateChanged(stateSuccess, true);
    playerPot.setDisable(true);
    playerPot.setOpacity(1);
  }

  private void newTurn() {
    GlobalElements.setGameTurn(0);
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.initModality(Modality.APPLICATION_MODAL);
    alert.initOwner(stage.getScene().getWindow());
    alert.setHeaderText("New Turn?");
    alert.setContentText("Do you want to continue playing another round?");
    Optional<ButtonType> newTurnResult = alert.showAndWait();
    if (newTurnResult.isPresent() && newTurnResult.get() == ButtonType.OK) {
      refreshGameBoard();
    } else {
      exitGameAlert();
    }
  }

  private void exitGameAlert() {
    Alert exit = new Alert(Alert.AlertType.CONFIRMATION);
    exit.initModality(Modality.APPLICATION_MODAL);
    exit.initOwner(stage.getScene().getWindow());
    exit.setHeaderText("Exit Game?");
    exit.setContentText("Do you want to exit the game?");
    Optional<ButtonType> exitResult = exit.showAndWait();
    if (exitResult.isPresent() && exitResult.get() == ButtonType.OK) {
      System.exit(0);
    } else {
      newTurn();
    }
  }

  /**
   * This function is called when the AI player goes all in.
   * It plays a sound effect and disables all the buttons.
   */
  public void aiAllIn() {
    playRaiseAndDisablePlayerButtons(audioRaiseString);
  }

  /**
   * If the player's pot is greater than the previous bet,
   * and the maximum of the player's pot and the previous bet times
   * two is equal to the previous bet times two,
   * then the raise button is set to allIn, and the raise, call, and fold
   * buttons are enabled. Otherwise,
   * if the player's pot is less than or equal to the previous bet, then the raise button
   * is set to allIn,
   * and the raise and fold buttons are enabled.
   * Otherwise, the raise, call, and fold buttons are enabled.
   */
  public void aiRaised() {
    AudioClip raisePlayer = new AudioClip(new File(audioRaiseString).toURI().toString());
    if (!MainMenu.getMuteButton().isSelected()) {
      raisePlayer.play();
    }
    if (Poker.getPlayerPot() > GlobalElements.getPreviousBet()
        && Math.max(Poker.getPlayerPot(), GlobalElements.getPreviousBet() * 2)
        == GlobalElements.getPreviousBet() * 2) {
      raise.setText(allIn);
      raise.setDisable(false);
      call.setDisable(false);
      fold.setDisable(false);
    } else if (Poker.getPlayerPot() <= GlobalElements.getPreviousBet()) {
      raise.setText(allIn);
      raise.setDisable(false);
      fold.setDisable(false);
    } else {
      raise.setDisable(false);
      call.setDisable(false);
      fold.setDisable(false);
    }
  }

  /**
   * If the AI checks, then if the player has checked, then the next turn is called,
   * otherwise the AI has checked and the
   * player can raise, check, or fold.
   *
   * @param checked boolean, true if the AI called, false if the AI raised
   */
  public void aiCalledChecked(boolean checked) {
    AudioClip raisePlayer = new AudioClip(new File(audioCallString).toURI().toString());
    if (!MainMenu.getMuteButton().isSelected()) {
      raisePlayer.play();
    }
    if (checked) {
      if (playerChecked) {
        nextTurn(true);
      } else {
        aiChecked = true;
        raise.setDisable(false);
        check.setDisable(false);
        fold.setDisable(false);
      }
    } else {
      nextTurn(true);
    }
  }

  /**
   * When the AI folds, play a sound, add the turn pot to the player's pot, and start a new turn.
   */
  public void aiFolded() {
    String s = "src/main/resources/audio/aiFold.mp3";
    AudioClip raisePlayer = new AudioClip(new File(s).toURI().toString());
    if (!MainMenu.getMuteButton().isSelected()) {
      raisePlayer.play();
    }
    Poker.setPlayerPot(Poker.getPlayerPot() + turnPot.getDouble());
    playerPot.setDouble(Poker.getPlayerPot());
    turnPot.setDouble(0.0);
    Platform.runLater(GameFloor::newTurn);
  }

  /**
   * Make sure the board has five cards, then set the turn to 3 and call nextTurn.
   */
  public static void allIn() {
    switch (board.size()) {
      case 0 -> {
        board.add(getDrawnCards().remove(0));
        board.add(getDrawnCards().remove(0));
        board.add(getDrawnCards().remove(0));
        board.add(getDrawnCards().remove(0));
        board.add(getDrawnCards().remove(0));
        GlobalElements.setGameTurn(3);
        nextTurn(false);
      }
      case 1 -> {
        board.add(getDrawnCards().remove(0));
        board.add(getDrawnCards().remove(0));
        board.add(getDrawnCards().remove(0));
        board.add(getDrawnCards().remove(0));
        GlobalElements.setGameTurn(3);
        nextTurn(false);
      }
      case 2 -> {
        board.add(getDrawnCards().remove(0));
        board.add(getDrawnCards().remove(0));
        board.add(getDrawnCards().remove(0));
        GlobalElements.setGameTurn(3);
        nextTurn(false);
      }
      case 3 -> {
        board.add(getDrawnCards().remove(0));
        board.add(getDrawnCards().remove(0));
        GlobalElements.setGameTurn(3);
        nextTurn(false);
      }
      case 4 -> {
        board.add(getDrawnCards().remove(0));
        GlobalElements.setGameTurn(3);
        nextTurn(false);
      }
      case 5 -> {
        GlobalElements.setGameTurn(3);
        nextTurn(false);
      }
      default -> throw new IllegalArgumentException("Invalid board size: " + board.size());
    }
  }

  /**
   * Based on the current turn add the cards for that turn, animate the board,
   * update the gameTurn and call whoGoes.
   *
   * @param aiCalled boolean to determine if the aiCalled the players raise.
   */
  public static void nextTurn(boolean aiCalled) {
    playerChecked = false;
    aiChecked = false;
    if (GlobalElements.getGameTurn() == 0) {
      board.add(getDrawnCards().remove(0));
      board.add(getDrawnCards().remove(0));
      board.add(getDrawnCards().remove(0));
      Animate.flop(deckOfCard);
      GlobalElements.setGameTurn(1);
      whoGoes(aiCalled);
    } else if (GlobalElements.getGameTurn() == 1) {
      board.add(getDrawnCards().remove(0));
      Animate.turn(deckOfCard);
      GlobalElements.setGameTurn(2);
      whoGoes(aiCalled);
    } else if (GlobalElements.getGameTurn() == 2) {
      board.add(getDrawnCards().remove(0));
      Animate.river(deckOfCard);
      GlobalElements.setGameTurn(3);
      whoGoes(aiCalled);
    } else {
      Card[] playerHand = {playerCardOne, playerCardTwo};
      Card[] opponentHand = {opponentCardOne, opponentCardTwo};

      Animate.showCards(getPlayerHand(), getOpponentHand());
      PauseTransition pauseTransition = new PauseTransition(new Duration(1000));
      pauseTransition.setOnFinished(event -> whoWon(playerHand, opponentHand));
      pauseTransition.play();
    }
  }

  private static void whoWon(Card[] playerHand, Card[] opponentHand) {
    double playerHandStrength = Poker.getHandStrength(playerHand);
    double opponentHandStrength = Poker.getHandStrength(opponentHand);
    tooltip = playerHandStrength > opponentHandStrength
        ? new Tooltip("Player Wins")
        : playerHandStrength < opponentHandStrength
            ? new Tooltip("Computer Wins")
            : new Tooltip("It's a Tie");
    if (playerHandStrength != opponentHandStrength) {
      Poker.setPlayerPot(playerHandStrength > opponentHandStrength
          ? Poker.getPlayerPot() + turnPot.getDouble()
          : Poker.getPlayerPot());
      Poker.setOpponentPot(playerHandStrength < opponentHandStrength
          ? Poker.getOpponentPot() + turnPot.getDouble()
          : Poker.getOpponentPot());
    } else {
      double halfPot = turnPot.getDouble() / 2.0;
      Poker.setOpponentPot(Poker.getOpponentPot() + halfPot);
      Poker.setPlayerPot(Poker.getPlayerPot() + halfPot);
    }
    turnPot.setDouble(0.0);
    tooltip.show(stage);
    PauseTransition delay = new PauseTransition(new Duration(2500));
    delay.setOnFinished(event -> {
      tooltip.hide();
      Platform.runLater(GameFloor::newTurn);
    });
    delay.play();
  }

  private static void whoGoes(boolean aiCalled) {
    if (aiCalled) {
      double minPot = Math.min(Poker.getPlayerPot(), GlobalElements.getPreviousBet());
      if (minPot == Poker.getPlayerPot() || minPot == GlobalElements.getPreviousBet() * 2) {
        raise.setText(allIn);
      }
      raise.setDisable(false);
      check.setDisable(false);
      fold.setDisable(false);
    } else {
      runAi(stage, 0, false, false);
    }
  }

  /**
   * The AI waits 2.5 seconds, then makes a bet.
   */
  public void awakenAi() {
    runAi(stage, 0, false, true);
  }

  private void updateWidthConstraints(double width) {
    AnchorPane.setLeftAnchor(opponentHand, width / 2.45);
    AnchorPane.setLeftAnchor(opponentPot, width / 2.33);
    AnchorPane.setLeftAnchor(player, width / 2.65);
    AnchorPane.setLeftAnchor(playerPot, width / 2.33);
    AnchorPane.setLeftAnchor(deckOfCard, width / 2.175);
    AnchorPane.setLeftAnchor(turnPot, width / 2.33);
  }

  private void updateHeightConstraints(double height) {
    AnchorPane.setTopAnchor(deckOfCard, height / 2.5);
    AnchorPane.setTopAnchor(turnPot, height / 3.5);
  }
}
