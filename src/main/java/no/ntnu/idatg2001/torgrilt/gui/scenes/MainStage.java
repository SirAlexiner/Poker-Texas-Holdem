package no.ntnu.idatg2001.torgrilt.gui.scenes;

import javafx.application.Application;
import javafx.scene.AmbientLight;
import javafx.scene.DepthTest;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import no.ntnu.idatg2001.torgrilt.gui.globalelements.GlobalElements;
import no.ntnu.idatg2001.torgrilt.gui.scenes.GameFloor;
import no.ntnu.idatg2001.torgrilt.gui.scenes.MainMenu;
import no.ntnu.idatg2001.torgrilt.gui.utilities.CustomCursor;
import no.ntnu.idatg2001.torgrilt.io.github.siralexiner.fxmanager.FxManager;

public class MainStage extends Application {

  private static final AmbientLight ambientLight = new AmbientLight(Color.WHITE.darker());

  @Override
  public void start(Stage stage) {
    System.setProperty("prism.order", "sw");

    FxManager.setup(stage);
    stage.setFullScreenExitHint("");
    stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
    stage.setFullScreen(true);
    stage.setResizable(false);


    // set the stage title and scene
    stage.setTitle("Texas HoldEm Poker");
    stage.getIcons().add(new Image("images/icon.png"));


    StackPane root = new StackPane(
        GameFloor.getGamescene(stage).getRoot(),
        MainMenu.getMainMenu(stage).getRoot()
    );
    root.setDepthTest(DepthTest.DISABLE);

    Scene scene =
        new Scene(
            root, GlobalElements.getSceneWidth(), GlobalElements.getSceneHeight(), true,
            SceneAntialiasing.BALANCED
        );
    scene.getStylesheets().addAll("styles/style.css");
    CustomCursor.setCustomCursor(scene);


    // Create a PointLight
    PointLight pointLight = new PointLight();
    pointLight.setColor(Color.rgb(60, 60, 60));
    pointLight.setTranslateX(scene.getWidth() / 2);
    pointLight.setTranslateY(scene.getHeight() / 2);
    pointLight.setTranslateZ(-6000);

    root.getChildren().addAll(pointLight, ambientLight);

    stage.setScene(scene);
    stage.show();
  }

  @Override
  public void stop() {
    System.exit(0);
  }

  public static void open(String[] args) {
    launch(args);
  }
}
