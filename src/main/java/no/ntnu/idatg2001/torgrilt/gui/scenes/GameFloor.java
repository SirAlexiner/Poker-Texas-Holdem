package no.ntnu.idatg2001.torgrilt.gui.scenes;

import static atlantafx.base.theme.Styles.STATE_ACCENT;
import static atlantafx.base.theme.Styles.STATE_DANGER;
import static atlantafx.base.theme.Styles.STATE_SUCCESS;

import atlantafx.base.theme.Styles;
import java.io.File;
import java.util.ArrayList;
import java.util.Optional;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
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
import no.ntnu.idatg2001.torgrilt.gui.utilities.Web;
import no.ntnu.idatg2001.torgrilt.poker.Card;
import no.ntnu.idatg2001.torgrilt.poker.DeckOfCards;
import no.ntnu.idatg2001.torgrilt.poker.Poker;
import no.ntnu.idatg2001.torgrilt.poker.PokerAI;

@UtilityClass
public class GameFloor {
  private Scene singleInstance = null;
  @Getter
  private Group deckOfCard;
  @Getter
  private Group playerHand;
  private TextField playerPot;
  private Card playerCardOne;
  private Card playerCardTwo;
  @Getter
  private Group opponentHand;
  private TextField opponentPot;
  private Card opponentCardOne;
  private Card opponentCardTwo;
  @Getter
  @Setter
  private TextField turnPot;
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
  private Button newGame;
  @Getter
  private ArrayList<Card> drawnCards;

  private boolean playerChecked;
  private boolean aiChecked;

  private Tooltip tooltip;

  public Scene getGamescene(Stage stage) {
    if (singleInstance == null) {
      singleInstance = createGameScene(stage);
    }
    return singleInstance;
  }

