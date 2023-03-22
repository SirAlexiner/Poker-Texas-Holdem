package no.ntnu.idatg2001.torgrilt.gui.utilities;

import java.io.File;
import java.io.IOException;
import javafx.scene.control.ComboBox;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import lombok.experimental.UtilityClass;
import no.ntnu.idatg2001.torgrilt.gui.scenes.MainMenu;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * > This class is used to read and write settings to an XML file.
 */
@UtilityClass
public class XmlSettings {
  /**
   * > This function returns the value of the `Muted` element in the `Settings.xml` file.
   *
   * @return The value of the Muted element in the XML file.
   */
  public static String getSettingMuted()
      throws ParserConfigurationException, SAXException, IOException {
    // Create a new DocumentBuilderFactory instance
    Element rootElement = getXmlElement();

    return rootElement.getElementsByTagName("Muted").item(0).getTextContent();
  }

  /**
   * > This function returns the value of the `Theme` element in the `Settings.xml` file.
   *
   * @return The theme of the application.
   */
  public static String getSettingTheme()
      throws ParserConfigurationException, SAXException, IOException {
    // Create a new DocumentBuilderFactory instance
    Element rootElement = getXmlElement();

    return rootElement.getElementsByTagName("Theme").item(0).getTextContent();
  }

  /**
   * > This function returns the suit of the splash card.
   *
   * @return The suit of the splash card.
   */
  public static String getSplashCardSuit()
      throws ParserConfigurationException, SAXException, IOException {
    // Create a new DocumentBuilderFactory instance
    Element rootElement = getXmlElement();

    return rootElement.getElementsByTagName("Suit").item(0).getTextContent();
  }

  /**
   * > This function returns the rank of the splash card.
   *
   * @return The rank of the card.
   */
  public static String getSplashCardRank()
      throws ParserConfigurationException, SAXException, IOException {
    Element rootElement = getXmlElement();

    return rootElement.getElementsByTagName("Rank").item(0).getTextContent();
  }

  private static Element getXmlElement()
      throws ParserConfigurationException, SAXException, IOException {
    // Create a new DocumentBuilderFactory instance
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
    factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
    // Create a new DocumentBuilder instance
    DocumentBuilder builder = factory.newDocumentBuilder();
    // Parse the XML file and create a new Document instance
    Document document = builder.parse(new File("Poker.xml"));

    // Get the root element
    return document.getDocumentElement();
  }

  /**
   * It creates a new XML file and writes the settings to it.
   *
   * @param theme The theme that the user has selected
   * @param cardSuitSelect The ComboBox that contains the card suits
   * @param cardRankSelect The ComboBox that contains the card ranks
   */
  public static void saveToXmlSettings(ComboBox<String> theme,
                                       ComboBox<String> cardSuitSelect,
                                       ComboBox<String> cardRankSelect)
      throws ParserConfigurationException, TransformerException {
    // Create a new DocumentBuilderFactory instance
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    // Create a new DocumentBuilder instance
    DocumentBuilder builder = factory.newDocumentBuilder();
    // Create a new Document instance
    Document document = builder.newDocument();

    // Create a root element
    Element rootElement = document.createElement("Settings");
    document.appendChild(rootElement);

    // Create sub-elements for employee
    Element muted = document.createElement("Muted");
    if (MainMenu.getMuteButton().isSelected()) {
      muted.appendChild(document.createTextNode("True"));
    } else {
      muted.appendChild(document.createTextNode("False"));
    }
    rootElement.appendChild(muted);

    Element documentElement = document.createElement("Theme");
    documentElement.appendChild(
        document.createTextNode(theme.getSelectionModel().getSelectedItem()));
    rootElement.appendChild(documentElement);

    Element cardElement = document.createElement("Card");
    rootElement.appendChild(cardElement);

    Element cardSuitElement = document.createElement("Suit");
    cardSuitElement.appendChild(
        document.createTextNode(cardSuitSelect.getSelectionModel().getSelectedItem()));
    cardElement.appendChild(cardSuitElement);

    Element cardRankElement = document.createElement("Rank");
    cardRankElement.appendChild(
        document.createTextNode(cardRankSelect.getSelectionModel().getSelectedItem()));
    cardElement.appendChild(cardRankElement);

    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
    transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
    Transformer transformer = transformerFactory.newTransformer();
    DOMSource source = new DOMSource(document);

    // Create a new StreamResult instance with the file path
    File file = new File("Poker.xml");
    StreamResult result = new StreamResult(file);

    // Transform the DOMSource to the StreamResult
    transformer.transform(source, result);
  }
}
