package org.gruppe2.game.Calculation;

import org.gruppe2.game.old.Card;
import org.gruppe2.game.old.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by Mikal on 11.04.2016.
 */
public class Flush {

    public boolean canGetFlush(ArrayList<Card> communityCard, Player p){
        return false;
    }

    public static HashMap<Card.Suit, Integer> numberOfEachType (Collection<Card> allCards){
        HashMap<Card.Suit, Integer> numTypes = new HashMap<>();
        numTypes.put(Card.Suit.CLUBS, 0);
        numTypes.put(Card.Suit.DIAMONDS, 0);
        numTypes.put(Card.Suit.SPADES, 0);
        numTypes.put(Card.Suit.HEARTS, 0);

        for(Card c : allCards)
            numTypes.put(c.getSuit(), numTypes.get(c.getSuit()) + 1);

        return numTypes;
    }
}
