package org.gruppe2.ai;

import org.gruppe2.game.Card;
import org.gruppe2.game.PossibleActions;
import org.gruppe2.game.RoundPlayer;

import java.util.List;

/**
 * POJO that contains the information AI needs to make decisions.
 * This way we don't have to pass around references for every turn,
 * but instead GameInfo object
 */
public class GameInfo {
    PossibleActions possibleActions;
    List<Card> communityCards;
    int bigBlind;
    int highestBet;
    List<RoundPlayer> activePlayers;

    public int getHighestBet() {
        return highestBet;
    }

    public void setHighestBet(int highestBet) {
        this.highestBet = highestBet;
    }

    public List<RoundPlayer> getActivePlayers() {
        return activePlayers;
    }

    public void setActivePlayers(List<RoundPlayer> activePlayers) {
        this.activePlayers = activePlayers;
    }

    public PossibleActions getPossibleActions() {
        return possibleActions;
    }

    public void setPossibleActions(PossibleActions possibleActions) {
        this.possibleActions = possibleActions;
    }

    public List<Card> getCommunityCards() {
        return communityCards;
    }

    public void setCommunityCards(List<Card> communityCards) {
        this.communityCards = communityCards;
    }

    public int getBigBlind() {
        return bigBlind;
    }

    public void setBigBlind(int bigBlind) {
        this.bigBlind = bigBlind;
    }
}