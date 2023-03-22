package no.ntnu.idatg2001.torgrilt.gui.utilities;

import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import lombok.experimental.UtilityClass;
import no.ntnu.idatg2001.torgrilt.io.github.siralexiner.fxmanager.FxManager;

/**
 * It creates a custom cursor.
 */
@UtilityClass

public class CustomCursor {

  /**
   * If the theme is dark, set the cursor to a white cursor, otherwise set it to a black cursor.
   *
   * @param scene The scene to set the cursor for.
   */
  public static void setCustomCursor(Scene scene) {
    if (FxManager.isDark()) {
      scene.setCursor(new ImageCursor(new Image("cursors/cursor_white.png")));
    } else {
      scene.setCursor(new ImageCursor(new Image("cursors/cursor_black.png")));
    }
  }
}
