package no.ntnu.idatg2001.torgrilt.gui.scenes;

import atlantafx.base.theme.Styles;
import com.jthemedetecor.OsThemeDetector;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.experimental.UtilityClass;
import no.ntnu.idatg2001.torgrilt.gui.elements3d.Card3D;
import no.ntnu.idatg2001.torgrilt.gui.globalelements.GlobalElements;
import no.ntnu.idatg2001.torgrilt.gui.utilities.CustomCursor;
import no.ntnu.idatg2001.torgrilt.gui.utilities.XmlSettings;
import no.ntnu.idatg2001.torgrilt.io.github.siralexiner.fxmanager.FxManager;

@UtilityClass
public class Settings {
  public static void addSettingsScene(Stage stage) {
    String themeStr = null;
    String rankStr = null;
    String suitStr = null;
    try {
      themeStr = XmlSettings.getSettingTheme();
      rankStr = XmlSettings.getSplashCardRank();
      suitStr = XmlSettings.getSplashCardSuit();
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Create the content for the settings window
    VBox settingsBox = new VBox();
    settingsBox.setId("Main Menu");
    settingsBox.setStyle("-fx-background-color: rgba(128, 128, 128, 0.5);");
    settingsBox.setSpacing(15);
    settingsBox.setAlignment(Pos.CENTER);
    settingsBox.setTranslateZ(-50);

    ComboBox<String> theme = new ComboBox<>();
    theme.setPromptText("Theme");
    theme.setStyle("-fx-font-size: 20px;");
    theme.setPrefWidth(GlobalElements.getButtonWidth());
    theme.getItems().add("Dark Mode");
    theme.getItems().add("Light Mode");
    theme.getItems().add("OS Mode");
    theme.getSelectionModel().select(themeStr);
    theme.setOnAction(event1 -> {
      String selectedOption = theme.getSelectionModel().getSelectedItem();
      // Execute different actions based on selected option
      switch (selectedOption) {
        case "Dark Mode" -> {
          FxManager.enableDarkMode(stage);
          CustomCursor.setCustomCursor(stage.getScene());
        }
        case "Light Mode" -> {
          FxManager.enableLightMode(stage);
          CustomCursor.setCustomCursor(stage.getScene());
        }
        default -> {
          if (OsThemeDetector.getDetector().isDark()) {
            FxManager.enableDarkMode(stage);
            CustomCursor.setCustomCursor(stage.getScene());
          } else {
            FxManager.enableLightMode(stage);
            CustomCursor.setCustomCursor(stage.getScene());
          }
        }
      }
    });

    ComboBox<String> cardSuitSelect = new ComboBox<>();
    cardSuitSelect.setPromptText("Card Suit");
    cardSuitSelect.setStyle("-fx-font-size: 20px;");
    cardSuitSelect.setPrefWidth(GlobalElements.getButtonWidth());
    cardSuitSelect.getItems().add("Random");
    cardSuitSelect.getItems().add("Clubs");
    cardSuitSelect.getItems().add("Diamonds");
    cardSuitSelect.getItems().add("Hearts");
    cardSuitSelect.getItems().add("Spades");
    cardSuitSelect.getSelectionModel().select(suitStr);

    ComboBox<String> cardRankSelect = new ComboBox<>();
    cardRankSelect.setPromptText("Card Suit");
    cardRankSelect.setStyle("-fx-font-size: 20px;");
    cardRankSelect.setPrefWidth(GlobalElements.getButtonWidth());
    cardRankSelect.getItems().add("Random");
    cardRankSelect.getItems().add("Two");
    cardRankSelect.getItems().add("Three");
    cardRankSelect.getItems().add("Four");
    cardRankSelect.getItems().add("Five");
    cardRankSelect.getItems().add("Six");
    cardRankSelect.getItems().add("Seven");
    cardRankSelect.getItems().add("Eight");
    cardRankSelect.getItems().add("Nine");
    cardRankSelect.getItems().add("Ten");
    cardRankSelect.getItems().add("Jack");
    cardRankSelect.getItems().add("Queen");
    cardRankSelect.getItems().add("King");
    cardRankSelect.getItems().add("Ace");
    cardRankSelect.getSelectionModel().select(rankStr);

    cardSuitSelect.setOnAction(event -> {
      try {
        XmlSettings.saveToXmlSettings(theme, cardSuitSelect, cardRankSelect);
      } catch (Exception e) {
        e.printStackTrace();
      }
      Card3D.updateSplashCard();
    });

    cardRankSelect.setOnAction(event -> {
      try {
        XmlSettings.saveToXmlSettings(theme, cardSuitSelect, cardRankSelect);
      } catch (Exception e) {
        e.printStackTrace();
      }
      Card3D.updateSplashCard();
    });

    Label label = new Label("Splash Screen Card:");
    label.setStyle("-fx-font-size: 20px;");
    label.setLabelFor(cardSuitSelect);

    Button goBack = new Button("Save and Go Back");
    goBack.setStyle("-fx-font-size: 20px;");
    goBack.setPrefWidth(GlobalElements.getButtonWidth());
    goBack.getStyleClass().addAll(Styles.BUTTON_OUTLINED);


    settingsBox.getChildren().addAll(
        MainMenu.getMuteBox(), theme, label, cardSuitSelect, cardRankSelect, goBack
    );

    AnchorPane.setTopAnchor(settingsBox, 0.0);
    AnchorPane.setLeftAnchor(settingsBox, 0.0);
    AnchorPane.setBottomAnchor(settingsBox, 0.0);
    AnchorPane.setRightAnchor(settingsBox, 0.0);

    AnchorPane anchorPane = new AnchorPane(settingsBox);
    StackPane root = (StackPane) stage.getScene().getRoot();
    root.getChildren().add(anchorPane);

    goBack.setOnAction(event1 -> {

      try {
        XmlSettings.saveToXmlSettings(theme, cardSuitSelect, cardRankSelect);
      } catch (Exception e) {
        e.printStackTrace();
      }

      root.getChildren().remove(anchorPane);

    });
  }
}