  private Scene createGameScene(Stage stage) {
    GameFloor.stage = stage;

    DeckOfCards deck = new DeckOfCards();
    deck.shuffle();

    drawnCards = (ArrayList<Card>) deck.draw(12);
    deckOfCard = Card3D.getDeck(drawnCards);
    deckOfCard.setScaleY(1.25);
    deckOfCard.setScaleX(1.25);
    deckOfCard.setTranslateZ(300);

    playerPot = new TextField(Poker.getPlayerPotString());
    setStylePot(playerPot, STATE_SUCCESS);

    playerCardOne = drawnCards.remove(0);
    playerCardTwo = drawnCards.remove(0);

    playerHand = Card3D.getPlayerHand(playerCardOne, playerCardTwo, stage);
    playerHand.setScaleY(1.5);
    playerHand.setScaleX(1.5);
    playerHand.setTranslateY(350);
    playerHand.setTranslateX(25);
    playerHand.setRotate(-11.25);

    opponentPot = new TextField(Poker.getOpponentPotString());
    setStylePot(opponentPot, STATE_DANGER);

    opponentCardOne = drawnCards.remove(0);
    opponentCardTwo = drawnCards.remove(0);

    opponentHand = Card3D.getOpponentHand(opponentCardOne, opponentCardTwo);
    opponentHand.setTranslateY(-350);
    opponentHand.setTranslateX(75);
    opponentHand.setRotationAxis(new Point3D(1, 0, 1));
    opponentHand.setRotate(-11.25);

    turnPot = new TextField("0");
    setStylePot(turnPot, STATE_ACCENT);

    newGame = new Button("New Game");
    newGame.setStyle("-fx-font-size: 20px;");
    newGame.setPrefWidth(GlobalElements.getButtonWidth() * 0.75);
    newGame.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
    newGame.setOnMouseClicked(event -> {
      newGame();
    });
    newGame.setDisable(true);

    Button rules = new Button("Rules");
    rules.setStyle("-fx-font-size: 20px;");
    rules.setPrefWidth(GlobalElements.getButtonWidth() * 0.75);
    rules.getStyleClass().addAll(Styles.BUTTON_OUTLINED);
    rules.setOnMouseClicked(event -> {
      Web web = new Web();
      web.openRules();
    });

    Button settings = new Button("Settings");
    settings.setStyle("-fx-font-size: 20px;");
    settings.setPrefWidth(GlobalElements.getButtonWidth() * 0.75);
    settings.getStyleClass().addAll(Styles.BUTTON_OUTLINED);

    settings.setOnAction(event -> Settings.addSettingsScene(stage));

    Button exitGame = new Button("Exit Game");
    exitGame.setStyle("-fx-font-size: 20px;");
    exitGame.setPrefWidth(GlobalElements.getButtonWidth() * 0.75);
    exitGame.getStyleClass().addAll(Styles.DANGER, Styles.BUTTON_OUTLINED);
    exitGame.setOnAction(event -> System.exit(0));

    sideButtons = new VBox(newGame, rules, settings, exitGame);
    sideButtons.setAlignment(Pos.CENTER);
    sideButtons.setSpacing(15);
    sideButtons.setPadding(new Insets(0, 0, 0, 15));

    raise = new Button("Raise");
    raise.setStyle("-fx-font-size: 20px;");
    raise.setPrefWidth(GlobalElements.getButtonWidth() / 2);
    raise.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
    raise.setDisable(true);
    raise.setOnMouseClicked(event -> {
      String raiseAudio = "src/main/resources/audio/raise.mp3";
      AudioClip raisePlayer = new AudioClip(new File(raiseAudio).toURI().toString());
      if (!MainMenu.getMuteButton().isSelected()) {
        raisePlayer.play();
      }
      int playerBet =
          Math.min(GlobalElements.getPreviousBet() * 2, Poker.getPlayerPot());

      GlobalElements.setPreviousBet(playerBet);
      turnPot.setText(
          String.valueOf(
              Integer.parseInt(turnPot.getText()) + playerBet));
      Poker.setPlayerPot(Poker.getPlayerPot() - playerBet);
      playerPot.setText(Poker.getPlayerPotString());
      raise.setDisable(true);
      call.setDisable(true);
      check.setDisable(true);
      fold.setDisable(true);
      PauseTransition aiThink = new PauseTransition(new Duration(2500));
      aiThink.setOnFinished(event1 -> {
        Card[] cards = new Card[] {opponentCardOne, opponentCardTwo};
        int opponentBet =
            PokerAI.getBet(stage, cards, playerBet, Poker.getOpponentPot(),
                true, false);
        turnPot.setText(String.valueOf(Integer.parseInt(turnPot.getText()) + opponentBet));
        Poker.setOpponentPot(Poker.getOpponentPot() - opponentBet);
        opponentPot.setText(Poker.getOpponentPotString());
        GlobalElements.setPreviousBet(opponentBet);
      });
      aiThink.play();
    });

    check = new Button("Check");
    check.setStyle("-fx-font-size: 20px;");
    check.setPrefWidth(GlobalElements.getButtonWidth() / 2);
    check.getStyleClass().addAll(Styles.BUTTON_OUTLINED);
    check.setDisable(true);
    check.setOnAction(event -> {
      String s = "src/main/resources/audio/call.mp3";
      AudioClip raisePlayer = new AudioClip(new File(s).toURI().toString());
      if (!MainMenu.getMuteButton().isSelected()) {
        raisePlayer.play();
      }
      raise.setDisable(true);
      call.setDisable(true);
      check.setDisable(true);
      fold.setDisable(true);
      if (aiChecked) {
        nextTurn(false);
      } else {
        playerChecked = true;
        PauseTransition aiThink = new PauseTransition(new Duration(5000));
        aiThink.setOnFinished(event1 -> {
          Card[] cards = new Card[] {opponentCardOne, opponentCardTwo};
          int opponentBet =
              PokerAI.getBet(stage, cards, 0,
                  Poker.getOpponentPot(),
                  false, false);
          turnPot.setText(String.valueOf(Integer.parseInt(turnPot.getText()) + opponentBet));
          Poker.setOpponentPot(Poker.getOpponentPot() - opponentBet);
          opponentPot.setText(Poker.getOpponentPotString());
        });
        aiThink.play();
      }
    });

    call = new Button("Call");
    call.setStyle("-fx-font-size: 20px;");
    call.setPrefWidth(GlobalElements.getButtonWidth() / 2);
    call.getStyleClass().addAll(Styles.BUTTON_OUTLINED);
    call.setDisable(true);
    call.setOnAction(event -> {
      raise.setDisable(true);
      call.setDisable(true);
      check.setDisable(true);
      fold.setDisable(true);

      turnPot.setText(
          String.valueOf(Integer.parseInt(turnPot.getText()) + GlobalElements.getPreviousBet()));
      Poker.setPlayerPot(Poker.getPlayerPot() - GlobalElements.getPreviousBet());
      playerPot.setText(Poker.getPlayerPotString());

      PauseTransition aiThink = new PauseTransition(new Duration(5000));
      aiThink.setOnFinished(event1 -> {
        Card[] cards = new Card[] {opponentCardOne, opponentCardTwo};
        int opponentBet =
            PokerAI.getBet(stage, cards, GlobalElements.getPreviousBet(),
                Poker.getOpponentPot(),
                false, false);
        turnPot.setText(String.valueOf(Integer.parseInt(turnPot.getText()) + opponentBet));
        Poker.setOpponentPot(Poker.getOpponentPot() - opponentBet);
        opponentPot.setText(Poker.getOpponentPotString());
      });
      aiThink.play();

      String s = "src/main/resources/audio/call.mp3";
      AudioClip raisePlayer = new AudioClip(new File(s).toURI().toString());
      if (!MainMenu.getMuteButton().isSelected()) {
        raisePlayer.play();
      }
      nextTurn(false);
    });

    fold = new Button("Fold");
    fold.setStyle("-fx-font-size: 20px;");
    fold.setPrefWidth(GlobalElements.getButtonWidth() / 2);
    fold.getStyleClass().addAll(Styles.DANGER, Styles.BUTTON_OUTLINED);
    fold.setDisable(true);
    fold.setOnAction(event -> {
      String s = "src/main/resources/audio/userFold.mp3";
      AudioClip raisePlayer = new AudioClip(new File(s).toURI().toString());
      if (!MainMenu.getMuteButton().isSelected()) {
        raisePlayer.play();
      }

      raise.setDisable(true);
      call.setDisable(true);
      check.setDisable(true);
      fold.setDisable(true);

      Poker.setOpponentPot(Poker.getOpponentPot() + Integer.parseInt(turnPot.getText()));
      opponentPot.setText(Poker.getOpponentPotString());
      turnPot.setText("0");
      Platform.runLater(GameFloor::newTurn);
    });

    HBox buttons = new HBox(raise, call, check, fold);
    buttons.setSpacing(10);
    buttons.setTranslateY(90);

    // --------------- //
    player = new StackPane(playerHand, buttons);

    AnchorPane anchor = new AnchorPane();
    anchor.setId("Root");
    anchor.setPrefSize(1920, 1080);
    anchor.getChildren()
        .addAll(opponentHand, sideButtons, player, deckOfCard, opponentPot, turnPot, playerPot);

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

  public static void newGame() {
    Poker.setOpponentPot(GlobalElements.getDefaultStartingPot());
    Poker.setPlayerPot(GlobalElements.getDefaultStartingPot());
    if (tooltip != null) {
      tooltip.hide();
    }
    GlobalElements.setGameTurn(0);
    Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
    dialog.initModality(Modality.APPLICATION_MODAL);
    dialog.initOwner(stage.getScene().getWindow());
    Optional<ButtonType> result = dialog.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {
      board.clear();
      GlobalElements.setRestarted(true);
      Scene tempInstance = getGamescene(stage);
      singleInstance = createGameScene(stage);
      StackPane root = (StackPane) stage.getScene().getRoot();
      root.getChildren().add(singleInstance.getRoot());
      root.getChildren().remove(tempInstance.getRoot());
    }
  }

  private static void setStylePot(TextField playerPot, PseudoClass stateSuccess) {
    playerPot.setStyle("-fx-font-size: 20px;");
    playerPot.getStyleClass().addAll(Styles.BUTTON_OUTLINED, Styles.ROUNDED);
    playerPot.pseudoClassStateChanged(stateSuccess, true);
    playerPot.setDisable(true);
    playerPot.setOpacity(1);
  }

  public void newTurn() {
    GlobalElements.setGameTurn(0);
    Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
    dialog.initModality(Modality.APPLICATION_MODAL);
    dialog.initOwner(stage.getScene().getWindow());
    Optional<ButtonType> result = dialog.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {
      board.clear();
      GlobalElements.setRestarted(true);
      Scene tempInstance = getGamescene(stage);
      singleInstance = createGameScene(stage);
      StackPane root = (StackPane) stage.getScene().getRoot();
      root.getChildren().add(singleInstance.getRoot());
      root.getChildren().remove(tempInstance.getRoot());
    }
  }

  public void aiAllIn() {
    String s = "src/main/resources/audio/raise.mp3";
    AudioClip raisePlayer = new AudioClip(new File(s).toURI().toString());
    if (!MainMenu.getMuteButton().isSelected()) {
      raisePlayer.play();
    }
    newGame.setDisable(false);
    raise.setDisable(true);
    call.setDisable(true);
    check.setDisable(true);
    fold.setDisable(true);
  }

  public void aiRaised() {
    String s = "src/main/resources/audio/raise.mp3";
    AudioClip raisePlayer = new AudioClip(new File(s).toURI().toString());
    if (!MainMenu.getMuteButton().isSelected()) {
      raisePlayer.play();
    }
    if (Math.min(Poker.getPlayerPot(), GlobalElements.getPreviousBet())
        == Poker.getPlayerPot()
        || Math.min(Poker.getPlayerPot(), GlobalElements.getPreviousBet() * 2)
        == Poker.getPlayerPot()) {
      raise.setText("All In");
      raise.setDisable(false);
      fold.setDisable(false);
    } else {
      newGame.setDisable(false);
      raise.setDisable(false);
      call.setDisable(false);
      fold.setDisable(false);
    }
  }

  public void aiCalledChecked(boolean checked) {
    String s = "src/main/resources/audio/call.mp3";
    AudioClip raisePlayer = new AudioClip(new File(s).toURI().toString());
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

  public void aiFolded() {
    String s = "src/main/resources/audio/aiFold.mp3";
    AudioClip raisePlayer = new AudioClip(new File(s).toURI().toString());
    if (!MainMenu.getMuteButton().isSelected()) {
      raisePlayer.play();
    }
    Poker.setPlayerPot(Poker.getPlayerPot() + Integer.parseInt(turnPot.getText()));
    playerPot.setText(Poker.getPlayerPotString());
    turnPot.setText("0");
    Platform.runLater(GameFloor::newTurn);
  }

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

      PauseTransition delay = new PauseTransition(new Duration(500));

      Animate.showCards(getPlayerHand(), getOpponentHand());
      PauseTransition pauseTransition = new PauseTransition(new Duration(1000));
      pauseTransition.setOnFinished(event -> whoWon(playerHand, opponentHand));
      pauseTransition.play();
    }
  }

