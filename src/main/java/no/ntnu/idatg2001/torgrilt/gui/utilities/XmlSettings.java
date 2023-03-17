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

@UtilityClass
public class XmlSettings {
  public static String getSettingMuted()
      throws ParserConfigurationException, SAXException, IOException {
    // Create a new DocumentBuilderFactory instance
    Element rootElement = getXmlElement();

    return rootElement.getElementsByTagName("Muted").item(0).getTextContent();
  }

  public static String getSettingTheme()
      throws ParserConfigurationException, SAXException, IOException {
    // Create a new DocumentBuilderFactory instance
    Element rootElement = getXmlElement();

    return rootElement.getElementsByTagName("Theme").item(0).getTextContent();
  }

  public static String getSplashCardSuit()
      throws ParserConfigurationException, SAXException, IOException {
    // Create a new DocumentBuilderFactory instance
    Element rootElement = getXmlElement();

    return rootElement.getElementsByTagName("Suit").item(0).getTextContent();
  }

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

    // Create a new TransformerFactory instance
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    // Create a new Transformer instance
    Transformer transformer = transformerFactory.newTransformer();
    // Create a new DOMSource instance with the document
    DOMSource source = new DOMSource(document);

    // Create a new StreamResult instance with the file path
    File file = new File("Poker.xml");
    StreamResult result = new StreamResult(file);

    // Transform the DOMSource to the StreamResult
    transformer.transform(source, result);
  }
}
