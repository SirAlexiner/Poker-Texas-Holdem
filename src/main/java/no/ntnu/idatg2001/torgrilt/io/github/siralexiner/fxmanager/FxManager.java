package no.ntnu.idatg2001.torgrilt.io.github.siralexiner.fxmanager;

import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import com.jthemedetecor.OsThemeDetector;
import java.io.File;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import no.ntnu.idatg2001.torgrilt.io.github.mimoguz.window.StageOps;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * > This class manages the JavaFX application.
 */
@UtilityClass
public class FxManager {
  @Getter
  private boolean isDark;

  private final OsThemeDetector detector = OsThemeDetector.getDetector();

  /**
   * It sets the user agent stylesheet to the light theme,
   * and then sets the window caption color to the light theme's
   * color.
   *
   * @param stage The stage to apply the theme to.
   */
  public void enableLightMode(Stage stage) {
    Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
    isDark = false;
    Platform.runLater(() -> {
      final var handle = StageOps.findWindowHandle(stage);
      StageOps.setCaptionColor(handle, Color.rgb(255, 255, 255));
      StageOps.setBorderColor(handle, Color.rgb(225, 225, 225));
    });
  }

  /**
   * It sets the user agent stylesheet to the PrimerDark theme,
   * and then sets the caption color to the PrimerDark caption color.
   *
   * @param stage The stage to apply the dark mode to.
   */
  public void enableDarkMode(Stage stage) {
    Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
    isDark = true;
    Platform.runLater(() -> {
      final var handle = StageOps.findWindowHandle(stage);
      StageOps.setCaptionColor(handle, Color.rgb(13, 17, 23));
      StageOps.setBorderColor(handle, Color.rgb(13, 17, 23));
    });
  }

  /**
   * If the user is using a dark theme, enable dark mode, otherwise enable light mode.
   *
   * @param stage The stage of the application.
   */
  public static void setup(Stage stage) {
    File xmlFile = new File("Poker.xml");
    if (xmlFile.exists()) {
      readxml(stage);
    } else {
      isDark = detector.isDark();
    }
    if (isDark) {
      enableDarkMode(stage);
    } else {
      enableLightMode(stage);
    }
  }

  private static void readxml(Stage stage) {
    try {
      // Create a new DocumentBuilderFactory instance
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
      factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
      // Create a new DocumentBuilder instance
      DocumentBuilder builder = factory.newDocumentBuilder();
      // Parse the XML file and create a new Document instance
      Document document = builder.parse(new File("Poker.xml"));

      // Get the root element
      Element rootElement = document.getDocumentElement();

      String theme = rootElement.getElementsByTagName("Theme").item(0).getTextContent();
      isDark = theme.equals("Dark Mode") || (theme.equals("System Default") && detector.isDark());
      if (isDark) {
        enableDarkMode(stage);
      } else {
        enableLightMode(stage);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
