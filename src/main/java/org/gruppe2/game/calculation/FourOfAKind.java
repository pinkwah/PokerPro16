package org.gruppe2.game.calculation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.gruppe2.game.Card;
import org.gruppe2.game.Hand;

class FourOfAKind implements HandCalculation {

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
        List<Card> pureFourOfAKindCards = getBestHandCards(cards);
        
        cards.removeAll(pureFourOfAKindCards);
        
        List<Card> highCards = highCard.getBestCards(cards);
        
        getBestCards.addAll(pureFourOfAKindCards);
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

        if(cards.size() < 5)
            return true;
        if(cards.size() == 5 &&  amountOfSameFace >=2)
            return true;
        if(cards.size() == 6 && amountOfSameFace >=3)
            return true;
        if (cards.size() == 7 && amountOfSameFace == 4)
            return true;

        return false;
    }

    @Override
    public double probability(List<Card> cards) {
        return 0;
    }

    @Override
    public Hand getType() {
        return Hand.FOUROFAKIND;
    }

    /**
	 * Assumes both o1 and o2 are FourOfAKind excluding the Highcard.
	 * @return int (1, 0, -1).
	 */
    @Override
    public int compare(List<Card> o1, List<Card> o2) {
    	return Integer.compare(Generic.calculateFacevalueOfAllCards(o1), Generic.calculateFacevalueOfAllCards(o2));
    }

	@Override
	public List<Card> getBestHandCards(List<Card> cards) {
		ArrayList<Card> listOfCardsInFourOfAKind = new ArrayList<>();

		HashMap<Integer, Integer> recurringFaceValues = Generic.recurringFaceValuesMap(cards);
		int highestCardValue = -1;
		
		for(int i= Card.ACE; i >= 2; i--){
            if(recurringFaceValues.containsKey(i)){
                if(recurringFaceValues.get(i) > 3) {
                	highestCardValue = i;
                	break;
                }
                    
            }
        }
		
		if(highestCardValue > 1) {
			for(Card c : cards) {
				if(c.getFaceValue() == highestCardValue) {
					listOfCardsInFourOfAKind.add(c);
					if(listOfCardsInFourOfAKind.size() >= 4)
						break;
				}
			}
		}

		return listOfCardsInFourOfAKind;
	}
}
