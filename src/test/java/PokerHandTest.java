import java.util.ArrayList;
import no.ntnu.idatg2001.torgrilt.poker.enums.Hands;
import no.ntnu.idatg2001.torgrilt.poker.enums.Ranks;
import no.ntnu.idatg2001.torgrilt.poker.enums.Suits;
import no.ntnu.idatg2001.torgrilt.gui.scenes.GameFloor;
import no.ntnu.idatg2001.torgrilt.poker.Card;
import no.ntnu.idatg2001.torgrilt.poker.DeckOfCards;
import no.ntnu.idatg2001.torgrilt.poker.Poker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PokerHandTest {

  private DeckOfCards deck;

  private final ArrayList<Card[]> hands = new ArrayList<>();
  private final Card[] highCard = {new Card(Ranks.ACE, Suits.SPADES)};
  private final Card[] pair = {new Card(Ranks.ACE, Suits.SPADES), new Card(Ranks.ACE, Suits.CLUBS)};
  private final Card[] twoPair =
      {new Card(Ranks.ACE, Suits.SPADES), new Card(Ranks.ACE, Suits.CLUBS),
          new Card(Ranks.KING, Suits.SPADES), new Card(Ranks.KING, Suits.CLUBS)};
  private final Card[] tres = {new Card(Ranks.ACE, Suits.SPADES), new Card(Ranks.ACE, Suits.CLUBS),
      new Card(Ranks.ACE, Suits.DIAMONDS)};
  private final Card[] straight =
      {new Card(Ranks.TEN, Suits.SPADES), new Card(Ranks.JACK, Suits.CLUBS),
          new Card(Ranks.QUEEN, Suits.SPADES), new Card(Ranks.KING, Suits.CLUBS),
          new Card(Ranks.ACE, Suits.SPADES)};
  private final Card[] flush =
      {new Card(Ranks.SIX, Suits.SPADES), new Card(Ranks.EIGHT, Suits.SPADES),
          new Card(Ranks.TEN, Suits.SPADES), new Card(Ranks.QUEEN, Suits.SPADES),
          new Card(Ranks.ACE, Suits.SPADES)};
  private final Card[] house = {new Card(Ranks.ACE, Suits.SPADES), new Card(Ranks.ACE, Suits.CLUBS),
      new Card(Ranks.ACE, Suits.DIAMONDS),
      new Card(Ranks.KING, Suits.SPADES), new Card(Ranks.KING, Suits.CLUBS)};
  private final Card[] quad = {new Card(Ranks.ACE, Suits.SPADES), new Card(Ranks.ACE, Suits.CLUBS),
      new Card(Ranks.ACE, Suits.DIAMONDS), new Card(Ranks.ACE, Suits.HEARTS)};
  private final Card[] straightFlush =
      {new Card(Ranks.NINE, Suits.SPADES), new Card(Ranks.TEN, Suits.SPADES),
          new Card(Ranks.JACK, Suits.SPADES), new Card(Ranks.QUEEN, Suits.SPADES),
          new Card(Ranks.KING, Suits.SPADES)};
  private final Card[] royalStraightFlush =
      {new Card(Ranks.TEN, Suits.SPADES), new Card(Ranks.JACK, Suits.SPADES),
          new Card(Ranks.QUEEN, Suits.SPADES), new Card(Ranks.KING, Suits.SPADES),
          new Card(Ranks.ACE, Suits.SPADES)};

  @BeforeEach
  void setUp() {
    deck = new DeckOfCards();
    deck.shuffle();

    hands.add(highCard);
    hands.add(pair);
    hands.add(twoPair);
    hands.add(tres);
    hands.add(straight);
    hands.add(flush);
    hands.add(house);
    hands.add(quad);
    hands.add(straightFlush);
    hands.add(royalStraightFlush);
  }

  @AfterEach
  void tearDown() {
    hands.clear();
  }

  @Test
  void isDeckFull() {
    Assertions.assertEquals(52, deck.getDeckList().size());
  }

  @Test
  void testCardsCorrect() {
    Assertions.assertEquals(Hands.HIGH_CARD, Poker.getHand(highCard));
    Assertions.assertEquals(Hands.ONE_PAIR, Poker.getHand(pair));
    Assertions.assertEquals(Hands.TWO_PAIR, Poker.getHand(twoPair));
    Assertions.assertEquals(Hands.THREE_OF_A_KIND, Poker.getHand(tres));
    Assertions.assertEquals(Hands.STRAIGHT, Poker.getHand(straight));
    Assertions.assertEquals(Hands.FLUSH, Poker.getHand(flush));
    Assertions.assertEquals(Hands.FULL_HOUSE, Poker.getHand(house));
    Assertions.assertEquals(Hands.FOUR_OF_A_KIND, Poker.getHand(quad));
    Assertions.assertEquals(Hands.STRAIGHT_FLUSH, Poker.getHand(straightFlush));
    Assertions.assertEquals(Hands.ROYAL_STRAIGHT_FLUSH, Poker.getHand(royalStraightFlush));
  }

  @Test
  void testHandCombinationsPlayerWins() {
    hands.stream()
        .limit(hands.size() - 1)
        .forEachOrdered(p -> Assertions.assertEquals("Player Wins", whoWon(hands.get(hands.indexOf(p) + 1), p)));
  }

  @Test
  void testHandCombinationsTie() {
    hands.forEach(h -> Assertions.assertEquals("Tie", whoWon(h,h)));
  }

  String whoWon(Card[] playerHand, Card[] opponentHand) {
    if (Poker.getHandStrength(playerHand) > Poker.getHandStrength(opponentHand)) {
      return "Player Wins";
    } else if (Poker.getHandStrength(playerHand) < Poker.getHandStrength(opponentHand)) {
      return "Computer Wins";
    } else {
      return "Tie";
    }
  }

  @Test
  void isHighCard() {
    Card[] cards = {new Card(Ranks.ACE, Suits.SPADES), new Card(Ranks.TWO, Suits.HEARTS)};
    ArrayList<Card> board = new ArrayList<>();
    board.add(new Card(Ranks.FOUR, Suits.CLUBS));
    board.add(new Card(Ranks.FIVE, Suits.DIAMONDS));
    board.add(new Card(Ranks.QUEEN, Suits.DIAMONDS));
    board.add(new Card(Ranks.NINE, Suits.HEARTS));
    board.add(new Card(Ranks.JACK, Suits.CLUBS));
    GameFloor.setBoard(board);
    Hands hand =
        Poker.getHand(cards);
    Assertions.assertEquals(Hands.HIGH_CARD, hand);
  }

  @Test
  void isNotHighCard() {
    Card[] cards = {new Card(Ranks.ACE, Suits.SPADES), new Card(Ranks.TWO, Suits.HEARTS)};
    ArrayList<Card> board = new ArrayList<>();
    board.add(new Card(Ranks.FOUR, Suits.CLUBS));
    board.add(new Card(Ranks.FIVE, Suits.DIAMONDS));
    board.add(new Card(Ranks.ACE, Suits.DIAMONDS));
    board.add(new Card(Ranks.NINE, Suits.HEARTS));
    board.add(new Card(Ranks.JACK, Suits.CLUBS));
    GameFloor.setBoard(board);
    Hands hand =
        Poker.getHand(cards);
    Assertions.assertNotEquals(Hands.HIGH_CARD, hand);
  }

  @Test
  void isPair() {
    Card[] cards = {new Card(Ranks.ACE, Suits.SPADES), new Card(Ranks.KING, Suits.HEARTS)};
    ArrayList<Card> board = new ArrayList<>();
    board.add(new Card(Ranks.TWO, Suits.CLUBS));
    board.add(new Card(Ranks.ACE, Suits.DIAMONDS));
    board.add(new Card(Ranks.QUEEN, Suits.DIAMONDS));
    board.add(new Card(Ranks.NINE, Suits.HEARTS));
    board.add(new Card(Ranks.JACK, Suits.CLUBS));
    GameFloor.setBoard(board);
    Hands hand =
        Poker.getHand(cards);
    Assertions.assertEquals(Hands.ONE_PAIR, hand);
  }

  @Test
  void isNotPair() {
    Card[] cards = {new Card(Ranks.ACE, Suits.SPADES), new Card(Ranks.KING, Suits.HEARTS)};
    ArrayList<Card> board = new ArrayList<>();
    board.add(new Card(Ranks.KING, Suits.CLUBS));
    board.add(new Card(Ranks.ACE, Suits.DIAMONDS));
    board.add(new Card(Ranks.QUEEN, Suits.DIAMONDS));
    board.add(new Card(Ranks.NINE, Suits.HEARTS));
    board.add(new Card(Ranks.JACK, Suits.CLUBS));
    GameFloor.setBoard(board);
    Hands hand =
        Poker.getHand(cards);
    Assertions.assertNotEquals(Hands.ONE_PAIR, hand);
  }

  @Test
  void isTwoPair() {
    Card[] cards = {new Card(Ranks.ACE, Suits.SPADES), new Card(Ranks.KING, Suits.HEARTS)};
    ArrayList<Card> board = new ArrayList<>();
    board.add(new Card(Ranks.NINE, Suits.CLUBS));
    board.add(new Card(Ranks.ACE, Suits.DIAMONDS));
    board.add(new Card(Ranks.KING, Suits.DIAMONDS));
    board.add(new Card(Ranks.NINE, Suits.HEARTS));
    board.add(new Card(Ranks.JACK, Suits.CLUBS));
    GameFloor.setBoard(board);
    Hands hand =
        Poker.getHand(cards);
    Assertions.assertEquals(Hands.TWO_PAIR, hand);
  }

  @Test
  void isNotTwoPair() {
    Card[] cards = {new Card(Ranks.ACE, Suits.SPADES), new Card(Ranks.KING, Suits.HEARTS)};
    ArrayList<Card> board = new ArrayList<>();
    board.add(new Card(Ranks.NINE, Suits.CLUBS));
    board.add(new Card(Ranks.ACE, Suits.DIAMONDS));
    board.add(new Card(Ranks.KING, Suits.DIAMONDS));
    board.add(new Card(Ranks.NINE, Suits.HEARTS));
    board.add(new Card(Ranks.KING, Suits.CLUBS));
    GameFloor.setBoard(board);
    Hands hand =
        Poker.getHand(cards);
    Assertions.assertNotEquals(Hands.TWO_PAIR, hand);
  }

  @Test
  void isTres() {
    Card[] cards = {new Card(Ranks.ACE, Suits.SPADES), new Card(Ranks.ACE, Suits.HEARTS)};
    ArrayList<Card> board = new ArrayList<>();
    board.add(new Card(Ranks.ACE, Suits.DIAMONDS));
    board.add(new Card(Ranks.TWO, Suits.DIAMONDS));
    board.add(new Card(Ranks.THREE, Suits.CLUBS));
    board.add(new Card(Ranks.FIVE, Suits.CLUBS));
    board.add(new Card(Ranks.SEVEN, Suits.SPADES));
    GameFloor.setBoard(board);
    Hands hand =
        Poker.getHand(cards);
    Assertions.assertEquals(Hands.THREE_OF_A_KIND, hand);
  }

  @Test
  void isNotTres() {
    Card[] cards = {new Card(Ranks.ACE, Suits.SPADES), new Card(Ranks.ACE, Suits.HEARTS)};
    ArrayList<Card> board = new ArrayList<>();
    board.add(new Card(Ranks.ACE, Suits.DIAMONDS));
    board.add(new Card(Ranks.TWO, Suits.DIAMONDS));
    board.add(new Card(Ranks.THREE, Suits.CLUBS));
    board.add(new Card(Ranks.FIVE, Suits.CLUBS));
    board.add(new Card(Ranks.ACE, Suits.SPADES));
    GameFloor.setBoard(board);
    Hands hand =
        Poker.getHand(cards);
    Assertions.assertNotEquals(Hands.THREE_OF_A_KIND, hand);
  }

  @Test
  void isStraight() {
    Card[] cards = {new Card(Ranks.SIX, Suits.SPADES), new Card(Ranks.TWO, Suits.HEARTS)};
    ArrayList<Card> board = new ArrayList<>();
    board.add(new Card(Ranks.THREE, Suits.DIAMONDS));
    board.add(new Card(Ranks.FOUR, Suits.DIAMONDS));
    board.add(new Card(Ranks.FIVE, Suits.DIAMONDS));
    board.add(new Card(Ranks.SEVEN, Suits.CLUBS));
    board.add(new Card(Ranks.NINE, Suits.CLUBS));
    GameFloor.setBoard(board);
    Hands hand =
        Poker.getHand(cards);
    Assertions.assertEquals(Hands.STRAIGHT, hand);
  }

  @Test
  void isNotStraight() {
    Card[] cards = {new Card(Ranks.SIX, Suits.SPADES), new Card(Ranks.ACE, Suits.HEARTS)};
    ArrayList<Card> board = new ArrayList<>();
    board.add(new Card(Ranks.THREE, Suits.DIAMONDS));
    board.add(new Card(Ranks.FOUR, Suits.DIAMONDS));
    board.add(new Card(Ranks.FIVE, Suits.DIAMONDS));
    board.add(new Card(Ranks.JACK, Suits.CLUBS));
    board.add(new Card(Ranks.NINE, Suits.CLUBS));
    GameFloor.setBoard(board);
    Hands hand =
        Poker.getHand(cards);
    Assertions.assertNotEquals(Hands.STRAIGHT, hand);
  }

  @Test
  void isFlush() {
    Card[] cards = {new Card(Ranks.JACK, Suits.SPADES), new Card(Ranks.THREE, Suits.SPADES)};
    ArrayList<Card> board = new ArrayList<>();
    board.add(new Card(Ranks.FIVE, Suits.SPADES));
    board.add(new Card(Ranks.SEVEN, Suits.SPADES));
    board.add(new Card(Ranks.NINE, Suits.SPADES));
    board.add(new Card(Ranks.NINE, Suits.HEARTS));
    board.add(new Card(Ranks.NINE, Suits.CLUBS));
    GameFloor.setBoard(board);
    Hands hand =
        Poker.getHand(cards);
    Assertions.assertEquals(Hands.FLUSH, hand);
  }

  @Test
  void isNotFlush() {
    Card[] cards = {new Card(Ranks.JACK, Suits.SPADES), new Card(Ranks.THREE, Suits.SPADES)};
    ArrayList<Card> board = new ArrayList<>();
    board.add(new Card(Ranks.FIVE, Suits.SPADES));
    board.add(new Card(Ranks.SEVEN, Suits.SPADES));
    board.add(new Card(Ranks.NINE, Suits.DIAMONDS));
    board.add(new Card(Ranks.NINE, Suits.HEARTS));
    board.add(new Card(Ranks.NINE, Suits.CLUBS));
    GameFloor.setBoard(board);
    Hands hand =
        Poker.getHand(cards);
    Assertions.assertNotEquals(Hands.FLUSH, hand);
  }

  @Test
  void isHouse() {
    Card[] cards = {new Card(Ranks.ACE, Suits.SPADES), new Card(Ranks.KING, Suits.HEARTS)};
    ArrayList<Card> board = new ArrayList<>();
    board.add(new Card(Ranks.KING, Suits.SPADES));
    board.add(new Card(Ranks.ACE, Suits.DIAMONDS));
    board.add(new Card(Ranks.ACE, Suits.HEARTS));
    board.add(new Card(Ranks.TWO, Suits.CLUBS));
    board.add(new Card(Ranks.FOUR, Suits.CLUBS));
    GameFloor.setBoard(board);
    Hands hand =
        Poker.getHand(cards);
    Assertions.assertEquals(Hands.FULL_HOUSE, hand);
  }

  @Test
  void isNotHouse() {
    Card[] cards = {new Card(Ranks.ACE, Suits.SPADES), new Card(Ranks.KING, Suits.HEARTS)};
    ArrayList<Card> board = new ArrayList<>();
    board.add(new Card(Ranks.KING, Suits.SPADES));
    board.add(new Card(Ranks.ACE, Suits.DIAMONDS));
    board.add(new Card(Ranks.ACE, Suits.HEARTS));
    board.add(new Card(Ranks.ACE, Suits.CLUBS));
    board.add(new Card(Ranks.FOUR, Suits.CLUBS));
    GameFloor.setBoard(board);
    Hands hand =
        Poker.getHand(cards);
    Assertions.assertNotEquals(Hands.FULL_HOUSE, hand);
  }

  @Test
  void isQuad() {
    Card[] cards = {new Card(Ranks.ACE, Suits.SPADES), new Card(Ranks.ACE, Suits.HEARTS)};
    ArrayList<Card> board = new ArrayList<>();
    board.add(new Card(Ranks.ACE, Suits.DIAMONDS));
    board.add(new Card(Ranks.ACE, Suits.CLUBS));
    board.add(new Card(Ranks.KING, Suits.SPADES));
    board.add(new Card(Ranks.KING, Suits.HEARTS));
    board.add(new Card(Ranks.KING, Suits.CLUBS));
    GameFloor.setBoard(board);
    Hands hand =
        Poker.getHand(cards);
    Assertions.assertEquals(Hands.FOUR_OF_A_KIND, hand);
  }

  @Test
  void isNotQuad() {
    Card[] cards = {new Card(Ranks.ACE, Suits.SPADES), new Card(Ranks.ACE, Suits.HEARTS)};
    ArrayList<Card> board = new ArrayList<>();
    board.add(new Card(Ranks.ACE, Suits.DIAMONDS));
    board.add(new Card(Ranks.QUEEN, Suits.CLUBS));
    board.add(new Card(Ranks.KING, Suits.SPADES));
    board.add(new Card(Ranks.KING, Suits.HEARTS));
    board.add(new Card(Ranks.KING, Suits.CLUBS));
    GameFloor.setBoard(board);
    Hands hand =
        Poker.getHand(cards);
    Assertions.assertNotEquals(Hands.FOUR_OF_A_KIND, hand);
  }

  @Test
  void isStraightFlush() {
    Card[] cards = {new Card(Ranks.TWO, Suits.SPADES), new Card(Ranks.THREE, Suits.SPADES)};
    ArrayList<Card> board = new ArrayList<>();
    board.add(new Card(Ranks.FOUR, Suits.SPADES));
    board.add(new Card(Ranks.FIVE, Suits.HEARTS));
    board.add(new Card(Ranks.FIVE, Suits.SPADES));
    board.add(new Card(Ranks.SIX, Suits.SPADES));
    board.add(new Card(Ranks.SIX, Suits.DIAMONDS));
    GameFloor.setBoard(board);
    Hands hand =
        Poker.getHand(cards);
    Assertions.assertEquals(Hands.STRAIGHT_FLUSH, hand);
  }

  @Test
  void isNotStraightFlush() {
    Card[] cards = {new Card(Ranks.TWO, Suits.SPADES), new Card(Ranks.THREE, Suits.SPADES)};
    ArrayList<Card> board = new ArrayList<>();
    board.add(new Card(Ranks.FOUR, Suits.SPADES));
    board.add(new Card(Ranks.FIVE, Suits.SPADES));
    board.add(new Card(Ranks.SIX, Suits.HEARTS));
    board.add(new Card(Ranks.ACE, Suits.CLUBS));
    board.add(new Card(Ranks.QUEEN, Suits.SPADES));
    GameFloor.setBoard(board);
    Hands hand =
        Poker.getHand(cards);
    Assertions.assertNotEquals(Hands.STRAIGHT_FLUSH, hand);
  }

  @Test
  void isRoyalStraightFlush() {
    Card[] cards = {new Card(Ranks.ACE, Suits.SPADES), new Card(Ranks.KING, Suits.SPADES)};
    ArrayList<Card> board = new ArrayList<>();
    board.add(new Card(Ranks.QUEEN, Suits.SPADES));
    board.add(new Card(Ranks.JACK, Suits.SPADES));
    board.add(new Card(Ranks.TEN, Suits.SPADES));
    board.add(new Card(Ranks.TEN, Suits.HEARTS));
    board.add(new Card(Ranks.TEN, Suits.CLUBS));
    GameFloor.setBoard(board);
    Hands hand =
        Poker.getHand(cards);
    Assertions.assertEquals(Hands.ROYAL_STRAIGHT_FLUSH, hand);
  }

  @Test
  void isNotRoyalStraightFlush() {
    Card[] cards = {new Card(Ranks.TEN, Suits.SPADES), new Card(Ranks.JACK, Suits.SPADES)};
    ArrayList<Card> board = new ArrayList<>();
    board.add(new Card(Ranks.QUEEN, Suits.SPADES));
    board.add(new Card(Ranks.KING, Suits.SPADES));
    board.add(new Card(Ranks.ACE, Suits.CLUBS));
    board.add(new Card(Ranks.TEN, Suits.HEARTS));
    board.add(new Card(Ranks.TEN, Suits.DIAMONDS));
    GameFloor.setBoard(board);
    Hands hand =
        Poker.getHand(cards);
    Assertions.assertNotEquals(Hands.ROYAL_STRAIGHT_FLUSH, hand);
  }
}
