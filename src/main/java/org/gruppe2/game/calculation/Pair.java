package org.gruppe2.game.calculation;

import java.util.List;

import org.gruppe2.game.Card;
import org.gruppe2.game.Hand;

class Pair implements HandCalculation {

    @Override
    public boolean isHand(List<Card> cards) {
        return false;
    }

    @Override
    public List<Card> getBestCards(List<Card> cards) {
        return null;
    }

    @Override
    public boolean canGet(List<Card> cards) {
        if (cards == null || cards.size() == 2)
            return true;

        int amountOfSameFace = Generic.amountOfSameFace(cards);
        if(cards.size() == 7 &&  amountOfSameFace < 2)
            return false;
        return true;
    }

    @Override
    public double probability(List<Card> cards) {
        return 0;
    }

    @Override
    public Hand getType() {
        return Hand.PAIR;
    }

    /**
	 * Assumes both o1 and o2 are pairs.
	 * @return int (1, 0, -1).
	 */
    @Override
    public int compare(List<Card> o1, List<Card> o2) {
        return Integer.compare(Generic.calculateFacevalueOfAllCards(o1), Generic.calculateFacevalueOfAllCards(o2));
    }
}
