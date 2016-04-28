package org.gruppe2.game.event;

import java.util.UUID;

public class NetworkClientEvent implements Event {
	
	String joinOrCreate;
	String message;
	
	public NetworkClientEvent(String joinOrCreate,String message){
		//TODO -->
		this.joinOrCreate = joinOrCreate;
		this.message = message;
	}
	public String getJoinOrCreate(){
		return joinOrCreate;
	}
	public String getMessage(){
		return message;
	}
}