package org.gruppe2.ui.javafx.ingame;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import org.gruppe2.game.Card;
import org.gruppe2.game.event.CommunityCardsEvent;
import org.gruppe2.game.event.RoundEndEvent;
import org.gruppe2.game.event.RoundStartEvent;
import org.gruppe2.game.session.Handler;
import org.gruppe2.ui.UIResources;
import org.gruppe2.ui.javafx.PokerApplication;

public class CommunityCards extends HBox {
    private List<ImageView> imageViews = new ArrayList<>();

    public CommunityCards() {
        super(5);
        Game.setAnnotated(this);

        for (int i = 0; i < 5; i++) {
            ImageView imageView = new ImageView(UIResources.getCardBack());

            imageView.setPreserveRatio(true);
            imageView.fitWidthProperty().bind(maxWidthProperty().divide(5));
            imageView.fitHeightProperty().bind(maxHeightProperty());
            imageView.setSmooth(true);
            imageView.setVisible(false);

            imageViews.add(imageView);
            getChildren().add(imageView);
        }

        //setVisible(false);
    }
    /** Guess we only need to reset images in {@link #onRoundEnd(RoundEndEvent)}. */
    @Handler
    public void onRoundStart(RoundStartEvent event) {
        resetImages();
    }

    @Handler
    public void onCommunityCards(CommunityCardsEvent event) {
        //resetImages();
        //setVisible(true);

        /*for (int i = 0; i < event.getCards().size(); i++) {
            imageViews.get(i).setImage(UIResources.getCard(event.getCards().get(i)));
        }*/
        communityCardsFlip(event.getCards());
    }

    @Handler
    public void onRoundEnd(RoundEndEvent event) {
        resetImages();
        setVisible(true);
    }

    private void resetImages() {
        for (ImageView imageView : imageViews){
            imageView.setImage(UIResources.getCardBack());
            imageView.setVisible(false);
        }
    }

    private void communityCardsFlip(List<Card> cardList) {
        ArrayList<ImageView> cards = new ArrayList<>();
        for (Card c : cardList) cards.add(new ImageView(UIResources.getCard(c)));
        if (cards.size() == 3){
            SequentialTransition sequentialTransition = new SequentialTransition();
            for (int i=0;i<3;i++){
                sequentialTransition.getChildren().add(flipCommunityCards(getChildren().get(i), cards.get(i)));
            }
            sequentialTransition.play();
            sequentialTransition.setOnFinished(clearNodes -> {
                sequentialTransition.getChildren().removeAll(sequentialTransition.getChildren());
            });
        }else if (cards.size() == 4){
            SequentialTransition sequentialTransition = new SequentialTransition(flipCommunityCards(getChildren().get(3), cards.get(3)));
            sequentialTransition.play();
            sequentialTransition.setOnFinished(clearNodes -> {
                sequentialTransition.getChildren().removeAll(sequentialTransition.getChildren());
            });

        }else if (cards.size() == 5){
            SequentialTransition sequentialTransition = new SequentialTransition(flipCommunityCards(getChildren().get(4), cards.get(4)));
            sequentialTransition.play();
            sequentialTransition.setOnFinished(clearNodes -> {
                sequentialTransition.getChildren().removeAll(sequentialTransition.getChildren());
            });

        }
    }

    private SequentialTransition flipCommunityCards(Node node, ImageView imageView) {
        ImageView initial = (ImageView) node;
        PauseTransition pauseTransition = new PauseTransition(Duration.millis(300));
        pauseTransition.setOnFinished(setVisible -> {
            initial.setVisible(true);
        });
        RotateTransition rotateTransition = new RotateTransition(Duration.millis(250), initial);
        rotateTransition.setAxis(Rotate.Y_AXIS);
        rotateTransition.setFromAngle(0);
        rotateTransition.setToAngle(90);
        rotateTransition.setOnFinished(changePicture -> {
            initial.imageProperty().setValue(imageView.getImage());
        });

        RotateTransition rotateTransition2 = new RotateTransition(Duration.millis(250), initial);
        rotateTransition2.setAxis(Rotate.Y_AXIS);
        rotateTransition2.setFromAngle(90);
        rotateTransition2.setToAngle(0);

        SequentialTransition sequentialTransition = new SequentialTransition(pauseTransition, rotateTransition, rotateTransition2);
        sequentialTransition.setCycleCount(1);
        return sequentialTransition;
    }
}