  private static void whoWon(Card[] playerHand, Card[] opponentHand) {
    if (Poker.getHandStrength(playerHand) > Poker.getHandStrength(opponentHand)) {
      tooltip = new Tooltip("Player Wins");
      tooltip.show(stage);
      Poker.setPlayerPot(Poker.getPlayerPot() + Integer.parseInt(turnPot.getText()));
      turnPot.setText("0");
    } else if (Poker.getHandStrength(playerHand) < Poker.getHandStrength(opponentHand)) {
      tooltip = new Tooltip("Computer Wins");
      tooltip.show(stage);
      Poker.setOpponentPot(Poker.getOpponentPot() + Integer.parseInt(turnPot.getText()));
      turnPot.setText("0");
      Poker.setOpponentPot(Poker.getOpponentPot() + Integer.parseInt(turnPot.getText()) / 2);
      Poker.setPlayerPot(Poker.getPlayerPot() + Integer.parseInt(turnPot.getText()) / 2);
      turnPot.setText("0");
    } else {
      tooltip = new Tooltip("It's a Tie");
      tooltip.show(stage);
    }
    PauseTransition delay = new PauseTransition(new Duration(5000));
    delay.setOnFinished(event -> {
      tooltip.hide();
      Platform.runLater(GameFloor::newTurn);
    });
    delay.play();
  }

