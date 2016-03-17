package org.gruppe2.ai;

import org.gruppe2.backend.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class AIClient extends GameClient {
	private static final List<String> names = Arrays.asList("Anne", "Bob", "Chuck", "Dennis", "Emma");

	public AIClient() {
		Random rand = new Random();
		setName(names.get(rand.nextInt(names.size())));
	}

	@Override
	public Action onTurn(Player player) {

		final int call = 0;
		final int check = 1;
		final int raise = 2;
		final int fold = 3;

		Random rand = new Random();
		ArrayList<Integer> types = new ArrayList<>();
		PossibleActions actions = getSession().getPlayerOptions(player);
        /*
		double handStrength = AIHandCalculator.getHandStrength(player.getClient().getSession().getTable(),
				(ArrayList<Player>) player.getClient().getSession().getPlayers(), this);
		double bank = player.getBank();
		double toRaise = player.getClient().getSession().getHighestBet() - player.getBet();
		double rateOfReturn = 0;
		if (toRaise != 0) {
			rateOfReturn = handStrength / (toRaise / bank);
			
		}
		return chooseAction(rateOfReturn,actions,bank,handStrength);
		*/

		if (actions.canCall()) {
			types.add(call);
		}

		if (actions.canCheck()) {
			types.add(check);
		}

		if (actions.canRaise()) {
			types.add(raise);
		}

		types.add(fold);

		switch (types.get(rand.nextInt(types.size()))) {
		case call:
			return new Action.Call();

		case check:
			return new Action.Check();

		case raise:
			if (actions.getMinRaise() == actions.getMaxRaise())
				return new Action.Raise(actions.getMaxRaise());
			return new Action.Raise(
					rand.nextInt(actions.getMaxRaise() - actions.getMinRaise()) + actions.getMinRaise());

		default:
			return new Action.Fold();
		}
	}

	public Action chooseAction(double rateOfReturn,PossibleActions actions, double bank, double handStrength){
    	Random r = new Random();
    	int random =r.nextInt(100)+1;
    	if (rateOfReturn==0){
    		if (handStrength>0.5){
    			if (actions.canRaise()){
    				return new Action.Raise(this.getSession().getBigBlindAmount());
    			}
    			else if (actions.canCall())
    				return new Action.Call();
    		}else{
    			if (actions.canCheck())
    				return new Action.Check();
    		}
    		return new Action.Fold();
    	}
    	if (rateOfReturn<0.8){
    		if (actions.canCheck())
    			return new Action.Check();
    		if (random>95){
    			if (actions.canRaise()){
    				if (bank>10*this.getSession().getBigBlindAmount())
    				return new Action.Raise(this.getSession().getBigBlindAmount());
    			}
    		}
    		else
    			return new Action.Fold();
    	}else if (rateOfReturn<1){
    		if (actions.canCheck()){
    			return new Action.Check();
    		}
    		if (random>80 && random<=95){
    			if (actions.canRaise() && bank>8*this.getSession().getBigBlindAmount()){
    				return new Action.Raise(this.getSession().getBigBlindAmount());
    			}
    		}
    		if (random>95){
    			if (actions.canCall())
    				return new Action.Call();
    		}
    		return new Action.Fold();
    	}else if (rateOfReturn<=1.3){
    		if (random<=60 && actions.canCall()){
    			return new Action.Call();
    		}else if (actions.canRaise() && bank>this.getSession().getBigBlindAmount()*3){
    			return new Action.Raise(this.getSession().getBigBlindAmount()*2);
    		}
    	}else if (rateOfReturn>1.3){
    		if (random<=30){
    			if (actions.canCall())
    				return new Action.Call();
    		}else{
    			if (actions.canRaise()){
    				int numberOfBigBlinds = (int) (bank/this.getSession().getBigBlindAmount());
    				int randomBlinds = r.nextInt(numberOfBigBlinds)+1;
    				return new Action.Raise(this.getSession().getBigBlindAmount()*randomBlinds);
    			}else if (actions.canCall())
    				return new Action.Call();
    			return new Action.Fold();
    		}
    	}
    	return new Action.Fold();
    }
}
