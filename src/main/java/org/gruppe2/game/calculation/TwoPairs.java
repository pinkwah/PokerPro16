package org.gruppe2.game.calculation;

import java.util.ArrayList;
import java.util.List;

import org.gruppe2.game.Card;
import org.gruppe2.game.Hand;

class TwoPairs implements HandCalculation {

    @Override
    public boolean isHand(List<Card> cards) {
    	if(cards == null || getBestHandCards(cards).isEmpty())
    		return false;
    	
    	return true;
    }

    @Override
    public List<Card> getBestCards(List<Card> cards) {
    	ArrayList<Card> getBestCards = new ArrayList<>();
        HighCard highCard = new HighCard();
        List<Card> pureTwoPairsCards = getBestHandCards(cards);
        
        cards.removeAll(pureTwoPairsCards);
        
        List<Card> highCards = highCard.getBestCards(cards);
        
        getBestCards.addAll(pureTwoPairsCards);
        for(int i = highCards.size()-1; i >= 0; i--) {
        	getBestCards.add(highCards.get(i));
        	if(getBestCards.size() >= 5)
        		break;
        }
        
        return getBestCards;
    }

    @Override
    public boolean canGet(List<Card> cards) {
        if (cards == null || cards.size() == 2)
            return true;

        int amountOfSameFace = Generic.amountOfSameFace(cards);

        if(cards.size() <=5)
            return true;
        if(cards.size() == 6 && amountOfSameFace >=2)
            return true;
        if(cards.size() == 7)
            if(Generic.recurringFaceValues(cards).size() >=4)
                return true;


        return false;
    }

    @Override
    public double probability(List<Card> cards) {
        return 0;
    }

    @Override
    public Hand getType() {
        return Hand.TWOPAIRS;
    }

    /**
	 * Assumes both o1 and o2 are TwoPairs excluding Highcards.
	 * @return int (1, 0, -1).
	 */
    @Override
    public int compare(List<Card> o1, List<Card> o2) {
    	Pair pair = new Pair();
		
		List<Card> bestTwoPair_1 = pair.getBestHandCards(o1);
		List<Card> bestTwoPair_2 = pair.getBestHandCards(o2);
		
		int compareBestPair = pair.compare(bestTwoPair_1, bestTwoPair_2);
		if(compareBestPair != 0) // If they have the same top pair, continue...
			return compareBestPair;
		
		o1.removeAll(bestTwoPair_1);
		o2.removeAll(bestTwoPair_2);
		
		List<Card> copy_hand_1 = Generic.copyListOfCards(o1);
		List<Card> copy_hand_2 = Generic.copyListOfCards(o2);
		
		return pair.compare(copy_hand_1, copy_hand_2);
    }

	@Override
	public List<Card> getBestHandCards(List<Card> cards) {
		ArrayList<Card> listOfCardsInTwoPair = new ArrayList<>();

		Pair pair = new Pair();
		
		List<Card> highestPair = pair.getBestHandCards(cards); // Get the highest
																// pair of cards
		cards.removeAll(highestPair);
		
		List<Card> lowestPair = pair.getBestHandCards(cards);
				
		// If we found two pairs, add them, if not, then we return an empty list
		if (!highestPair.isEmpty() && !lowestPair.isEmpty()) {
			listOfCardsInTwoPair.addAll(highestPair);
			listOfCardsInTwoPair.addAll(lowestPair);
		}

		return listOfCardsInTwoPair;
	}
}
