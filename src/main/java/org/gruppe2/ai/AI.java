package org.gruppe2.ai;

import org.gruppe2.game.Player;
import org.gruppe2.game.PossibleActions;

public interface AI {
    public void doAction(Player player, PossibleActions options);
}
