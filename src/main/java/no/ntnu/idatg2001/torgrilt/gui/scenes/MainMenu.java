package no.ntnu.idatg2001.torgrilt.gui.scenes;

import atlantafx.base.theme.Styles;
import java.io.File;
import java.util.Objects;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Camera;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.swing.Timer;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import no.ntnu.idatg2001.torgrilt.gui.elements3d.Card3D;
import no.ntnu.idatg2001.torgrilt.gui.elements3d.Logo3D;
import no.ntnu.idatg2001.torgrilt.gui.globalelements.GlobalElements;
import no.ntnu.idatg2001.torgrilt.gui.utilities.Animate;
import no.ntnu.idatg2001.torgrilt.gui.utilities.Web;
import no.ntnu.idatg2001.torgrilt.gui.utilities.XmlSettings;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * It creates a class called MainMenu.
 */
@UtilityClass
public class MainMenu {
  @Getter
  private static ToggleButton muteButton;
  @Getter
  private static HBox muteBox;
  @Getter
  private static Group card;

  @Getter
  private AudioClip mediaPlayer2;


  /**
   * It creates a scene with a 3D card, a logo, and a few buttons.
   *
   * @param stage The stage that the scene will be added to.
   * @return A scene object.
   */
  public static Scene getMainMenu(Stage stage) {
    card = Card3D.getSplashCard(stage);
    Card3D.animate3dCard(card);

    // Create a ToggleButton with no text
    muteButton = new ToggleButton("", new FontIcon(Feather.VOLUME_2));
    muteButton.getStyleClass().addAll(Styles.DANGER);
    muteButton.setStyle(GlobalElements.getDefaultStyle());

    try {
      String settingMuted = XmlSettings.getSettingMuted();
      if (settingMuted.equals("True")) {
        muteButton.setSelected(true);
        muteButton.setGraphic(new FontIcon(Feather.VOLUME_X));
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    muteBox = new HBox(muteButton);
    muteBox.setSpacing(15);
    muteBox.setAlignment(Pos.CENTER);

    Button startGame = new Button("START GAME");
    startGame.setStyle(GlobalElements.getDefaultStyle());
    startGame.setPrefWidth(GlobalElements.getButtonWidth());
    startGame.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
    Timer timer = new Timer(500, e -> Card3D.setAnimationSpeed(1));
    String clip = "src/main/resources/audio/shot.wav";
    AudioClip shotPlayer = new AudioClip(new File(clip).toURI().toString());
    startGame.setOnMouseEntered(mouseEvent -> {
      if (!shotPlayer.isPlaying() && !muteButton.isSelected()) {
        shotPlayer.play();
      }
      Card3D.setAnimationSpeed(8);
      timer.start();
    });
    startGame.setOnMouseExited(mouseEvent -> {
      timer.stop();
      Card3D.setAnimationSpeed(1);
    });

    Button rules = new Button("RULES");
    rules.setStyle(GlobalElements.getDefaultStyle());
    rules.setPrefWidth(GlobalElements.getButtonWidth());
    rules.getStyleClass().addAll(Styles.BUTTON_OUTLINED);
    menuHoverAudio(shotPlayer, rules);
    rules.setOnMouseClicked(event -> {
      Web web = new Web();
      web.openRules();
    });

    Button exit = new Button("EXIT GAME");
    exit.setStyle(GlobalElements.getDefaultStyle());
    exit.setPrefWidth(GlobalElements.getButtonWidth());
    exit.getStyleClass().addAll(Styles.DANGER, Styles.BUTTON_OUTLINED);
    menuHoverAudio(shotPlayer, exit);
    exit.setOnMouseClicked(mouseEvent -> System.exit(0));

    Button settings = new Button("SETTINGS");
    settings.setStyle(GlobalElements.getDefaultStyle());
    settings.setPrefWidth(GlobalElements.getButtonWidth());
    settings.getStyleClass().addAll(Styles.BUTTON_OUTLINED);
    menuHoverAudio(shotPlayer, settings);
    settings.setOnMouseClicked(event -> Settings.addSettingsScene(stage));

    HBox logoCon = new HBox(Logo3D.getLogoBox());
    logoCon.setPadding(new Insets(-15, 0, 30, 0));
    logoCon.setAlignment(Pos.CENTER);

    VBox center = new VBox(logoCon, startGame, rules, settings, exit);
    center.setAlignment(Pos.CENTER);
    center.setSpacing(10);

    StackPane stackPane = new StackPane(card, center);
    stackPane.setAlignment(Pos.CENTER);
    stackPane.setDepthTest(DepthTest.DISABLE);

    Scene scene = new Scene(stackPane, 0, 0, true);
    scene.setOnMouseClicked(event -> scene.getRoot().requestFocus());

    // set up the camera
    Camera camera = new PerspectiveCamera();
    camera.setNearClip(0.1);
    camera.setFarClip(100.0);
    scene.setCamera(camera);

    AudioClip mediaPlayer = getAudioClip();

    String start = "src/main/resources/audio/start.wav";
    AudioClip startPlayer = new AudioClip(new File(start).toURI().toString());

    startGame.setOnMouseClicked(mouseEvent -> {
      startGame.setDisable(true);
      if (!muteButton.isSelected()) {
        startPlayer.play();
        mediaPlayer.stop();
      }
      FadeTransition fade = new FadeTransition(Duration.millis(750));
      fade.setNode(stackPane);
      fade.setFromValue(1);
      fade.setToValue(0);

      fade.setOnFinished(event -> {
        StackPane root = (StackPane) stage.getScene().getRoot();
        root.getChildren().remove(1);
        Animate.shuffle(GameFloor.getDeckOfCard());
        String path2 = "src/main/resources/audio/theme2.mp3";
        mediaPlayer2 = new AudioClip(new File(path2).toURI().toString());
        if (!MainMenu.getMuteButton().isSelected()) {
          mediaPlayer2.setCycleCount(AudioClip.INDEFINITE);
          mediaPlayer2.play(Double.MAX_VALUE);
        }
      });
      fade.play();
    });
    return scene;
  }

  private static AudioClip getAudioClip() {
    //Initialising path of the media file, replace this with your file path
    String path = "src/main/resources/audio/theme.mp3";
    //Instantiating MediaPlayer class
    AudioClip mediaPlayer = new AudioClip(new File(path).toURI().toString());
    //by setting this property to true, the audio will be played
    if (!muteButton.isSelected()) {
      Objects.requireNonNullElse(mediaPlayer2, mediaPlayer).setCycleCount(AudioClip.INDEFINITE);
      Objects.requireNonNullElse(mediaPlayer2, mediaPlayer).play(Double.MAX_VALUE);
    }
    // Set the action to be performed when the button is clicked
    muteButton.setOnMouseClicked(e -> {
      if (muteButton.isSelected()) {
        // If the button is selected, set the unmute icon
        muteButton.setGraphic(new FontIcon(Feather.VOLUME_X));
        Objects.requireNonNullElse(mediaPlayer2, mediaPlayer).stop();
      } else {
        // If the button is not selected, set the mute icon
        muteButton.setGraphic(new FontIcon(Feather.VOLUME_2));
        if (mediaPlayer2 == null) {
          mediaPlayer.setCycleCount(AudioClip.INDEFINITE);
          mediaPlayer.play(Double.MAX_VALUE);
        } else {
          mediaPlayer2.setCycleCount(AudioClip.INDEFINITE);
          mediaPlayer2.play(Double.MAX_VALUE);
        }
      }
    });
    return mediaPlayer;
  }

  private static void menuHoverAudio(AudioClip audio, Button button) {
    button.setOnMouseEntered(mouseEvent -> {
      if (!audio.isPlaying() && !muteButton.isSelected()) {
        audio.play();
      }
    });
  }
}
