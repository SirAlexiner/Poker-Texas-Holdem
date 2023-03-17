package no.ntnu.idatg2001.torgrilt.gui.utilities;

import javafx.application.Application;
import javafx.stage.Stage;

public class Web extends Application {
  @Override
  public void start(Stage primaryStage) {
    // Auto Stub
  }

  public void openRules() {
    getHostServices().showDocument("https://en.wikipedia.org/wiki/Texas_hold_%27em#Rules");
  }
}
