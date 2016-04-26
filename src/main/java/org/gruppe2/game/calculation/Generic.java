package org.gruppe2.game.calculation;

import org.gruppe2.game.Card;
import org.gruppe2.game.Hand;
import org.gruppe2.game.Player;
import org.gruppe2.game.RoundPlayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by Åsmund on 26/04/2016.
 */
public class Generic {
    public static ArrayList<Integer> recurringFaceValues(Collection<Card> cards) {
        ArrayList<Integer> recurringFaceValues = new ArrayList<Integer>();

        HashMap<Integer, Integer> hashMapCards = new HashMap<Integer, Integer>();

        for (Card card: cards) {
            int faceValue = card.getFaceValue();
            if (hashMapCards.containsKey(faceValue))
                hashMapCards.put(faceValue, hashMapCards.get(faceValue) + 1);

            else hashMapCards.put(faceValue, 1);

        }
        for(int i= 2; i < Card.ACE +1; i++){
            if(hashMapCards.containsKey(i)){
                if(hashMapCards.get(i) > 1)
                    for(int j = 0; j < hashMapCards.get(i); j++){
                        recurringFaceValues.add(i);
                    }
            }
        }
        return recurringFaceValues;
    }



    public static int amountOfSameFace(Collection<Card> cards){
        HashMap<Integer, Integer> amountCards = new HashMap<Integer, Integer>();
        int amountOfSameKind = 1;



        for(Card card: cards){
            int faceValue = card.getFaceValue();
            if(amountCards.containsKey(faceValue)){
                int amountOfCard = amountCards.get(faceValue) +1;
                amountCards.put(faceValue, amountOfCard);
                if(amountOfSameKind < amountOfCard)
                    amountOfSameKind = amountOfCard;
            }

            else
                amountCards.put(faceValue, 1);
        }
        return amountOfSameKind;
    }

    /**
     * A list over all types of hands to check probability and/or possibility.
     * List is sorted by hand rank
     * @return
     */
    public static ArrayList<HandCalculation> getAllHandTypes(){
        ArrayList<HandCalculation> hands = new ArrayList<>();
        hands.add(new RoyalFlush());
        hands.add(new StraightFlush());
        hands.add(new FourOfAKind());
        hands.add(new FullHouse());
        hands.add(new Flush());
        hands.add(new Straight());
        hands.add(new ThreeOfAKind());
        hands.add(new TwoPairs());
        hands.add(new Pair());

        return hands;
    }

    public static HashMap<Card.Suit, Integer> numberOfEachSuit(Collection<Card> cards){
        HashMap<Card.Suit, Integer> numTypes = new HashMap<>();
        numTypes.put(Card.Suit.CLUBS, 0);
        numTypes.put(Card.Suit.DIAMONDS, 0);
        numTypes.put(Card.Suit.SPADES, 0);
        numTypes.put(Card.Suit.HEARTS, 0);

        for(Card c : cards)
            numTypes.put(c.getSuit(), numTypes.get(c.getSuit()) + 1);

        return numTypes;
    }

    public static Hand getBestHandForPlayer(Collection<Card> cards) {
        for (HandCalculation hand : getAllHandTypes())
            if (hand.canGet(cards))
                return hand.getType();
        return Hand.HIGHCARD;
    }

    public static HashMap<Hand, Boolean> getAllPossibleHandsForPlayer (Collection<Card> cards){
        HashMap<Hand, Boolean> types = new HashMap<>();

        return types;
    }

    public static double choose(int n, int k) {
        if (k < 0 || k > n) return 0;
        if (k > n/2) {
            // choose(n,k) == choose(n,n-k),
            // so this could save a little effort
            k = n - k;
        }

        double denominator = 1.0, numerator = 1.0;
        for (int i = 1; i <= k; i++) {
            denominator *= i;
            numerator *= (n + 1 - i);
        }
        return numerator / denominator;
    }
}
