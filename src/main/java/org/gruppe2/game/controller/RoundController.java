package org.gruppe2.game.controller;

import org.gruppe2.game.*;
import org.gruppe2.game.event.*;
import org.gruppe2.game.helper.GameHelper;
import org.gruppe2.game.helper.RoundHelper;
import org.gruppe2.game.session.Helper;
import org.gruppe2.game.session.Message;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class RoundController extends AbstractController {
    @Helper
    private RoundHelper round;
    @Helper
    private GameHelper game;

    private LocalDateTime timeToStart = null;
    private Player player = null;
    private RoundPlayer roundPlayer = null;
    private UUID lastPlayerInRound = null;
    private List<Card> deck = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void update() {
        if (round.isPlaying() && timeToStart != null) {
            if (LocalDateTime.now().isAfter(timeToStart)) {
                timeToStart = null;
                resetRound();
                addEvent(new RoundStartEvent());

                RoundPlayer blind = round.getSmallBlindPlayer(game.getButton());
                handleAction(game.findPlayerByUUID(blind.getUUID()), blind, new Action.Blind(game.getSmallBlind()));
                blind = round.getBigBlindPlayer(game.getButton());
                handleAction(game.findPlayerByUUID(blind.getUUID()), blind, new Action.Blind(game.getBigBlind()));
            } else {
                return;
            }
        }

        if (round.isPlaying()) {
            if (round.getActivePlayers().size() == 1)
                roundEnd();
            // Go to next player and do shit
            if (player == null) {
                round.setCurrent((round.getCurrent() + 1) % round.getActivePlayers().size());
                player = game.findPlayerByUUID(round.getCurrentUUID());
                roundPlayer = round.findPlayerByUUID(round.getCurrentUUID());
                addEvent(new PlayerPreActionEvent(player));

                if (player.getBank() > 0)
                    addEvent(new PlayerActionQuery(player, roundPlayer));
                else if (player.getBank() == 0)
                    player.getAction().set(new Action.Pass());
                else
                    throw new IllegalStateException("Player: " + player.getName() + " has less than 0 chips");
            }
            if (player.getAction().isDone()) {
                handleAction(player, roundPlayer, player.getAction().get());

                if (!(player.getAction().get() instanceof Action.Raise)
                        && ((round.getLastRaiserID() == null && player.getUUID().equals(lastPlayerInRound))
                        || player.getUUID().equals(round.getLastRaiserID()))) {
                    player.getAction().reset();
                    player = null;
                    roundPlayer = null;
                    nextRound();
                }
                else {
                    player.getAction().reset();
                    player = null;
                    roundPlayer = null;
                }
            }
        }
    }

    @Message
    public boolean roundStart() {
        if (!round.isPlaying()) {
            round.setPlaying(true);
            timeToStart = LocalDateTime.now().plusSeconds(3);
            return true;
        }

        return false;
    }

    private void resetRound(){
        List<RoundPlayer> active = round.getActivePlayers();
        active.clear();
        resetDeck();

        for (Player p: game.getPlayers())
            if (p.getBank() > 0 )
                active.add(new RoundPlayer(p.getUUID(), deck.remove(0), deck.remove(0)));

        if (active.size() <= 1) {
            addEvent(new QuitEvent());
            round.setPlaying(false);
            return;
        }

        round.setPot(0);
        round.setHighestBet(0);
        round.setCurrent(game.getButton());
        lastPlayerInRound = round.getCurrentUUID();
        round.resetRound();
        round.getCommunityCards().clear();
    }

    private void handleAction (Player player, RoundPlayer roundPlayer, Action action){
        if (!legalAction(player, roundPlayer, action))
            throw new IllegalArgumentException(player.getName() + " can't do action: " + action);

        int raise;
        if (action instanceof Action.Call) {
            raise = round.getHighestBet() - roundPlayer.getBet();
            moveChips(player, roundPlayer, roundPlayer.getBet() + raise, player.getBank() - raise, raise);
        }

        if (action instanceof Action.AllIn) {
            raise = player.getBank();
            moveChips(player, roundPlayer, roundPlayer.getBet() + raise, 0, raise);
        }

        if (action instanceof Action.Fold) {
            round.getActivePlayers().remove(round.getCurrent());
            round.setCurrent(round.getCurrent()-1);
            if (!player.getUUID().equals(lastPlayerInRound))
                lastPlayerInRound = round.getLastActivePlayerID();
        }

        if (action instanceof Action.Raise) {
            raise = ((Action.Raise) action).getAmount();
            int chipsToMove = (round.getHighestBet() - roundPlayer.getBet()) + raise;
            moveChips(player, roundPlayer, round.getHighestBet() + raise, player.getBank()-chipsToMove, chipsToMove);
            round.setLastRaiserID(player.getUUID());
        }

        if (action instanceof Action.Blind) {
            int amount = ((Action.Blind) action).getAmount();
            moveChips(player, roundPlayer, amount, player.getBank() - amount, amount);
        }

        if(roundPlayer.getBet() > round.getHighestBet())
            round.setHighestBet(roundPlayer.getBet());

        if (!(action instanceof Action.Blind))
            addEvent(new PlayerPostActionEvent(player, action));
    }

    private void moveChips(Player player, RoundPlayer roundPlayer, int playerSetBet, int playerSetBank, int addToTablePot){
        roundPlayer.setBet(playerSetBet);
        player.setBank(playerSetBank);
        round.addToPot(addToTablePot);
    }

    private boolean legalAction(Player player, RoundPlayer roundPlayer, Action action) {
        if (!round.getActivePlayers().contains(roundPlayer))
            return false;

        PossibleActions pa = round.getPlayerOptions(player.getUUID());
        if (action instanceof Action.Check)
            return pa.canCheck();
        else if (action instanceof Action.Raise) {
            int raise = ((Action.Raise) action).getAmount();
            if (raise < 1 || raise > player.getBank() + roundPlayer.getBet() - round.getHighestBet())
                return false;
            return pa.canRaise();
        } else if (action instanceof Action.Call)
            return pa.canCall();
        else if (action instanceof Action.Fold || action instanceof Action.Blind || action instanceof Action.AllIn || action instanceof Action.Pass)
            return true;
        else
            throw new IllegalArgumentException("Not an action");
    }

    private void nextRound() {
        if (round.getRoundNum() == 3){
            roundEnd();
        }
        else {
            round.nextRound();
            round.setLastRaiserID(null);
            lastPlayerInRound = round.getLastActivePlayerID();

            boolean foundNewCurrent = false;
            for (int i = game.getButton(); !foundNewCurrent; i++) {
                int firstPlayer = i % game.getPlayers().size();
                UUID id = game.getPlayers().get(firstPlayer).getUUID();
                for (RoundPlayer rp : round.getActivePlayers())
                    if (rp.getUUID().equals(id)) {
                        round.setCurrent(round.getActivePlayers().indexOf(rp)-1);
                        foundNewCurrent = true;
                        break;
                    }
            }

            if (round.getRoundNum() == 1) {
                for (int i = 0; i < 3; i++)
                    round.getCommunityCards().add(deck.remove(0));
            } else if (round.getRoundNum() == 2 || round.getRoundNum() == 3)
                round.getCommunityCards().add(deck.remove(0));

            addEvent(new CommunityCardsEvent(new ArrayList<>(round.getCommunityCards())));
        }
    }

    private void roundEnd() {
        round.setPlaying(false);
        addEvent(new RoundEndEvent());
        if (round.getActivePlayers().size() == 1)
            addEvent(new PlayerWonEvent(game.findPlayerByUUID(round.getActivePlayers().get(0).getUUID())));
        //Get winner and add chips to player bank
        game.setButton(game.getButton() + 1);
        roundStart();
    }

    private void resetDeck() {
        deck.clear();
        for (Card.Suit suit : Card.Suit.values()) {
            for (int face = 2; face <= 14; face++) {
                deck.add(new Card(face, suit));
            }
        }

        Collections.shuffle(deck);
    }
}