  private static void whoGoes(boolean aiCalled) {
    if (aiCalled) {
      if (Math.min(Poker.getPlayerPot(), GlobalElements.getPreviousBet())
          == Poker.getPlayerPot()
          || Math.min(Poker.getPlayerPot(), GlobalElements.getPreviousBet() * 2)
          == Poker.getPlayerPot()) {
        raise.setText("All In");
      }
      raise.setDisable(false);
      check.setDisable(false);
      fold.setDisable(false);
    } else {
      PauseTransition aiThink = new PauseTransition(new Duration(5000));
      aiThink.setOnFinished(event1 -> {
        Card[] cards = new Card[] {opponentCardOne, opponentCardTwo};
        int opponentBet =
            PokerAI.getBet(stage, cards, 0,
                Poker.getOpponentPot(),
                false, false);
        turnPot.setText(String.valueOf(Integer.parseInt(turnPot.getText()) + opponentBet));
        Poker.setOpponentPot(Poker.getOpponentPot() - opponentBet);
        opponentPot.setText(Poker.getOpponentPotString());
      });
      aiThink.play();
    }
  }

  @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
  public void awakenAI() {
    PauseTransition aiThink = new PauseTransition(new Duration(2500));
    aiThink.setOnFinished(event -> {
      Card[] cards = {opponentCardOne, opponentCardTwo};
      int opponentBet =
          PokerAI.getBet(stage, cards, 0,
              Poker.getOpponentPot(), false, true);
      turnPot.setText(String.valueOf(Integer.parseInt(turnPot.getText()) + opponentBet));
      Poker.setOpponentPot(Poker.getOpponentPot() - opponentBet);
      opponentPot.setText(Poker.getOpponentPotString());
      GlobalElements.setPreviousBet(opponentBet);
    });
    aiThink.play();
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
