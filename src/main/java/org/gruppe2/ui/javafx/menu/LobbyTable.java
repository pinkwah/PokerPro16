package org.gruppe2.ui.javafx.menu;

import java.util.UUID;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import org.gruppe2.ui.Resources;
import org.gruppe2.ui.javafx.PokerApplication;
import org.gruppe2.ui.javafx.ingame.ChoiceBar;

/**
 * Created by Petter on 27/04/2016.
 */
public class LobbyTable extends StackPane {

	@FXML
	private ImageView tableImage;
	@FXML
	private StackPane table;
	@FXML
	private Label players;
	@FXML
	private Label name;
	Lobby lobby;

	public LobbyTable(String players, String name, Lobby lobby) {
		Resources.loadFXML(this);
		this.players.setText(players);
		this.name.setText(name);
		this.lobby = lobby;
		setSize();
	}

	private void setSize() {
		double size = 0.13;
		tableImage.preserveRatioProperty().setValue(true);
		tableImage.fitWidthProperty().bind(
				PokerApplication.getRoot().widthProperty().multiply(size));
		players.fontProperty().bind(ChoiceBar.getFontTracking());
		name.fontProperty().bind(ChoiceBar.getFontTracking());
	}
	@FXML
	private void joinTable(){
		if(lobby != null){
			System.out.println("joining table in PHOTO!!!");
			lobby.joinGame(UUID.fromString(name.getText()));
			System.out.println("FINISHEDjoining table in PHOTO!!!");
		}
	}
}
