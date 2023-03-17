package no.ntnu.idatg2001.torgrilt.gui.elements3d;

import java.util.List;
import java.util.Random;
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

@UtilityClass
public class Card3D {

  private final Random rand = new Random();
  private RotateTransition rotate;
  private static double mouseX = 0;
  private static double mouseY = 0;

  public static Group getDeck(List<Card> cards) {
    Group deckOfCard = new Group();
    deckOfCard.setDepthTest(DepthTest.ENABLE);
    int i = 1;
    for (Card card : cards) {
      Group temp = assembleCard(card);
      temp.setTranslateZ(GlobalElements.getCardDepth() * i);
      deckOfCard.getChildren().add(temp);
      i++;
    }
    return deckOfCard;
  }

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
      Image backSideImage = new Image("playingCards/backFace/WhiteLarge.png");

      // create materials for the front and back boxes
      PhongMaterial backSideMaterial = getMaterial(backSideImage);

      Box cardOne = (Box) cardGroup1.getChildren().get(1);
      cardOne.setMaterial(backSideMaterial);
      Box cardTwo = (Box) cardGroup2.getChildren().get(1);
      cardTwo.setMaterial(backSideMaterial);
    });

    return hand;
  }

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

  public static void updateSplashCard() {
    Group card = MainMenu.getCard();

    Image frontImage = getFrontImage();
    PhongMaterial frontMaterial = getMaterial(frontImage);

    Box cardFace = (Box) card.getChildren().get(0);
    cardFace.setMaterial(frontMaterial);
  }

  public static Group getSplashCard(Stage stage) {
    Image frontImage = getFrontImage();
    PhongMaterial frontMaterial = getMaterial(frontImage);

    Box front =
        new Box(GlobalElements.getCardWidth(), GlobalElements.getCardHeight(),
            GlobalElements.getCardDepth() / 2);
    front.setTranslateZ(0);
    front.setMaterial(frontMaterial);

    Image backImage = new Image("playingCards/backFace/WhiteLarge.png");
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

      if (settingCardSuit.equals("Random") && settingCardRank.equals("Random")) {
        int indexRank = rand.nextInt(rank.length);
        String cardRank = rank[indexRank];

        int indexSuit = rand.nextInt(suit.length);
        String cardSuit = suit[indexSuit];

        animatedCard = "playingCards/" + cardSuit + "/" + cardRank + ".png";
      } else if (!settingCardSuit.equals("Random") && settingCardRank.equals("Random")) {
        int indexRank = rand.nextInt(rank.length);
        String cardRank = rank[indexRank];

        animatedCard = "playingCards/" + settingCardSuit + "/" + cardRank + ".png";
      } else if (settingCardSuit.equals("Random")) {
        int indexSuit = rand.nextInt(suit.length);
        String cardSuit = suit[indexSuit];

        animatedCard = "playingCards/" + cardSuit + "/" + settingCardRank + ".png";
      } else {
        animatedCard = "playingCards/" + settingCardSuit + "/" + settingCardRank + ".png";
      }

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

    Image backSideImage = new Image("playingCards/backFace/WhiteLarge.png");
    PhongMaterial backMaterial = getMaterial(backSideImage);

    Box cardBack =
        new Box(GlobalElements.getCardWidth() / 2.25, GlobalElements.getCardHeight() / 2.25,
            GlobalElements.getCardDepth() / 2);
    cardBack.setTranslateZ(0);
    cardBack.setMaterial(backMaterial);

    // create 3D boxes for the front and back images
    return new Group(card, cardBack);
  }

  public static PhongMaterial getMaterial(Image frontImage) {
    PhongMaterial frontMaterial = new PhongMaterial();
    frontMaterial.setDiffuseMap(frontImage);
    frontMaterial.setDiffuseColor(Color.rgb(255, 255, 255));
    frontMaterial.setSpecularColor(Color.WHITE);
    frontMaterial.setSpecularPower(30);
    return frontMaterial;
  }
}
