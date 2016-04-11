package org.gruppe2.ui.javafx;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.gruppe2.ui.Resources;

/**
 * Created by Petter on 11/04/2016.
 */
public class RightMenu extends VBox {

    @FXML private VBox innerBox;
    @FXML private ToggleButton showMenu;
    @FXML private Button viewLobby;
    @FXML private Button leaveTable;

    public RightMenu(){
        Resources.loadFXML(this);
        innerBox.prefWidthProperty().bind(PokerApplication.getRoot().widthProperty().multiply(0.15));
        viewLobby.setMaxWidth(Double.MAX_VALUE);
        leaveTable.setMaxWidth(Double.MAX_VALUE);
        innerBox.setVisible(false);
    }

    public void leaveTable(ActionEvent actionEvent) {

    }

    public void viewLobby(ActionEvent actionEvent) {
        SceneController.setModal(new Lobby());
    }

    public void showMenu(ActionEvent actionEvent) {
        menuAnimation();
    }

    private void menuAnimation(){
        if (!innerBox.isVisible()){
            innerBox.setVisible(true);
            innerBox.setTranslateX(innerBox.getWidth());
        }
        TranslateTransition openMenu = new TranslateTransition(new Duration(300), innerBox);
        openMenu.setToX(0);
        TranslateTransition closeMenu = new TranslateTransition(new Duration(300), innerBox);
        if (innerBox.getTranslateX() != 0){
            openMenu.play();
        }else {
            closeMenu.setToX(innerBox.getWidth());
            closeMenu.play();
        }
    }

}
