package org.gruppe2.game.old;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.List;

/**
 *
 */
public class CardsTest  {

    @Test
    public void ReturnedListHasValidSuits(){
        List<Card> cards = Cards.asList("1H 4D JS");

        assertEquals(Card.Suit.HEARTS, cards.get(0).getSuit());
        assertEquals(Card.Suit.DIAMONDS, cards.get(1).getSuit());
        assertEquals(Card.Suit.SPADES, cards.get(2).getSuit());

    }

    @Test
    public void ReturnedListHasValidValues(){
        List<Card> cards = Cards.asList("1H 4D JS");
        assertEquals(10, cards.get(0).getFaceValue());
        assertEquals(4, cards.get(1).getFaceValue());
        assertEquals(Card.JACK, cards.get(2).getFaceValue());

    }

    @Test
    public void InvalidInputCausesNullReturn(){
        List<Card> cards = Cards.asList("6D KD 10H");
        assertEquals(null, cards);
    }
}