package no.ntnu.idatg2001.torgrilt.gui.elements3d;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.experimental.UtilityClass;
import no.ntnu.idatg2001.torgrilt.gui.globalelements.GlobalElements;
import no.ntnu.idatg2001.torgrilt.gui.scenes.MainMenu;
import no.ntnu.idatg2001.torgrilt.gui.utilities.XmlSettings;
import no.ntnu.idatg2001.torgrilt.poker.Card;

/**
 * > A Card3D is a 3D object that represents a playing card.
 */
@UtilityClass
public class Card3D {

  private final Random rand = new Random();
  private RotateTransition rotate;
  private static double mouseX = 0;
  private static double mouseY = 0;

  private final String backFace = "playingCards/backFace/WhiteLarge.png";
  private final String randString = "Random";
  private final String cardImageFolder = "playingCards/";

  /**
   * It takes a list of cards and returns a group of cards.
   *
   * @param cards The list of cards to be displayed.
   * @return A group of cards.
   */
  public static Group getDeck(List<Card> cards) {
    Group deckOfCard = new Group();
    deckOfCard.setDepthTest(DepthTest.ENABLE);
    IntStream.range(0, cards.size()).mapToObj(i -> {
      Card card = cards.get(i);
      Group temp = assembleCard(card);
      temp.setTranslateZ(GlobalElements.getCardDepth() * i);
      return temp;
    }).forEach(deckOfCard.getChildren()::add);
    return deckOfCard;
  }

  /**
   * It creates a group of two cards, one for each of the player's cards,
   * and adds mouse listeners to the group so that
   * when the mouse enters the group, the cards flip over to show their front side,
   * and when the mouse exits the group, the
   * cards flip back over to show their back side.
   *
   * @param playerHand1 The first card in the player's hand.
   * @param playerHand2 The second card in the player's hand.
   * @param stage       the stage that the hand will be displayed on
   * @return A Group object that contains two cards.
   */
  public static Group getPlayerHand(Card playerHand1, Card playerHand2, Stage stage) {
    Group hand = getOpponentHand(playerHand1, playerHand2);

    Group cardGroup1 = (Group) hand.getChildren().get(0);
    Group cardGroup2 = (Group) hand.getChildren().get(1);

    stage.addEventFilter(MouseEvent.MOUSE_MOVED, event -> {
      mouseX = event.getSceneX();
      mouseY = event.getSceneY();
      updateBoxTransform(hand);
    });

    hand.setOnMouseEntered(event -> {
      Image frontImage1 = new Image(playerHand1.getCardImage());

      // create materials for the front and back boxes
      PhongMaterial frontMaterial1 = new PhongMaterial();
      frontMaterial1.setDiffuseMap(frontImage1);
      frontMaterial1.setSpecularColor(Color.WHITE);
      frontMaterial1.setSpecularPower(30);

      Image frontImage2 = new Image(playerHand2.getCardImage());
      PhongMaterial frontMaterial2 = new PhongMaterial();
      frontMaterial2.setDiffuseMap(frontImage2);
      frontMaterial2.setSpecularColor(Color.WHITE);
      frontMaterial2.setSpecularPower(30);

      Box cardOne = (Box) cardGroup1.getChildren().get(1);
      cardOne.setMaterial(frontMaterial1);
      Box cardTwo = (Box) cardGroup2.getChildren().get(1);
      cardTwo.setMaterial(frontMaterial2);
    });

    hand.setOnMouseExited(event -> {
      Image backSideImage = new Image(backFace);

      // create materials for the front and back boxes
      PhongMaterial backSideMaterial = getMaterial(backSideImage);

      Box cardOne = (Box) cardGroup1.getChildren().get(1);
      cardOne.setMaterial(backSideMaterial);
      Box cardTwo = (Box) cardGroup2.getChildren().get(1);
      cardTwo.setMaterial(backSideMaterial);
    });

    return hand;
  }

  /**
   * It takes two cards, assembles them into a group, and then returns that group.
   *
   * @param playerHand1 The first card in the player's hand.
   * @param playerHand2 The card that is on top of the deck
   * @return A group of two cards.
   */
  public static Group getOpponentHand(Card playerHand1, Card playerHand2) {
    Group card1 = assembleCard(playerHand1);
    card1.setDepthTest(DepthTest.ENABLE);
    card1.setTranslateX(15);
    card1.setRotate(22.5);

    Group card2 = assembleCard(playerHand2);
    card2.setDepthTest(DepthTest.ENABLE);
    card2.setTranslateZ(GlobalElements.getCardDepth());
    card2.setTranslateX(-15);

    return new Group(card1, card2);
  }


  /**
   * "Update the front of the card to show the current image."
   *
   * <p>.
   * The first line of the function gets the card from the MainMenu class
   */
  public static void updateSplashCard() {
    Group card = MainMenu.getCard();

    Image frontImage = getFrontImage();
    PhongMaterial frontMaterial = getMaterial(frontImage);

    Box cardFace = (Box) card.getChildren().get(0);
    cardFace.setMaterial(frontMaterial);
  }

