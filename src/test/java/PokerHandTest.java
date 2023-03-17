import java.util.ArrayList;
import no.ntnu.idatg2001.torgrilt.gui.scenes.GameFloor;
import no.ntnu.idatg2001.torgrilt.poker.Card;
import no.ntnu.idatg2001.torgrilt.poker.Poker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PokerHandTest {
  @Test
  void isHighCard() {
    Card[] cards = {new Card("Ace", "Spades"), new Card("Two", "Hearts")};
    String opponentHand =
        Poker.getHand(cards);
    Assertions.assertEquals("High Card", opponentHand);
  }

  @Test
  void isPair() {
    Card[] cards = {new Card("Ace", "Spades"), new Card("King", "Hearts")};
    ArrayList<Card> board = new ArrayList<>();
    board.add(new Card("Two", "Clubs"));
    board.add(new Card("Ace", "Diamonds"));
    board.add(new Card("Queen", "Diamonds"));
    board.add(new Card("Nine", "Hearts"));
    board.add(new Card("Jack", "Clubs"));
    GameFloor.setBoard(board);
    String opponentHand =
        Poker.getHand(cards);
    Assertions.assertEquals("One Pair", opponentHand);
  }

  @Test
  void isTwoPair() {
    Card[] cards = {new Card("Ace", "Spades"), new Card("King", "Hearts")};
    ArrayList<Card> board = new ArrayList<>();
    board.add(new Card("Nine", "Clubs"));
    board.add(new Card("Ace", "Diamonds"));
    board.add(new Card("King", "Diamonds"));
    board.add(new Card("Nine", "Hearts"));
    board.add(new Card("Jack", "Clubs"));
    GameFloor.setBoard(board);
    String opponentHand =
        Poker.getHand(cards);
    Assertions.assertEquals("Two Pairs", opponentHand);
  }

  @Test
  void isTres() {
    Card[] cards = {new Card("Ace", "Spades"), new Card("Ace", "Hearts")};
    ArrayList<Card> board = new ArrayList<>();
    board.add(new Card("Ace", "Diamonds"));
    board.add(new Card("Two", "Diamonds"));
    board.add(new Card("Three", "Diamonds"));
    GameFloor.setBoard(board);
    String opponentHand =
        Poker.getHand(cards);
    Assertions.assertEquals("Three of a Kind", opponentHand);
  }

  @Test
  void isStraight() {
    Card[] cards = {new Card("Six", "Spades"), new Card("Two", "Hearts")};
    ArrayList<Card> board = new ArrayList<>();
    board.add(new Card("Three", "Diamonds"));
    board.add(new Card("Four", "Diamonds"));
    board.add(new Card("Five", "Diamonds"));
    GameFloor.setBoard(board);
    String opponentHand =
        Poker.getHand(cards);
    Assertions.assertEquals("Straight", opponentHand);
  }

  @Test
  void isFlush() {
    Card[] cards = {new Card("Jack", "Spades"), new Card("Three", "Spades")};
    ArrayList<Card> board = new ArrayList<>();
    board.add(new Card("Five", "Spades"));
    board.add(new Card("Seven", "Spades"));
    board.add(new Card("Nine", "Spades"));
    GameFloor.setBoard(board);
    String opponentHand =
        Poker.getHand(cards);
    Assertions.assertEquals("Flush", opponentHand);
  }

  @Test
  void isHouse() {
    Card[] cards = {new Card("Ace", "Spades"), new Card("King", "Hearts")};
    ArrayList<Card> board = new ArrayList<>();
    board.add(new Card("King", "Spades"));
    board.add(new Card("Ace", "Diamonds"));
    board.add(new Card("Ace", "Hearts"));
    board.add(new Card("Two", "Clubs"));
    board.add(new Card("Four", "Clubs"));
    GameFloor.setBoard(board);
    String opponentHand =
        Poker.getHand(cards);
    Assertions.assertEquals("Full House", opponentHand);
  }

  @Test
  void isQuad() {
    Card[] cards = {new Card("Ace", "Spades"), new Card("Ace", "Hearts")};
    ArrayList<Card> board = new ArrayList<>();
    board.add(new Card("Ace", "Diamonds"));
    board.add(new Card("Ace", "Clubs"));
    board.add(new Card("King", "Spades"));
    GameFloor.setBoard(board);
    String opponentHand =
        Poker.getHand(cards);
    Assertions.assertEquals("Four of a Kind", opponentHand);
  }

  @Test
  void isStraightFlush() {
    Card[] cards = {new Card("Two", "Spades"), new Card("Three", "Spades")};
    ArrayList<Card> board = new ArrayList<>();
    board.add(new Card("Four", "Spades"));
    board.add(new Card("Five", "Hearts"));
    board.add(new Card("Five", "Spades"));
    board.add(new Card("Six", "Spades"));
    board.add(new Card("Six", "Diamonds"));
    GameFloor.setBoard(board);
    String opponentHand =
        Poker.getHand(cards);
    Assertions.assertEquals("Straight Flush", opponentHand);
  }

  @Test
  void isNotStraightFlush() {
    Card[] cards = {new Card("Two", "Spades"), new Card("Three", "Spades")};
    ArrayList<Card> board = new ArrayList<>();
    board.add(new Card("Four", "Spades"));
    board.add(new Card("Five", "Spades"));
    board.add(new Card("Six", "Hearts"));
    board.add(new Card("Ace", "Clubs"));
    board.add(new Card("Queen", "Spades"));
    GameFloor.setBoard(board);
    String opponentHand =
        Poker.getHand(cards);
    Assertions.assertEquals("Flush", opponentHand);
  }

  @Test
  void isRoyalStraightFlush() {
    Card[] cards = {new Card("Ace", "Spades"), new Card("King", "Spades")};
    ArrayList<Card> board = new ArrayList<>();
    board.add(new Card("Queen", "Spades"));
    board.add(new Card("Jack", "Spades"));
    board.add(new Card("Ten", "Spades"));
    board.add(new Card("Ten", "Hearts"));
    board.add(new Card("Ten", "Clubs"));
    GameFloor.setBoard(board);
    String opponentHand =
        Poker.getHand(cards);
    Assertions.assertEquals("Royal Straight Flush", opponentHand);
  }

  @Test
  void isNotRoyalStraightFlush() {
    Card[] cards = {new Card("Ten", "Spades"), new Card("Jack", "Spades")};
    ArrayList<Card> board = new ArrayList<>();
    board.add(new Card("Queen", "Spades"));
    board.add(new Card("King", "Spades"));
    board.add(new Card("Ace", "Clubs"));
    board.add(new Card("Ten", "Hearts"));
    board.add(new Card("Ten", "Diamonds"));
    GameFloor.setBoard(board);
    String opponentHand =
        Poker.getHand(cards);
    Assertions.assertEquals("Straight", opponentHand);
  }
}
