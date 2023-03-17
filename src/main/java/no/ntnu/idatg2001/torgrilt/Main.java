package no.ntnu.idatg2001.torgrilt;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

  @Override
  public void start(Stage stage){
    stage.setFullScreenExitHint("");
    stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
    stage.setFullScreen(true);
    stage.setResizable(false);

    stage.setTitle("Texas HoldEm Poker");

    StackPane root = new StackPane();

    Scene scene = new Scene(root);

    stage.setScene(scene);
    stage.show();
  }

  @Override
  public void stop() {
    System.exit(0);
  }

  public static void main(String[] args) {
    launch(args);
  }
}
