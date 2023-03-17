package no.ntnu.idatg2001.torgrilt.gui.utilities;

import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import lombok.experimental.UtilityClass;
import no.ntnu.idatg2001.torgrilt.io.github.siralexiner.fxmanager.FxManager;

@UtilityClass
public class CustomCursor {

  public static void setCustomCursor(Scene scene) {
    if (FxManager.isDark()) {
      scene.setCursor(new ImageCursor(new Image("cursors/cursor_white.png")));
    } else {
      scene.setCursor(new ImageCursor(new Image("cursors/cursor_black.png")));
    }
  }
}