  /**
   * It creates a 3D card with a front and back face,
   * and then rotates the card based on the mouse's position.
   *
   * @param stage The stage that the card will be displayed on.
   * @return A Group object that contains a Box object for the front and back of the card.
   */
  public static Group getSplashCard(Stage stage) {
    Image frontImage = getFrontImage();
    PhongMaterial frontMaterial = getMaterial(frontImage);

    Box front =
        new Box(GlobalElements.getCardWidth(), GlobalElements.getCardHeight(),
            GlobalElements.getCardDepth() / 2);
    front.setTranslateZ(0);
    front.setMaterial(frontMaterial);

    Image backImage = new Image(backFace);
    PhongMaterial backMaterial = getMaterial(backImage);

    Box back =
        new Box(GlobalElements.getCardWidth(), GlobalElements.getCardHeight(),
            GlobalElements.getCardDepth() / 2);
    back.setTranslateZ(2);
    back.setMaterial(backMaterial);

    // create 3D boxes for the front and back images
    Group card = new Group(front, back);
    card.setDepthTest(DepthTest.ENABLE);

    stage.addEventFilter(MouseEvent.MOUSE_MOVED, event -> {
      mouseX = event.getSceneX();
      mouseY = event.getSceneY();
      updateBoxTransform(card);
    });

    return card;
  }

  /**
   * Rotate the card around the Y axis for 5 seconds, indefinitely.
   *
   * @param card The card to be animated.
   */
  public static void animate3dCard(Group card) {
    rotate = new RotateTransition(Duration.millis(5000));
    rotate.setNode(card);
    rotate.setAxis(Rotate.Y_AXIS);
    rotate.setInterpolator(Interpolator.LINEAR);
    rotate.setFromAngle(0);
    rotate.setToAngle(360);
    rotate.setCycleCount(Animation.INDEFINITE);
    rotate.play();
  }

  private static void updateBoxTransform(Group card) {
    double centerX = GlobalElements.getSceneWidth() / 2;
    double centerY = GlobalElements.getSceneWidth() / 2;
    double translationFactor = 0.0025;
    double deltaX = -(mouseX - centerX) * translationFactor;
    double deltaY = -(mouseY - centerY) * translationFactor;

    // Apply a subtle translation to the box
    card.getTransforms().clear();
    card.getTransforms().add(new Translate(deltaX, deltaY, 0));

    // Apply a subtle rotation to the box
    double rotationFactor = 0.025;
    double angleX = -(mouseY - centerY) * rotationFactor;
    double angleY = (mouseX - centerX) * rotationFactor;
    card.getTransforms().add(new Rotate(angleX, Rotate.X_AXIS));
    card.getTransforms().add(new Rotate(angleY, Rotate.Y_AXIS));
  }

  public static void setAnimationSpeed(int speed) {
    rotate.setRate(speed);
  }

  private static Image getFrontImage() {
    String animatedCard = "";
    try {

      String settingCardSuit = XmlSettings.getSplashCardSuit();
      String settingCardRank = XmlSettings.getSplashCardRank();

      String[] rank = {
          "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen",
          "King", "Ace"
      };
      String[] suit = {"Clubs", "Diamonds", "Hearts", "Spades"};

      animatedCard = settingCardSuit.equals(randString) && settingCardRank.equals(randString)
          ? cardImageFolder + suit[rand.nextInt(suit.length)]
            + "/" + rank[rand.nextInt(rank.length)] + ".png"
          : !settingCardSuit.equals(randString) && settingCardRank.equals(randString)
          ? cardImageFolder + settingCardSuit + "/" + rank[rand.nextInt(rank.length)] + ".png"
          : settingCardSuit.equals(randString)
          ? cardImageFolder + suit[rand.nextInt(suit.length)] + "/" + settingCardRank + ".png"
          : cardImageFolder + settingCardSuit + "/" + settingCardRank + ".png";

    } catch (Exception e) {
      e.printStackTrace();
    }
    return new Image(animatedCard);
  }

  private Group assembleCard(Card drawnCard) {
    Image frontImage = new Image(drawnCard.getCardImage());

    // create materials for the front and back boxes
    PhongMaterial frontMaterial = getMaterial(frontImage);

    Box card = new Box(GlobalElements.getCardWidth() / 2.25, GlobalElements.getCardHeight() / 2.25,
        GlobalElements.getCardDepth() / 2);
    card.setTranslateZ(GlobalElements.getCardDepth() / 2);
    card.setMaterial(frontMaterial);

    Image backSideImage = new Image(backFace);
    PhongMaterial backMaterial = getMaterial(backSideImage);

    Box cardBack =
        new Box(GlobalElements.getCardWidth() / 2.25, GlobalElements.getCardHeight() / 2.25,
            GlobalElements.getCardDepth() / 2);
    cardBack.setTranslateZ(0);
    cardBack.setMaterial(backMaterial);

    // create 3D boxes for the front and back images
    return new Group(card, cardBack);
  }

  /**
   * It creates a PhongMaterial object, sets its diffuse map to the image passed in,
   * sets its diffuse color to white,
   * sets its specular color to white, and sets its specular power to 30.
   *
   * @param frontImage The image to be used as the front of the card.
   * @return A PhongMaterial object.
   */
  public static PhongMaterial getMaterial(Image frontImage) {
    PhongMaterial frontMaterial = new PhongMaterial();
    frontMaterial.setDiffuseMap(frontImage);
    frontMaterial.setDiffuseColor(Color.rgb(255, 255, 255));
    frontMaterial.setSpecularColor(Color.WHITE);
    frontMaterial.setSpecularPower(30);
    return frontMaterial;
  }
}
