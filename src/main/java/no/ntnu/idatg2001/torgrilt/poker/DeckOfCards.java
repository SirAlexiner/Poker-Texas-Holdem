package no.ntnu.idatg2001.torgrilt.poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeckOfCards {

  String[] rank = {
      "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen",
      "King", "Ace"};
  String[] suit = {"Clubs", "Diamonds", "Hearts", "Spades"};
  ArrayList<Card> deck = new ArrayList<>(52);

  public DeckOfCards() {
    for (String cardSuit : suit) {
      for (String cardRank : rank) {
        Card card = new Card(cardRank, cardSuit);
        deck.add(card);
      }
    }
  }

  public void shuffle() {
    Collections.shuffle(deck);
  }

  public List<Card> draw(int amount) {
    ArrayList<Card> drawn = new ArrayList<>();
    for (int i = 0; i < amount; i++) {
      drawn.add(deck.remove(0));
    }
    return drawn;
  }
}
