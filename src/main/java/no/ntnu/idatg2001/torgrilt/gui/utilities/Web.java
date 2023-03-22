package no.ntnu.idatg2001.torgrilt.gui.utilities;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * When the user clicks the rules button, open the rules' page in the user's default browser.
 */
public class Web extends Application {
  @Override
  public void start(Stage stage) {
    // Auto Stub
  }

  public void openRules() {
    getHostServices().showDocument("https://en.wikipedia.org/wiki/Texas_hold_%27em#Rules");
  }
}
