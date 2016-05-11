package org.gruppe2.game.model;

import org.gruppe2.ai.Difficulty;
import org.gruppe2.game.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class GameModel implements Serializable {

    private static final long serialVersionUID = -2147302592353293245L;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public enum BotPolicy { FILL, NONE }

    private final UUID uuid;
    private final int minPlayers;
    private final int maxPlayers;
    private final List<Player> players = Collections.synchronizedList(new ArrayList<>());
    private final int buyIn;
    private final BotPolicy botPolicy;
    private final Difficulty botDiff;
    private volatile String name = null;
    private volatile int button = 0;
    private volatile boolean waitingForPlayers = false;
    private volatile int smallBlind;
    private volatile int bigBlind;
    private volatile int waitTime;
    private volatile int roundsCompleted = 0;

    public GameModel(UUID uuid, int minPlayers, int maxPlayers, int buyIn, BotPolicy botPolicy, int smallBlind, int bigBlind, int waitTime, Difficulty botDiff) {
        this.uuid = uuid;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.buyIn = buyIn;
        this.botPolicy = botPolicy;
        this.smallBlind = smallBlind;
        this.bigBlind = bigBlind;
        this.waitTime = waitTime;
        this.botDiff = botDiff;
    }

    public UUID getUUID() {
        return uuid;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public boolean isWaitingForPlayers() {
        return waitingForPlayers;
    }

    public void setWaitingForPlayers(boolean waitingForPlayers) {
        this.waitingForPlayers = waitingForPlayers;
    }

    public int getButton() {
        return button;
    }

    public void setButton(int button) {
        this.button = button;
    }

    public BotPolicy getBotPolicy() {
        return botPolicy;
    }

    public int getBuyIn() {
        return buyIn;
    }

    public int getBigBlind() {
        return bigBlind;
    }

    public void setBigBlind(int bigBlind) {
        this.bigBlind = bigBlind;
    }

    public int getSmallBlind() {
        return smallBlind;
    }

    public void setSmallBlind(int smallBlind) {
        this.smallBlind = smallBlind;
    }

    public int getRoundsCompleted() {
        return roundsCompleted;
    }

    public void setRoundsCompleted(int roundsCompleted) {
        this.roundsCompleted = roundsCompleted;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public Difficulty getBotDiff() {
        return botDiff;
    }

    public void apply(GameModel object) {
        players.clear();
        players.addAll(object.players);

        name = object.name;
        button = object.button;
        waitingForPlayers = object.waitingForPlayers;
        smallBlind = object.smallBlind;
        bigBlind = object.bigBlind;
        roundsCompleted = object.roundsCompleted;
    }
}
